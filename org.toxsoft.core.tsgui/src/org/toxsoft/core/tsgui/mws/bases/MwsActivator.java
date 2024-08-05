package org.toxsoft.core.tsgui.mws.bases;

import static org.toxsoft.core.tsgui.mws.bases.ITsResources.*;

import org.osgi.framework.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import jakarta.inject.*;

/**
 * Base class for plugin activators designed for MWS.
 *
 * @author hazard157
 */
public class MwsActivator
    implements BundleActivator {

  @Inject

  private final String  pluginId;
  private BundleContext context = null;

  /**
   * Constructor.
   * <p>
   * Warning: subclass must have only one constructor without arguments.
   *
   * @param aPluginId String - the plugin ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public MwsActivator( String aPluginId ) {
    pluginId = StridUtils.checkValidIdPath( aPluginId );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  final protected <T extends MwsActivator> void checkInstance( T aInstance ) {
    if( aInstance != null ) {
      TsInternalErrorRtException ex = new TsInternalErrorRtException( FMT_ERR_NON_SINGLETON, pluginId );
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  // ------------------------------------------------------------------------------------
  // BundleActivator
  //

  @Override
  final public void start( BundleContext aBundleContext )
      throws Exception {
    LoggerUtils.defaultLogger().info( FMT_INFO_ACTIVATOR_START, pluginId, this.getClass().getSimpleName() );
    context = aBundleContext;
    // allow to register priority quants
    IMwsOsgiService mwsOsgiService = findOsgiService( IMwsOsgiService.class );
    if( mwsOsgiService != null ) { // mwsOsgiService = null only for plugin org.toxsoft.core.tsgui.mws
      IApplicationWideQuantManager appQMan = mwsOsgiService.context().find( IApplicationWideQuantManager.class );
      TsInternalErrorRtException.checkNull( appQMan );
      doRegisterPriorityQuants( appQMan );
    }
    // check plugin ID
    String pluginIdFromBundle = context.getBundle().getSymbolicName();
    if( !pluginIdFromBundle.equals( pluginId ) ) {
      throw new TsException( FMT_ERR_NONEQ_PLUGIN_IDS, pluginId, pluginId, pluginIdFromBundle );
    }
    // call subclass
    try {
      doStart();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  @Override
  final public void stop( BundleContext aBundleContext )
      throws Exception {
    LoggerUtils.defaultLogger().info( FMT_INFO_ACTIVATOR_STOP, pluginId, this.getClass().getSimpleName() );
    // call subclass
    try {
      doStop();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
    context = null;
  }

  // ------------------------------------------------------------------------------------
  // To override implement
  //

  protected void doRegisterPriorityQuants( IQuantRegistrator aQuantRegistrator ) {
    // nop
  }

  protected void doStart() {
    // nop
  }

  protected void doStop() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает контекст плагина.
   *
   * @return {@link BundleContext} - контекст плагина или <code>null</code> при остановленном плагине
   */
  public BundleContext getBundleContext() {
    return context;
  }

  /**
   * Возвращает идентификатор плагина.
   *
   * @return String - идентификатор плагина
   */
  public String getPluginId() {
    return pluginId;
  }

  /**
   * Возвращает ссылку на установленный в OSGI плагин.
   *
   * @return {@link Bundle} - OSGI плагин, реализующий модуль
   * @throws TsIllegalStateRtException метод вызван при остановленном плагине
   */
  public Bundle getBundle() {
    TsIllegalStateRtException.checkNull( context );
    return context.getBundle();
  }

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

}
