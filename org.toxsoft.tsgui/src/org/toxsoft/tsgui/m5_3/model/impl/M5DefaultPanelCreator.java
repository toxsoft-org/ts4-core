package org.toxsoft.tsgui.m5_3.model.impl;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_3.IM5Model;
import org.toxsoft.tsgui.m5_3.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.m5_3.gui.panels.IM5PanelCreator;
import org.toxsoft.tsgui.m5_3.gui.panels.impl.*;
import org.toxsoft.tsgui.m5_3.model.IM5LifecycleManager;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link IM5PanelCreator} implementation creates default panels.
 * <p>
 * Designed also to be subclassed. To create own panels one may override corresponding <code>doCreateXxxPanel()</code>
 * method.
 *
 * @author hazard157
 * @param <T> - modelled entity type
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

}
