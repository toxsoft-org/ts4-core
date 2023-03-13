package org.toxsoft.core.tsgui.mws.bases;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
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
public class MwaWindowStaff {

  /**
   * Close handler to be set as RCP means.
   */
  private final IWindowCloseHandler closeHandler = aWindow -> {
    if( canCloseWindow() ) {
      fireBeforeWindowCloseEvent();
      return true;
    }
    return false;
  };

  static class WindowLifecycleInterceptor
      implements IMainWindowLifeCylceListener {

    public WindowLifecycleInterceptor() {
      // TODO Auto-generated constructor stub
    }

    @Override
    final public void beforeMainWindowOpen( IEclipseContext aWinContext, MWindow aWindow ) {
      try {
        LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_INIT_WIN, nameForLog );
        ITsE4Helper e4Helper = new TsE4Helper( aWinContext );
        aWinContext.set( ITsE4Helper.class, e4Helper );
        // init quants
        quantManager.initWin( aWinContext );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }

    @Override
    final public boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
      try {
        return quantManager.canCloseMainWindow( aWinContext, aWindow );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        return true;
      }
    }

    @Override
    public void beforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
      try {
        quantManager.close();
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }

  }

  private final IListEdit<IMainWindowLifeCylceListener> windowInterceptors =
      new SynchronizedListEdit<>( new ElemArrayList<>( false ) );

  private final MWindow         window;
  private final IEclipseContext winContext;

  /**
   * Constructs instance and initialized windows context.
   *
   * @param aWindow {@link MWindow} - the window to bind the instance with
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MwaWindowStaff( MWindow aWindow ) {
    window = TsNullArgumentRtException.checkNull( aWindow );
    winContext = window.getContext();
    // put instance into the context
    TsInternalErrorRtException.checkNoNull( winContext.get( MwaWindowStaff.class ) );
    winContext.set( MwaWindowStaff.class, this );
    // init for RCP/E4
    winContext.set( IWindowCloseHandler.class, closeHandler );
    // init MWS
    ITsApplicationInfo appInfo = winContext.get( IMwsOsgiService.class ).appInfo();
    aWindow.setLabel( appInfo.nmName() );
    ITsE4Helper e4Helper = new TsE4Helper( winContext );
    winContext.set( ITsE4Helper.class, e4Helper );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Adds an lifecycle event interceptor for the main application window.
   *
   * @param aInterceptor {@link IMainWindowLifeCylceListener} - the interceptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void addMainWindowLifecycleInterceptor( IMainWindowLifeCylceListener aInterceptor ) {
    windowInterceptors.add( aInterceptor );
  }

  /**
   * Fires an event {@link IMainWindowLifeCylceListener#beforeMainWindowOpen(IEclipseContext, MWindow)}.
   */
  public final void fireBeforeWindowOpenEvent() {
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
   * All registered interceptors will ask by the method
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
