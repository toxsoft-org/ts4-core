package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Вершина, представляющая собой прямоугольник.
 * <p>
 *
 * @author vs
 */
public class VedRectVertex
    extends VedAbstractVertex {

  Rectangle swtRect = new Rectangle( 0, 0, 8, 8 );

  private final ECursorType cursorType;

  double width  = 8;
  double height = 8;

  /**
   * Конструктор.<br>
   *
   * @param aId String - ИД вершины
   * @param aW double - ширина прямоугольной вершины
   * @param aH double - высота прямоугольной вершины
   * @param aCursorType {@link ECursorType} - тип курсора
   */
  public VedRectVertex( String aId, double aW, double aH, ECursorType aCursorType ) {
    super( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING );
    cursorType = aCursorType;
    width = aW;
    height = aH;
    swtRect = new Rectangle( 0, 0, (int)width, (int)height );
  }

  /**
   * Конструктор.<br>
   *
   * @param aId String - ИД вершины
   * @param aName String - имя вершины
   * @param aDescr String - описание вершины
   * @param aW double - ширина прямоугольной вершины
   * @param aH double - высота прямоугольной вершины
   * @param aCursorType {@link ECursorType} - тип курсора
   */
  public VedRectVertex( String aId, String aName, String aDescr, double aW, double aH, ECursorType aCursorType ) {
    super( aId, aName, aDescr );
    cursorType = aCursorType;
    width = aW;
    height = aH;
    swtRect = new Rectangle( 0, 0, (int)width, (int)height );
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
    return bounds().contains( aX, aY );
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
    aPaintContext.gc().drawRectangle( swtRect.x, swtRect.y, swtRect.width, swtRect.height );
    aPaintContext.gc().fillRectangle( swtRect.x, swtRect.y, swtRect.width, swtRect.height );
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
