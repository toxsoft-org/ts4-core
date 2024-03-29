package org.toxsoft.core.tslib.bricks.gentask;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.logs.*;

/**
 * The package constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IGenericTaskConstants {

  String ID_PREFIX = TS_ID + ".GenericTask"; //$NON-NLS-1$

  String REFID_IN_LOGGER            = ID_PREFIX + ".in.Logger";           //$NON-NLS-1$
  String REFID_IN_PROGRESS_CALLBACK = ID_PREFIX + ".in.ProgressCallback"; //$NON-NLS-1$
  String REFID_OUT_TASK_RESULT      = ID_PREFIX + ".out.TaskResult";      //$NON-NLS-1$
  String REFID_OUT_TASK_INFO        = ID_PREFIX + ".out.TaskInfo";        //$NON-NLS-1$
  String REFID_OUT_INPUT            = ID_PREFIX + ".out.Input";           //$NON-NLS-1$

  /**
   * Task input: contains optional logger to be used during task execution.
   */
  ITsContextRefDef<ILogger> REFDEF_IN_TASK_LOGGER = //
      TsContextRefDef.create( REFID_IN_LOGGER, ILogger.class, //
          TSID_IS_MANDATORY, AV_FALSE //
      );

  /**
   * Task input: contains optional callback for long operations monitoring.
   */
  ITsContextRefDef<ILongOpProgressCallback> REFDEF_IN_PROGRESS_MONITOR = //
      TsContextRefDef.create( REFID_IN_PROGRESS_CALLBACK, ILongOpProgressCallback.class, //
          TSID_IS_MANDATORY, AV_FALSE //
      );

  /**
   * Task output: {@link ValidationResult} briefly describes success of the task execution.
   */
  ITsContextRefDef<ValidationResult> REFDEF_OUT_TASK_RESULT = //
      TsContextRefDef.create( REFID_OUT_TASK_RESULT, ValidationResult.class, //
          TSID_IS_MANDATORY, AV_FALSE //
      );

  /**
   * Task output: {@link IGenericTaskInfo} of the task is passed to the output.
   */
  ITsContextRefDef<IGenericTaskInfo> REFDEF_OUT_TASK_INFO = //
      TsContextRefDef.create( REFID_OUT_TASK_RESULT, IGenericTaskInfo.class, //
          TSID_IS_MANDATORY, AV_TRUE //
      );

  /**
   * Task output: {@link ITsContextRo} specified as an input is passed to the output.
   */
  ITsContextRefDef<ITsContextRo> REFDEF_OUT_INPUT = //
      TsContextRefDef.create( REFID_OUT_TASK_RESULT, ITsContextRo.class, //
          TSID_IS_MANDATORY, AV_TRUE //
      );

}
