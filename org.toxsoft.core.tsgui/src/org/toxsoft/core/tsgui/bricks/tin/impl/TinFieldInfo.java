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
    super( aId, aParams );
    typeInfo = TsNullArgumentRtException.checkNull( aTypeInfo );
    valueVisualizer = new DefaultValueVisualizer( this );
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

  // ------------------------------------------------------------------------------------
  // ITinTypeInfo
  //

  @Override
  public ITinTypeInfo typeInfo() {
    return typeInfo;
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

}
