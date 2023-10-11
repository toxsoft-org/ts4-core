package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreen} implementation.
 *
 * @author hazard157
 */
public class VedScreen
    implements IVedScreen, ITsGuiContextable, ICloseable {

  private final ITsGuiContext  tsContext;
  private final VedScreenModel model;
  private final VedScreenView  view;

  private boolean isPaused = false;

  /**
   * Constructor. Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedScreen( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
    model = new VedScreenModel( this );
    view = new VedScreenView( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IPausableAnimation
  //

  @Override
  public boolean isPaused() {
    return isPaused;
  }

  @Override
  public void pause() {
    isPaused = true;
  }

  @Override
  public void resume() {
    isPaused = false;
  }

  // ------------------------------------------------------------------------------------
  // IGwTimeFleetable
  //

  @Override
  public void whenGwTimePassed( long aGwTime ) {
    if( isPaused ) {
      return;
    }
    for( VedAbstractUserInputHandler h : model.screenHandlersBefore().list() ) {
      if( h.isActive() ) {
        h.whenGwTimePassed( aGwTime );
      }
    }
    for( VedAbstractActor a : model.actors().list() ) {
      if( a.isActive() ) {
        a.whenGwTimePassed( aGwTime );
      }
    }
    for( VedAbstractUserInputHandler h : model.screenHandlersAfter().list() ) {
      if( h.isActive() ) {
        h.whenGwTimePassed( aGwTime );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IRealTimeSensitive
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    if( isPaused ) {
      return;
    }
    for( VedAbstractUserInputHandler h : model.screenHandlersBefore().list() ) {
      if( h.isActive() ) {
        h.whenRealTimePassed( aRtTime );
      }
    }
    for( VedAbstractActor a : model.actors().list() ) {
      if( a.isActive() ) {
        a.whenRealTimePassed( aRtTime );
      }
    }
    for( VedAbstractUserInputHandler h : model.screenHandlersAfter().list() ) {
      if( h.isActive() ) {
        h.whenRealTimePassed( aRtTime );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    view.close();
    model.close();
  }

  // ------------------------------------------------------------------------------------
  // IVedScreen
  //

  @Override
  public VedScreenModel model() {
    return model;
  }

  @Override
  public VedScreenView view() {
    return view;
  }

  @Override
  public void attachTo( Canvas aCanvas ) {
    TsNullArgumentRtException.checkNull( aCanvas );
    view.attachCanvas( aCanvas );
    view.getControl().addDisposeListener( e -> close() );
  }

}
