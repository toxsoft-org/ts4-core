package org.toxsoft.core.tsgui.chart.api;

/**
 * Слушатель перемещения и/или масштабирования графиков.
 * 
 * @author goga
 */
public interface IChartAxisListener {

  /**
   * Изменилась X-шкала.
   * 
   * @param aSource {@link IG2Console} - источник сообщения
   */
  void onXAxisChanged( IG2Console aSource );

  /**
   * Изменилась указанная Y-шкала.
   * 
   * @param aSource {@link IG2Console} - источник сообщения
   * @param aPlotId String - идентификатор изменившейся Y-шкалы
   */
  void onYAxisChanged( IG2Console aSource, String aPlotId );

}
