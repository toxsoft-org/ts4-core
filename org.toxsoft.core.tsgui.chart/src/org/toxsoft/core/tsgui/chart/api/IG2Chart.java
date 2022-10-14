package org.toxsoft.core.tsgui.chart.api;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.impl.*;
import ru.toxsoft.tsgui.chart.layouts.*;

/**
 * Компонента графиков.
 *
 * @author goga
 */
public interface IG2Chart
    extends ILazyControl<Composite> {

  /**
   * Возвращает установки X-шкалы (шкалы времени).
   *
   * @return {@link IXAxisDef} - установки X-шкалы (шкалы времени)
   */
  IXAxisDef xAxisDef();

  /**
   * Задает установки X-шкалы (шкалы времени).
   *
   * @param aInfo {@link IXAxisDef} - установки X-шкалы (шкалы времени)
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setXAxisDef( IXAxisDef aInfo );

  INotifierStridablesListEdit<IYAxisDef> yAxisDefs();

  INotifierStridablesListEdit<IG2DataSet> dataSets();

  INotifierStridablesListEdit<IPlotDef> plotDefs();

  // работа с визуальными частями

  /**
   * Возвращает раскладку частей компоненты графиков.
   * <p>
   * Раскладка задается в момент создания компоненты графиков.
   * <p>
   * <b>Замечание:</b><br>
   * Функция названа "chartLayout" вместо просто "layout" для того чтобы не путать с действием - расположить
   * (расставить).
   *
   * @return {@link IG2Layout} - раскладка частей компоненты графиков
   */
  IG2Layout chartLayout();

  /**
   * Перерысовывает графики.
   */
  void refresh();

  IVisir visir();

  IZone zone();

  IGrid grid();

  /**
   * Возвращает консоль управления графиками.
   * <p>
   * Консоль позволяет получить информацию о текущем состоянии графиков, и задавать параметры - положение масштаб по
   * осям.
   *
   * @return {@link IG2Console} - консоль управления графиками
   */
  IG2Console console();

  /**
   * Устанавливает обработчик событий мыши.
   *
   * @param aHandler G2ChartMouseHandler - обработчик событий мыши
   */
  void setMouseHandler( G2ChartMouseHandler aHandler );

}
