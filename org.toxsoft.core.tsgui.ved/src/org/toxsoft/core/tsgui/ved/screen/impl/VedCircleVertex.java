package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Вершина, представляющая собой окружность.
 * <p>
 *
 * @author vs
 */
public class VedCircleVertex
    extends VedAbstractVertex {

  Rectangle swtRect = new Rectangle( 0, 0, 8, 8 );

  private double radius;

  private final ECursorType cursorType;

  /**
   * Конструктор.<br>
   *
   * @param aId String - ИД вершины
   * @param aRadius double - радиус
   * @param aCursorType {@link ECursorType} - тип курсора
   */
  public VedCircleVertex( String aId, double aRadius, ECursorType aCursorType ) {
    super( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING );
    cursorType = aCursorType;
    radius = aRadius;
    setSize( radius * 2, radius * 2 );
    updateSwtRect();
  }

  /**
   * Конструктор.<br>
   *
   * @param aId String - ИД вершины
   * @param aName String - имя вершины
   * @param aDescription String - описание вершины
   * @param aRadius double - радиус
   * @param aCursorType {@link ECursorType} - тип курсора
   */
  public VedCircleVertex( String aId, String aName, String aDescription, double aRadius, ECursorType aCursorType ) {
    super( aId, aName, aDescription );
    cursorType = aCursorType;
    radius = aRadius;
    setSize( radius * 2, radius * 2 );
    updateSwtRect();
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVertex
  //

  @Override
  public ECursorType cursorType() {
    return cursorType;
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    ID2Rectangle d2r = bounds();
    double cx = d2r.x1() + d2r.width() / 2.;
    double cy = d2r.y1() + d2r.height() / 2.;

    double l = Math.sqrt( (aX - cx) * (aX - cx) + (aY - cy) * (aY - cy) );

    return l <= radius;
  }

  @Override
  public void setLocation( double aX, double aY ) {
    super.setLocation( aX, aY );
    updateSwtRect();
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.gc().setForeground( foregroundColor() );
    aPaintContext.gc().setBackground( backgroundColor() );
    aPaintContext.gc().fillOval( swtRect.x, swtRect.y, swtRect.width, swtRect.height );
    aPaintContext.gc().drawOval( swtRect.x, swtRect.y, swtRect.width, swtRect.height );
  }

  @Override
  protected void doOnZoomFactorChanged() {
    updateSwtRect();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateSwtRect() {
    swtRect.x = (int)Math.round( bounds().x1() );
    swtRect.y = (int)Math.round( bounds().y1() );
    swtRect.width = (int)Math.round( bounds().width() );
    swtRect.height = (int)Math.round( bounds().height() );
  }

}
