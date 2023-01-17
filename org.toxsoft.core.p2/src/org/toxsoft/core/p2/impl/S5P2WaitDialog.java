package org.toxsoft.core.p2.impl;

import static org.toxsoft.core.p2.impl.IS5Resources.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.ETsDialogCode;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.TsGuiUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Диалог ожидания действий пользователя
 *
 * @author mvk
 */
class S5P2WaitDialog
    extends MessageDialog {

  private int LABEL_DLUS = 12;
  private int BAR_DLUS   = 3;

  protected Display           display;
  protected ProgressIndicator progressIndicator;
  private Label               subTaskLabel;
  private String              nextActionMessage;
  protected long              timeout;
  protected boolean           closed = false;
  protected boolean           paused = false;
  private Button              pausedButton;

  /**
   * Конструктор
   *
   * @param aDisplay {@link Display} объект синхронизации с GUI
   * @param aShell {@link Shell} родительское окно. null: нет родительского окна
   * @param aDialogTitle String заголовок диалога
   * @param aMessage String сообщение диалога. Допускается мультистрочное ('\n') определение.
   * @param aActionMessage сообщение о действии которое будет выполнено в случае бездействия пользователя
   * @param aTimeout int таймаут (мсек) ожидания по истечению которого диалог будет автоматически закрыт с кодом
   *          возврата {@link IDialogConstants#OK_ID}.
   * @throws TsNullArgumentRtException любой аргумент (кроме aShell) = null
   */
  S5P2WaitDialog( Display aDisplay, Shell aShell, String aDialogTitle, String aMessage, String aActionMessage,
      long aTimeout ) {
    super( aShell, aDialogTitle, titleImage(), aMessage, MessageDialog.INFORMATION,
        new String[] { ETsDialogCode.CLOSE.description() }, SWT.SHEET );
    display = TsNullArgumentRtException.checkNull( aDisplay );
    nextActionMessage = aActionMessage;
    timeout = aTimeout;
  }

  // ------------------------------------------------------------------------------------
  // Переопределение MessageDialog и его родителей
  //
  @Override
  protected Control createCustomArea( Composite aParent ) {
    // create the top level composite for the dialog area
    Composite customArea = new Composite( aParent, SWT.NONE );
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    customArea.setLayout( layout );
    GridData data = new GridData( GridData.FILL_BOTH );
    data.horizontalSpan = 2;
    customArea.setLayoutData( data );

    progressIndicator = new ProgressIndicator( customArea );
    GridData gd = new GridData();
    gd.heightHint = convertVerticalDLUsToPixels( BAR_DLUS );
    gd.horizontalAlignment = GridData.FILL;
    gd.grabExcessHorizontalSpace = true;
    gd.horizontalSpan = 2;
    progressIndicator.setLayoutData( gd );
    progressIndicator.beginTask( 100 );
    progressIndicator.showNormal();
    // label showing current task
    subTaskLabel = new Label( customArea, SWT.LEFT | SWT.WRAP );
    subTaskLabel.setText( nextActionMessage );
    gd = new GridData( GridData.FILL_HORIZONTAL );
    gd.heightHint = convertVerticalDLUsToPixels( LABEL_DLUS );
    gd.horizontalSpan = 2;
    subTaskLabel.setLayoutData( gd );
    subTaskLabel.setFont( aParent.getFont() );
    // createLabel( composite );
    return customArea;
  }

  @Override
  protected void createButtonsForButtonBar( Composite aParent ) {
    pausedButton = createButton( aParent, IDialogConstants.STOP_ID, IDialogConstants.STOP_LABEL, true );
    createButton( aParent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true );
    createButton( aParent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true );
  }

  @Override
  protected void buttonPressed( int buttonId ) {
    if( IDialogConstants.STOP_ID == buttonId ) {
      progressIndicator.beginTask( 0 );
      subTaskLabel.setText( MSG_RESTART_CANCEL );
      pausedButton.setVisible( false );
      paused = true;
      return;
    }
    super.buttonPressed( buttonId );
  }

  @Override
  public int open() {
    Thread thread = new Thread( () -> {
      long sleep = timeout / 100;
      for( int index = 0; index < 100; index++ ) {
        display.asyncExec( () -> {
          if( progressIndicator.isDisposed() ) {
            closed = true;
            return;
          }
          if( paused ) {
            return;
          }
          progressIndicator.worked( 1 );
        } );
        if( closed ) {
          return;
        }
        try {
          Thread.sleep( sleep );
        }
        catch( InterruptedException e ) {
          e.printStackTrace();
        }
      }
      if( !paused ) {
        display.asyncExec( this::close );
      }
    } );
    thread.start();
    return super.open();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  private static Image titleImage() {
    ITsIconManager iconManager = TsGuiUtils.getGuiThreadWinContext().get( ITsIconManager.class );
    return iconManager.loadStdIcon( ITsStdIconIds.ICONID_TSAPP_WINDOWS_ICON, EIconSize.IS_16X16 );
  }

}
