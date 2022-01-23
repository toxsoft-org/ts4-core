package org.toxsoft.tsgui.utils.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.tslib.utils.errors.TsNotAllEnumsUsedRtException;

/**
 * Port of AWT BorderLayout to SWT.
 *
 * @author Yannick Saillet
 */
public class BorderLayout
    extends AWTLayout {

  /**
   * Расположение виджета: по центу родителя.
   */
  public static final EBorderLayoutPlacement CENTER = EBorderLayoutPlacement.CENTER;
  /**
   * Расположение виджета: вдоль правого края родителя.
   */
  public final static EBorderLayoutPlacement EAST   = EBorderLayoutPlacement.EAST;
  /**
   * Расположение виджета: вдоль верхнего края родителя.
   */
  public final static EBorderLayoutPlacement NORTH  = EBorderLayoutPlacement.NORTH;
  /**
   * Расположение виджета: вдоль нижнего края родителя.
   */
  public final static EBorderLayoutPlacement SOUTH  = EBorderLayoutPlacement.SOUTH;
  /**
   * Расположение виджета: вдоль левого края родителя.
   */
  public final static EBorderLayoutPlacement WEST   = EBorderLayoutPlacement.WEST;

  // -----------------------

  private int     hgap      = 0, vgap = 0;
  private int     topMargin = 0, bottomMargin = 0, eastMargin = 0, westMargin = 0;
  private Control centerChild, eastChild, northChild, southChild, westChild;

  /**
   * Создает размещение с нулевыми промежутками.
   */
  public BorderLayout() {
    super();
  }

  /**
   * Создает размещение с заданными промежутками.
   *
   * @param aHGap int - промежуток между контролями по горизонтали в пикселях
   * @param aVGap int - промежуток между контролями по вертикали в пикселях
   */
  public BorderLayout( int aHGap, int aVGap ) {
    this.hgap = aHGap;
    this.vgap = aVGap;
  }

  private static ITsPoint p2p( Point aP ) {
    return new TsPoint( aP.x, aP.y );
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    readLayoutData( composite );
    Point size = new Point( westMargin + eastMargin, topMargin + bottomMargin );
    // ITsPoint psNorth = ITsPoint.ZERO;
    if( northChild != null ) {
      // psNorth = p2p( getPreferredSize( northChild, wHint, SWT.DEFAULT, flushCache ) );
      size.y += vgap;
    }
    // ITsPoint psSouth = ITsPoint.ZERO;
    if( southChild != null ) {
      // psSouth = p2p( getPreferredSize( southChild, wHint, SWT.DEFAULT, flushCache ) );
      size.y += vgap;
    }
    ITsPoint psWest = ITsPoint.ZERO;
    if( westChild != null ) {
      psWest = p2p( getPreferredSize( westChild, SWT.DEFAULT, hHint, flushCache ) );
      size.x += hgap;
    }
    ITsPoint psEast = ITsPoint.ZERO;
    if( eastChild != null ) {
      psEast = p2p( getPreferredSize( eastChild, SWT.DEFAULT, hHint, flushCache ) );
      size.x += hgap;
    }
    ITsPoint psCenter = ITsPoint.ZERO;
    if( centerChild != null ) {
      psCenter = p2p( getPreferredSize( centerChild, wHint, hHint, flushCache ) );
      size.x += hgap;
      size.y += vgap;
    }
    int west_east_center_height = Math.max( Math.max( psWest.y(), psEast.y() ), psCenter.y() );
    size.x += psWest.x() + psEast.x() + psCenter.x();
    size.y += west_east_center_height;
    // int west_east_center_height = Math.max( Math.max( psWest.y(), psEast.y() ), psCenter.y() );
    // size.x += psNorth.x() + psSouth.x() + psCenter.x();
    // size.y += west_east_center_height;
    return size;
  }

  // protected Point computeSize1( Composite composite, int wHint, int hHint, boolean flushCache ) {
  // readLayoutData( composite );
  // Point size = new Point( westMargin + eastMargin, topMargin + bottomMargin );
  // Point preferredSize;
  // if( northChild != null ) {
  // preferredSize = getPreferredSize( northChild, wHint, SWT.DEFAULT, flushCache );
  // size.y += preferredSize.y + vgap;
  // }
  // if( southChild != null ) {
  // preferredSize = getPreferredSize( southChild, wHint, SWT.DEFAULT, flushCache );
  // size.y += preferredSize.y + vgap;
  // }
  // if( westChild != null ) {
  // preferredSize = getPreferredSize( westChild, SWT.DEFAULT, hHint, flushCache );
  // size.x += preferredSize.x + hgap;
  // }
  // if( eastChild != null ) {
  // preferredSize = getPreferredSize( eastChild, SWT.DEFAULT, hHint, flushCache );
  // size.x += preferredSize.x + hgap;
  // }
  // if( centerChild != null ) {
  // preferredSize = getPreferredSize( centerChild, wHint, hHint, flushCache );
  // size.x += preferredSize.x;
  // size.y += preferredSize.y;
  // }
  // return size;
  // }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    readLayoutData( composite );
    Rectangle clientArea = composite.getClientArea();
    int top = clientArea.y + topMargin;
    int bottom = clientArea.y + clientArea.height - bottomMargin;
    int left = clientArea.x + eastMargin;
    int right = clientArea.x + clientArea.width - westMargin;

    Point preferredSize;
    if( northChild != null ) {
      preferredSize = getPreferredSize( northChild, clientArea.width, SWT.DEFAULT, flushCache );
      northChild.setBounds( left, top, right - left, preferredSize.y );
      top += preferredSize.y + vgap;
    }
    if( southChild != null ) {
      preferredSize = getPreferredSize( southChild, clientArea.width, SWT.DEFAULT, flushCache );
      southChild.setBounds( left, bottom - preferredSize.y, right - left, preferredSize.y );
      bottom -= preferredSize.y + vgap;
    }
    if( westChild != null ) {
      preferredSize = getPreferredSize( westChild, SWT.DEFAULT, bottom - top, flushCache );
      westChild.setBounds( left, top, preferredSize.x, bottom - top );
      left += preferredSize.x + hgap;
    }
    if( eastChild != null ) {
      preferredSize = getPreferredSize( eastChild, SWT.DEFAULT, bottom - top, flushCache );
      eastChild.setBounds( right - preferredSize.x, top, preferredSize.x, bottom - top );
      right -= preferredSize.x + hgap;
    }
    if( centerChild != null ) {
      centerChild.setBounds( left, top, right - left, bottom - top );
    }
  }

  /**
   * Read the layout data of the children of a composite.
   *
   * @param composite the parent composite
   */
  private void readLayoutData( Composite composite ) {
    northChild = southChild = eastChild = westChild = centerChild = null;
    Control[] children = composite.getChildren();
    for( int i = 0; i < children.length; i++ ) {
      if( !children[i].getVisible() ) {
        continue;
      }
      Object layoutData = children[i].getLayoutData();
      if( layoutData instanceof EBorderLayoutPlacement ) {
        switch( (EBorderLayoutPlacement)layoutData ) {
          case CENTER:
            centerChild = children[i];
            break;
          case EAST:
            eastChild = children[i];
            break;
          case NORTH:
            northChild = children[i];
            break;
          case SOUTH:
            southChild = children[i];
            break;
          case WEST:
            westChild = children[i];
            break;
          default:
            throw new TsNotAllEnumsUsedRtException();
        }
      }
      else {
        centerChild = children[i];
      }
    }
  }

  /**
   * Возаращает значение промежутка между контролями по горизонтали.
   *
   * @return int - промежуток между контролями по горизонтали в пикселях
   */
  public int getHgap() {
    return hgap;
  }

  /**
   * Задает значение промежутка между контролями по горизонтали.
   *
   * @param aHGap - промежуток между контролями по горизонтали в пикселях
   */
  public void setHgap( int aHGap ) {
    this.hgap = aHGap;
  }

  /**
   * Возаращает значение промежутка между контролями по вертикали.
   *
   * @return int - промежуток между контролями по вертикали в пикселях
   */
  public int getVgap() {
    return vgap;
  }

  /**
   * Задает значение промежутка между контролями по вертикали.
   *
   * @param aVGap - промежуток между контролями по вертикали в пикселях
   */
  public void setVgap( int aVGap ) {
    this.vgap = aVGap;
  }

  /**
   * Задает поля со всех сторон контроли.
   *
   * @param aTopMargin int - верхнее поле в пикселях
   * @param aBottomMargin int - нижнее поле в пикселях
   * @param aEastMargin int - левое поле в пикселях
   * @param aWestMargin int - правое поле в пикселях
   */
  public void setMargins( int aTopMargin, int aBottomMargin, int aEastMargin, int aWestMargin ) {
    topMargin = aTopMargin;
    bottomMargin = aBottomMargin;
    eastMargin = aEastMargin;
    westMargin = aWestMargin;
  }

  /**
   * Возвращает верхнее поле в пикселях.
   *
   * @return int - верхнее поле в пикселях
   */
  public int getTopMargin() {
    return topMargin;
  }

  /**
   * Возвращает нижнее поле в пикселях.
   *
   * @return int - нижнее поле в пикселях
   */
  public int getBottomMargin() {
    return bottomMargin;
  }

  /**
   * Возвращает левое поле в пикселях.
   *
   * @return int - левое поле в пикселях
   */
  public int getEastMargin() {
    return eastMargin;
  }

  /**
   * Возвращает правое поле в пикселях.
   *
   * @return int - правое поле в пикселях
   */
  public int getWestMargin() {
    return westMargin;
  }

}
