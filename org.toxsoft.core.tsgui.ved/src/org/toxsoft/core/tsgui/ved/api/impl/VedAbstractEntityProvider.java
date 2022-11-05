package org.toxsoft.core.tsgui.ved.api.impl;

import static org.toxsoft.core.tsgui.ved.api.impl.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link IVedEntityProvider}.
 *
 * @author hazard157
 */
public abstract class VedAbstractEntityProvider
    extends StridableParameterized
    implements IVedEntityProvider {

  private final EVedEntityKind                kind;
  private final IStridablesListEdit<IDataDef> propDefs = new StridablesList<>();

  /**
   * Constructor.
   * <p>
   * {@link #params()} and {@link #propDefs()} may be updated in subclass constructor.
   *
   * @param aKind {@link EVedEntityKind} - the kind of created entities
   * @param aId String - provider ID (entity kind ID)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - proprties definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public VedAbstractEntityProvider( EVedEntityKind aKind, String aId, IOptionSet aParams,
      IStridablesList<IDataDef> aPropDefs ) {
    super( aId, aParams );
    kind = TsNullArgumentRtException.checkNull( aKind );
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
  // IVedEntityProvider
  //

  @Override
  public EVedEntityKind entityKind() {
    return kind;
  }

  @Override
  public IStridablesListEdit<IDataDef> propDefs() {
    return propDefs;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public <T extends IVedEntity> T create( IVedEntityConfig aCfg, IVedEnvironment aVedEnv ) {
    TsNullArgumentRtException.checkNulls( aCfg, aVedEnv );
    TsIllegalArgumentRtException.checkTrue( aCfg.entityKind() != kind );
    // check props and prepare argument only for defined properties
    IOptionSetEdit propVals = new OptionSet();
    for( String pid : aCfg.propValues().keys() ) {
      IDataDef pdef = propDefs.findByKey( pid );
      if( pdef != null ) {
        IAtomicValue av = aCfg.propValues().getValue( pid );
        if( !AvTypeCastRtException.canAssign( pdef.atomicType(), av.atomicType() ) ) {
          throw new AvTypeCastRtException( FMT_ERR_INV_PROP_TYPE, pid, pdef.atomicType().id(), av.atomicType().id() );
        }
        propVals.put( pid, av );
      }
    }
    // create entity
    IVedEntity c = doCreateEntity( aCfg, aVedEnv );
    TsInternalErrorRtException.checkNull( c );
    TsInternalErrorRtException.checkTrue( c.provider() != this );
    TsInternalErrorRtException.checkFalse( entityKind().entityClass().isInstance( c ) );
    return (T)c;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must create the entity.
   *
   * @param aCfg {@link IVedEntityConfig} - entity config data
   * @param aVedEnv {@link IVedEnvironment} the VED environment
   * @return &lt;T&gt; - created entity
   */
  protected abstract IVedEntity doCreateEntity( IVedEntityConfig aCfg, IVedEnvironment aVedEnv );

}
