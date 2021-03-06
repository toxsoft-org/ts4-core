package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tsgui.ved.impl.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link IVedComponentProvider}.
 *
 * @author hazard157
 */
public abstract class VedAbstractComponentProvider
    extends StridableParameterized
    implements IVedComponentProvider, IStdParameterized {

  private final String                        libraryId;
  private final IStridablesListEdit<IDataDef> propDefs = new StridablesList<>();

  /**
   * Constructor.
   * <p>
   * {@link #params()} and {@link #propDefs()} may be updated in subclass constructor.
   *
   * @param aLibraryId Sting the owner library ID
   * @param aId String - provider ID (component kind ID)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aPropDefs {@link IDataDef}[] - proprties definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public VedAbstractComponentProvider( String aLibraryId, String aId, IOptionSet aParams, IDataDef... aPropDefs ) {
    super( aId, aParams );
    libraryId = StridUtils.checkValidIdPath( aLibraryId );
    propDefs.addAll( aPropDefs );
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

  // ------------------------------------------------------------------------------------
  // IVedComponentProvider
  //

  @Override
  public String libraryId() {
    return libraryId;
  }

  @Override
  public IStridablesListEdit<IDataDef> propDefs() {
    return propDefs;
  }

  @Override
  public IVedComponent createComponent( String aCompId, IVedEnvironment aEnvironment, IOptionSet aProps,
      IOptionSet aExtdata ) {
    StridUtils.checkValidIdPath( aCompId );
    TsNullArgumentRtException.checkNulls( aEnvironment, aProps, aExtdata );
    // check props and prepare argument only for defined properties
    IOptionSetEdit propVals = new OptionSet();
    for( String pid : aProps.keys() ) {
      IDataDef pdef = propDefs.findByKey( pid );
      if( pdef != null ) {
        IAtomicValue av = aProps.getByKey( pid );
        if( !AvTypeCastRtException.canAssign( pdef.atomicType(), av.atomicType() ) ) {
          throw new AvTypeCastRtException( FMT_ERR_INV_PROP_TYPE, pid, pdef.atomicType().id(), av.atomicType().id() );
        }
        propVals.put( pid, av );
      }
    }
    // create component
    IVedComponent c = doCreateComponent( aCompId, aEnvironment, propVals, aExtdata );
    TsInternalErrorRtException.checkNull( c );
    TsInternalErrorRtException.checkTrue( c.provider() != this );
    return c;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must create the component.
   *
   * @param aCompId String - the ID of component to be created
   * @param aEnvironment {@link IVedEnvironment} - the target environment
   * @param aProps {@link IOptionSet} - values of the properties (may contain not all defined properties values)
   * @param aExtdata {@link IOptionSet} - extra data
   * @return {@link IVedComponent} - created component
   */
  protected abstract IVedComponent doCreateComponent( String aCompId, IVedEnvironment aEnvironment, IOptionSet aProps,
      IOptionSet aExtdata );

}
