package org.toxsoft.core.tsgui.mws.bases;

import static org.toxsoft.core.tsgui.mws.bases.ITsResources.*;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.toxsoft.core.tsgui.Activator;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * MWS plugin addons base class.
 *
 * @author hazard157
 */
public abstract class MwsAbstractAddon {

  IMainWindowLifeCylceListener windowsInterceptor = new IMainWindowLifeCylceListener() {

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
      MwsMainWindowStaff mainWindowStaff = aAppContext.get( MwsMainWindowStaff.class );
      TsInternalErrorRtException.checkNull( mainWindowStaff );
      mainWindowStaff.addMainWindowLifecycleInterceptor( windowsInterceptor );
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
   * Находит зарегистрированный в OSGi сервис по его типу.
   *
   * @param <S> - тип (класс) сервиса
   * @param aSeviceClass {@link Class}&lt;S&gt; - класс сервиса
   * @return &lt;S&gt; - сервис или <code>null</code>
   * @throws TsIllegalStateRtException метод вызван при остановленном плагине
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public <S> S findOsgiService( Class<S> aSeviceClass ) {
    BundleContext context = Activator.getInstance().getBundle().getBundleContext();
    TsIllegalStateRtException.checkNull( context );
    TsNullArgumentRtException.checkNull( aSeviceClass );
    ServiceReference<S> ref = context.getServiceReference( aSeviceClass );
    if( ref != null ) {
      return context.getService( ref );
    }
    return null;
  }

  /**
   * Возвращает зарегистрированный в OSGi сервис по его типу.
   *
   * @param <S> - тип (класс) сервиса
   * @param aSeviceClass {@link Class}&lt;S&gt; - класс сервиса
   * @return &lt;S&gt; - сервис или <code>null</code>
   * @throws TsIllegalStateRtException метод вызван при остановленном плагине
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет такого сервиса
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
   * @param aWinContext {@link IEclipseContext} - контекст уровня главного окна
   * @param aWindow {@link MWindow} - the window to be closed
   */
  protected void doBeforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
    // nop
  }

  /**
   * Subclasses may register quants before inititialization {@link #initApp(IEclipseContext)} starts.
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
