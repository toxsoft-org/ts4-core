package org.toxsoft.core.tsgui.dialogs.datarec;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;

/**
 * {@link TsDialog} framework constants.
 *
 * @author hazard157
 */
public interface ITsDialogConstants {

  /**
   * Dialog flag: use vertical scroll bar to scroll dialog content panel.
   */
  int DF_V_SCROLLER = 0x00000001;

  /**
   * Dialog flag: use horizontal scroll bar to scroll dialog content panel.
   */
  int DF_H_SCROLLER = 0x00000002;

  /**
   * Dialog flag: make dialog non-modal (by default dialogs ar modal).
   * <p>
   * Note: methods <code>{@link TsDialog},execXxx()</code> return immediately leaving open dialog window.
   */
  int DF_NONMODAL = 0x00000004;

  /**
   * Dialog flag: use "Close" button instead of pair "OK"/"Cancel".
   */
  int DF_NO_APPROVE = 0x00000010;

  // TODO TRANSLATE

  /**
   * Dialog flag: добавить кпонку "Принять" (Apply) к кнопкам диалога.
   * <p>
   * Без указания этого флага, на диалоге будет либо кнопки OK/Cancel либо одна кнопка Close - в зависимости от флага
   * {@link #DF_NO_APPROVE}.
   */
  int DF_CAN_APPLY = 0x00000020;

  /**
   * Dialog flag: данные доступны только для просмотра, не для редактирования.
   * <p>
   * Этот флаг имеет смысл как для диалогов просмотра (у которых только одна кнопка Close), так и для панелей
   * {@link AbstractTsDialogPanel} просмотра информации. В зависимости от наличии этого флага (который обязательно
   * передается в конструкторе, могут создаваться разные котнтроли. Например, дата в режиме редактирования отображается
   * в {@link DateTime}, в в режиме только просмотра в {@link Label}.
   */
  int DF_READONLY = 0x00000040;

  /**
   * Dialog flag: при каждом изменении в панели диалога проверять валидность данных.
   * <p>
   * Флаг не нужно устанавливать когда выставлен {@link #DF_CAN_APPLY} или сброшен {@link #DF_NO_APPROVE} - в таких
   * случаях диалог сам значет, что нужно валидировать содержимое.
   */
  int DF_NEED_VALIDATION = 0x00000080;

  /**
   * Набор битов, выделенных для пользовательских нужд в флагах диалога.
   * <p>
   * Старшая половина из 32-х возможных битов (int - 32-х разрядное число) выделено для пользовательских флагов.
   */
  int USER_BITS_MASK = 0xFFFF0000;

  /**
   * Common use case - data viewer dialog with Close button.
   */
  int DFSET_INFO = DF_NO_APPROVE | DF_READONLY;

  /**
   * Common use case - data edit dialog with OK/Cancel buttons.
   */
  int DFSET_EDIT = 0;

}
