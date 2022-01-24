package org.toxsoft.tsgui.mws.bases;

import static org.toxsoft.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.tsgui.mws.bases.ITsResources.*;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.toxsoft.tsgui.Activator;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.tsgui.graphics.icons.impl.TsIconManagerUtils;
import org.toxsoft.tsgui.mws.IMwsCoreConstants;
import org.toxsoft.tsgui.mws.osgi.IMwsOsgiService;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * Base class for Eclipse e4 processors implementation.
 *
 * @author hazard157
 */
public abstract class MwsAbstractProcessor {

  private IEclipseContext appContext;
  private MApplication    application;
  private EModelService   modelService;
  private MTrimmedWindow  mainWindow;
  private IMwsOsgiService mwsService;

  /**
   * Пустой конструктор без аргументов - такой же должен быть у наследника.
   */
  protected MwsAbstractProcessor() {
    // nop
  }

  @PostConstruct
  void init( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
    application = aAppContext.get( MApplication.class );
    modelService = aAppContext.get( EModelService.class );
    mainWindow = getElement( application, MWSID_WINDOW_MAIN, MTrimmedWindow.class, EModelService.ANYWHERE );
    mwsService = getOsgiService( IMwsOsgiService.class );
    try {
      doProcess();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the application level context.
   *
   * @return {@link IEclipseContext} - the application level context
   */
  IEclipseContext eclipseContext() {
    return appContext;
  }

  /**
   * Returns the e4 application.
   *
   * @return {@link MApplication} - the e4 application
   */
  public MApplication application() {
    return application;
  }

  /**
   * Returns the e4 model service.
   *
   * @return {@link EModelService} - the e4 model service
   */
  public EModelService modelService() {
    return modelService;
  }

  /**
   * Returns MWS application main window.
   *
   * @return {@link MTrimmedWindow} - MWS main window with ID {@link IMwsCoreConstants#MWSID_WINDOW_MAIN}.
   */
  public MTrimmedWindow mainWindow() {
    return mainWindow;
  }

  /**
   * Returns the {@link IMwsOsgiService}.
   *
   * @return {@link IMwsOsgiService} - OSGI service {@link IMwsOsgiService}
   */
  public IMwsOsgiService mwsService() {
    return mwsService;
  }

  /**
   * Finds specified element in e4 model of application.
   *
   * @param <T> - the type of element being searched for
   * @param aRoot {@link MElementContainer} - search root in e4 model of application
   * @param aId String - the ID of element being searched for
   * @param aClass {@link Class}&lt;T&gt; - the type of element being searched for
   * @param aFlags int - search flags {@link EModelService}<code>.XXX</code>
   * @return &lt;T&gt; - found element or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public <T> T findElement( MElementContainer<?> aRoot, String aId, Class<T> aClass, int aFlags ) {
    TsNullArgumentRtException.checkNulls( aRoot, aId, aClass );
    ElementMatcher matcher = new ElementMatcher( aId, aClass, (String)null );
    List<T> elems = modelService.findElements( aRoot, aClass, aFlags, matcher );
    if( elems.isEmpty() ) {
      return null;
    }
    return elems.get( 0 );
  }

  /**
   * Returns specified element in e4 model of application.
   *
   * @param <T> - the type of element being searched for
   * @param aRoot {@link MElementContainer} - search root in e4 model of application
   * @param aId String - the ID of element being searched for
   * @param aClass {@link Class}&lt;T&gt; - the type of element being searched for
   * @param aFlags int - search flags {@link EModelService}<code>.XXX</code>
   * @return &lt;T&gt; - found element
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException element not found
   */
  public <T> T getElement( MElementContainer<?> aRoot, String aId, Class<T> aClass, int aFlags ) {
    T elem = findElement( aRoot, aId, aClass, aFlags );
    if( elem == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_E4_ELEM, aClass.getSimpleName(), aId );
    }
    return elem;
  }

  /**
   * Fings an OSGi service by type.
   *
   * @param <S> - the type of service being searched for
   * @param aSeviceClass {@link Class}&lt;S&gt; - the type of service being searched for
   * @return &lt;S&gt; - found service or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public <S> S findOsgiService( Class<S> aSeviceClass ) {
    TsNullArgumentRtException.checkNull( aSeviceClass );
    BundleContext bundleContext = Activator.getInstance().getBundle().getBundleContext();
    ServiceReference<S> ref = bundleContext.getServiceReference( aSeviceClass );
    if( ref != null ) {
      return bundleContext.getService( ref );
    }
    return null;
  }

  /**
   * Returns an OSGi service by type.
   *
   * @param <S> - the type of service being searched for
   * @param aSeviceClass {@link Class}&lt;S&gt; - the type of service being searched for
   * @return &lt;S&gt; - found service
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException service not found
   */
  public <S> S getOsgiService( Class<S> aSeviceClass ) {
    S service = findOsgiService( aSeviceClass );
    if( service == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_AP_NO_OSGI_SERVICE, aSeviceClass.getSimpleName() );
    }
    return service;
  }

  /**
   * Constructs icon URI string.
   * <p>
   * Metho assumes that iconfiles are arranges as specified in
   * {@link ITsIconManager#registerStdIconByIds(String, Class, String)}
   * <p>
   * Returnes string may be used to set icons for e4 model entities like {@link MHandledMenuItem#setIconURI(String)}.
   *
   * @param aPluginId String - the plugin ID
   * @param aIconId String - icon ID
   * @param aIconSize {@link EIconSize} - icon size
   * @return String - URI to access icon resource in plugin
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is an empty string
   */
  public static String makeStdIconUriString( String aPluginId, String aIconId, EIconSize aIconSize ) {
    return TsIconManagerUtils.makeStdIconUriString( aPluginId, aIconId, aIconSize );
  }

  /**
   * Constructs icon URI string for <code>tsgui</code> builtin icons.
   *
   * @param aIconId String - icon ID
   * @param aIconSize {@link EIconSize} - icon size
   * @return String - URI to access icon resource in plugin
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is an empty string
   */
  public String makeTsguiIconUri( String aIconId, EIconSize aIconSize ) {
    return makeStdIconUriString( Activator.PLUGIN_ID, aIconId, aIconSize );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass may perform required processing here.
   */
  protected abstract void doProcess();

}
