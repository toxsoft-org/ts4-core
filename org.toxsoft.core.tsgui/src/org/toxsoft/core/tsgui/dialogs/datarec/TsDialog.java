package org.toxsoft.core.tsgui.dialogs.datarec;

import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Common dialog with OK/Cancel or Close buttons that has a {@link AbstractTsDialogPanel} as its content.
 * <p>
 * <h3>Motivation</h3> Main goal of {@link TsDialog} framework is to make dialog creation easy and straightforward for
 * developers. Created dialogs have yhe same look and behave the same way.
 * <h3>Concept</h3> Dialog is considered as means to view and/or edit some <b>data record</b> in specified optional
 * <b>environment</b>. Date record has type <b>&lt;T&gt;</b> and environmant has type <b>&lt;E&gt;</b>. There is few
 * main use cases of dialog usage:
 * <ul>
 * <li>informational - modal dialog just displays the data record and has one button "Close". This mode is specified by
 * the set of flags {@link ITsDialogConstants#DFSET_INFO};</li>
 * <li>editor - modal dialog allows to edit data record. Two buttons "OK" and "Cancel" allows to confirm or ignore
 * edits. This is default mode and specified with the set of flags {@link ITsDialogConstants#DFSET_INFO}</li>
 * </ul>
 * Other <code>DF_XXX</code> flags from {@link ITsDialogConstants} allows to fine-tune dialog window like make it
 * non-modal ({@link ITsDialogConstants#DF_NONMODAL}) or add "Apply" button ({@link ITsDialogConstants#DF_CAN_APPLY}).
 * <h3>Usage</h3>
 * <ul>
 * <li>create content panel as described in comments to the class {@link AbstractTsDialogPanel};</li>
 * <li>prepare dialog settings - create instance of {@link ITsDialogInfo};</li>
 * <li>create {@link TsDialog} instance with content and tuned info;</li>
 * <li>call one of the <b><code>execXxx()</code></b> method to invoke dialog.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - data transfet object type passed to/from dialog
 * @param <E> - client specified optional environment type
 */
public class TsDialog<T, E>
    implements ITsGuiContextable {

  // TODO TRANSLATE

  /**
   * Window widget implementation.
   *
   * @author hazard157
   */
  class DialogWindow
      extends TitleAreaDialog
      implements IGenericChangeListener {

    /**
     * Стиль окна диалога.
     * <p>
     * Имеет установленные флаги {@link SWT#RESIZE} и {@link SWT#CLOSE}.
     */
    public static final int DEFAULT_DIALOG_STYLE = SWT.RESIZE | SWT.CLOSE;

    /**
     * Признак, что закрытие окна происходит по нажатию кнопки диалога или средствами окна: (крестик на заголовке, меню
     * окна).
     * <p>
     * Только если этот признак false, будет вызван {@link IButtonPressCallback#onDialogButtonPressed(ETsDialogCode)} с
     * кодом {@link ETsDialogCode#CLOSE} при попытке закрыть окно.
     */
    private boolean buttonWasPressed = false;

    DialogWindow( Shell parentShell ) {
      super( parentShell );
      boolean isNonModal = (dialogInfo.flags() & DF_NONMODAL) != 0;
      int style = getShellStyle() | DEFAULT_DIALOG_STYLE;
      if( isNonModal ) {
        style = style & ~SWT.APPLICATION_MODAL;
        style |= SWT.MODELESS;
      }
      setShellStyle( style );
      setBlockOnOpen( !isNonModal );
    }

    // ------------------------------------------------------------------------------------
    // implementation
    //

    /**
     * Добавляет кнопку в диалог с заданным в {@link TsDialog#setButtonText(ETsDialogCode, String)} текстом.
     *
     * @param aParent Composite - куда добавляется кнопка
     * @param aButtonId {@link ETsDialogCode} - идентификатор кнопки
     */
    private void addDialogButton( Composite aParent, ETsDialogCode aButtonId ) {
      boolean isDefault = aButtonId == ETsDialogCode.OK || aButtonId == ETsDialogCode.CLOSE;
      createButton( aParent, aButtonId.jfaceButtonId(), buttonTexts.findByKey( aButtonId ), isDefault );
    }

    private int getAdjustedWidth( Rectangle aDispBounds, int aWidth ) {
      if( aWidth == 0 || aWidth == SWT.DEFAULT ) {
        return 0;
      }
      if( aWidth < 0 ) {
        int percentage = -aWidth;
        int displayWidth = aDispBounds.width;
        return (displayWidth * percentage) / 100;
      }
      return aWidth;
    }

    private int getAdjustedHeight( Rectangle aDispBounds, int aHeight ) {
      if( aHeight == 0 || aHeight == SWT.DEFAULT ) {
        return 0;
      }
      if( aHeight < 0 ) {
        int percentage = -aHeight;
        int displayHeight = aDispBounds.height;
        return (displayHeight * percentage) / 100;
      }
      return aHeight;
    }

    void updateButtonsOfValidationResult( ValidationResult aValidationresult ) {
      boolean enableButtons = aValidationresult.type() != EValidationResultType.ERROR;
      Button b = findButton( ETsDialogCode.APPLY );
      if( b != null ) {
        b.setEnabled( enableButtons );
      }
      b = findButton( ETsDialogCode.OK );
      if( b != null ) {
        b.setEnabled( enableButtons );
      }
    }

    // ------------------------------------------------------------------------------------
    // IGenericChangeListener
    //

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      if( needValidation() ) {
        validateDialogData();
      }
    }

    // ------------------------------------------------------------------------------------
    // TitleAreaDialog
    //

    @Override
    protected Control createDialogArea( Composite aParent ) {
      // load the dialog window icon
      Image img = iconManager().loadStdIcon( ICONID_TSAPP_WINDOWS_ICON, EIconSize.IS_16X16 );
      getShell().setImage( img );
      //
      getShell().setText( dialogInfo.caption() );
      setTitle( dialogInfo.title() );

      // create content widget
      // GOGA 2023-02-24 remove unnecessary composite
      // OLD code
      // Composite area = (Composite)super.createDialogArea( aParent );
      // Composite container = new Composite( area, SWT.NONE );
      // container.setLayoutData( new GridData( GridData.FILL_BOTH ) );
      // NEW code
      Composite container = new Composite( aParent, SWT.NONE );
      container.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
      container.setBackground( colorManager().getColor( ETsColor.RED ) );
      // ---

      container.setLayout( new BorderLayout() );
      contentPanel = panelCreator.createDialogPanel( container, TsDialog.this );
      contentPanel.setLayoutData( BorderLayout.CENTER );
      // contentPanel.pack();
      if( titleImage != null ) {
        this.setTitleImage( titleImage );
      }
      if( titleMessage != null ) {
        this.setMessage( titleMessage, IMessageProvider.NONE );
      }
      if( needValidation() ) {
        contentPanel.genericChangeEventer().addListener( dialogWindow );
      }
      contentPanel.setDataRecord( contentPanel.dataRecordInput() );
      return contentPanel;
    }

    @Override
    protected void createButtonsForButtonBar( Composite aParent ) {
      if( (dialogInfo.flags() & DF_NO_APPROVE) != 0 ) { // dialog with Close button
        addDialogButton( aParent, ETsDialogCode.CLOSE );
        if( (dialogInfo.flags() & DF_CAN_APPLY) != 0 ) { // additional Apply button
          addDialogButton( aParent, ETsDialogCode.APPLY );
        }
      }
      else { // dialog with Ok/Cancel buttons
        addDialogButton( aParent, ETsDialogCode.OK );
        if( (dialogInfo.flags() & DF_CAN_APPLY) != 0 ) { // additional Apply button
          addDialogButton( aParent, ETsDialogCode.APPLY );
        }
        addDialogButton( aParent, ETsDialogCode.CANCEL );
      }
    }

    @Override
    protected void buttonPressed( int aButtonId ) {
      ETsDialogCode dc = ETsDialogCode.getByJFaceId( aButtonId );
      if( dc != null ) {
        buttonWasPressed = true;
        // check if default action need to be done
        if( !whenDialogButtonPressed( dc ) ) {
          buttonWasPressed = false;
          return; // no default action
        }
        // default behavour
        switch( dc ) {
          case OK:
            setReturnCode( OK );
            close();
            break;
          case CANCEL:
            setReturnCode( CANCEL );
            close();
            break;
          case APPLY:
            // do nothing, Apply was already handled
            break;
          case CLOSE:
            close();
            break;
          case NO:
          case YES:
            throw new TsInternalErrorRtException();
          default:
            throw new TsNotAllEnumsUsedRtException();
        }
      }
    }

    @Override
    public void create() {
      super.create();
      validateDialogData(); // set initial buttons state
      widgetsExist = true;
    }

    @Override
    public boolean close() {
      if( !buttonWasPressed ) { // closing dialog window not by button
        if( !whenDialogButtonPressed( ETsDialogCode.CLOSE ) ) {
          buttonWasPressed = false;
          return false; // handler prevents dialog closing
        }
      }
      boolean retVal = super.close();
      if( retVal ) { // dialog is closed
        widgetsExist = false;
      }
      else { // dialog is still open
        buttonWasPressed = false;
      }
      return retVal;
    }

    @Override
    protected Rectangle getConstrainedShellBounds( Rectangle aPreferredSize ) {
      Rectangle newBounds =
          new Rectangle( aPreferredSize.x, aPreferredSize.y, aPreferredSize.width, aPreferredSize.height );
      // monitor size
      Monitor mon = getClosestMonitor( getShell().getDisplay(), Geometry.centerPoint( newBounds ) );

      // FIXME adjst vertical size at least to monitor screen ratio

      Rectangle monBounds = mon.getClientArea();
      // apply dialog window size constraints
      int minW = getAdjustedWidth( monBounds, dialogInfo.minSize().x() );
      if( minW != 0 && newBounds.width < minW ) {
        newBounds.width = minW;
      }
      int maxW = getAdjustedWidth( monBounds, dialogInfo.maxSize().x() );
      if( maxW != 0 && newBounds.width > maxW ) {
        newBounds.width = maxW;
      }
      int minH = getAdjustedHeight( monBounds, dialogInfo.minSize().y() );
      if( minH != 0 && newBounds.height < minH ) {
        newBounds.height = minH;
      }
      int maxH = getAdjustedHeight( monBounds, dialogInfo.maxSize().y() );
      if( maxH != 0 && newBounds.height > maxH ) {
        newBounds.height = maxH;
      }
      // center on parent shell or monitor
      Shell parentShell = getParentShell();
      Rectangle parentBounds;
      if( parentShell == null ) {
        parentBounds = mon.getBounds();
      }
      else {
        parentBounds = parentShell.getBounds();
      }
      newBounds.x = parentBounds.x + (parentBounds.width - newBounds.width) / 2;
      newBounds.y = parentBounds.y + (parentBounds.height - newBounds.height) / 2;
      return newBounds;
    }

    // method code is took from Window.getClosestMonitor()
    private Monitor getClosestMonitor( Display aToSearch, Point aToFind ) {
      int closest = Integer.MAX_VALUE;
      Monitor[] monitors = aToSearch.getMonitors();
      Monitor result = monitors[0];
      for( int idx = 0; idx < monitors.length; idx++ ) {
        Monitor current = monitors[idx];
        Rectangle clientArea = current.getClientArea();
        if( clientArea.contains( aToFind ) ) {
          return current;
        }
        int distance = Geometry.distanceSquared( Geometry.centerPoint( clientArea ), aToFind );
        if( distance < closest ) {
          closest = distance;
          result = current;
        }
      }
      return result;
    }

    // ------------------------------------------------------------------------------------
    // API

    Button findButton( ETsDialogCode aButtonCode ) {
      return getButton( aButtonCode.jfaceButtonId() );
    }

  }

  private final DialogWindow                dialogWindow;
  private ITsCommonDialogApplyHandler<T, E> applyButtonHandler = ITsCommonDialogApplyHandler.NULL;
  final IMapEdit<ETsDialogCode, String>     buttonTexts        = new ElemMap<>();
  final E                                   environment;
  T                                         dialogData         = null;

  /**
   * Следующие поля используются для начальной настройки окна диалога.
   * <p>
   * Окно диалога создается в методе {@link TsDialog#execDialog()}, а методы настройки, задающие изображения, сообщения,
   * заголовки могут быть вызваны сразу после конструктора. Данные поля хранят информацию до создания окна диалога,
   * который и нстравивает себя согласно значениям этих полей.
   */
  String titleMessage = null; // начальное сообщение на title area диалога или null
  Image  titleImage   = null; // изображение на title area диалога или null

  final IDialogPanelCreator<T, E> panelCreator;

  /**
   * Признак существования окна и других визуальных компонент диалога.
   * <p>
   * Окно и других визуальных компонент диалога создаются и уничтожаются внутри метода {@link #execDialog()}, все до и
   * после этот флаг имеет значение false.
   */
  boolean widgetsExist = false;

  final ITsDialogInfo dialogInfo;

  /**
   * С каким кодом закрывается диалог.
   * <p>
   * Значение {@link ETsDialogCode#CLOSE} также используется, когда имело место закрытие окна другими средствами (меню
   * окна диалога, кнопка на заголовке окна и др.).
   */
  ETsDialogCode retCode = ETsDialogCode.CLOSE;

  /**
   * Панель с содержимым диалога.
   */
  AbstractTsDialogPanel<T, E> contentPanel = null;

  /**
   * Constructor.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - diwlog window parameters
   * @param aData &lt;T&gt; - data passed to dialog may be <code>null</code>
   * @param aEnviron E - TS dialog environment may be <code>null</code>
   * @param aPanelCreator {@link IDialogPanelCreator} - content panel factory
   */
  public TsDialog( ITsDialogInfo aDialogInfo, T aData, E aEnviron, IDialogPanelCreator<T, E> aPanelCreator ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aPanelCreator );
    dialogData = aData;
    environment = aEnviron;
    dialogInfo = aDialogInfo;
    panelCreator = aPanelCreator;
    for( ETsDialogCode dc : ETsDialogCode.values() ) {
      buttonTexts.put( dc, dc.nmName() );
    }
    dialogWindow = new DialogWindow( aDialogInfo.shell() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Обработка сообщений о нажатии кнопок от {@link DialogWindow}.
   *
   * @param aButtonId {@link ETsDialogCode} - идентификатор кнопки диалога
   * @return boolean - признак того, что обработку кнопки надо продолжить
   * @see IButtonPressCallback#onDialogButtonPressed(ETsDialogCode)
   */
  boolean whenDialogButtonPressed( ETsDialogCode aButtonId ) {
    switch( aButtonId ) {
      case OK:
        ValidationResult vr1 = validateDialogData();
        if( vr1.isError() ) {
          return false; // диалог нельзя закрывать
        }
        if( onOk() ) { // диалог будет закрыт
          dialogData = contentPanel.getDataRecord();
          retCode = ETsDialogCode.OK;
          return true;
        }
        return false;
      case CANCEL:
        if( onCancel() ) { // диалог будет закрыт
          retCode = ETsDialogCode.CANCEL;
          return true;
        }
        return false;
      case APPLY:
        ValidationResult vr2 = validateDialogData();
        if( !vr2.isError() ) {
          dialogData = contentPanel.getDataRecord();
          onApply();
          return false;
        }
        return true;
      case CLOSE:
        if( onClose() ) { // диалог будет закрыт
          // GOGA 12.10.2012 retCode = ETsDialogCode.CLOSE;
          /**
           * Строка выше акоментирована, поскольку в это время retCode либо равен CLOSE (как было инициализировано
           * значение), либо диалог закрывется методом closeDialog(ETsDialogCode), и надо вернуть заданный код возврата.
           */
          return true;
        }
        return false;
      case NO:
      case YES:
        throw new TsInternalErrorRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  private boolean onOk() {
    try {
      return doOkPressed();
    }
    catch( Exception e ) {
      TsDialogUtils.error( dialogWindow.getShell(), e );
    }
    return false; // ошибка при подтверждении - диалог не закроется!
  }

  private void onApply() {
    try {
      applyButtonHandler.onApplyDialogData( this );
      doApplyPressed();
    }
    catch( Exception e ) {
      TsDialogUtils.error( dialogWindow.getShell(), e );
    }
  }

  private boolean onCancel() {
    try {
      return doCancelPressed();
    }
    catch( Exception e ) {
      TsDialogUtils.error( dialogWindow.getShell(), e );
    }
    return true; // ошибки при отмене игнорируем - диалог закроется
  }

  private boolean onClose() {
    try {
      return doCloseDialog();
    }
    catch( Exception e ) {
      TsDialogUtils.error( dialogWindow.getShell(), e );
    }
    return true; // ошибки при закрытии игнорируем - диалог закроется
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Определяет по типу диалога, нужна ли валидация данных по мере ввода.
   * <p>
   * Валидация нужна, если есть кнопка Apply или кнопка Ok, чтобы для невалидного ввода можно было их запретить.
   *
   * @return boolean - нужна ли валидация данных по мере ввода
   */
  boolean needValidation() {
    if( (dialogInfo.flags() & (DF_CAN_APPLY | DF_NEED_VALIDATION)) != 0 ) {
      return true;
    }
    if( (dialogInfo.flags() & DF_NO_APPROVE) != 0 ) {
      return false;
    }
    return true;
  }

  /**
   * Проверяет валидность вводимых данных методом {@link AbstractTsDialogPanel#validateData()} и обновлят сообщения в
   * области заголовка диалога.
   *
   * @return {@link ValidationResult} - результат валидации вводимых данных
   */
  ValidationResult validateDialogData() {
    if( contentPanel == null || !needValidation() ) {
      return ValidationResult.SUCCESS;
    }
    ValidationResult vr = contentPanel.validateData();
    dialogWindow.updateButtonsOfValidationResult( vr );
    switch( vr.type() ) {
      case ERROR:
        setErrorMessage( vr.message() );
        break;
      case WARNING:
        setErrorMessage( null );
        setWarningMessage( vr.message() );
        break;
      case OK:
        setErrorMessage( null );
        setWarningMessage( null );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return vr;
  }

  // ------------------------------------------------------------------------------------
  // Методы для использования наследниками
  //

  /**
   * Возвращает панель с содержимым диалога.
   * <p>
   * Метод можно вызывать только после того, как с панель содержимого был создан. Определить, панель с содержимым (а
   * также все виджеты: кнопки, само окно и пр.) было уже создано или нет, можно методом {@link #widgetsExist()}.
   *
   * @return {@link AbstractTsDialogPanel} - панель с содержимым
   * @throws TsIllegalStateRtException панель с содержимым не существует
   */
  final protected AbstractTsDialogPanel<T, E> panel() {
    TsIllegalStateRtException.checkFalse( widgetsExist() );
    return contentPanel;
  }

  /**
   * Определяет, были ли уже созданы визуальные компоненты (виджеты).
   * <p>
   * При создании экземпляра диалога, виджеты (само окно, панель содержимого, панель заголовка, кнопки) создаются в
   * момент открытия окна в методе {@link #execDialog()} и по завершению метода прекащают свое существование. Данный
   * метод определяет, существуют ли виджеты на момент выхова.
   *
   * @return boolean - признак существования визуальных компонент<br>
   *         <b>true</b> - виджеты (окно, кнопки, панель...) существуют;<br>
   *         <b>false</b> - виджеты еще не были созданы, или уже удалены.
   */
  final protected boolean widgetsExist() {
    return widgetsExist;
  }

  /**
   * Задает сообщение об ошибке в области заголовка диалога.
   * <p>
   * Сообщение об ошибке имеет наивысший приоритет при отображении и не перебивается дрeгими соообщениями. При сбросе
   * сообщения об ошибке для этого аргумент aMessageFmt надо задать null), восстанавливается предыдущее сообщение.
   *
   * @param aMessageFmt String - форматная строка или null
   * @param aArgs Object[] - аргументы форматной строки для {@link String#format(String, Object...)}
   * @throws TsNullArgumentRtException любой aArgs = null
   */
  final protected void setErrorMessage( String aMessageFmt, Object... aArgs ) {
    if( aMessageFmt != null ) {
      String msg = String.format( aMessageFmt, aArgs );
      dialogWindow.setErrorMessage( msg );
    }
    else {
      dialogWindow.setErrorMessage( null );
    }
  }

  /**
   * Задает сообщение-предупреждение в области заголовка диалога.
   * <p>
   * Внимание: сообщение об ошибке имеет более высокий приоритет, и не перебивается этим методом, см.
   * {@link #setErrorMessage(String, Object...)}.
   *
   * @param aMessageFmt String - форматная строка или null
   * @param aArgs Object[] - аргументы форматной строки для {@link String#format(String, Object...)}
   * @throws TsNullArgumentRtException любой aArgs = null
   */
  final protected void setWarningMessage( String aMessageFmt, Object... aArgs ) {
    if( aMessageFmt != null ) {
      String msg = String.format( aMessageFmt, aArgs );
      dialogWindow.setMessage( msg, IMessageProvider.WARNING );
    }
    else {
      dialogWindow.setMessage( null );
    }
  }

  /**
   * Задает обычное сообщение в области заголовка диалога.
   * <p>
   * Внимание: сообщение об ошибке имеет более высокий приоритет, и не перебивается этим методом, см.
   * {@link #setErrorMessage(String, Object...)}.
   *
   * @param aMessageFmt String - форматная строка
   * @param aArgs Object[] - аргументы форматной строки для {@link String#format(String, Object...)}
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  final protected void setCommonMessage( String aMessageFmt, Object... aArgs ) {
    TsNullArgumentRtException.checkNull( aMessageFmt );
    String msg = String.format( aMessageFmt, aArgs );
    dialogWindow.setMessage( msg, IMessageProvider.NONE );
  }

  /**
   * Возвращает диалоговое окно (RCP окно), реализующее диалог.
   * <p>
   * Метод диалогового окна {@link DialogWindow#close()} можно использовать для программного закрытия окна.
   *
   * @return {@link DialogWindow} - диалоговое окно
   */
  final protected DialogWindow dialogWindow() {
    return dialogWindow;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник может переопределить для обработки нажатия кнопки {@link ETsDialogCode#OK}.
   * <p>
   * Реализация просто возвращает <code>true</code>, при переопределении родительский метод вызывать не нужно.
   *
   * @return boolean - признак штатного завершения диалога<br>
   *         <b>true</b> - штатно будет обработано нажатие OK, диалог закроектся;<br>
   *         <b>false</b> - нажатие кнопки OK будет игнорировано.
   */
  protected boolean doOkPressed() {
    return true;
  }

  /**
   * Наследник может переопределить для обработки нажатия кнопки {@link ETsDialogCode#APPLY}.
   * <p>
   * Реализация ничего не делает, при переопределении родительский метод вызывать не нужно.
   */
  protected void doApplyPressed() {
    // nop
  }

  /**
   * Наследник может переопределить для обработки нажатия кнопки {@link ETsDialogCode#CANCEL}.
   * <p>
   * Реализация просто возвращает <code>true</code>, при переопределении родительский метод вызывать не нужно.
   *
   * @return boolean - признак штатного завершения диалога<br>
   *         <b>true</b> - штатно будет обработано нажатие CANCEL, диалог закроектся;<br>
   *         <b>false</b> - нажатие кнопки CANCEL будет игнорировано.
   */
  protected boolean doCancelPressed() {
    return true;
  }

  /**
   * Наследник может переопределить для обработки нажатия кнопки {@link ETsDialogCode#CLOSE}.
   * <p>
   * Этот метод <b>не</b> вызывается при закрытии диалога кнопкой закрытия в окна.
   * <p>
   * Реализация просто возвращает <code>true</code>, при переопределении родительский метод вызывать не нужно.
   *
   * @return boolean - признак штатного завершения диалога<br>
   *         <b>true</b> - штатно будет обработано нажатие CANCEL, диалог закроектся;<br>
   *         <b>false</b> - нажатие кнопки CANCEL будет игнорировано.
   */
  protected boolean doCloseDialog() {
    return true;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return dialogInfo.tsContext();
  }

  @Override
  public Shell getShell() {
    return dialogInfo.shell();
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Задает данные диалога для отображения в панели содержимого.
   * <p>
   * В качестве аргумента можно указать null - что и получит панель в своем методе
   * {@link AbstractTsDialogPanel#setDataRecord(Object)}.
   *
   * @param aData T - данные диалога для отображения в панели
   */
  public void setData( T aData ) {
    if( contentPanel != null ) {
      contentPanel.setDataRecord( aData );
    }
    dialogData = aData;
    validateDialogData();
  }

  /**
   * Возвращает данные, введенные пользователем.
   * <p>
   * Если панель содержимого не предусматривает возвращение данных (например, панель сделан для диалога с одной кнопкой
   * Close), то возвращает данные, заданные в конструкторе или в {@link #setData(Object)}.
   * <p>
   * Внимание: метод может вернуть null!
   *
   * @return T - введенные пользователем данные или ранее указанные данные
   */
  public T getData() {
    return dialogData;
  }

  /**
   * Возвращает контекст, с которым был создан диалога.
   *
   * @return E - контекст правки данных диалога
   */
  public E environment() {
    return environment;
  }

  /**
   * Returns dialog windows information as specified in constructor.
   *
   * @return {@link ITsDialogInfo} - info about dialog window
   */
  public ITsDialogInfo dialogInfo() {
    return dialogInfo;
  }

  /**
   * Заменяет стандартный текст на кнопке диалога на новый.
   *
   * @param aButtonId {@link ETsDialogCode} - одна из кнопок диалога
   * @param aText String - новый текст (надпись) кнопки
   * @see ETsDialogCode
   * @see ITsDialogConstants#DF_NO_APPROVE
   * @see ITsDialogConstants#DF_CAN_APPLY
   */
  public void setButtonText( ETsDialogCode aButtonId, String aText ) {
    TsNullArgumentRtException.checkNulls( aButtonId, aText );
    if( widgetsExist() ) {
      Button btn = dialogWindow.findButton( aButtonId );
      if( btn != null ) {
        btn.setText( aText );
      }
    }
    buttonTexts.put( aButtonId, aText );
  }

  /**
   * Задает значок, отображаемый в правой части заголовочной области диалога.
   * <p>
   * Метод можно вызывать в любое время - как до создания окна диалога, так и при открытом окне диалога.
   *
   * @param aImage {@link Image} - задаваемое изображение
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setTitleImage( Image aImage ) {
    TsNullArgumentRtException.checkNull( aImage );
    if( widgetsExist ) {
      dialogWindow.setTitleImage( aImage );
    }
    titleImage = aImage;
  }

  /**
   * Задает сообщение, выводимое в области заголовка (title area).
   * <p>
   * Особенности использования см. в {@link TitleAreaDialog#setMessage(String)}.
   * <p>
   * Метод можно вызывать в любое время - как до создания окна диалога, так и при открытом окне диалога.
   *
   * @param aMessage String - сторка сообщения
   */
  public void setTitleMessage( String aMessage ) {
    TsNullArgumentRtException.checkNull( aMessage );
    if( widgetsExist ) {
      dialogWindow.setMessage( aMessage );
    }
    titleMessage = aMessage;
  }

  /**
   * Задает верхнюю строку в titile area (то есть, заменяет заданную при создании строку).
   * <p>
   * Внимание: не следует путать два метода:
   * <ul>
   * <li>{@link #setTitleMessage(String)} - задает переменное сообщение методом
   * {@link TitleAreaDialog#setMessage(String)} или {@link TitleAreaDialog#setMessage(String, int)};</li>
   * <li>{@link #setTitleMessage(String)} - задает переменное сообщение методом
   * {@link TitleAreaDialog#setTitle(String)}.</li>
   * </ul>
   *
   * @param aText String - текст сообщения
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public void setTitle( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    dialogWindow.setTitle( aText );
  }

  /**
   * Задает обработчик нажатия кнокпи диалога Apply.
   *
   * @param aHandler {@link ITsCommonDialogApplyHandler} - обработчик Apply
   * @return {@link ITsCommonDialogApplyHandler} - предыдущий обработчик
   */
  public ITsCommonDialogApplyHandler<T, E> setDialogApplyButtonHandler( ITsCommonDialogApplyHandler<T, E> aHandler ) {
    ITsCommonDialogApplyHandler<T, E> oldHandler = applyButtonHandler;
    applyButtonHandler = TsNullArgumentRtException.checkNull( aHandler );
    return oldHandler;
  }

  /**
   * Прогораммно закрывает диалог с возвратом указанного кода.
   * <p>
   * Код закрытия может иметь одно из следующих значений: {@link ETsDialogCode#OK}, {@link ETsDialogCode#CANCEL} или
   * {@link ETsDialogCode#CLOSE}.
   * <p>
   * Обратите внимание на поведении при {@link ETsDialogCode#OK}: если данные диалога валидны (то есть,
   * {@link #validateDialogData()} вернул не ошибку), то метод {@link #execData()} вернет корректные данные. Если же
   * данные не валидны, {@link #execData()} вернет null.
   * <p>
   * И вообще, этот метод не рекомендуется использовать. Одно из допустимых применений - это простой диалог выбора
   * чего-нибудь из списка, когда двойной щелчок на выбранном элементе должен закрыть диалог с кодом
   * {@link ETsDialogCode#OK}.
   *
   * @param aRetCode {@link ETsDialogCode} - код возврата диалога
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент имеет недопустимое значение
   */
  public void closeDialog( ETsDialogCode aRetCode ) {
    TsNullArgumentRtException.checkNull( aRetCode );
    switch( aRetCode ) {
      case OK:
        if( !validateDialogData().isError() ) {
          dialogData = contentPanel.getDataRecord();
        }
        //$FALL-THROUGH$
      case CANCEL:
      case CLOSE:
        retCode = aRetCode;
        dialogWindow.close();
        return;
      case APPLY:
      case NO:
      case YES:
        throw new TsIllegalArgumentRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Открывает окно диалога и дожидается его закрытия.
   * <p>
   * Метод возвращает true если только ползователь завершил работу с диалогом кнопкой OK, во всех остальных случаях
   * возвращает false. В частности, с диалогом типа {@link ITsDialogConstants#DF_NO_APPROVE} всегда возвращает false.
   *
   * @return boolean - признак подтверждения ввода пользователем<br>
   *         <b>true</b> - диалог был закрыт кнопкой OK;<br>
   *         <b>false</b> - диалог был закрыт любым дрегим способом.
   */
  public boolean execOk() {
    dialogWindow.open();
    return retCode == ETsDialogCode.OK;
  }

  /**
   * Открывает окно диалога и дожидается его закрытия.
   * <p>
   * Метод возвращает данное диалога (возвращенное методом {@link #getData()}) если коно было закрыто кнопкой OK или
   * null во всех остальных случаях. В частности, с диалогом типа {@link ITsDialogConstants#DF_NO_APPROVE} всегда
   * возвращает null.
   * <p>
   * Метод может вернуть null и при завершении диалога кнопкой OK, в том случае, когда {@link #getData()} возвращает
   * null. Но нам такие диалоги не нужны, верно?
   *
   * @return T - данные диалога или null при отказе пользователя
   */
  public T execData() {
    dialogWindow.open();
    if( retCode == ETsDialogCode.OK ) {
      return getData();
    }
    return null;
  }

  /**
   * Открывает окно диалога и дожидается его закрытия.
   * <p>
   * Метод возвращает код кноки, которая привела к закрытию окнаю. Это может быть одна из кнопок
   * {@link ETsDialogCode#OK}, {@link ETsDialogCode#CANCEL} или {@link ETsDialogCode#CLOSE}. Если окно было закрыто не
   * кнопкой диалога (например, через меню окна), то также возвращается {@link ETsDialogCode#CLOSE}.
   *
   * @return {@link ETsDialogCode} - кнопка, которая привела к закрытию окна
   */
  public ETsDialogCode execDialog() {
    dialogWindow.open();
    return retCode;
  }

}
