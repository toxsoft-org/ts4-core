package org.toxsoft.core.tsgui.m5.gui.panels.provreg;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Creates different purpose collection panels {@link IM5CollectionPanel} for the M5-model.
 * <p>
 * User implementation of this interface may be registered to the registry (TODO which registry?) so then M5 clients may
 * ask to create and use registered provided panels.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <M> - concrete class of the M5-model (may be useful to access field definitions)
 */
public class M5CollectionPanelProvider<T, M extends M5Model<T>>
    extends StridableParameterized {

  private final M model;

  /**
   * Constructor.
   *
   * @param aModel &lt;M&gt; - the model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5CollectionPanelProvider( M aModel ) {
    TsNullArgumentRtException.checkNull( aModel );
    model = aModel;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the M5-model.
   *
   * @return &lt;M&gt; - the M5-model
   */
  final protected M model() {
    return model;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Creates panel to view list of entities.
   * <p>
   * The create panel can be used to view the list of exsiting entities and to select one of them.
   * <p>
   * If specified provider is <code>null</code> it must be set to non-<code>null</code> value by
   * {@link IM5CollectionPanel#setItemsProvider(IM5ItemsProvider)} before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  final public IM5CollectionPanel<T> createCollViewerPanel( ITsGuiContext aContext,
      IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5CollectionPanel<T> p = doCreateCollViewerPanel( aContext, aItemsProvider );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  /**
   * Creates entities list edit panel: add, remove items to the list.
   * <p>
   * If specified <code>null</code> at creation time, both lifecycle manager and items provider must be set to
   * non-<code>null</code> values before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - manager to CRUD the entity, may be <code>null</code>
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  final public IM5CollectionPanel<T> createCollEditPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5CollectionPanel<T> p = doCreateCollEditPanel( aContext, aItemsProvider, aLifecycleManager );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  /**
   * Creates the several lookup items selection panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup items provider
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final public IM5MultiLookupPanel<T> createMultiLookupPanel( ITsGuiContext aContext,
      IM5LookupProvider<T> aLookupProvider ) {
    TsNullArgumentRtException.checkNulls( aContext, aLookupProvider );
    IM5MultiLookupPanel<T> p = doCreateMultiLookupPanel( aContext, aLookupProvider );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  /**
   * Creates panel to select several items from the list of entities.
   * <p>
   * To get selected items check marks supplier {@link IM5CollectionPanel#checkSupport()}, namely with method
   * {@link ITsCheckSupport#listCheckedItems(boolean)}.
   * <p>
   * If specified provider is <code>null</code> it must be set to non-<code>null</code> value by
   * {@link IM5CollectionPanel#setItemsProvider(IM5ItemsProvider)} before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  final public IM5CollectionPanel<T> createCollChecksPanel( ITsGuiContext aContext,
      IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5CollectionPanel<T> p = doCreateCollChecksPanel( aContext, aItemsProvider );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may create any implementation of {@link IM5EntityPanel} in viewer mode.
   * <p>
   * In the base class returns new instance of {@link M5DefaultEntityViewerPanel}, no need to call superclass method
   * when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IM5EntityPanel} - created instance
   */
  protected IM5EntityPanel<T> doCreateEntityViewerPanel( ITsGuiContext aContext ) {
    return new M5DefaultEntityViewerPanel<>( aContext, model );
  }

  /**
   * Subclass may create any implementation of {@link IM5EntityPanel} in editormode.
   * <p>
   * However the panel must allow edit all neccessary fields to correctly create and end entities.
   * <p>
   * In the base class returns new instance of {@link M5DefaultEntityEditorPanel}, no need to call superclass method
   * when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   * @return {@link IM5EntityPanel} - created instance
   */
  protected IM5EntityPanel<T> doCreateEntityEditorPanel( ITsGuiContext aContext,
      IM5LifecycleManager<T> aLifecycleManager ) {
    return new M5DefaultEntityEditorPanel<>( aContext, model, aLifecycleManager );
  }

  /**
   * Subclass may create any implementation of {@link IM5EntityPanel} for entity details view.
   * <p>
   * In the base class returns new instance of {@link M5DefaultEntityDetailsPanel}, no need to call superclass method
   * when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IM5EntityPanel} - created instance
   */
  protected IM5EntityPanel<T> doCreateEntityDetailsPanel( ITsGuiContext aContext ) {
    OPDEF_IS_DETAILS_PANE.setValue( aContext.params(), AV_FALSE ); // to avoid infinite recusion
    return new M5DefaultEntityDetailsPanel<>( aContext, model );
  }

  /**
   * Subclass may create any implementation of {@link M5EntityPanelWithValeds} in editor mode.
   * <p>
   * However the panel must allow edit all neccessary fields to correctly create and end entities.
   * <p>
   * In the base class returns new instance of {@link M5DefaultEntityEditorPanel}, no need to call superclass method
   * when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   * @param aController {@link M5EntityPanelWithValedsController} - the controller or <code>null</code> for default
   * @return {@link IM5EntityPanel} - created instance
   */
  protected M5EntityPanelWithValeds<T> doCreateEntityControlledPanel( ITsGuiContext aContext,
      IM5LifecycleManager<T> aLifecycleManager, M5EntityPanelWithValedsController<T> aController ) {
    return new M5DefaultEntityControlledPanel<>( aContext, model, aLifecycleManager, aController );
  }

  /**
   * Subclass may create any implementation of {@link IM5FilterPanel}.
   * <p>
   * In the base class returns new instance of {@link M5DefaultEntityFilterPanel}, no need to call superclass method
   * when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IM5FilterPanel} - new instance of the filter panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected IM5FilterPanel<T> doCreateFilterPanel( ITsGuiContext aContext ) {
    return new M5DefaultEntityFilterPanel<>( aContext, model );
  }

  /**
   * Subclass may create own implementation of {@link IM5CollectionPanel} in viewer mode.
   * <p>
   * In the base class returns new instance of {@link M5CollectionPanelMpcModownWrapper}, no need to call superclass
   * method when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   * @return {@link IM5CollectionPanel} - created panel
   */
  protected IM5CollectionPanel<T> doCreateCollViewerPanel( ITsGuiContext aContext,
      IM5ItemsProvider<T> aItemsProvider ) {
    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
    MultiPaneComponentModown<T> mpc = new MultiPaneComponentModown<>( aContext, model, aItemsProvider );
    return new M5CollectionPanelMpcModownWrapper<>( mpc, true );
  }

  /**
   * Subclass may create own implementation of {@link IM5CollectionPanel} in editor mode.
   * <p>
   * In the base class returns new instance of {@link M5CollectionPanelMpcModownWrapper}, no need to call superclass
   * method when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager
   * @return {@link IM5CollectionPanel} - created panel
   */
  protected IM5CollectionPanel<T> doCreateCollEditPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    MultiPaneComponentModown<T> mpc =
        new MultiPaneComponentModown<>( aContext, model, aItemsProvider, aLifecycleManager );
    return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
  }

  /**
   * Subclass may create own implementation of {@link IM5MultiLookupPanel}.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup items provider
   * @return {@link IM5MultiLookupPanel} - new instance of the panel
   */
  protected IM5MultiLookupPanel<T> doCreateMultiLookupPanel( ITsGuiContext aContext,
      IM5LookupProvider<T> aLookupProvider ) {
    MultiPaneComponentLookup<T> mpc = new MultiPaneComponentLookup<>( aContext, model, aLookupProvider );
    return new M5MultiLookupPanelMpcLookupWrapper<>( mpc, false );
  }

  /**
   * Subclass may create own implementation of {@link IM5CollectionPanel} in viewer mode with check support enabled.
   * <p>
   * In the base class returns new instance of {@link M5CollectionPanelMpcModownWrapper}, no need to call superclass
   * method when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   * @return {@link IM5CollectionPanel} - created panel
   */
  protected IM5CollectionPanel<T> doCreateCollChecksPanel( ITsGuiContext aContext,
      IM5ItemsProvider<T> aItemsProvider ) {
    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_CHECKS.setValue( aContext.params(), AV_TRUE );
    MultiPaneComponentModown<T> mpc = new MultiPaneComponentModown<>( aContext, model, aItemsProvider );
    return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
  }

}
