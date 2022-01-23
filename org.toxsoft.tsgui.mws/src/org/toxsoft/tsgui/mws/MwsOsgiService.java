package org.toxsoft.tsgui.mws;

import static org.toxsoft.tslib.ITsHardConstants.*;

import org.osgi.service.component.annotations.Component;
import org.toxsoft.tsgui.bricks.quant.IQuant;
import org.toxsoft.tsgui.mws.appinf.ITsApplicationInfo;
import org.toxsoft.tsgui.mws.appinf.TsApplicationInfo;
import org.toxsoft.tsgui.mws.osgi.IMwsOsgiService;
import org.toxsoft.tslib.bricks.ctx.ITsContext;
import org.toxsoft.tslib.bricks.ctx.impl.TsContext;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * An OSGi service {@link IMwsOsgiService} implementation.
 *
 * @author hazard157
 */
@Component
public class MwsOsgiService
    implements IMwsOsgiService {

  private static final String DEFAULT_MWS_APPLICATION_ID = TS_FULL_ID + ".mws.application.default"; //$NON-NLS-1$

  private final IListEdit<IQuant> quants = new ElemArrayList<>();

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

  @Override
  public void addQuant( IQuant aQuant ) {
    synchronized (quants) {
      if( !quants.hasElem( aQuant ) ) {
        quants.add( aQuant );
      }
    }
  }

  @Override
  public IList<IQuant> listQuants() {
    synchronized (quants) {
      return new ElemArrayList<>( quants );
    }
  }

}
