package org.toxsoft.core.tsgui.utils.margins;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Grid of rectangles drawn left-to-right and top-to-bottom in the rectangular canvas.
 * <p>
 * This class is intended to encapsulate cells grid geometry calculations. The canvas height {@link #getCanvasHeight()}
 * is calculated depending on the parameters: {@link #margins()}, cell sizes {@link #getCellHeight()} and
 * {@link #getCellWidth()}, number of cells {@link #getCellsCount()} for the given width {@link #getCanvasWidth()}. The
 * height of the canvas is defined as the minimum height that all rows of cells fit into with the given parameters and
 * intervals. When changing the width of the canvas, the number of columns is changed so that all the cells in the row
 * fit in the width. Only if the width is less than one column, the only column will be cut off on the right.
 * <p>
 * Fires an event {@link IGenericChangeListener#onGenericChangeEvent(Object)} on every change of the layout settings.
 * <p>
 * Note: user specifies the cell <b>inner</b> size by {@link #setCellSize(int, int)}, border is applied outside the
 * cell.
 *
 * @author hazard157
 */
public class CellsGrid
    implements ICellsGrid {

  private final GenericChangeEventer genericChangeEventer;

  private final TsGridMargins margins = new TsGridMargins();

  // user-specified settings
  int cellW      = 16;
  int cellH      = 16;
  int canvasW    = 100;
  int cellsCount = 0;

  // calculated settings
  int colsCount = 0;
  int rowsCount = 0;
  int canvasH   = 100;

  // cells rectangles
  IListEdit<ITsRectangle> cells = new ElemArrayList<>( getListInitialCapacity( estimateOrder( 300 ) ) );

  /**
   * Constructor.
   */
  public CellsGrid() {
    genericChangeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void recalculate() {
    cells.clear();
    // calculate cell outer bounds
    int outerW = cellW + 2 * margins.borderWidth();
    int outerH = cellH + 2 * margins.borderWidth();
    // count the number of columns and for a given width
    int effectiveWidth = canvasW - margins.left() - margins.right();
    colsCount = effectiveWidth / outerW;
    if( colsCount < 1 ) {
      colsCount = 1;
    }
    // create cells
    int row = 0;
    int col = 0;
    int x = margins.left();
    int y = margins.right();
    int deltaX = outerW + margins.horGap();
    int deltaY = outerH + margins.verGap();
    for( int i = 0; i < cellsCount; i++ ) {
      ITsRectangle rect = new TsRectangle( x + margins.borderWidth(), y + margins.borderWidth(), cellW, cellH );
      cells.add( rect );
      x += deltaX;
      if( ++col >= colsCount ) {
        col = 0;
        ++row;
        x = margins.left();
        y += deltaY;
      }
    }
    // count the number of lines
    rowsCount = row; // number of complete lines
    if( col != 0 ) { // if there are elements in the last line, then plus 1 line
      ++rowsCount;
    }
    // calculate the height
    canvasH = margins.top() + margins.bottom(); // first calculate the top/bottom borders
    if( rowsCount != 0 ) { // if there are rows, then add their height
      canvasH += rowsCount * outerH; // height of all cells
      canvasH += (rowsCount - 1) * margins.verGap(); // height of all gaps
    }
    genericChangeEventer.fireChangeEvent();
  }

  @Override
  public ITsGridMargins margins() {
    return margins;
  }

  @Override
  public IList<ITsRectangle> getCells() {
    return cells;
  }

  @Override
  public int getCanvasWidth() {
    return canvasW;
  }

  @Override
  public int getCanvasHeight() {
    return canvasH;
  }

  @Override
  public int getRowsCount() {
    return rowsCount;
  }

  @Override
  public int getColsCount() {
    return colsCount;
  }

  @Override
  public int getCellWidth() {
    return cellW;
  }

  @Override
  public int getCellHeight() {
    return cellH;
  }

  @Override
  public int getCellsCount() {
    return cellsCount;
  }

  @Override
  public int getColsInRow( int aRow ) {
    if( aRow < 0 || aRow >= rowsCount || colsCount == 0 ) {
      return 0;
    }
    if( aRow < rowsCount - 1 ) {
      return colsCount;
    }
    int n = cellsCount - ((rowsCount - 1) * colsCount);
    return n;
  }

  @Override
  public int getRowY( int aRow ) {
    int rowHeight = cellH + margins.verGap();
    int y = margins.top() + aRow * rowHeight;
    return y;
  }

  @Override
  public int getColX( int aCol ) {
    int colWdth = cellW + margins.horGap();
    int x = margins.left() + aCol * colWdth;
    return x;
  }

  @Override
  public int getIndexAtCoors( int aX, int aY ) {
    for( int i = 0; i < cells.size(); i++ ) {
      if( cells.get( i ).contains( aX, aY ) ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int getIndex( int aCol, int aRow ) {
    if( aRow < 0 || aRow >= rowsCount ) {
      return -1;
    }
    if( aCol < 0 || aCol >= getColsInRow( aRow ) ) {
      return -1;
    }
    return aRow * colsCount + aCol;
  }

  @Override
  public ITsRectangle getCell( int aCol, int aRow ) {
    return cells.get( getIndex( aCol, aRow ) );
  }

  @Override
  public ITsRectangle getCell( int aIndex ) {
    return cells.get( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // class API
  //

  /**
   * Sets new grid margins.
   * <p>
   * When the value changes, it recalculates the internals and fires an event
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aMargins {@link ITsGridMargins} - new margins
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setMargins( ITsGridMargins aMargins ) {
    TsNullArgumentRtException.checkNull( aMargins );
    if( !margins.equals( aMargins ) ) {
      margins.copyFrom( aMargins );
      recalculate();
    }
  }

  /**
   * Sets the inner size of the cells.
   * <p>
   * When the value changes, it recalculates the internals and fires an event
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aWidth int - width of the cell in pixels
   * @param aHeght int - height of the cell in pixels
   * @throws TsIllegalArgumentRtException any dimension < 1
   */
  public void setCellSize( int aWidth, int aHeght ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 || aHeght < 1 );
    if( cellW != aWidth || cellH != aHeght ) {
      cellW = aWidth;
      cellH = aHeght;
      recalculate();
    }
  }

  /**
   * Sets the canvas width in pixels
   * <p>
   * When the value changes, it recalculates the internals and fires an event
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aWidth int - canvas width in pixels
   * @throws TsIllegalArgumentRtException argument < 0
   */
  public void setCanvasWidth( int aWidth ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 0 );
    if( canvasW != aWidth ) {
      canvasW = aWidth;
      recalculate();
    }
  }

  /**
   * Specifies the total number of cells.
   * <p>
   * When the value changes, it recalculates the internals and fires an event
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aCellsCount int - the total number of cells
   * @throws TsIllegalArgumentRtException argument < 0
   */
  public void setCellsCount( int aCellsCount ) {
    TsIllegalArgumentRtException.checkTrue( aCellsCount < 0 );
    if( cellsCount != aCellsCount ) {
      cellsCount = aCellsCount;
      recalculate();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns change eventer.
   *
   * @return {@link IGenericChangeEventer} - the eventer
   */
  public IGenericChangeEventer eventer() {
    return genericChangeEventer;
  }

}
