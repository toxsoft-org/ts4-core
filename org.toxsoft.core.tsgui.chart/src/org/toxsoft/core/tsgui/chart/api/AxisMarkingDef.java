package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Описание свойств разметки шкалы.
 * <p>
 * При разметке шкалы могут использоваться засечки трех типов:
 * <ul>
 * <li><b>большие</b> - присутствуют всегда</li>
 * <li><b>средние</b> - могут отсутствовать</li>
 * <li><b>малые</b> - могут отсутствовать</li>
 * </ul>
 * Поскольку количество малых и средних засечек задается отдельно, ответственность за отсутствие визуальных конфликтов
 * при отрисовке засечек ( координаты средних засечки обычно должны совпадать с соответствующими координатами малых )
 * лежит на том кто создает конкретное описание разметки.
 *
 * @author vs
 */
public class AxisMarkingDef {

  // FIXME оформить как полагается

  final int tickSize;
  final int midTickSize;
  final int littTickSize;
  final int midTickQtty;
  final int littTickQtty;

  final int bigTickNumber;
  final int midTickNumber;
  final int littTickNumber = 1;

  public AxisMarkingDef() {
    tickSize = 15;
    midTickSize = 8;
    littTickSize = 4;
    midTickQtty = 1;
    littTickQtty = 4;

    bigTickNumber = littTickQtty + midTickQtty * (littTickQtty + 1) + 1;
    midTickNumber = littTickQtty + 1;
  }

  public AxisMarkingDef( int aTickSize, int aMidTickSize, int aLittTickSize, int aMidTickQtty, int aLittTickQtty ) {
    tickSize = aTickSize;
    midTickSize = aMidTickSize;
    littTickSize = aLittTickSize;
    midTickQtty = aMidTickQtty;
    littTickQtty = aLittTickQtty;

    bigTickNumber = littTickQtty + midTickQtty * (littTickQtty + 1) + 1;
    midTickNumber = littTickQtty + 1;
  }

  /**
   * Конструктор копирования.
   *
   * @param aAxisMarking IAxisMarking - информация о разметке, которую необходимо скопировать
   */
  public AxisMarkingDef( AxisMarkingDef aAxisMarking ) {
    tickSize = aAxisMarking.tickSize( ETickType.BIG );
    midTickSize = aAxisMarking.tickSize( ETickType.MIDDLE );
    littTickSize = aAxisMarking.tickSize( ETickType.LITTLE );
    midTickQtty = aAxisMarking.midTickQtty();
    littTickQtty = aAxisMarking.littTickQtty();

    bigTickNumber = littTickQtty + midTickQtty * (littTickQtty + 1) + 1;
    midTickNumber = littTickQtty + 1;
  }

  /**
   * Конструктор частичного копирования. Меняется структура засечек, но не их размеры.
   *
   * @param aAxisMarking IAxisMarking - информация о разметке, которую необходимо скопировать
   * @param aMidTickQtty int - количество средних тиков между большими
   * @param aLittTickQtty int - количество малых тиков между средними
   */
  public AxisMarkingDef( AxisMarkingDef aAxisMarking, int aMidTickQtty, int aLittTickQtty ) {
    tickSize = aAxisMarking.tickSize( ETickType.BIG );
    midTickSize = aAxisMarking.tickSize( ETickType.MIDDLE );
    littTickSize = aAxisMarking.tickSize( ETickType.LITTLE );
    midTickQtty = aMidTickQtty;
    littTickQtty = aLittTickQtty;

    bigTickNumber = littTickQtty + midTickQtty * (littTickQtty + 1) + 1;
    midTickNumber = littTickQtty + 1;
  }

  // --------------------------------------------------------------------------
  // Публичный API
  //

  /**
   * Размер засечки в пикселях, соответствующей переданному типу.
   *
   * @param aType ETickType - тип засечки шкалы
   * @return int - размер засечки в пикселях, соответствующей делению шкалы
   */
  public int tickSize( ETickType aType ) {
    switch( aType ) {
      case BIG:
        return tickSize;
      case LITTLE:
        return littTickSize;
      case MIDDLE:
        return midTickSize;
      default:
        throw new TsNotAllEnumsUsedRtException( "AxisMArking unknown tickType - " + aType.toString() ); //$NON-NLS-1$

    }
  }

  /**
   * Возвращает тип засечки по ее порядковому номеру.<br>
   *
   * @param aNumber int - номер засечки
   * @return ETickType - тип засечки
   */
  public ETickType tickType( int aNumber ) {
    if( aNumber % bigTickNumber == 0 ) {
      return ETickType.BIG;
    }
    if( aNumber % midTickNumber == 0 ) {
      return ETickType.MIDDLE;
    }
    // int n = aNumber / bigTickNumber;
    return ETickType.LITTLE;
  }

  /**
   * Количество (в штуках) средних делений между большими.<br>
   * Положение средней засечки не может совпадать с положением большой засечки. Это означает, что если указано
   * количество средних засечек равно 2-ум, то между большими засечками будут отображены 2 средние, таким образом, что
   * расстояние между левой большой засечкой и первой средней будет рано расстоянию между двумя соседними средними
   * засечками и растоянию между последней средней засечкой и второй большой засечкой. То есть расстояние между
   * соседними большими засечками будет разбито на 3 равных интервала.
   *
   * @return int - количество средних делений между большими
   */
  public int midTickQtty() {
    return midTickQtty;
  }

  /**
   * Количество (в штуках) малых делений между большими.<br>
   * Положение малой засечки не может совпадать с положением большой засечки. Это означает, что если указано количество
   * малых засечек равно 5-ти, то между большими засечками будут отображены 5 малых, таким образом, что расстояние между
   * левой большой засечкой и первой малой будет рано расстоянию между двумя соседними малыми засечками и растоянию
   * между последней малой засечкой и второй большой засечкой. То есть расстояние между соседними большими засечками
   * будет разбито на 6 равных интервалов.
   *
   * @return int - количество малых делений между большими
   */
  public int littTickQtty() {
    return littTickQtty;
  }

  public int bigTickNumber() {
    return bigTickNumber;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

}
