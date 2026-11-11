package org.toxsoft.core.tslib.bricks.coopcomp;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * {@link ITsCooperativeComponent} subsystem constants.
 *
 * @author hazard157
 */
public interface ITsCoopCompConstants {

  /**
   * ID of the option {@link #OPDEF_COMPONENT_STOPPING_TIMEOUT_MSECS}.
   */
  String OPID_COMPONENT_STOPPING_TIMEOUT_MSECS = TS_ID + ".components.StoppingTimoutMsecs"; //$NON-NLS-1$

  /**
   * {@link ITsCooperativeContainer} option: time interval in milliseconds after stop request and forced destroy of the
   * unit.<br>
   * Usage: call to the {@link ITsCooperativeComponent#queryStop()} may begin the stopping process. If stopping process
   * takes more than specified by this parameter time, unit will be forcefully destroyed and removed from the
   * container.<br>
   * Type: {@link EAtomicType#INTEGER}<br>
   * Default value: 3000 (3 seconds)
   */
  IDataDef OPDEF_COMPONENT_STOPPING_TIMEOUT_MSECS = DataDef.create( OPID_COMPONENT_STOPPING_TIMEOUT_MSECS, INTEGER, //
      TSID_MIN_INCLUSIVE, AV_1, //
      TSID_MAX_INCLUSIVE, avInt( 10 * 365 * 86_400_000L ), //
      TSID_DEFAULT_VALUE, avInt( 3000L ) //
  );

}
