package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreen} implementation.
 *
 * @author hazard157
 */
public class VedScreen
    implements IVedScreen, ICloseable {

  private final ITsGuiContext  tsContext;
  private final VedScreenModel model;
  private final VedScreenView  view;

  private boolean isPaused = false;

  private boolean       actorsEnable               = false;
  private IVedScreenCfg savedConfigOfActorsEnabled = IVedScreenCfg.NONE;

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
  // implementation
  //

  private final IStringListEdit idsOfViselsToRedraw = new StringArrayList( getListInitialCapacity( 3 ) );
  private final TsRectangleEdit rectToRedraw        = new TsRectangleEdit();

  private void internalCallViselsAnimation( long aRtTime ) {
    IVedCoorsConverter cc = view.coorsConverter();
    boolean isFirst = true;
    boolean needRedraw = false;
    for( VedAbstractVisel v : model.visels().list() ) {
      if( v.doProcessRealTimePassed( aRtTime ) ) {
        idsOfViselsToRedraw.add( v.id() );
        if( isFirst ) {
          ITsRectangle r = cc.visel2Swt( v.bounds(), v );
          rectToRedraw.union( r );
          isFirst = false;
        }
        needRedraw = true;
      }
    }
    if( needRedraw ) {
      view.redrawSwtRect( rectToRedraw );
    }
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
    if( actorsEnable ) {
      for( VedAbstractActor a : model.actors().list() ) {
        if( a.isActive() ) {
          a.whenGwTimePassed( aGwTime );
        }
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
    // process VISELs animated drawing even if snippets and actors are paused
    internalCallViselsAnimation( aRtTime );
    //
    if( isPaused ) {
      return;
    }
    for( VedAbstractUserInputHandler h : model.screenHandlersBefore().list() ) {
      if( h.isActive() ) {
        h.whenRealTimePassed( aRtTime );
      }
    }
    if( actorsEnable ) {
      for( VedAbstractActor a : model.actors().list() ) {
        if( a.isActive() ) {
          a.whenRealTimePassed( aRtTime );
        }
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

  @Override
  public boolean isActorsEnabled() {
    return actorsEnable;
  }

  @Override
  public void setActorsEnabled( boolean aEnable ) {
    if( actorsEnable != aEnable ) {
      if( aEnable ) {
        savedConfigOfActorsEnabled = VedScreenUtils.getVedScreenConfig( this );
      }
      else {
        IVedScreenCfg curr = VedScreenUtils.getVedScreenConfig( this );
        if( !curr.equals( savedConfigOfActorsEnabled ) ) {
          VedScreenUtils.setVedScreenConfig( this, savedConfigOfActorsEnabled );
        }
      }
      actorsEnable = aEnable;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Рсисует содержимое экрана в переданный графический контекст.
   *
   * @param aGc GC - графический контекст
   */
  public void paint( GC aGc ) {
    view.paint( aGc );
  }

}
