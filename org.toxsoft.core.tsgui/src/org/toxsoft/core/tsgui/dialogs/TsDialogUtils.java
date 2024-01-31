package org.toxsoft.core.tsgui.dialogs;

import static org.toxsoft.core.tsgui.dialogs.ETsDialogCode.*;
import static org.toxsoft.core.tsgui.dialogs.ITsGuiDialogResources.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods for common dialog display.
 *
 * @author hazard157
 */
public class TsDialogUtils {

  /**
   * Default number of exception stakc items to show in error dialog.
   */
  private static final int DEFAULT_DISPLAYED_EX_STACK_ITEMS = 10;

  //
  // arrays of button names for different query dialogs
  private static String[] labelsClose       = { CLOSE.nmName() };
  private static String[] labelsYesNoCancel = { YES.nmName(), NO.nmName(), CANCEL.nmName() };

  // ------------------------------------------------------------------------------------
  // Message dialogs
  //

  /**
   * Displays informational modal dialog with message like "under development".
   * <p>
   * The only meaningful usage is to temporary insert in the source code under development.
   *
   * @param aShell {@link Shell} - the parent window or <code>null</code> for top-level shell
   */
  public static void underDevelopment( Shell aShell ) {
    MessageDialog dialog = new MessageDialog( aShell, //
        DLG_KIND_INFORMATION, //
        getTitleIcon(), //
        MSG_UNDER_DEVELOPMENT, //
        MessageDialog.INFORMATION, //
        labelsClose, //
        0 );
    dialog.open();
  }

  /**
   * Displays informational modal dialog with the specified message.
   * <p>
   * {@link String#format(String, Object...)} is used for text creation.
   *
   * @param aShell {@link Shell} - the parent window or <code>null</code> for top-level shell
   * @param aMsg String - format string
   * @param aArgs Object[] - arguments for format string (may contain <code>null</code>s)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void info( Shell aShell, String aMsg, Object... aArgs ) {
    TsNullArgumentRtException.checkNulls( aMsg, aArgs );
    MessageDialog dialog = new MessageDialog( aShell, //
        DLG_KIND_INFORMATION, //
        getTitleIcon(), //
        String.format( aMsg, aArgs ), //
        MessageDialog.INFORMATION, //
        labelsClose, //
        SWT.SHEET );
    dialog.open();
  }

  /**
   * Displays warning modal dialog with the specified message.
   * <p>
   * {@link String#format(String, Object...)} is used for text creation.
   *
   * @param aShell {@link Shell} - the parent window or <code>null</code> for top-level shell
   * @param aMsg String - format string
   * @param aArgs Object[] - arguments for format string (may contain <code>null</code>s)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void warn( Shell aShell, String aMsg, Object... aArgs ) {
    TsNullArgumentRtException.checkNulls( aMsg, aArgs );
    MessageDialog dialog = new MessageDialog( aShell, //
        DLG_KIND_WARNING, //
        getTitleIcon(), //
        String.format( aMsg, aArgs ), //
        MessageDialog.WARNING, //
        labelsClose, //
        SWT.SHEET );
    dialog.open();
  }

  /**
   * Displays error modal dialog with the specified message.
   * <p>
   * {@link String#format(String, Object...)} is used for text creation.
   *
   * @param aShell {@link Shell} - the parent window or <code>null</code> for top-level shell
   * @param aMsg String - format string
   * @param aArgs Object[] - arguments for format string (may contain <code>null</code>s)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void error( Shell aShell, String aMsg, Object... aArgs ) {
    TsNullArgumentRtException.checkNulls( aMsg, aArgs );
    MessageDialog dialog = new MessageDialog( aShell, //
        DLG_KIND_ERROR, //
        getTitleIcon(), //
        String.format( aMsg, aArgs ), //
        MessageDialog.ERROR, //
        labelsClose, //
        SWT.SHEET );
    dialog.open();
  }

  /**
   * Displays error modal dialog with the message about exception.
   *
   * @param aShell {@link Shell} - the parent window or <code>null</code> for top-level shell
   * @param aEx {@link Throwable} - exception to be shown
   */
  public static void error( Shell aShell, Throwable aEx ) {
    MessageDialog dialog = new MessageDialog( aShell, //
        DLG_KIND_ERROR, //
        getTitleIcon(), //
        helperFormatExceptionMessage( aEx, DEFAULT_DISPLAYED_EX_STACK_ITEMS ), //
        MessageDialog.ERROR, //
        labelsClose, //
        SWT.SHEET );
    dialog.open();
  }

