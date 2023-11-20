package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Базовый класс для создания вершин "активной" границы.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertex
    extends Stridable
    implements IVedVertex {

  private Color fgColor = new Color( new RGB( 0, 0, 0 ) );

  private Color bgColor = new Color( new RGB( 255, 0, 0 ) );

  private double originX = 0;

  private double originY = 0;

  protected D2RectangleEdit bounds = new D2RectangleEdit( 0, 0, 8, 8 );

  protected D2RectangleEdit actualBounds = new D2RectangleEdit( 0, 0, 8, 8 );

  private double zoomFactor = 1.0;

  protected VedAbstractVertex( String aId, String aName, String aDescr ) {
    super( aId, aName, aDescr );
  }

  /**
   * Возвращает цвет заливки.
   *
   * @return Color - цвет заливки
   */
  public final Color foregroundColor() {
    return fgColor;
  }

  /**
   * Возвращает цвет рисования.
   *
   * @return Color - цвет рисования
   */
  public final Color backgroundColor() {
    return bgColor;
  }

  /**
   * Задает цвет заливки.
   *
   * @param aColor Color - цвет заливки
   */
  public final void setForeground( Color aColor ) {
    fgColor = aColor;
  }

  /**
   * Задает цвет рисования.
   *
   * @param aColor Color - цвет рисования
   */
  public final void setBackground( Color aColor ) {
    bgColor = aColor;
  }

  // ------------------------------------------------------------------------------------
  // IDisplayable
  //

  @Override
  public final ID2Rectangle bounds() {
    actualBounds.setRect( bounds );
    actualBounds.setSize( bounds.width() / zoomFactor, bounds.width() / zoomFactor );
    return actualBounds;
  }

  // ------------------------------------------------------------------------------------
  // IPortable
  //

  @Override
  public final double originX() {
    return originX;
  }

  @Override
  public final double originY() {
    return originY;
  }

  @Override
  public void setLocation( double aX, double aY ) {
    originX = aX;
    originY = aY;
    bounds.setRect( aX, aY, bounds.width(), bounds.height() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public double zoomFactor() {
    return zoomFactor;
  }

  public void setZoomFactor( double aZoomFactor ) {
    if( Double.compare( aZoomFactor, zoomFactor ) != 0 ) {
      zoomFactor = aZoomFactor;
      doOnZoomFactorChanged();
    }
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract void doOnZoomFactorChanged();

  // ------------------------------------------------------------------------------------
  // To use
  //

  protected void setSize( double aWidth, double aHeight ) {
    bounds.setRect( originX, originY, aWidth, aHeight );
  }

}
