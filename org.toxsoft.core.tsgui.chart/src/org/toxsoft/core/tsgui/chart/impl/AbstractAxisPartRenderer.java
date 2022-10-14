package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый класс для всех отрисовщиков шкал графика.
 * <p>
 *
 * @author vs
 */
class AbstractAxisPartRenderer
    implements ITsGuiContextable {

  G2AxisViewBase axisView;

  private final ITsGuiContext guiContext;

  protected AbstractAxisPartRenderer( ITsGuiContext aContext ) {
    guiContext = aContext;
  }

  void setAxisView( G2AxisViewBase aAxisView ) {
    TsNullArgumentRtException.checkNull( aAxisView );
    axisView = aAxisView;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return guiContext;
  }
}
