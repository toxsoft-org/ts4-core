package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericTaskInfo} implementation.
 *
 * @author hazard157
 */
public class GenericTaskInfo
    extends StridableParameterized
    implements IGenericTaskInfo {

  private final IStridablesListEdit<IDataDef>       inOps   = new StridablesList<>();
  private final IStringMapEdit<ITsContextRefDef<?>> inRefs  = new StringMap<>();
  private final IStridablesListEdit<IDataDef>       outOps  = new StridablesList<>();
  private final IStringMapEdit<ITsContextRefDef<?>> outRefs = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public GenericTaskInfo( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  /**
   * Static constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aIdsAndValues Object[] - identifier / value pairs of {@link #params()}
   * @return {@link GenericTaskInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static GenericTaskInfo create( String aId, Object... aIdsAndValues ) {
    return new GenericTaskInfo( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // IGenericTaskInfo
  //

  @Override
  public IStridablesListEdit<IDataDef> inOps() {
    return inOps;
  }

  @Override
  public IStringMapEdit<ITsContextRefDef<?>> inRefs() {
    return inRefs;
  }

  @Override
  public IStridablesListEdit<IDataDef> outOps() {
    return outOps;
  }

  @Override
  public IStringMapEdit<ITsContextRefDef<?>> outRefs() {
    return outRefs;
  }

}
