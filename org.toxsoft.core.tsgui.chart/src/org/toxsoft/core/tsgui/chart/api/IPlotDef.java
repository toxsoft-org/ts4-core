package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Вся информация для создания и отображения графика.
 *
 * @author goga, vs
 */
public interface IPlotDef
    extends IStridable {

  /**
   * Возвращает идентификатор Y шкалы, к которй привязан график.
   *
   * @return String - идентификатор Y-шкалы, одной из {@link IG2Chart#yAxisDefs()}
   */
  String yAxisId();

  /**
   * Возвращает идентификатор холста, на котором должен рисоваться график.
   * <p>
   * <b>Мотивация:</b><br>
   * Менеджер размещений сам не может определить на каком холсте рисовать график, поэтому нужно явное указание.<br>
   * Добавлено 14.12.2015 vs
   *
   * @return
   */
  String canvasId();

  /**
   * Возвращает идентификатор набора данных графика.
   *
   * @return String - идентификатор набора данных, одного из {@link IG2Chart#dataSets()}
   */
  String dataSetid();

  /**
   * Вовзращат параметры рендерера (отрисовщика) графика.
   *
   * @return {@link IG2Params} - параметры рендерера (отрисовщика) графика
   */
  IG2Params rendererParams();

}
