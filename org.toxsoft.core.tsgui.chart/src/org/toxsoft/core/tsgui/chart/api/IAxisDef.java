package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Базовая информация для создания шкалы (как X, так и Y шкал).
 *
 * @author goga, vs
 */
public interface IAxisDef {

  /**
   * Возвращает параметры разметки шкалы.
   *
   * @return {@link AxisMarkingDef} - параметры разметки шкалы
   */
  AxisMarkingDef axisMarkingDef();

  /**
   * Вовзращат параметры рендерера (отрисовщика) шкалы.
   *
   * @return {@link IG2Params} - параметры рендерера (отрисовщика) шкалы
   */
  IG2Params rendererParams();

  /**
   * Возвращает параметры поведения шкалы.
   * <p>
   * К поведению относятся такие вещи как перемещение шкалы, изменение размеров визуальных компонент, автоподбор и т.п.
   *
   * @return {@link IG2Params} - параметры поведения шкалы
   */
  IG2Params behaviorParams();

  /**
   * Возвращает редактируемый список "опорных" линий.
   *
   * @return IStridablesList - список "опорных" линий
   */
  IStridablesList<IReferenceLine> refLines();

  // Sol++ добавлено 2016-02-22
  /**
   * Заголовок шкалы.
   *
   * @return String - заголовок шкалы
   */
  String title();

}
