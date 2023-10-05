package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IVedItem} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractItem
    implements IVedItem, IParameterizedEdit, IDisposable {

  private final IVedItemCfg    initialConfig;
  private final IOptionSetEdit params;
  private final IPropertiesSet propSet;

  private boolean disposed = false;

  /**
   * Constructor for subclasses.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  protected VedAbstractItem( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs ) {
    TsNullArgumentRtException.checkNulls( aConfig, aPropDefs );
    initialConfig = aConfig;
    params = new OptionSet( aConfig.params() );
    propSet = new PropertiesSet( aPropDefs );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return initialConfig.id();
  }

  @Override
  final public String nmName() {
    return DDEF_NAME.getValue( propSet ).asString();
  }

  @Override
  final public String description() {
    return DDEF_DESCRIPTION.getValue( propSet ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  final public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  final public IPropertiesSet props() {
    return propSet;
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  final public boolean isDisposed() {
    return disposed;
  }

  @Override
  final public void dispose() {
    if( !disposed ) {
      doDispose();
      disposed = true;
    }
    else {
      LoggerUtils.errorLogger().warning( STR_WARN_DUPLICATE_DIPOSAL, toString() );
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedItem
  //

  @Override
  final public boolean isActive() {
    return props().getBool( PROP_IS_ACTIVE );
  }

  @Override
  public String factoryId() {
    return initialConfig.factoryId();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "%d (%s) - %s", id(), initialConfig.factoryId(), nmName() ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform the real disposal of resources if necessary.
   * <p>
   * Method is called once, even if {@link #dispose()} is called multiple times.
   */
  protected void doDispose() {
    // nop
  }

}
