package org.toxsoft.core.tsgui.mws;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.mws.l10n.ITsguiMwsSharedResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.osgi.service.environment.*;
import org.osgi.framework.*;
import org.osgi.service.component.*;
import org.osgi.service.component.annotations.*;
import org.toxsoft.core.tsgui.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tsgui.mws.quants.progargs.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;

/**
 * An OSGi service {@link IMwsOsgiService} implementation.
 *
 * @author hazard157
 */
@Component
public class MwsOsgiService
    implements IMwsOsgiService {

  // FIXME when and how to load TsApplicationInfo from file ?

  /**
   * Specifies the file (as kept by {@link TsApplicationInfo#KEEPER} to load application info from.
   */
  public static final String CMDLINEARG_TS_APP_INFO_FILE = "TsAppInfoFile"; //$NON-NLS-1$

  static class ApplicationWideQuantManager
      extends QuantBase
      implements IApplicationWideQuantManager {

    public ApplicationWideQuantManager() {
      super( "ApplicationWideQuantManager" ); //$NON-NLS-1$
    }

    /**
     * This method is called after all quants was processed.
     */
    @Override
    protected void doInitWin( IEclipseContext aWinContext ) {
      // window and application icon
      MTrimmedWindow mainWindow = aWinContext.get( MTrimmedWindow.class );
      ITsIconManager iconMan = aWinContext.get( ITsIconManager.class );
      String imgUri = iconMan.findStdIconBundleUri( ICONID_TSAPP_WINDOWS_ICON, EIconSize.IS_48X48 );
      mainWindow.setIconURI( imgUri );
    }

  }

  private static final String DEFAULT_MWS_APPLICATION_ID = TS_FULL_ID + ".mws.application.default"; //$NON-NLS-1$

  private final TsContext context = new TsContext();

  /**
   * Application at startup must load real app info with {@link #setAppInfo(ITsApplicationInfo)}.
   */
  private ITsApplicationInfo appInfo = new TsApplicationInfo( DEFAULT_MWS_APPLICATION_ID );

  ComponentContext componentContext;

  /**
   * Constructor.
   */
  public MwsOsgiService() {
    LoggerUtils.defaultLogger().info( "MWS:        %s()", this.getClass().getSimpleName() ); //$NON-NLS-1$
    IApplicationWideQuantManager appQMan = new ApplicationWideQuantManager();
    context.put( IApplicationWideQuantManager.class, appQMan );
    appQMan.registerQuant( new QuantProgramArgs() );
    appQMan.registerQuant( new QuantTsGui() );
    appQMan.registerQuant( new QuantM5() );
  }

  @Activate
  void onActivate( ComponentContext aComponentContext ) {
    // load TS application info from the file specified in command line if any
    componentContext = aComponentContext;
    BundleContext bc = componentContext.getBundleContext();
    ServiceReference<EnvironmentInfo> ref = bc.getServiceReference( EnvironmentInfo.class );
    EnvironmentInfo envInfo = ref != null ? bc.getService( ref ) : null;
    if( envInfo != null ) {
      ProgramArgs pa = new ProgramArgs( envInfo.getCommandLineArgs() );
      String fileName = pa.getArgValue( CMDLINEARG_TS_APP_INFO_FILE, null );
      if( fileName != null ) {
        File f = new File( fileName );
        if( f.exists() ) {
          try {
            appInfo = TsApplicationInfo.KEEPER.read( f );
          }
          catch( @SuppressWarnings( "unused" ) Exception ex ) {
            LoggerUtils.errorLogger().warning( FMT_LOG_WARN_INV_APP_INFO_FILE, f.getAbsoluteFile() );
          }
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IMwsOsgiService
  //

  @Override
  public ITsContext context() {
    return context;
  }

  @Override
  public ITsApplicationInfo appInfo() {
    return appInfo;
  }

  @Override
  public void setAppInfo( ITsApplicationInfo aAppInfo ) {
    TsNullArgumentRtException.checkNull( aAppInfo );
    appInfo = aAppInfo;
  }

}
