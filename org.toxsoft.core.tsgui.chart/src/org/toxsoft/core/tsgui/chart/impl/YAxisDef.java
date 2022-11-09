package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Описание Y шкалы.
 *
 * @author vs
 */
class YAxisDef
    extends Stridable
    implements IYAxisDef {

  // FIXME реализовать!!!

  private static final long serialVersionUID = 050563L;

  private final EAtomicType                         valueType;
  private final IStridablesListEdit<IReferenceLine> refLines = new StridablesList<>();
  private final AxisMarkingDef                      axisMarking;

  private final IAtomicValue initialStartValue;
  private final IAtomicValue initialEndValue;
  private final IAtomicValue initialUnitValue;

  private IG2Params rendererParams;

  /**
   * Конструктор, создающий объект с полями по умолчанию.
   *
   * @param aId String - идентификатор сущности (ИД-имя или ИД-путь)
   * @param aDescription String - описание сущности
   * @param aName String - удобочитаемое имя сущности
   */
  // dima 08.11.22 без контекста не работаем
  // YAxisDef( String aId, String aDescription, String aName ) {
  // super( aId, aDescription, aName );
  // valueType = EAtomicType.FLOATING;
  // axisMarking = new AxisMarkingDef();
  // initialStartValue = AvUtils.avFloat( 0 );
  // initialEndValue = AvUtils.avFloat( 100 );
  // initialUnitValue = AvUtils.avFloat( 10 );
  // rendererParams = new G2Params( StdG2AxisRenderer.class.getName() );
  // }

  YAxisDef( String aId, String aDescription, String aName, IG2Params aParams ) {
    super( aId, aDescription, aName );
    valueType = EAtomicType.FLOATING;
    axisMarking = new AxisMarkingDef();
    initialStartValue = AvUtils.avFloat( 0 );
    initialEndValue = AvUtils.avFloat( 100 );
    initialUnitValue = AvUtils.avFloat( 10 );
    rendererParams = aParams;
  }

  YAxisDef( String aId, String aDescription, String aName, IG2Params aParams, double aStartVal, double aEndVal,
      double aStepVal ) {
    super( aId, aDescription, aName );
    valueType = EAtomicType.FLOATING;
    axisMarking = new AxisMarkingDef();
    initialStartValue = AvUtils.avFloat( aStartVal );
    initialEndValue = AvUtils.avFloat( aEndVal );
    initialUnitValue = AvUtils.avFloat( aStepVal );
    rendererParams = aParams;
  }

  YAxisDef( String aId, String aDescription, String aName, EAtomicType aValueType, IG2Params aParams, double aStartVal,
      double aEndVal, double aStepVal ) {
    super( aId, aDescription, aName );
    valueType = aValueType;
    axisMarking = new AxisMarkingDef();
    initialStartValue = AvUtils.avFloat( aStartVal );
    initialEndValue = AvUtils.avFloat( aEndVal );
    initialUnitValue = AvUtils.avFloat( aStepVal );
    rendererParams = aParams;
  }

  YAxisDef( String aId, String aDescription, String aName, EAtomicType aValueType, IG2Params aParams, double aStartVal,
      double aEndVal, double aStepVal, AxisMarkingDef aMarkingDef ) {
    super( aId, aDescription, aName );
    valueType = aValueType;
    axisMarking = aMarkingDef;
    initialStartValue = AvUtils.avFloat( aStartVal );
    initialEndValue = AvUtils.avFloat( aEndVal );
    initialUnitValue = AvUtils.avFloat( aStepVal );
    rendererParams = aParams;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IYAxisDef
  //

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
    return refLines;
  }

  @Override
  public EAtomicType valueType() {
    return valueType;
  }

  @Override
  public IAtomicValue initialStartValue() {
    return initialStartValue;
  }

  @Override
  public IAtomicValue initialEndValue() {
    return initialEndValue;
  }

  @Override
  public IAtomicValue initialUnitValue() {
    return initialUnitValue;
  }

  @Override
  public String title() {
    return nmName();
  }

}
