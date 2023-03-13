package org.toxsoft.core.tsgui.mws.bases;

import static org.toxsoft.core.tsgui.mws.bases.ITsResources.*;

import javax.annotation.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.osgi.framework.*;
import org.toxsoft.core.tsgui.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * MWS plugin addon base class.
 *
 * @author hazard157
 */
public abstract class MwsAbstractAddon {

  /**
   * Calls windows lifecycle handling methods of the registered quants and this addon.
   */
  private final IMainWindowLifeCylceListener windowsInterceptor = new IMainWindowLifeCylceListener() {

    @Override
    public void beforeMainWindowOpen( IEclipseContext aWinContext, MWindow aWindow ) {
      try {
        quantManager.initWin( aWinContext );
        LoggerUtils.defaultLogger().info( FMT_INFO_ADDON_INIT_WIN, nameForLog );
        initWin( aWinContext );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        throw ex;
      }
    }

    @Override
    public void beforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
      quantManager.close();
      doBeforeMainWindowClose( aWinContext, aWindow );
    }

    @Override
    public boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
      if( quantManager.canCloseMainWindow( aWinContext, aWindow ) ) {
        return doCanCloseMainWindow( aWinContext, aWindow );
      }
      return false;
    }

  };

  final String pluginId;
  final String nameForLog;
  final IQuant quantManager;

  /**
   * Constructor for subclasses.
   * <p>
   * Warning: subclasses must have the only one constructor without arguments!
   *
   * @param aPluginId String - plugin identifier must match plugin's symblic name from MANIFEST.MF
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public MwsAbstractAddon( String aPluginId ) {
    pluginId = StridUtils.checkValidIdPath( aPluginId );
    nameForLog = this.getClass().getSimpleName();
    quantManager = new QuantBase( super.getClass().getName() );
  }

  @PostConstruct
  final void init( IEclipseContext aAppContext ) {
    LoggerUtils.defaultLogger().info( FMT_INFO_ADDON_STARTING, nameForLog );
    try {
      MwaWindowStaff winStaff = aAppContext.get( MwaWindowStaff.class );
      TsInternalErrorRtException.checkNull( winStaff );
      winStaff.addMainWindowLifecycleInterceptor( windowsInterceptor );
      doRegisterQuants( quantManager );
      LoggerUtils.defaultLogger().info( FMT_INFO_ADDON_INIT_APP, nameForLog );
      quantManager.initApp( aAppContext );
      initApp( aAppContext );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Finds registered OSGi service.
   *
   * @param <S> - the expected type of the service
   * @param aSeviceClass {@link Class}&lt;S&gt; - the expected type of the service
   * @return &lt;S&gt; - found service or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException method is called when plugin is stopped
   */
  public <S> S findOsgiService( Class<S> aSeviceClass ) {
    BundleContext context = Activator.getInstance().getBundle().getBundleContext();
    TsNullArgumentRtException.checkNull( aSeviceClass );
    TsIllegalStateRtException.checkNull( context );
    ServiceReference<S> ref = context.getServiceReference( aSeviceClass );
    if( ref != null ) {
      return context.getService( ref );
    }
    return null;
  }

  /**
   * Returns registered OSGi service.
   *
   * @param <S> - the expected type of the service
   * @param aSeviceClass {@link Class}&lt;S&gt; - the expected type of the service
   * @return &lt;S&gt; - found service
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException method is called when plugin is stopped
   * @throws TsItemNotFoundRtException no such service
   */
  public <S> S getOsgiService( Class<S> aSeviceClass ) {
    S service = findOsgiService( aSeviceClass );
    if( service == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_OSGI_SERVICE, pluginId, aSeviceClass.getName() );
    }
    return service;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Determines if main window may be closed.
   * <p>
   * In base class returns s<code>true</code>, there is no need to call superclass method in subclasses.
   *
   * @param aWinContext {@link IEclipseContext} - the window level context
   * @param aWindow {@link MWindow} - the window to be closed
   * @return boolean - <code>true</code> if window may be closed
   */
  protected boolean doCanCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
    return true;
  }

  /**
   * Subclass may perform additional clean-up before window is closed.
   * <p>
   * Note: #{@link #doCanCloseMainWindow(IEclipseContext, MWindow)} may not be called if someone already declined
   * windows closeing so use only this method if clean-up must be done unconditionally.
   * <p>
   * In base class does nothing, there is no need to call superclass method in subclasses.
   *
   * @param aWinContext {@link IEclipseContext} - window level context
   * @param aWindow {@link MWindow} - the window to be closed
   */
  protected void doBeforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
    // nop
  }

  /**
   * Subclasses may register quants before initialization {@link #initApp(IEclipseContext)} starts.
   * <p>
   * In base class does nothing, there is no need to call superclass method in subclasses.
   *
   * @param aQuantRegistrator {@link IQuantRegistrator} - quants registrator
   */
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    // nop
  }

  /**
   * Is called one per application.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
  abstract protected void initApp( IEclipseContext aAppContext );

  /**
   * Is called one when main window is already created but not yet shown.
   *
   * @param aWinContext {@link IEclipseContext} - window level context
   */
  abstract protected void initWin( IEclipseContext aWinContext );

}
