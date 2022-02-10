package org.toxsoft.core.tsgui.dialogs.datarec;

import org.toxsoft.core.tsgui.dialogs.ETsDialogCode;
import org.toxsoft.core.tslib.bricks.validator.EValidationResultType;

/**
 * Handles {@link ETsDialogCode#APPLY} button press in dialog window.
 *
 * @author hazard157
 * @param <T> - data transfet object type passed to/from dialog
 * @param <E> - client specified optional environment type
 */
public interface ITsCommonDialogApplyHandler<T, E> {

  /**
   * Handler that does nothing.
   */
  @SuppressWarnings( "rawtypes" )
  ITsCommonDialogApplyHandler NULL = aDialogPanel -> {
    // nop
  };

  /**
   * Implementation must handle {@link ETsDialogCode#APPLY} button press.
   * <p>
   * Apply button is enabled only if dialog contains valid data. That is method
   * {@link TsDialog#validateDialogData()} returns {@link EValidationResultType#OK} or
   * {@link EValidationResultType#WARNING}.
   *
   * @param aDialog {@link TsDialog} - caller dialog
   */
  void onApplyDialogData( TsDialog<T, E> aDialog );

}
