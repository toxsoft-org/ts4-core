package org.toxsoft.core.tsgui.chart.impl;

import static ru.toxsoft.tsgui.chart.api.ETimeUnit.*;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Редактируемая реализация {@link IXAxisDef}.
 *
 * @author goga, vs
 */
class XAxisDef
    implements IXAxisDef {

  private ITimeInterval                   initialTimeInterval;
  private ETimeUnit                       initialTimeUnit  = ETimeUnit.MIN10;
  private final IListBasicEdit<ETimeUnit> allowedTimeUnits = new SortedElemLinkedBundleList<>();
  private final AxisMarkingDef            axisMarking;

  private IG2Params rendererParams;

  /**
   * Конструктор, создающий объект с полями по умолчанию.
   */
  XAxisDef() {
    long now = System.currentTimeMillis();
    initialTimeInterval = new TimeInterval( now - 3600L * 1000L, now );
    allowedTimeUnits.addAll( SEC01, MIN10 ); // TODO задать что-то более осмысленное
    axisMarking = new AxisMarkingDef();
    rendererParams = new G2Params( StdG2AxisRenderer.class.getName() );
    // G2Params annoParams = new G2Params( IStdG2TimeAxisAnnotationRendererOptions.CONSUMER_NAME );
    // IAtomicValue arClass = DvUtils.dvStr( IStdG2TimeAxisAnnotationRendererOptions.CONSUMER_NAME );
    // IStdG2TimeAxisAnnotationRendererOptions.ANNOTATION_RENDERER_CLASS.setValue(
    // (IOptionSetEdit)rendererParams.params(), arClass );
    // IStdG2TimeAxisAnnotationRendererOptions.ANNOTATION_RENDERER_OPS.setValue(
    // (IOptionSetEdit)rendererParams.params(),
    // annoParams.params() );
  }

  XAxisDef( IG2Params aRendererParams ) {
    long now = System.currentTimeMillis();
    initialTimeInterval = new TimeInterval( now - 3600L * 1000L, now );
    allowedTimeUnits.addAll( SEC01, MIN10 ); // TODO задать что-то более осмысленное
    axisMarking = new AxisMarkingDef();
    rendererParams = aRendererParams;
  }

  XAxisDef( IG2Params aRendererParams, long aStartTime, long aEndTime ) {
    // long now = System.currentTimeMillis();
    initialTimeInterval = new TimeInterval( aStartTime, aEndTime );
    allowedTimeUnits.addAll( SEC01, MIN10 ); // TODO задать что-то более осмысленное
    axisMarking = new AxisMarkingDef();
    rendererParams = aRendererParams;
  }

  XAxisDef( IG2Params aRendererParams, long aStartTime, long aEndTime, ETimeUnit aTimeUnit, AxisMarkingDef aMarking ) {
    // long now = System.currentTimeMillis();
    initialTimeInterval = new TimeInterval( aStartTime, aEndTime );
    allowedTimeUnits.addAll( SEC01, MIN10 ); // TODO задать что-то более осмысленное
    axisMarking = aMarking;
    rendererParams = aRendererParams;
    initialTimeUnit = aTimeUnit;
  }

  // ------------------------------------------------------------------------------------
  // API редатирования
  //

  /**
   * Задает начальный интервал времени X-шкалы.
   *
   * @param aTimeInterval {@link ITimeInterval} - начальный интервал времени X-шкалы
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setInitialTimeInterval( ITimeInterval aTimeInterval ) {
    TsNullArgumentRtException.checkNull( aTimeInterval );
    initialTimeInterval = aTimeInterval;
  }

  /**
   * Задает установки цены деления шкалы.
   *
   * @param aIninitalUnit {@link ETimeUnit} - начальная цена деления
   * @param aAllowedUnits IList&lt;{@link ETimeUnit}&gt; - список разрешенных цен деления при масштабировании шкалы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException список разрешенных цен делений пустой
   * @throws TsItemNotFoundRtException список разрешенных цен делений не содержит aIninitalUnit
   */
  public void setUnits( ETimeUnit aIninitalUnit, IList<ETimeUnit> aAllowedUnits ) {
    TsNullArgumentRtException.checkNulls( aIninitalUnit, aAllowedUnits );
    TsIllegalArgumentRtException.checkTrue( aAllowedUnits.isEmpty() );
    TsItemNotFoundRtException.checkFalse( aAllowedUnits.hasElem( aIninitalUnit ) );
    initialTimeUnit = aIninitalUnit;
    allowedTimeUnits.clear();
    for( ETimeUnit u : aAllowedUnits ) {
      if( !allowedTimeUnits.hasElem( u ) ) {
        allowedTimeUnits.add( u );
      }
    }
  }

  /**
   * Задает параметры отрисовщика.
   *
   * @param aParams IG2Params - параметры отрисовщика
   */
  public void setRendererParams( IG2Params aParams ) {
    rendererParams = aParams;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IXAxisDef
  //

  @Override
  public ITimeInterval initialTimeInterval() {
    return initialTimeInterval;
  }

  @Override
  public ETimeUnit initialTimeUnit() {
    return initialTimeUnit;
  }

  @Override
  public IList<ETimeUnit> allowedTimeUnits() {
    return allowedTimeUnits;
  }

  @Override
  public AxisMarkingDef axisMarkingDef() {
    return axisMarking;
  }

  @Override
  public IG2Params rendererParams() {
    return rendererParams;
  }

  @Override
  public IG2Params behaviorParams() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStridablesList<IReferenceLine> refLines() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String title() {
    return TsLibUtils.EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + initialTimeInterval.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof XAxisDef that ) {
      return this.initialTimeUnit == that.initialTimeUnit && this.initialTimeInterval.equals( that.initialTimeInterval )
          && this.allowedTimeUnits.equals( that.allowedTimeUnits );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + initialTimeUnit.hashCode();
    result = TsLibUtils.PRIME * result + initialTimeInterval.hashCode();
    result = TsLibUtils.PRIME * result + allowedTimeUnits.hashCode();
    return result;
  }

}
