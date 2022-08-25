package org.toxsoft.core.tsgui.dialogs.misc;

import static org.toxsoft.core.tsgui.dialogs.ITsGuiDialogResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Password change dialog consists of two texts for password and confirmation.
 *
 * @author hazard157
 */
public class DialogEnterPassword {

  static class Panel
      extends AbstractTsDialogPanel<String, Object> {

    // TODO change common Text to hidden eneter with "view" button

    private final ITsValidator<String> passwordValidator;

    private final Text text1;
    private final Text text2;

    Panel( Composite aParent, TsDialog<String, Object> aOwnerDialog, ITsValidator<String> aValidator ) {
      super( aParent, aOwnerDialog );
      passwordValidator = aValidator;
      this.setLayout( new GridLayout( 2, false ) );
      // password eneter field 1
      Label l1 = new Label( this, SWT.LEFT );
      l1.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
      l1.setText( STR_L_ENTER_PASSWORD1 );
      text1 = new Text( this, SWT.BORDER );
      text1.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
      text1.addModifyListener( notificationModifyListener );
      // password eneter field 2
      Label l2 = new Label( this, SWT.LEFT );
      l2.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
      l2.setText( STR_L_ENTER_PASSWORD2 );
      text2 = new Text( this, SWT.BORDER );
      text2.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
      text2.addModifyListener( notificationModifyListener );
    }

    @Override
    protected ValidationResult validateData() {
      String s1 = text1.getText();
      String s2 = text2.getText();
      String s = s1.length() >= s2.length() ? s1 : s2;
      ValidationResult vr = passwordValidator.validate( s );
      if( !vr.isError() ) {
        if( !s1.equals( s2 ) ) {
          return ValidationResult.error( MSG_ERR_PSWD_NO_MATCH );
        }
      }
      return vr;
    }

    @Override
    protected void doSetDataRecord( String aData ) {
      String s = aData != null ? aData : TsLibUtils.EMPTY_STRING;
      text1.setText( s );
      text2.setText( s );
    }

    @Override
    protected String doGetDataRecord() {
      return text1.getText();
    }

  }

  /**
   * Invokes password editing dialog.
   *
   * @param aDialogInfo {@link ITsDialogConstants} - dialog window parameters
   * @param aValidator {@link ITsValidator}&lt;String&gt; - passowrd validator
   * @return String - entered, valid password or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final String enterPassword( ITsDialogInfo aDialogInfo, ITsValidator<String> aValidator ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aValidator );
    IDialogPanelCreator<String, Object> creator =
        ( aParent, aOwnerDialog ) -> new Panel( aParent, aOwnerDialog, aValidator );
    TsDialog<String, Object> d = new TsDialog<>( aDialogInfo, null, new Object(), creator );
    return d.execData();
  }

  /**
   * Invokes password editing dialog with default caption and title
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aValidator {@link ITsValidator}&lt;String&gt; - passowrd validator
   * @return String - entered, valid password or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final String enterPassword( ITsGuiContext aContext, ITsValidator<String> aValidator ) {
    TsNullArgumentRtException.checkNulls( aContext, aValidator );
    TsDialogInfo dialogInfo = new TsDialogInfo( aContext, DLG_C_ENTER_PASSWORD, DLG_T_ENTER_PASSWORD );
    dialogInfo.setMinSizeShellRelative( 10, 10 );
    return enterPassword( dialogInfo, aValidator );
  }

}
