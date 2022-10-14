package org.toxsoft.core.tsgui.chart.impl;

import static org.toxsoft.core.tsgui.chart.renderers.IGradientBackgroundRendererOptions.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация отрисовщика фона с помощью градиентной заливки.
 *
 * @author vs
 */
public final class GradientBackgroundRenderer
    implements IBackgroundRenderer {

  private final Color   startColor;
  private final Color   endColor;
  private final boolean horizontal;

  /**
   * Конструктор с параметрами настройки.
   *
   * @param aOpSet IOptionSet - параметры настройки
   * @param aColorManager ITsColorManager - менеджер цветов
   */
  GradientBackgroundRenderer( IOptionSet aOpSet, ITsColorManager aColorManager ) {
    horizontal = HORIZONTAL.getValue( aOpSet ).asBool();
    RGBA rgba;
    rgba = IGradientBackgroundRendererOptions.START_COLOR.getValue( aOpSet ).asValobj();
    startColor = aColorManager.getColor( rgba.rgb );
    rgba = IGradientBackgroundRendererOptions.END_COLOR.getValue( aOpSet ).asValobj();
    endColor = aColorManager.getColor( rgba.rgb );
  }

  // -------------------------------------------------------------------------
  // Реализация методов IBackgroundRenderer
  //

  @Override
  public void drawBackground( GC aGc, ITsRectangle aBounds, EBorderLayoutPlacement aPlace, boolean aFocused,
      boolean aSelected ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds, aPlace );
    aGc.setBackground( endColor );
    aGc.setForeground( startColor );
    aGc.fillGradientRectangle( aBounds.x1(), aBounds.y1(), aBounds.width(), aBounds.height(), !horizontal );
  }

}
