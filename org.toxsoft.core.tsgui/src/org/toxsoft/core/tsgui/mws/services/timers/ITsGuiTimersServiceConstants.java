package org.toxsoft.core.tsgui.mws.services.timers;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * {@link ITsGuiTimersService} configuration constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsGuiTimersServiceConstants {

  String OPID_QUICK_TIMER_PERIOD  = "QuickTimerPeriod";   //$NON-NLS-1$
  String OPID_SLOW_TIMER_PERIOD   = "QuickTimerPeriod";   //$NON-NLS-1$
  String OPID_GRANULARITY_DIVIDER = "GranularityDivider"; //$NON-NLS-1$

  IDataDef OPDEF_QUICK_TIMER_PERIOD = DataDef.create( OPID_QUICK_TIMER_PERIOD, INTEGER, //
      TSID_DEFAULT_VALUE, avInt( 20 ) //
  );

  IDataDef OPDEF_SLOW_TIMER_PERIOD = DataDef.create( OPID_SLOW_TIMER_PERIOD, INTEGER, //
      TSID_DEFAULT_VALUE, avInt( 300 ) //
  );

  IDataDef OPDEF_GRANULARITY_DIVIDER = DataDef.create( OPID_GRANULARITY_DIVIDER, INTEGER, //
      TSID_DEFAULT_VALUE, avInt( 12 ) //
  );

}
