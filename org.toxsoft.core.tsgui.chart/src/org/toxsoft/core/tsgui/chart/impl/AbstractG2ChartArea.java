package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Класс, от которого должны наследоваться классы конкретных областей компоненты графиков.
 * <p>
 * <b>Мотивация:</b><br>
 * Данный класс был создан для реализации общей для всех областей функциональности, связанной с размерами и положением.
 *
 * @author vs
 */
abstract class AbstractG2ChartArea
    extends Stridable
    implements IG2ChartArea {

  ITsRectangle bounds = new TsRectangle( 0, 0, 1, 1 );

  AbstractG2ChartArea( String aId, String aName, String aDescription ) {
    super( aId, aName, aDescription );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILayoutSubject
  //

  @Override
  public ITsRectangle bounds() {
    return bounds;
  }

  @Override
  public void setBounds( ITsRectangle aBounds ) {
    bounds = aBounds;
    onBoundsChanged();
  }

  // ------------------------------------------------------------------------------------
  // Методы, которые могут переопределены
  //

  /**
   * Вызывается при каждом изменении границ области.<br>
   * По умолчанию, ничего не делает. Наследник может переопределить данный метод и сделать необходимую дополнительную
   * работу. Метод родителя вызывать не нужно.
   */
  protected void onBoundsChanged() {
    // nop
  }
}
