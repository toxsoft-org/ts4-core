package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Отрисовщик выделения компонент по-умолчанию.
 * <p>
 *
 * @author vs
 */
public class VedDefaultSelectionDecorator
    extends VedAbstractSelectionDecorator {

  private final Color selColor;

  /**
   * Конструктор.<br>
   *
   * @param aScreen IVedScreen - экран редактора
   * @param aEnv IVedEnvironment - окружение редактора
   * @param aSelectionColor Color - цвет выделения
   */
  public VedDefaultSelectionDecorator( IVedScreen aScreen, IVedEnvironment aEnv, Color aSelectionColor ) {
    super( aScreen, aEnv );
    selColor = aSelectionColor;
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenSelectionDecorator
  //

  @Override
  protected void paintSelection( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    int oldStyle = aGc.getLineStyle();
    int oldWidth = aGc.getLineWidth();
    Color oldColor = aGc.getForeground();

    aGc.setLineStyle( SWT.LINE_DASH );
    aGc.setLineWidth( 1 );
    aGc.setForeground( selColor );

    ITsRectangle rect = vedScreen().coorsConvertor().rectBounds( aView.outline().bounds() );

    aGc.drawRectangle( rect.a().x() - 2, rect.a().y() - 2, rect.width() + 4, rect.height() + 4 );

    aGc.setLineStyle( oldStyle );
    aGc.setLineWidth( oldWidth );
    aGc.setForeground( oldColor );
  }

  @Override
  public IOptionSet params() {
    // TODO Auto-generated method stub
    return null;
  }

}
