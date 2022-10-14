package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Базовая модель шкалы, от которой должный наследоваться модели конкретных шкал.
 * <p>
 * Изменямый класс, который хранит текущие значения базовых параметров шкалы.
 *
 * @author vs
 */
abstract class AxisModelBase
    implements IGenericChangeEventCapable {

  // private final IListEdit<IGenericChangeListener> listeners = new ElemArrayList<>();
  private final IAxisDef axisDef;
  AxisMarkingDef         markingDef;

  private final GenericChangeEventer eventer;

  AxisModelBase( IAxisDef aAxisDef ) {
    axisDef = aAxisDef;
    markingDef = aAxisDef.axisMarkingDef();
    eventer = new GenericChangeEventer( this );
  }

  /**
   * Возвращает информацию для создания шкалы.
   *
   * @return IAxisDef - информация для создания шкалы
   */
  public IAxisDef axisDef() {
    return axisDef;
  }

  /**
   * Возвращает параметры разметки шкалы.<br>
   * При изменении масштаба шкалы параметры разметки могут меняться.
   *
   * @return {@link AxisMarkingDef} - параметры разметки шкалы
   */
  AxisMarkingDef axisMarkingDef() {
    return markingDef;
  }

  /**
   * Устанавливает параматры разметки шкалы.
   *
   * @param aMarkingDef AxisMarkingDef - параматры разметки шкалы
   */
  void setAxisMarkingDef( AxisMarkingDef aMarkingDef ) {
    markingDef = aMarkingDef;
  }

  /**
   * Устанавливает значение начальной точки шкалы.
   *
   * @param aStartValue IAtomicValue - значение начальной точки шкалы
   * @throws TsNullArgumentRtException - если aStartValue null
   */
  void locate( IAtomicValue aStartValue ) {
    TsNullArgumentRtException.checkNull( aStartValue );
    doLocate( aStartValue );
    fireChangeEvent();
  }

  /**
   * Устанавливает значение начальной и конечной точкек шкалы.
   *
   * @param aStartValue IAtomicValue - значение начальной точки шкалы
   * @param aEndValue IAtomicValue - значение конечной точки шкалы
   * @throws TsNullArgumentRtException - если любой аргумент null
   */
  void setRange( IAtomicValue aStartValue, IAtomicValue aEndValue ) {
    TsNullArgumentRtException.checkNulls( aStartValue, aEndValue );
    doSetRange( aStartValue, aEndValue );
    fireChangeEvent();
  }

  /**
   * Меняет масштаб шкалы и осуществляет ее сдвиг.
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
  void shiftAndScale( double aShift, double aScale ) {
    // FIXME определить есть ли изменения
    doShiftAndScale( aShift, aScale );
    // firstTickIndent = doGetFirstTickIndent();
    fireChangeEvent();
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //

  abstract protected void doLocate( IAtomicValue aStartValue );

  abstract protected void doSetRange( IAtomicValue aStartValue, IAtomicValue aEndValue );

  abstract protected void doShiftAndScale( double aShift, double aScale );

  abstract IAtomicValue startMarkingValue();

  /**
   * Преобразовывает нормализованное значение в значение шкалы.
   *
   * @param aNormalValue double - нормализованное значение
   * @return IAtomicValue - значение шкалы
   */
  abstract IAtomicValue normalToValue( double aNormalValue );

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void fireChangeEvent() {
    eventer.fireChangeEvent();
  }

}
