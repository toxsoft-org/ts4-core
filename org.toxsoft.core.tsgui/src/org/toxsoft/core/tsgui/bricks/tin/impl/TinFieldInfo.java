package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinFieldInfo} implementation.
 *
 * @author hazard157
 */
public class TinFieldInfo
    extends StridableParameterized
    implements ITinFieldInfo {

  private final ITinTypeInfo typeInfo;

  /**
   * <code>null</code> means to use {@link ITinTypeInfo#valueVisualizer()}.
   */
  private ITsVisualsProvider<ITinValue> valueVisualizer;

  private ITinValue defaultValue = null;

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aTypeInfo {@link ITinTypeInfo} - the field type info
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TinFieldInfo( String aId, IOptionSet aParams, ITinTypeInfo aTypeInfo ) {
    this( aId, aParams, aTypeInfo, null );
  }

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aTypeInfo {@link ITinTypeInfo} - the field type info
   * @param aDefaultValue {@link ITinValue} - default field value or <code>null</code> for
   *          {@link ITinTypeInfo#defaultValue()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TinFieldInfo( String aId, IOptionSet aParams, ITinTypeInfo aTypeInfo, ITinValue aDefaultValue ) {
    super( aId, aParams );
    typeInfo = TsNullArgumentRtException.checkNull( aTypeInfo );
    valueVisualizer = new DefaultValueVisualizer( this );
    defaultValue = aDefaultValue;
  }

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aTypeInfo {@link ITinTypeInfo} - the field type info
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()} options
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TinFieldInfo( String aId, ITinTypeInfo aTypeInfo, Object... aIdsAndValues ) {
    super( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
    typeInfo = TsNullArgumentRtException.checkNull( aTypeInfo );
    valueVisualizer = new DefaultValueVisualizer( this );
  }

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aTypeInfo {@link ITinTypeInfo} - the field type info
   * @param aOpSet {@link IOptionSet} - options
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TinFieldInfo( String aId, ITinTypeInfo aTypeInfo, IOptionSet aOpSet ) {
    super( aId, aOpSet );
    TsNullArgumentRtException.checkNulls( aTypeInfo, aOpSet );
    typeInfo = aTypeInfo;
    valueVisualizer = new DefaultValueVisualizer( this );
  }

  /**
   * Constructor.
   *
   * @param aDef {@link IDataDef} - the option definition determining the field
   * @param aTypeInfo {@link ITinTypeInfo} - the field type info
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TinFieldInfo( IDataDef aDef, ITinTypeInfo aTypeInfo ) {
    super( aDef );
    typeInfo = TsNullArgumentRtException.checkNull( aTypeInfo );
    valueVisualizer = new DefaultValueVisualizer( this );
  }

  /**
   * Creates the adjusted copy of the {@link ITinFieldInfo}.
   * <p>
   * Note: does <b>not</b> copies the value visualizer!
   *
   * @param aInfo {@link ITinFieldInfo} - the source field info
   * @param aAdditionalParams Object[] - identifier / value pairs for additional {@link #params()} options
   * @return {@link TinFieldInfo} - created instance
   */
  public static TinFieldInfo makeCopy( ITinFieldInfo aInfo, Object... aAdditionalParams ) {
    TsNullArgumentRtException.checkNull( aInfo );
    TinFieldInfo finf = new TinFieldInfo( aInfo.id(), aInfo.params(), aInfo.typeInfo(), aInfo.defaultValue() );
    finf.params().addAll( OptionSetUtils.createOpSet( aAdditionalParams ) );
    return finf;
  }

  /**
   * Creates the adjusted copy of the {@link ITinFieldInfo}.
   * <p>
   * Note: does <b>not</b> copies the value visualizer!
   *
   * @param aInfo {@link ITinFieldInfo} - the source field info
   * @param aDefaultValue {@link ITinValue} - new default value
   * @param aAdditionalParams Object[] - identifier / value pairs for additional {@link #params()} options
   * @return {@link TinFieldInfo} - created instance
   */
  public static TinFieldInfo makeCopy( ITinFieldInfo aInfo, ITinValue aDefaultValue, Object... aAdditionalParams ) {
    TsNullArgumentRtException.checkNulls( aInfo, aDefaultValue );
    TinFieldInfo finf = new TinFieldInfo( aInfo.id(), aInfo.params(), aInfo.typeInfo(), aInfo.defaultValue() );
    finf.params().addAll( OptionSetUtils.createOpSet( aAdditionalParams ) );
    finf.setDefaultValue( aDefaultValue );
    return finf;
  }

  // ------------------------------------------------------------------------------------
  // ITinTypeInfo
  //

  @Override
  public ITinTypeInfo typeInfo() {
    return typeInfo;
  }

  @Override
  public ITinValue defaultValue() {
    if( defaultValue == null ) {
      return typeInfo.defaultValue();
    }
    return defaultValue;
  }

  @Override
  public ITsVisualsProvider<ITinValue> valueVisualizer() {
    if( valueVisualizer == null ) {
      return typeInfo.valueVisualizer();
    }
    return valueVisualizer;
  }

  // ------------------------------------------------------------------------------------
  // API for subclass
  //

  /**
   * Sets the {@link #valueVisualizer()}.
   * <p>
   * Specifying <code>null</code> argument sets {@link ITinFieldInfo#valueVisualizer()} to return type default
   * visualizer {@link ITinTypeInfo#valueVisualizer()}.
   *
   * @param aVisualizer {@link ITsVisualsProvider}&lt;{@link ITinValue}&gt; - value visualizer or <code>null</code>
   */
  protected void setValueVisualizer( ITsVisualsProvider<ITinValue> aVisualizer ) {
    valueVisualizer = aVisualizer;
  }

  /**
   * Sets the {@link #defaultValue()}.
   * <p>
   * Specifying <code>null</code> argument sets {@link ITinFieldInfo#defaultValue()} to return type default value
   * {@link ITinTypeInfo#defaultValue()}.
   *
   * @param aDefaultValue {@link ITinValue} - default value or <code>null</code>
   */
  protected void setDefaultValue( ITinValue aDefaultValue ) {
    defaultValue = aDefaultValue;
  }

}