  /**
   * Shows dialog with validation result message.
   * <p>
   * Dialog kind depends on {@link ValidationResult#type()}.
   *
   * @param aShell {@link Shell} - the parent window or <code>null</code> for top-level shell
   * @param aValidationResult {@link ValidationResult} - result to show
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void validationResult( Shell aShell, ValidationResult aValidationResult ) {
    TsNullArgumentRtException.checkNull( aValidationResult );
    switch( aValidationResult.type() ) {
      case OK:
        if( aValidationResult.message().isBlank() ) {
          info( aShell, MSG_OK );
        }
        else {
          info( aShell, aValidationResult.message() );
        }
        break;
      case WARNING:
        warn( aShell, aValidationResult.message() );
        break;
      case ERROR:
        error( aShell, aValidationResult.message() );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Displays list of validation results as table with some filtering/sorting controls.
   * <p>
   * If list is OK, that is {@link IValResList#isOk()} == <code>true</code> than displays info message.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - information about dialog windows
   * @param aVrList {@link IValResList} - the list to display
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void showValResList( ITsDialogInfo aDialogInfo, IValResList aVrList ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aVrList );

    // TODO TsDialogUtils.showValResList()

  }

  // ------------------------------------------------------------------------------------
  // Query dialogs
  //

  /**
   * Show modal dialog with specified query text and buttons Yes, No and Cancel.
   * <p>
   * Depending on user action returns one of the following code:
   * <ul>
   * <li>{@link ETsDialogCode#YES} - Yes button was pressed;</li>
   * <li>{@link ETsDialogCode#NO} - No button was pressed;</li>
   * <li>{@link ETsDialogCode#CANCEL} - Cancel button was pressed;</li>
   * <li>{@link ETsDialogCode#CLOSE} - dialog window was close with any other means.</li>
   * </ul>
   *
   * @param aShell {@link Shell} - parent shell, may be <code>null</code>
   * @param aMsgFmt String - message format string
   * @param aArgs Object[] - message formating arguments
   * @return {@link ETsDialogCode} - user action
   * @throws TsNullArgumentRtException any argument = <code>null</code> (except aShell)
   */
  public static ETsDialogCode askYesNoCancel( Shell aShell, String aMsgFmt, Object... aArgs ) {
    TsNullArgumentRtException.checkNull( aMsgFmt );
    TsErrorUtils.checkArrayArg( aArgs );
    MessageDialog dialog = new MessageDialog( aShell, //
        DLG_KIND_CONFIRMATION, //
        getTitleIcon(), //
        String.format( aMsgFmt, aArgs ), //
        MessageDialog.CONFIRM, //
        labelsYesNoCancel, //
        SWT.SHEET );
    return switch( dialog.open() ) {
      case 0 -> ETsDialogCode.YES;
      case 1 -> ETsDialogCode.NO;
      case 2 -> ETsDialogCode.CANCEL;
      default -> ETsDialogCode.CLOSE;
    };
  }

  // TODO TRANSLATE

