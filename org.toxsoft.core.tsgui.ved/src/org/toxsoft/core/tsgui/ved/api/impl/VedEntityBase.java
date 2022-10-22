package org.toxsoft.core.tsgui.ved.api.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEntity} base implementation.
 *
 * @author hazard157
 */
public class VedEntityBase
    implements IVedEntity, IVedContextable {

  private final GenericChangeEventer eventer;

  private final VedAbstractEntityProvider creator;
  private final IVedEnvironment           vedEnv;

  private final IOptionSetEdit         capabilities = new OptionSet();
  private final INotifierOptionSetEdit extdata      = new NotifierOptionSetEditWrapper( new OptionSet() );

  private final PropertiesSet props;

  private String id;

  /**
   * Contsructor.
   *
   * @param aProvider {@link VedAbstractEntityProvider} - the creator
   * @param aVedEnv {@link IVedEnvironment} - environment for component creation
   * @param aId String - conmonent ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedEntityBase( VedAbstractEntityProvider aProvider, IVedEnvironment aVedEnv, String aId ) {
    TsNullArgumentRtException.checkNulls( aProvider, aVedEnv );
    TsInternalErrorRtException.checkFalse( aProvider.entityKind().entityClass().isInstance( this ) );
    id = StridUtils.checkValidIdPath( aId );
    eventer = new GenericChangeEventer( this );
    creator = aProvider;
    vedEnv = aVedEnv;
    props = new PropertiesSet( creator.propDefs() );
    props.propsEventer().addListener( ( s, p, o, n ) -> eventer.fireChangeEvent() );
    extdata.addCollectionChangeListener( eventer );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the information about properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  public IStridablesList<IDataDef> propDefs() {
    return creator.propDefs();
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return props().getStr( TSID_NAME, EMPTY_STRING );
  }

  @Override
  public String description() {
    return props().getStr( TSID_DESCRIPTION, EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  public IPropertiesSet props() {
    return props;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IVedEntity
  //

  @Override
  public IVedEntityProvider provider() {
    return creator;
  }

  @Override
  public IOptionSet capabilities() {
    return capabilities;
  }

  @Override
  public INotifierOptionSetEdit extdata() {
    return extdata;
  }

}
