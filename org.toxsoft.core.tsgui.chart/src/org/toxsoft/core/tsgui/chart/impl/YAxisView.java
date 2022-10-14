package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Визуальная компонента для отображения Y шкалы.
 *
 * @author vs
 */
public abstract class YAxisView
    extends G2AxisViewBase {

  private static final long serialVersionUID = 050563L;

  public YAxisView( String aId, String aDescr, YAxisModel aAxisModel ) {
    super( aId, TsLibUtils.EMPTY_STRING, aDescr, EG2ChartElementKind.Y_AXIS, aAxisModel );
  }

  @Override
  public YAxisModel axisModel() {
    return (YAxisModel)axisModel;
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  public ITsPoint titleSize( GC aGc ) {
    return renderer.requiredTitleSize( aGc );
  }

  public ETsOrientation titleOrientation() {
    return renderer.titleOrientation();
  }

}
