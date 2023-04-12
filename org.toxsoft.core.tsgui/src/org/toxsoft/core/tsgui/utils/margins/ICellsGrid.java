package org.toxsoft.core.tsgui.utils.margins;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The read-only interface to the {@link CellsGrid}.
 *
 * @author hazard157
 */
public interface ICellsGrid {

  /**
   * Returns the settings for the borders and spacing of the grid.
   *
   * @return {@link ITsGridMargins} - grid margins settings
   */
  ITsGridMargins margins();

  /**
   * Returns the coordinates of all cells.
   *
   * @return {@link IList}&lt;{@link ITsRectangle}&gt; - the list of cells inner rectangles
   */
  IList<ITsRectangle> getCells();

  /**
   * Returns the user-specified width of the canvas.
   *
   * @return int - canvas width in pixels
   */
  int getCanvasWidth();

  /**
   * Returns the computed height of the canvas.
   *
   * @return int - canvas height in pixels
   */
  int getCanvasHeight();

  /**
   * Returns the number of rows, which depends on the number of cells.
   *
   * @return int - number of rows, always >= 0
   */
  int getRowsCount();

  /**
   * Returns the number of columns in the grid (regardless of the number of cells).
   *
   * @return int - number of columns, always >= 1
   */
  int getColsCount();

  /**
   * Returns the width of the cell.
   *
   * @return int - cell width in pixels, always >= 1
   */
  int getCellWidth();

  /**
   * Returns the height of the cell.
   *
   * @return int - cell height in pixels, always >= 1
   */
  int getCellHeight();

  /**
   * Returns the number of cells.
   *
   * @return int - number of cells, always >= 0
   */
  int getCellsCount();

  /**
   * Returns the number of columns (more precisely, used cells) in a row.
   * <p>
   * The last row can contain from 1 to {@link #getColsCount()} cells, all the rest rows - exactly
   * {@link #getColsCount()}.
   * <p>
   * If the argument is out of range, the method simply returns 0.
   *
   * @param aRow int - column index
   * @return int - the number of used cells in the row
   */
  int getColsInRow( int aRow );

  /**
   * Returns the Y-coordinate of the upper left corner of the given row's rectangle.
   * <p>
   * The first (more precisely, zero by index) row starts at the Y coordinate = {@link ITsGridMargins#top()}. Each the
   * next line is positioned lower by the row height. Row height equals to cell height {@link #getCellHeight()} plus
   * line spacing {@link ITsGridMargins#verGap()}.
   * <p>
   * The argument can take any value, positive or negative.
   *
   * @param aRow int - the row index
   * @return int - Y-coordinate
   */
  int getRowY( int aRow );

  /**
   * Returns the X-coordinate of the upper left corner of the specified column's rectangle.
   * <p>
   * The first (more precisely, zero by index) column starts at the x coordinate = {@link ITsGridMargins#left()}. Each
   * next column is located to the right by the width of the column. The width of the column is the width of the cell
   * {@link #getCellWidth()} plus distance {@link ITsGridMargins#horGap()}.
   * <p>
   * The argument can take any value, positive or negative. *
   *
   * @param aCol int - the column index
   * @return int - X-coordinate
   */
  int getColX( int aCol );

  /**
   * Returns the index of the cell containing the point with the given coordinates.
   * <p>
   * Note that only cell rectangles {@link #getCells()} are taken into account if the point is on between cells or cells
   * and canvas borders, returns -1.
   *
   * @param aX int - X-coordinate of the point in pixels
   * @param aY int - Y-coordinate of the point in pixels
   * @return int - cell index or -1
   */
  int getIndexAtCoors( int aX, int aY );

  /**
   * Returns the cell index by row and column.
   * <p>
   * Note that only the existing cell index is returned. That is, if the arguments point to Cells missing in the last
   * row, method returns -1.
   *
   * @param aCol int - column index
   * @param aRow int - row index
   * @return int - cell index or -1
   */
  int getIndex( int aCol, int aRow );

  /**
   * Returns cell bounds by row and column.
   * <p>
   * Note that only the existing cell is returned. That is, if the arguments point to those missing in the last line of
   * the cell, the method will throw an exception.
   *
   * @param aCol int - column index
   * @param aRow int - row index
   * @return {@link ITsRectangle} - cell position and size in pixels
   * @throws TsIllegalArgumentRtException arguments are out of range
   */
  ITsRectangle getCell( int aCol, int aRow );

  /**
   * Returns cell bounds by index in a list of cells.
   *
   * @param aIndex int - index in the list of cells
   * @return {@link ITsRectangle} - cell position and size in pixels
   * @throws TsIllegalArgumentRtException index is out of range
   */
  ITsRectangle getCell( int aIndex );

}
