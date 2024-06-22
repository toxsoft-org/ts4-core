package org.toxsoft.core.tsgui.mws.bases;

import static org.toxsoft.core.tsgui.mws.bases.ITsResources.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.toxsoft.core.tsgui.Activator;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

/**
 * MWS plugin addon base class.
 *
 * @author hazard157
 */
public abstract class MwsAbstractAddon {

  /**
   * Class allows to registered this addon as quant to the application-wide quant manager.
   *
   * @author hazard157
   */
  class ThisAddonAsQuant
      extends AbstractQuant {

    ThisAddonAsQuant( String aName ) {
      super( aName );
    }

    @Override
    protected void doInitApp( IEclipseContext aAppContext ) {
      MwsAbstractAddon.this.initApp( aAppContext );
    }

    @Override
    protected void doInitWin( IEclipseContext aWinContext ) {
      LoggerUtils.defaultLogger().info( FMT_INFO_ADDON_INIT_WIN, nameForLog );
      MwsAbstractAddon.this.initWin( aWinContext );
    }

    @Override
    protected boolean doCanCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
      return MwsAbstractAddon.this.doCanCloseMainWindow( aWinContext, aWindow );
    }

    @Override
    protected void doCloseWin( MWindow aWindow ) {
      LoggerUtils.defaultLogger().info( FMT_INFO_ADDON_CLOSE_WIN, nameForLog );
      MwsAbstractAddon.this.doBeforeMainWindowClose( aWindow.getContext(), aWindow );
    }

    @Override
    protected void doClose() {
      MwsAbstractAddon.this.doClose();
    }

  }

  @Inject
  MApplication e4Application;

  final String pluginId;
  final String nameForLog;

  /**
   * Constructor for subclasses.
   * <p>
   * Warning: subclasses must have the only one constructor without arguments!
   *
   * @param aPluginId String - plugin identifier must match plugin's symbolic name from MANIFEST.MF
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public MwsAbstractAddon( String aPluginId ) {
    pluginId = StridUtils.checkValidIdPath( aPluginId );
    nameForLog = this.getClass().getSimpleName();
  }

  @PostConstruct
  final void init( IEclipseContext aAppContext ) {
    LoggerUtils.defaultLogger().info( FMT_INFO_ADDON_STARTING, nameForLog );
    try {
      /**
       * FIXME I'don't know if is it a feature or a bug?...<br>
       * AddonXxx.initApp() are processed BEFORE quants processing starts.<br>
       * AddonXxx.initWin() are processed TOGETHER with quants processing.<br>
       * This happens because initApp() is called by E4 container @PostConstruct method (actual constructor of addon).
       * While initWin() is called from the quant processing sequence, because this addon is registered as quant.
       * <p>
       * Having initApp() run before quants is sometimes necessary to setup the plugin. However, most time it is
       * confusing for user who had set up the sequence of initialization via Quants - the code from addon's initApp()
       * runs BEFORE the quants sequence starts. Even more - only addon's initApp() behaves the 'bad' way, while
       * initWin() works as expected. The only way to avoid such misunderstanding - not to use addon's initApp() but to
       * create own quant and register it instead.
       */
      // create temporary quant manager to a) register quants, b) call #initApp()
      IQuant tmpQuantManager = new ThisAddonAsQuant( getClass().getName() );
      doRegisterQuants( tmpQuantManager );
      tmpQuantManager.initApp( aAppContext );
      // now move quants to the application-wide quant manager - windows lifecycle methods will be called on them
      IApplicationWideQuantManager appQM = e4Application.getContext().get( IApplicationWideQuantManager.class );
      appQM.registerQuant( tmpQuantManager );
      LoggerUtils.defaultLogger().info( FMT_INFO_ADDON_INIT_APP, nameForLog );
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
   * windows closing so use only this method if clean-up must be done unconditionally.
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
   * Called before application quits, after all windows are closed.
   * <p>
   * In base class does nothing, there is no need to call superclass method in subclasses.
   */
  protected void doClose() {
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

  // ------------------------------------------------------------------------------------
  // IQuant
  //

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
