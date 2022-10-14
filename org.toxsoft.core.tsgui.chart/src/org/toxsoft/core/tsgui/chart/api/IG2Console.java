package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Консоль управления графиками.
 *
 * @author goga, vs
 */
public interface IG2Console {

  /**
   * Возвращает начальную точку по X-координате (времени).
   *
   * @return long - начальная точка по оси X (в миллисекундах с начала эпохи)
   */
  long getX1();

  /**
   * Возвращает конечную точку по X-координате (времени).
   *
   * @return long - конечная точка по оси X (в миллисекундах с начала эпохи)
   */
  long getX2();

  /**
   * Возвращает начальную точку по Y-координате указанной шкалы.
   *
   * @param aPlotId String - идентификатор Y-шкалы
   * @return {@link IAtomicValue} - физическое значение в начале шкалы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такой Y-шкалы
   */
  IAtomicValue getY1( String aPlotId );

  /**
   * Возвращает конечную точку по Y-координате указанной шкалы.
   *
   * @param aPlotId String - идентификатор Y-шкалы
   * @return {@link IAtomicValue} - физическое значение в конце шкалы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такой Y-шкалы
   */
  IAtomicValue getY2( String aPlotId );

  /**
   * Устанавливает новое значение интервала времени между большими засечками шкалы.
   *
   * @param aTimeUnit {@link ETimeUnit} - новое значение интервала времени между большими засечками шкалы
   */
  void setTimeUnit( ETimeUnit aTimeUnit );

  /**
   * Меняет масштаб по X-координате.
   * <p>
   * Сдвиг aShift исчисляется в нормированных единицах. Соответственно, значение 100.0 приводит к сдвигу вправо на всю
   * ширину экрана (канвы) графикаов, -50.0 - на пол-экрана влево, 0.0 - нет сдвига.
   * <p>
   * Коеффициент масштабирования aScale это во всколько раз раcтягивется (сжимается) график с точки начала шкалы.
   * Соответственно, значение 2.0 приводит к растягиванию шкалы в два раза, 0.5 - к сжатию шкалы в два раза, а значение
   * 1.0 не меняет масштаб. Коеффициент масштабирования должен быть в заданном диапазоне <br>
   * TODO определить диапазон, например, 0.001 - 1000.0
   * <p>
   * Приводит к выдаче извещения {@link IChartAxisListener#onXAxisChanged(IG2Console)}.
   *
   * @param aShift double - смещение в относительных единицах
   * @param aScale double - коеффициент масштабирования
   * @throws TsIllegalArgumentRtException aScale выходит за допустимый диапазон
   */
  void changeXScale( double aShift, double aScale );

  /**
   * Меняет масштаб по Y-координате.
   * <p>
   * Действует аналогично методу {@link #changeXScale(double, double)} и применяется ко всем канвам с графиками.
   * <p>
   * Приводит к выдаче извещения {@link IChartAxisListener#onYAxisChanged(IG2Console, String)}.
   *
   * @param aShift double - смещение в относительных единицах
   * @param aScale double - коеффициент масштабирования
   * @throws TsIllegalArgumentRtException aScale выходит за допустимый диапазон
   */
  void changeYScale( double aShift, double aScale );

  /**
   * Сдвигает конкретную Y-шкалу на величину в нормированных единицах.
   *
   * @param aAxisId String - идентификатор шкалы
   * @param aShift double - величина сдвига в нормированных единицах
   */
  void shiftYAxis( String aAxisId, double aShift );

  /**
   * Изменяет масштаб конкретной Y-шкалы.
   *
   * @param aAxisId String - идентификатор шкалы
   * @param aScale double - коэффициент масштабирования
   */
  void scaleYAxis( String aAxisId, double aScale );

  /**
   * Смещает график по X-координате так, чтобы начальная точка была в aT1.
   * <p>
   * После выполнения метода, график (точнее, шкала), в зависимости от своей логики поведения, не обязательно будет
   * начинаться имеено с aT1. То есть, после вызова этого метода {@link #getX1()} вернет значение равное aT1 или
   * максимально возможно близкое к нему.
   * <p>
   * Приводит к выдаче извещения {@link IChartAxisListener#onXAxisChanged(IG2Console)}.
   *
   * @param aT1 long - начальная точка по оси X (в миллисекундах с начала эпохи)
   */
  void locateX( long aT1 );

