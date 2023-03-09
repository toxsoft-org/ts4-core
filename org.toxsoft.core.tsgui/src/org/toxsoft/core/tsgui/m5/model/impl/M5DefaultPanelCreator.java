package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5PanelCreator} implementation creates default panels.
 * <p>
 * Designed also to be subclassed. To create own panels one may override corresponding <code>doCreateXxxPanel()</code>
 * method.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5DefaultPanelCreator<T>
    implements IM5PanelCreator<T> {

  private IM5Model<T> model = null;

  /**
   * Constructor.
   */
  public M5DefaultPanelCreator() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  void papiSetOwnerModel( IM5Model<T> aModel ) {
    model = aModel;
  }

  // ------------------------------------------------------------------------------------
  // API fo subclasses
  //

  /**
   * Returns the entity model.
   *
   * @return {@link IM5Model} - the entity model
   * @throws TsIllegalStateRtException method is called before panel creator was set to model
   */
  public IM5Model<T> model() {
    TsIllegalStateRtException.checkNull( model );
    return model;
  }

  // ------------------------------------------------------------------------------------
  // IM5PanelCreator
  //

  @Override
  public IM5EntityPanel<T> createEntityViewerPanel( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5EntityPanel<T> p = doCreateEntityViewerPanel( aContext );
    TsInternalErrorRtException.checkNull( p );
    TsInternalErrorRtException.checkFalse( p.isViewer() );
    return p;
  }

  @Override
  public IM5EntityPanel<T> createEntityEditorPanel( ITsGuiContext aContext, IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5EntityPanel<T> p = doCreateEntityEditorPanel( aContext, aLifecycleManager );
    TsInternalErrorRtException.checkNull( p );
    TsInternalErrorRtException.checkTrue( p.isViewer() );
    return p;
  }

  @Override
  public IM5EntityPanel<T> createEntityDetailsPanel( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5EntityPanel<T> p = doCreateEntityDetailsPanel( aContext );
    TsInternalErrorRtException.checkNull( p );
    TsInternalErrorRtException.checkFalse( p.isViewer() );
    return p;
  }

  @Override
  public IM5EntityPanel<T> createEntityControlledPanel( ITsGuiContext aContext,
      IM5LifecycleManager<T> aLifecycleManager, M5EntityPanelWithValedsController<T> aController ) {
    TsNullArgumentRtException.checkNull( aContext );
    M5EntityPanelWithValeds<T> p = doCreateEntityControlledPanel( aContext, aLifecycleManager, aController );
    TsInternalErrorRtException.checkNull( p );
    TsInternalErrorRtException.checkTrue( p.isViewer() );
    return p;
  }

  @Override
  public IM5FilterPanel<T> createFilterPanel( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5FilterPanel<T> p = doCreateFilterPanel( aContext );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  @Override
  public IM5CollectionPanel<T> createCollViewerPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5CollectionPanel<T> p = doCreateCollViewerPanel( aContext, aItemsProvider );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  @Override
  public IM5CollectionPanel<T> createCollEditPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5CollectionPanel<T> p = doCreateCollEditPanel( aContext, aItemsProvider, aLifecycleManager );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  @Override
  public IM5MultiLookupPanel<T> createMultiLookupPanel( ITsGuiContext aContext, IM5LookupProvider<T> aLookupProvider ) {
    TsNullArgumentRtException.checkNulls( aContext, aLookupProvider );
    IM5MultiLookupPanel<T> p = doCreateMultiLookupPanel( aContext, aLookupProvider );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  @Override
  public IM5CollectionPanel<T> createCollChecksPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider ) {
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
