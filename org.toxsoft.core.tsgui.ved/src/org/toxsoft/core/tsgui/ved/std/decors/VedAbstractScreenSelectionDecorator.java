package org.toxsoft.core.tsgui.ved.std.decors;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * {@link IVedScreenSelectionDecorator} base implementation.
 *
 * @author vs
 */
public abstract class VedAbstractScreenSelectionDecorator
    implements IVedScreenSelectionDecorator, IVedContextable {

  private final INotifierOptionSetEdit params = new NotifierOptionSetEditWrapper( new OptionSet() );

  private final IVedEnvironment vedEnv;
  private final IVedScreen      vedScreen;
  private final IStringListEdit ignoredIds = new StringArrayList();

  boolean disposed = false;

  /**
   * Constructor for subclasses.
   * <p>
   * Note: constructor does not registers decorator on screen. Method
   * {@link IVedScreenPaintingManager#addViewsDecorator(IVedViewDecorator)} must be explicitly called.
   *
   * @param aScreen {@link IVedScreen} - the screen
   * @param aEnv {@link IVedEnvironment} - the VED nevironment
   */
  protected VedAbstractScreenSelectionDecorator( IVedScreen aScreen, IVedEnvironment aEnv ) {
    vedScreen = aScreen;
    vedEnv = aEnv;
    params.addCollectionChangeListener( ( s, o, i ) -> doUpdateOnParamsChange() );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenSelectionDecorator
  //

  @Override
  public IStringListEdit ignoredViewIds() {
    return ignoredIds;
  }

  // ------------------------------------------------------------------------------------
  // IVedViewDecorator
  //

  @Override
  public void paintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    for( IVedComponentView view : vedScreen().selectionManager().selectedViews() ) {
      if( view.id().equals( aView.id() ) && !isHidden( aView.id() ) ) {
        paintSelection( aView, aGc, aPaintBounds );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }
  // ------------------------------------------------------------------------------------
  // to use
  //

  IVedScreen vedScreen() {
    return vedScreen;
  }

  boolean isHidden( String aViewId ) {
    return ignoredIds.hasElem( aViewId );
  }

  // ------------------------------------------------------------------------------------
  // to override
  //

  /**
   * Subclass may release allocated resources.
   * <p>
   * In the base class does nothing, there is no need to call superclass method when overriding.
   */
  protected void doDispose() {
    // nop
  }

  /**
   * Subclass may update internal resources when an of the {@link #params()} changes.
   * <p>
   * In the base class does nothing, there is no need to call superclass method when overriding.
   */
  protected void doUpdateOnParamsChange() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  /**
   * Implementation must paint selection outline for the specified view.
   * <p>
   * This method is called for selected views <b>not</b> in {@link #ignoredViewIds()}.
   *
   * @param aView {@link IVedComponentView} - the view to be decorated with selection outline
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region that need to be painted
   */
  protected abstract void paintSelection( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds );

}
