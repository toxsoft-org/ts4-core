package org.toxsoft.core.tsgui.chart.impl;

import static org.toxsoft.core.tsgui.chart.renderers.IGradientBackgroundRendererOptions.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
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
   * @param aOpSet {@link IOptionSet} - параметры настройки
   * @param aContext {@link ITsGuiContext } - контекст приложения
   */
  GradientBackgroundRenderer( IOptionSet aOpSet, ITsGuiContext aContext ) {
    ITsColorManager colorManager = aContext.get( ITsColorManager.class );
    horizontal = HORIZONTAL.getValue( aOpSet ).asBool();
    RGBA rgba;
    // dima, 25.11.24 базовый цвет - цвет фона
    // rgba = IGradientBackgroundRendererOptions.START_COLOR.getValue( aOpSet ).asValobj();
    // startColor = colorManager.getColor( rgba.rgb );
    // rgba = IGradientBackgroundRendererOptions.END_COLOR.getValue( aOpSet ).asValobj();
    // endColor = colorManager.getColor( rgba.rgb );
    rgba = Display.getDefault().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ).getRGBA();
    startColor = colorManager.getColor( rgba.rgb );
    rgba = new RGBA( rgba.rgb.red - 20, rgba.rgb.green - 20, rgba.rgb.blue - 20, rgba.alpha );
    endColor = colorManager.getColor( rgba.rgb );

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
