package org.toxsoft.core.tsgui.chart.impl;

import java.lang.reflect.*;
import java.util.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Вспомогательные методы работы с модулем графиков.
 *
 * @author goga
 */
public class G2ChartUtils {

  /**
   * Создает компоненту графиков.
   *
   * @param aContext ITsGuiContext - соответствующий контекст
   * @return {@link IG2Chart} - компонента графиков
   */
  public static IG2Chart createChart( ITsGuiContext aContext ) {
    return new G2Chart( G2LayoutUtils.createDefaultChartLayout( aContext ), aContext );
  }

  /**
   * Созлдает набор настроечных параметров
   *
   * @param aConsumerClassName класс потребителя
   * @param aParams начльные значения
   * @param aContext контекст приложения
   * @return набор настроечных параметров
   */
  public static IG2Params createParams( String aConsumerClassName, IOptionSet aParams, ITsGuiContext aContext ) {
    G2Params g2params = new G2Params( aConsumerClassName, aContext );
    g2params.params().addAll( aParams );
    return g2params;
  }

  /**
   * Создает объект по имени класса и набору опций, которые ранее были установлены.<br>
   * <b>На заметку:</b><br>
   * Объект создается путем вызова конструктора класса с параметрами {@link IOptionSet} и {@link ITsGuiContext}
   *
   * @param aParams {@link IG2Params} - набор опций
   * @return Object - созданный объект или null в случае неудачи
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public static Object createObject( IG2Params aParams ) {
    try {
      Class clazz = Class.forName( aParams.consumerName() );
      // dima, 08.11.22 ищем конструктор с двумя параметрами IOptionSet и ITsGuiContext
      Constructor constructor = clazz.getDeclaredConstructor( IOptionSet.class, ITsGuiContext.class );
      constructor.setAccessible( true );
      return constructor.newInstance( aParams.params(), aParams.сontext() );
    }
    catch( Throwable ex ) {
      try {
        Class clazz = Class.forName( aParams.consumerName() );
        Constructor constructor = clazz.getDeclaredConstructor( IOptionSet.class );
        constructor.setAccessible( true );
        return constructor.newInstance( aParams.params() );
      }
      catch( Throwable ex2 ) {
        LoggerUtils.errorLogger().error( ex2 );
      }
    }
    return null;
  }

  // dima 08.11.22 не могу создать без ITsGuiContext
  // public static IXAxisDef createXAxisDef() {
  // return new XAxisDef();
  // }

  public static IXAxisDef createXAxisDef( IG2Params aRendererParams ) {
    return new XAxisDef( aRendererParams );
  }

  public static IXAxisDef createXAxisDef( IG2Params aRendererParams, long aStartTime, long aEndTime ) {
    return new XAxisDef( aRendererParams, aStartTime, aEndTime );
  }

  public static IXAxisDef createXAxisDef( IG2Params aRendererParams, long aStartTime, long aEndTime,
      ETimeUnit aTimeUnit, AxisMarkingDef aMarking ) {
    return new XAxisDef( aRendererParams, aStartTime, aEndTime, aTimeUnit, aMarking );
  }

  // dima 08.11.22 не могу создать без ITsGuiContext
  // public static IYAxisDef createYAxisDef( String aId, String aDescription, String aName ) {
  // return new YAxisDef( aId, aDescription, aName );
  // }

  public static IYAxisDef createYAxisDef( String aId, String aDescription, String aName, IG2Params aParams ) {
    return new YAxisDef( aId, aDescription, aName, aParams );
  }

  public static IYAxisDef createYAxisDef( String aId, String aDescription, String aName, IG2Params aParams,
      double aStartVal, double aEndVal, double aStepVal ) {
    return new YAxisDef( aId, aDescription, aName, aParams, aStartVal, aEndVal, aStepVal );
  }

  public static IYAxisDef createIntYAxisDef( String aId, String aDescription, String aName, IG2Params aParams,
      double aStartVal, double aEndVal, double aStepVal ) {
    AxisMarkingDef marking = new AxisMarkingDef( 8, 8, 4, 0, 0 ); // FIXME временно для УЗТК
    return new YAxisDef( aId, aDescription, aName, EAtomicType.INTEGER, aParams, aStartVal, aEndVal, aStepVal,
        marking );
  }

  public static IYAxisDef createFloatingYAxisDef( String aId, String aDescription, String aName, G2Params aParams,
      double aStartVal, double aEndVal, double aStepVal ) {
    return new YAxisDef( aId, aDescription, aName, EAtomicType.FLOATING, aParams, aStartVal, aEndVal, aStepVal );
  }

  /**
   * Переводит нормализованную координату в экранную.
   *
   * @param aCoord double - нормализованная координата
   * @param aLength int - длина области на экране в пикселях
   * @return int - экранная координата в пикселях
   */
  public static int normToScreen( double aCoord, int aLength ) {
    return (int)Math.round( (aCoord * aLength) / 100. );
  }

  public static double screenToNorm( int aCoord, int aLength ) {
    return (aCoord * 100.) / aLength;
  }

  // ------------------------------------------------------------------------------------
  // Методы работы с временем
  //

  /**
   * Находит ближайший не превосходящий момент времени, так чтобы надписи на шкале были целыми в терминах переданного
   * интервала между тиками.
   * <p>
   * Например, если для шкалы с 5-ти минутным интервалом было передано время 13:07:23, то фукция вернет 13:05:00
   *
   * @param aTime long произвольшый момент времени.
   * @param aTimeUnit ETimeUnit - предопределенный интервал времени между большими засечками шкалы
   * @return long время кратное интервалу, ближайшее не превосходящее заданное
   */
  public static long timeForAnnotation( long aTime, ETimeUnit aTimeUnit ) {
    long mills = aTimeUnit.timeInMills();
    return (aTime / mills) * mills;
  }

  public static void printTimeInterval( ITimeInterval aInterval, String aPrefix ) {
    Date sd = new Date( aInterval.startTime() );
    Date ed = new Date( aInterval.endTime() );
    System.out.println( aPrefix + sd.toString() + " - " + ed.toString() );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private G2ChartUtils() {
    // nop
  }

}
