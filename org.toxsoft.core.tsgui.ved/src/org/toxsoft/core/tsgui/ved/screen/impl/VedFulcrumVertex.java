package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;

/**
 * Вершина, представляющая собой опорную точку прямоугольника.
 *
 * @author vs
 */
public class VedFulcrumVertex
    extends VedAbstractVertex {

  private final ETsFulcrum fulcrum;

  Rectangle swtRect = new Rectangle( 0, 0, 8, 8 );

  /**
   * Конструктор.
   *
   * @param aFulcrum {@link ETsFulcrum} - опорная точка прямоугольника
   */
  public VedFulcrumVertex( ETsFulcrum aFulcrum ) {
    super( aFulcrum.id(), aFulcrum.nmName(), aFulcrum.description() );
    fulcrum = aFulcrum;
  }

  @Override
  public ECursorType cursorType() {
    return ECursorType.cursorForFulcrum( fulcrum );
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
  public void paint( ITsGraphicsContext aGc ) {
    aGc.gc().setForeground( foregroundColor() );
    aGc.gc().setBackground( backgroundColor() );
    aGc.gc().fillRectangle( swtRect );
    aGc.gc().drawRectangle( swtRect );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает тип точки опоры прямоугольника.
   *
   * @return ETsFulcrum - тип точки опоры прямоугольника
   */
  public ETsFulcrum fulcrum() {
    return fulcrum;
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
