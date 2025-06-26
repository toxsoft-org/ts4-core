package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * {@link IVedItem} base implementation.
 *
 * @author hazard157
 */
public abstract class VedAbstractItem
    implements IVedItem, IParameterizedEdit, IDisposable, ITsGuiContextable {

  private final IListEdit<IVedItemPropertyChangeInterceptor<VedAbstractItem>> interseptors = new ElemArrayList<>();

  private final IVedItemCfg              initialConfig;
  private final IOptionSetEdit           params;
  private final IPropertiesSet<IVedItem> propSet;
  private final VedScreen                vedScreen;

  private final KeepablesStorageAsKeepable  extraData = new KeepablesStorageAsKeepable();
  private final CompoundTsActionSetProvider aspOfItem = new CompoundTsActionSetProvider();

  private boolean disposed = false;

  /**
   * Constructor for subclasses.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  protected VedAbstractItem( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNulls( aConfig, aPropDefs, aVedScreen );
    vedScreen = aVedScreen;
    initialConfig = aConfig;
    params = new OptionSet( aConfig.params() );
    propSet = new PropertiesSet<>( this, aPropDefs ) {

      @Override
      protected void doAfterPropValuesSet( IOptionSet aChangedValues ) {
        doUpdateCachesAfterPropsChange( aChangedValues );
      }
    };
    extraData.copyFrom( aConfig.extraData() );
    props().setInterceptor( ( s, aNewValues, aValuesToSet ) -> interceptPropsChange( aNewValues, aValuesToSet ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  final private void interceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    for( IVedItemPropertyChangeInterceptor<VedAbstractItem> l : new ElemArrayList<>( interseptors ) ) {
      l.interceptPropsChange( this, aNewValues, aValuesToSet );
    }
    // call subclass interceptor
    doInterceptPropsChange( aNewValues, aValuesToSet );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
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
    return PROP_NAME.getValue( propSet ).asString();
  }

  @Override
  final public String description() {
    return PROP_DESCRIPTION.getValue( propSet ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    IVedItemFactoryBase<?> f = tsContext().get( IVedViselFactoriesRegistry.class ).find( initialConfig.factoryId() );
    return f != null ? f.iconId() : null;
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
  final public IPropertiesSet<IVedItem> props() {
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
  public abstract EVedItemKind kind();

  @Override
  final public boolean isActive() {
    return props().getBool( PROP_IS_ACTIVE );
  }

  @Override
  final public String factoryId() {
    return initialConfig.factoryId();
  }

  @Override
  public KeepablesStorageAsKeepable extraData() {
    return extraData;
  }

  /**
   * Subclass may add actions to returned instance of {@link CompoundTsActionSetProvider}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  final public CompoundTsActionSetProvider actionsProvider() {
    return aspOfItem;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    // Sol-- почему %d ???
    // return String.format( "%d (%s) - %s", id(), initialConfig.factoryId(), nmName() ); //$NON-NLS-1$
    return String.format( "%s (%s) - %s", id(), initialConfig.factoryId(), nmName() ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the owner VED screen.
   *
   * @return {@link IVedScreen} - the owner VED screen
   */
  public VedScreen vedScreen() {
    return vedScreen;
  }

  /**
   * Returns the coordinates converter between VED coordinates spaces.
   *
   * @return {@link IVedCoorsConverter} - the converter
   */
  public IVedCoorsConverter coorsConverter() {
    return vedScreen.view().coorsConverter();
  }

  /**
   * Adds interceptor of itemproperty changes.
   *
   * @param aInterseptor {@link IVedItemPropertyChangeInterceptor} - the interceptor
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public void addInterceptor( IVedItemPropertyChangeInterceptor aInterseptor ) {
    if( !interseptors.hasElem( aInterseptor ) ) {
      interseptors.add( aInterseptor );
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may process property values change request.
   * <p>
   * Editable argument <code>aValuesToSet</code> is the values, that will be set to properties. It initially contains
   * the same vales as <code>aNewValues</code>. Interceptor may remove values from <code>aValuesToSet</code> edit
   * existing, add any other properties values or event clear to cancel changes. Current values of the properties may be
   * accessed via {@link #props()}.
   * <p>
   * Does nothing in the base class, but in the inheritance tree, subclasses must call the superclass method.
   *
   * @param aNewValues {@link IOptionSetEdit} - changed properties values after change
   * @param aValuesToSet {@link IOptionSet} - the values to be set after interception
   */
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // nop
  }

  /**
   * Subclass may update internal caches and perform other actions after the property(ies) change.
   * <p>
   * Does nothing in the base class, but in the inheritance tree, subclasses <b>must call</b> the superclass method to
   * successfully update the cache at all levels.
   * <p>
   * Note: this method is also called immediately after item was created and properties are set to the initial values.
   * In such case the argument <code>aChangedValues</code> contains all properties.
   * <p>
   * Warning: setting the properties values from this method is strictly prohibited!
   *
   * @param aChangedValues {@link IOpsSetter} - set of really changed properties new values, never is empty
   */
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    // nop
  }

  /**
   * Subclass may process property change event.
   * <p>
   * Does nothing in the base class, but in the inheritance tree, subclasses must call the superclass method.
   * <p>
   * Warning: setting the properties values from this method is strictly prohibited!
   *
   * @param aNewValues {@link IOptionSet} - changed properties values after change
   * @param aOldValues {@link IOptionSet} - all properties values before change
   */
  protected void onPropsChanged( IOptionSet aNewValues, IOptionSet aOldValues ) {
    // nop
  }

  /**
   * Subclass may perform the real disposal of resources if necessary.
   * <p>
   * Method is called once, even if {@link #dispose()} is called multiple times. Implementation must call superclass
   * method.
   */
  protected void doDispose() {
    // nop
  }

}
