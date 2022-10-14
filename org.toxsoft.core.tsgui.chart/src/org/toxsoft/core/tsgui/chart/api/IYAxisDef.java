package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Информация об Y-шкале.
 *
 * @author goga
 */
public interface IYAxisDef
    extends IAxisDef, IStridable {

  EAtomicType valueType();

  // ------------------------------------------------------------------------------------
  // Начальные границы шкалы
  //

  IAtomicValue initialStartValue();

  IAtomicValue initialEndValue();

  IAtomicValue initialUnitValue();

}
