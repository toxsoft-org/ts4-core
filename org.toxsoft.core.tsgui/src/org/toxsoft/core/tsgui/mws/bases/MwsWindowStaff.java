package org.toxsoft.core.tsgui.mws.bases;

import static org.toxsoft.core.tsgui.mws.bases.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Each created {@link MWindow} / {@link MTrimmedWindow} will receive instance of this class in windows context.
 * <p>
 * Note: instance of this class must be placed in windows context before any MWS activity with the window.
 *
 * @author hazard157
 */
public class MwsWindowStaff {

  /**
   * Calls windows lifecycle handling methods of the registered quants and this addon.
   */
  class WindowLifeCylceListener
      implements IMainWindowLifeCylceListener {

    @Override
    public void beforeMainWindowOpen( IEclipseContext aWinContext, MWindow aWindow ) {
      appWideQuantManager.initWin( aWinContext );
    }

    @Override
    public boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
      return appWideQuantManager.canCloseMainWindow( aWinContext, aWindow );
    }

    @Override
    public void beforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
      appWideQuantManager.whenCloseMainWindow( aWinContext, aWindow );
    }

  }

  /**
   * Close handler to be set as RCP means.
   * <p>
   * Close handler is called when user closes application by system mean (window menu, window close button, etc). Close
   * handler is <b>not called</b> when finishing application by {@link ITsE4Helper#quitApplication()}.
   */
  private final IWindowCloseHandler closeHandler = aWindow -> {
    if( canCloseWindow() ) {
      fireBeforeWindowCloseEvent();
      return true;
    }
    return false;
  };

  private final IListEdit<IMainWindowLifeCylceListener> windowInterceptors =
      new SynchronizedListEdit<>( new ElemArrayList<>( false ) );

  private final MWindow                      window;
  private final IApplicationWideQuantManager appWideQuantManager;
  private final IMainWindowLifeCylceListener mainWindowLifeCylceListener = new WindowLifeCylceListener();

  /**
   * Constructs instance and initialized window context.
   *
   * @param aWindow {@link MWindow} - the window to bind the instance with
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MwsWindowStaff( MWindow aWindow ) {
    window = TsNullArgumentRtException.checkNull( aWindow );
    IEclipseContext winCtx = window.getContext();
    // put instance into the context

    /**
     * FIXME here is an error when extracting any UIpath to the new window. Next line causes an exception.
     */

    TsInternalErrorRtException.checkNoNull( winCtx.get( MwsWindowStaff.class ) );
    window.getContext().set( MwsWindowStaff.class, this );
    appWideQuantManager = winCtx.get( IApplicationWideQuantManager.class );
    TsInternalErrorRtException.checkNull( appWideQuantManager );
    LoggerUtils.defaultLogger().info( FMT_INFO_WIN_STAFF_INIT, aWindow.getElementId() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * The key of the {@link Boolean} reference in the window context as the sign of the .
   */
  private static final String KEY_IS_INITED_PARTS_FOR_WINDOW = TS_FULL_ID + ".MwsWindowWasInited"; //$NON-NLS-1$

  private final boolean isWindowInitFlag() {
    Object val = window.getContext().get( KEY_IS_INITED_PARTS_FOR_WINDOW );
    if( val instanceof Boolean boolVal ) {
      if( boolVal.booleanValue() ) {
        return true;
      }
    }
    return false;
  }

  private final void setWindowInitFlag() {
    window.getContext().set( KEY_IS_INITED_PARTS_FOR_WINDOW, Boolean.TRUE );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  /**
   * Called when any part is created in the {@link #window}.
   * <p>
   * Called from {@link MwsAbstractPart#initPart(Composite)}.
   *
   * @param aPart {@link MwsAbstractPart} - the part
   */
  void papiOnPartInit( MwsAbstractPart aPart ) {
    boolean wasWindowInited = isWindowInitFlag();
    if( !wasWindowInited ) {
      setWindowInitFlag();
      addMainWindowLifecycleInterceptor( mainWindowLifeCylceListener );
      fireBeforeWindowOpenEvent();
    }
  }

  /**
   * Called when any part is destroyed in the {@link #window}.
   * <p>
   * Called from {@link MwsAbstractPart#destroyPart()}.
   *
   * @param aPart {@link MwsAbstractPart} - the part
   */
  void papiOnPartDestroy( MwsAbstractPart aPart ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Adds a lifecycle event interceptor for the main application window.
   *
   * @param aInterceptor {@link IMainWindowLifeCylceListener} - the interceptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void addMainWindowLifecycleInterceptor( IMainWindowLifeCylceListener aInterceptor ) {
    windowInterceptors.add( aInterceptor );
  }

  /**
   * Removes a lifecycle event interceptor for the main application window.
   *
   * @param aInterceptor {@link IMainWindowLifeCylceListener} - the interceptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void removeMainWindowLifecycleInterceptor( IMainWindowLifeCylceListener aInterceptor ) {
    windowInterceptors.remove( aInterceptor );
  }

  /**
   * Fires an event {@link IMainWindowLifeCylceListener#beforeMainWindowOpen(IEclipseContext, MWindow)}.
   */
  public final void fireBeforeWindowOpenEvent() {
    // init for RCP/E4
    window.getContext().set( IWindowCloseHandler.class, closeHandler );
    // init MWS
    ITsApplicationInfo appInfo = window.getContext().get( IMwsOsgiService.class ).appInfo();
    window.setLabel( appInfo.nmName() );
    // fire event
    IListEdit<IMainWindowLifeCylceListener> ll = new ElemArrayList<>();
    windowInterceptors.copyTo( ll );
    for( IMainWindowLifeCylceListener l : ll ) {
      try {
        l.beforeMainWindowOpen( window.getContext(), window );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Fires an event {@link IMainWindowLifeCylceListener#beforeMainWindowClose(IEclipseContext, MWindow)}.
   */
  public void fireBeforeWindowCloseEvent() {
    LoggerUtils.defaultLogger().info( FMT_INFO_WIN_STAFF_CLOSING, window.getElementId() );
    IListEdit<IMainWindowLifeCylceListener> ll = new ElemArrayList<>();
    windowInterceptors.copyTo( ll );
    for( IMainWindowLifeCylceListener l : ll ) {
      try {
        l.beforeMainWindowClose( window.getContext(), window );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Determines if window may be closed.
   * <p>
   * All registered interceptors will be asked by the method
   * {@link IMainWindowLifeCylceListener#canCloseMainWindow(IEclipseContext, MWindow)}.
   *
   * @return boolean - permission to close the window<br>
   *         <b>true</b> - the window will be closed;<br>
   *         <b>false</b> - the window will remain open.
   */
  public boolean canCloseWindow() {
    IListEdit<IMainWindowLifeCylceListener> ll = new ElemArrayList<>();
    windowInterceptors.copyTo( ll );
    boolean canClose = true;
    for( IMainWindowLifeCylceListener l : ll ) {
      try {
        if( !l.canCloseMainWindow( window.getContext(), window ) ) {
          canClose = false;
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    if( canClose ) {
      fireBeforeWindowCloseEvent();
    }
    return canClose;
  }

}
