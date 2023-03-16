package org.toxsoft.core.tsgui.mws.bases;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.toxsoft.core.tsgui.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The single instance of this class is placed in the application level context.
 *
 * @author hazard157
 */
public class MwaApplicationStaff__1 {

  private final MApplication    e4App;
  private final IEclipseContext appContext;
  private final IQuant          quantManager;

  /**
   * Constructs instance and initialized application context.
   *
   * @param aApplication {@link MApplication} - the application
   */
  public MwaApplicationStaff__1( MApplication aApplication ) {
    TsNullArgumentRtException.checkNull( aApplication );
    e4App = aApplication;
    appContext = aApplication.getContext();
    TsInternalErrorRtException.checkNull( appContext );
    quantManager = new QuantBase( super.getClass().getName() );
    // put instance into the context
    TsInternalErrorRtException.checkNoNull( appContext.get( MwaApplicationStaff__1.class ) );
    appContext.set( MwaApplicationStaff__1.class, this );
    quantManager.registerQuant( new QuantTsGui() );
  }

  void initApp( IEclipseContext aAppContext ) {
    quantManager.initApp( aAppContext );
  }

  void initWin( IEclipseContext aWinContext ) {
    quantManager.initWin( aWinContext );
  }

}