  /**
   * Interactively handles message about validation result.
   * <p>
   * Behaviour depends on validation result type:
   * <ul>
   * <li>success {@link EValidationResultType#OK} - immediately returns {@link ETsDialogCode#YES};</li>
   * <li>error {@link EValidationResultType#ERROR} - displays error message dialog
   * {@link #error(Shell, String, Object...)}, and after {@link ETsDialogCode#NO};</li>
   * <p>
   * <p>
   * <p>
   * // TODO TRANSLATE
   * <p>
   * <p>
   * <p>
   * <li>предупреждение {@link EValidationResultType#ERROR} - показывает диалог с кнопками Yes, No, Cancel и сообщением
   * {@link ValidationResult#message()}, дополненным вопросом aWarningQuestion. В зависимости от реакции пользователя,
   * возвращает одно из значении {@link ETsDialogCode#YES}, {@link ETsDialogCode#NO}, {@link ETsDialogCode#CANCEL},
   * {@link ETsDialogCode#CLOSE}.</li>
   * </ul>
   * Смысл этого метода - отработать с пользователем проверямое действие. При успехе молча продолжается выполнение
   * программы (код возврата {@link ETsDialogCode#YES YES}), при ошибке показывается модальный диалог без выбора, и
   * выполнение проверяемого действия следует прервать (код возврата {@link ETsDialogCode#NO NO}). При предупреждении,
   * выбор продолжения или отмены действия остается от пользователем.
   * <p>
   * Возвращаемое значение {@link ETsDialogCode#YES YES} всегда означает продолжение работы, все другие коды - означают,
   * что проверяемое действие следует прервать.
   *
   * @param aShell {@link Shell} - родительское окно
   * @param aValidationResult {@link ValidationResult} - результат проверки
   * @param aWarningQuestion String - дополнительный вопрос при предупреждении (форматная строка вопроса)
   * @param aArgs Object[] - аргументы форматной строки aWarningQuestion по правилам
   *          {@link String#format(String, Object...)}
   * @return {@link ETsDialogCode} - YES означает, что надо продолжить проверяемое действие, остальное - действие
   *         следует прервать
   * @throws TsNullArgumentRtException aValidationResult или aWarningQuestion = null
   */
  public static ETsDialogCode askContinueOnValidation( Shell aShell, ValidationResult aValidationResult,
      String aWarningQuestion, Object... aArgs ) {
    TsNullArgumentRtException.checkNulls( aValidationResult, aWarningQuestion );
    if( aValidationResult.isOk() ) {
      return ETsDialogCode.YES;
    }
    if( aValidationResult.isError() ) {
      error( aShell, aValidationResult.message() );
      return ETsDialogCode.NO;
    }
    String msg = String.format( aWarningQuestion, aArgs );
    MessageDialog dialog = new MessageDialog( aShell, //
        DLG_KIND_WARNING, //
        getTitleIcon(), //
        aValidationResult.message() + '\n' + '\n' + msg, //
        MessageDialog.WARNING, //
        labelsYesNoCancel, //
        SWT.SHEET );
    return switch( dialog.open() ) {
      case 0 -> ETsDialogCode.YES;
      case 1 -> ETsDialogCode.NO;
      case 2 -> ETsDialogCode.CANCEL;
      default -> ETsDialogCode.CLOSE;
    };
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static Image getTitleIcon() {
    Image titleIcon = null;
    // FIXME GOGA MWA
    // IEclipseContext winContext = TsGuiUtils.findGuiThreadWinContext();
    // if( winContext != null ) {
    // ITsIconManager iconManager = winContext.get( ITsIconManager.class );
    // titleIcon = iconManager.loadStdIcon( ITsStdIconIds.ICONID_TSAPP_WINDOWS_ICON, EIconSize.IS_16X16 );
    // }
    return titleIcon;
  }

  /**
   * Constructs multi-line text from the exception.
   *
   * @param aEx {@link Throwable} - exception to be shown
   * @param aMaxDisplayedstackItems - number of shown stack items or <=0 for default number
   * @return String - multi-line text
   */
  private static String helperFormatExceptionMessage( Throwable aEx, int aMaxDisplayedstackItems ) {
    StringBuilder sb = new StringBuilder();
    String exClassName = aEx.getClass().getSimpleName();
    if( aEx instanceof ITimestampable ) {
      Long time = Long.valueOf( ((ITimestampable)aEx).timestamp() );
      sb.append( String.format( "%tF %tT ", time, time ) ); //$NON-NLS-1$
    }
    String exMsg = aEx.getLocalizedMessage();
    if( exMsg == null ) {
      exMsg = exClassName;
    }
    sb.append( String.format( FMT_TS_EXCEPTION_MSG, exClassName, exMsg ) );
    StackTraceElement[] se = aEx.getStackTrace();
    int displayedstackItemsCount = aMaxDisplayedstackItems;
    if( displayedstackItemsCount < 0 ) {
      displayedstackItemsCount = DEFAULT_DISPLAYED_EX_STACK_ITEMS;
    }
    if( displayedstackItemsCount > se.length ) {
      displayedstackItemsCount = se.length;
    }
    for( int i = 0; i < displayedstackItemsCount; i++ ) {
      sb.append( '\n' );
      sb.append( se[i].toString() );
    }
    return sb.toString();
  }

  /**
   * Inhibit instance creation.
   */
  private TsDialogUtils() {
    // nop
  }

}
