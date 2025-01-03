package org.toxsoft.core.tsgui.ved.valeds;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Диалог позволяющий ввести произвольный много-строчный текст.
 * <p>
 *
 * @author vs
 */
public class MultiLineInputDialog
    extends Dialog {

  /**
   * The title of the dialog.
   */
  private String title;

  /**
   * The message to display, or <code>null</code> if none.
   */
  private String message;

  /**
   * The input value; the empty string by default.
   */
  private String value = "";//$NON-NLS-1$

  /**
   * The input validator, or <code>null</code> if none.
   */
  private IInputValidator validator;

  /**
   * Ok button widget.
   */
  private Button okButton;

  /**
   * Input text widget.
   */
  private Text text;

  /**
   * Error message label widget.
   */
  private Text errorMessageText;

  /**
   * Error message string.
   */
  private String errorMessage;

  /**
   * Creates an input dialog with OK and Cancel buttons. Note that the dialog will have no visual representation (no
   * widgets) until it is told to open.
   * <p>
   * Note that the <code>open</code> method blocks for input dialogs.
   * </p>
   *
   * @param parentShell the parent shell, or <code>null</code> to create a top-level shell
   * @param dialogTitle the dialog title, or <code>null</code> if none
   * @param dialogMessage the dialog message, or <code>null</code> if none
   * @param initialValue the initial input value, or <code>null</code> if none (equivalent to the empty string)
   * @param aValidator an input validator, or <code>null</code> if none
   */
  public MultiLineInputDialog( Shell parentShell, String dialogTitle, String dialogMessage, String initialValue,
      IInputValidator aValidator ) {
    super( parentShell );
    this.title = dialogTitle;
    message = dialogMessage;
    if( initialValue == null ) {
      value = "";//$NON-NLS-1$
    }
    else {
      value = initialValue;
    }
    validator = aValidator;
  }

  @Override
  protected void buttonPressed( int buttonId ) {
    if( buttonId == IDialogConstants.OK_ID ) {
      value = text.getText();
    }
    else {
      value = null;
    }
    super.buttonPressed( buttonId );
  }

  @Override
  protected void configureShell( Shell shell ) {
    super.configureShell( shell );
    if( title != null ) {
      shell.setText( title );
    }
  }

  @Override
  protected void createButtonsForButtonBar( Composite parent ) {
    // create OK and Cancel buttons by default
    okButton = createButton( parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false );
    createButton( parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false );
    // do this here because setting the text will set enablement on the ok
    // button
    text.setFocus();
    if( value != null ) {
      text.setText( value );
      text.selectAll();
    }
  }

  @Override
  protected Control createDialogArea( Composite parent ) {
    // create composite
    Composite composite = (Composite)super.createDialogArea( parent );
    // create message
    if( message != null ) {
      Label label = new Label( composite, SWT.WRAP );
      label.setText( message );
      GridData data = new GridData( GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
          | GridData.VERTICAL_ALIGN_CENTER );
      data.widthHint = convertHorizontalDLUsToPixels( IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH );
      label.setLayoutData( data );
      label.setFont( parent.getFont() );
    }
    text = new Text( composite, SWT.BORDER | SWT.MULTI );
    GridData gd = new GridData( GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL );
    gd.heightHint = 100;
    text.setLayoutData( gd );
    text.addModifyListener( e -> validateInput() );
    errorMessageText = new Text( composite, SWT.READ_ONLY | SWT.WRAP );
    errorMessageText.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL ) );
    errorMessageText.setBackground( errorMessageText.getDisplay().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    // Set the error message text
    // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=66292
    setErrorMessage( errorMessage );

    applyDialogFont( composite );
    return composite;
  }

  /**
   * Returns the ok button.
   *
   * @return the ok button
   */
  protected Button getOkButton() {
    return okButton;
  }

  /**
   * Returns the text area.
   *
   * @return the text area
   */
  protected Text getText() {
    return text;
  }

  /**
   * Returns the validator.
   *
   * @return the validator
   */
  protected IInputValidator getValidator() {
    return validator;
  }

  /**
   * Returns the string typed into this input dialog.
   *
   * @return the input string
   */
  public String getValue() {
    return value;
  }

  /**
   * Validates the input.
   * <p>
   * The default implementation of this framework method delegates the request to the supplied input validator object;
   * if it finds the input invalid, the error message is displayed in the dialog's message line. This hook method is
   * called whenever the text changes in the input field.
   * </p>
   */
  protected void validateInput() {
    String errMessage = null;
    if( validator != null ) {
      errMessage = validator.isValid( text.getText() );
    }
    // Bug 16256: important not to treat "" (blank error) the same as null
    // (no error)
    setErrorMessage( errMessage );
  }

  /**
   * Sets or clears the error message. If not <code>null</code>, the OK button is disabled.
   *
   * @param aErrorMessage the error message, or <code>null</code> to clear
   * @since 3.0
   */
  public void setErrorMessage( String aErrorMessage ) {
    errorMessage = aErrorMessage;
    if( errorMessageText != null && !errorMessageText.isDisposed() ) {
      errorMessageText.setText( errorMessage == null ? " \n " : errorMessage ); //$NON-NLS-1$
      // Disable the error message text control if there is no error, or
      // no error text (empty or whitespace only). Hide it also to avoid
      // color change.
      // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=130281
      boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces( errorMessage )).length() > 0;
      errorMessageText.setEnabled( hasError );
      errorMessageText.setVisible( hasError );
      errorMessageText.getParent().update();
      // Access the ok button by id, in case clients have overridden button creation.
      // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
      Control button = getButton( IDialogConstants.OK_ID );
      if( button != null ) {
        button.setEnabled( errorMessage == null );
      }
    }
  }

  /**
   * Returns the style bits that should be used for the input text field. Defaults to a single line entry. Subclasses
   * may override.
   *
   * @return the integer style bits that should be used when creating the input text
   * @since 3.4
   */
  protected int getInputTextStyle() {
    return SWT.SINGLE | SWT.BORDER;
  }
}