  /**
   * Задает начальную и конечноу точку графиков по X-оси.
   * <p>
   * Аналогично методу {@link #locateX(long)}, после вызова данного метода шкала постарается поставить начальные и
   * конечные значения максимально близко (а може, даже точно) к заданным значениям.
   * <p>
   * Приводит к выдаче извещения {@link IChartAxisListener#onXAxisChanged(IG2Console)}.
   *
   * @param aT1 long - начальная точка по оси X (в миллисекундах с начала эпохи)
   * @param aT2 long - конечная точка по оси X (в миллисекундах с начала эпохи)
   * @throws TsIllegalArgumentRtException aT2 <= aT1
   */
  void setXRange( long aT1, long aT2 );

  /**
   * Смещает график по Y-координате так, чтобы начальная точка была в aV1.
   * <p>
   * Аналогично методу {@link #locateX(long)}, после вызова данного метода шкала постарается поставить начальное
   * значение максимально близко (а може, даже точно) к заданной величине.
   * <p>
   * Приводит к выдаче извещения {@link IChartAxisListener#onYAxisChanged(IG2Console, String)}.
   *
   * @param aPlotId String - идентификатор Y-шкалы
   * @param aV1 {@link IAtomicValue} - физическое значение в начале шкалы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException нет такой шкалы
   * @throws DvTypeCastRtException аргумент имеет несовместимый с типом данных шкалы атомарный тип
   */
  void locateY( String aPlotId, IAtomicValue aV1 );

  /**
   * Задает начальную и конечноу точку графиков по Y-оси указанной шкалы.
   * <p>
   * Аналогично методу {@link #locateX(long)}, после вызова данного метода шкала постарается поставить начальные и
   * конечные значения максимально близко (а може, даже точно) к заданным значениям.
   * <p>
   * Приводит к выдаче извещения {@link IChartAxisListener#onYAxisChanged(IG2Console, String)}.
   *
   * @param aPlotId String - идентификатор Y-шкалы
   * @param aV1 {@link IAtomicValue} - физическое значение в начале шкалы
   * @param aV2 {@link IAtomicValue} - физическое значение в конце шкалы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException нет такой шкалы
   * @throws TsIllegalArgumentRtException сравнение значений показывает, что aV2 <= aV1
   * @throws DvTypeCastRtException аргументы имеют несовместимый с типом данных шкалы атомарный тип
   */
  void setYRange( String aPlotId, IAtomicValue aV1, IAtomicValue aV2 );

  /**
   * Делает график выделенным. vs
   * <p>
   * При этом Y шкала выделенного графика становится видимой и располагается слева от канвы рядом с ней. Если сетка
   * видима, то горизонтальные линии рисуются в соотвествии с этой шкалой. Выделенный график рисуется в последнюю
   * очередь, для того чтобы его не перекрывали дргугие графиики.
   *
   * @param aPlotId String - идентификатор графика
   */
  void selectPlot( String aPlotId );

  /**
   * Задает видимость Y шкалы.
   *
   * @param aAxisId String идентификатор шкалы
   * @param aVisible <b>true</b> - шкала должна быть видима<br>
   *          <b>false</b> - шкала не должна быть видима
   */
  void setYAxisVisible( String aAxisId, boolean aVisible );

  /**
   * Добавляет слушатель изменений шкал.
   * <p>
   * Если такой слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IChartAxisListener} - слушатель изменений шкал
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addListener( IChartAxisListener aListener );

  /**
   * Добавляет слушатель изменений шкал.
   * <p>
   * Если такой слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IChartAxisListener} - слушатель изменений шкал
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeListener( IChartAxisListener aListener );

  // ------------------------------------------------------------------------------------
  // Методы добавленные vs
  //

  /**
   * Устанавливает признак видимости "Визира".
   * <p>
   *
   * @param aVisible <b>true</b> - "визир" должен быть видим<br>
   *          <b>false</b> - "визир" не должен быть видим
   */
  void setVizirVisible( boolean aVisible );

  /**
   * Устанавливает признак видимости "Сетки".
   * <p>
   *
   * @param aVisible <b>true</b> - "сетка" должна быть видима<br>
   *          <b>false</b> - "сетка" не должна быть видима
   */
  void setGridVisible( boolean aVisible );

  /**
   * Устанавливает видимость графика.
   * <p>
   * Если график стал невидимым и он является последним для шкалы, от кала также становится невидимой. Если график стал
   * видимым, то его шкала также становится видимой.
   *
   * @param aPlotId String - идентификатор графика
   * @param aVisible boolean - видимость графика
   */
  void setPlotVisible( String aPlotId, boolean aVisible );

}
