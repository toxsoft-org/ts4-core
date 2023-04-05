package org.toxsoft.core.tsgui.mws;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.osgi.service.component.annotations.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * An OSGi service {@link IMwsOsgiService} implementation.
 *
 * @author hazard157
 */
@Component
public class MwsOsgiService
    implements IMwsOsgiService {

  private static final String DEFAULT_MWS_APPLICATION_ID = TS_FULL_ID + ".mws.application.default"; //$NON-NLS-1$

  private final TsContext context = new TsContext();

  /**
   * Application at startup must load real app info with {@link #setAppInfo(ITsApplicationInfo)}.
   */
  private ITsApplicationInfo appInfo = new TsApplicationInfo( DEFAULT_MWS_APPLICATION_ID );

  /**
   * Constructor.
   */
  public MwsOsgiService() {
    LoggerUtils.defaultLogger().info( "MWS:        %s()", this.getClass().getSimpleName() ); //$NON-NLS-1$
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
