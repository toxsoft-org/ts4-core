package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods.
 *
 * @author hazard157
 */
public class GenericTaskUtils {

  /**
   * Checks if task input is valid.
   * <p>
   * The input options and references are checked against {@link IGenericTaskInfo#inOps()} and
   * {@link IGenericTaskInfo#inRefs()} definitions. Options and references not listed in
   * {@link IGenericTaskInfo#inOps()} and {@link IGenericTaskInfo#inRefs()} are ignored.
   *
   * @param aTaskInfo {@link IGenericTaskInfo} - task information to check for
   * @param aInput {@link ITsContextRo} - the task input (options and references)
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult validateInput( IGenericTaskInfo aTaskInfo, ITsContextRo aInput ) {
    TsNullArgumentRtException.checkNull( aInput );
    // check against option definitions
    ValidationResult vr = OptionSetUtils.validateOptionSet( aInput.params(), aTaskInfo.inOps() );
    if( vr.isError() ) {
      return vr;
    }
    // check against references definitions
    vr = ValidationResult.firstNonOk( vr, TsContextBase.validateRefDefs( aInput, aTaskInfo.inRefs() ) );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * No subclasses.
   */
  private GenericTaskUtils() {
    // nop
  }

}
