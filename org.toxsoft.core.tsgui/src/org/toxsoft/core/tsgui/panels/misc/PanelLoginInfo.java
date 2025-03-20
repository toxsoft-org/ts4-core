package org.toxsoft.core.tsgui.panels.misc;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.panels.misc.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.login.*;

/**
 * Panel to edit {@link ILoginInfo}.
 * <p>
 * Note: entered values for login/password/role is first checked with
 * {@link LoginInfo#canCreate(String, String, String)} and the by the supplied
 * {@link ITsValidator}&lt;{@link ILoginInfo}&gt;.
 *
 * @author hazard157
 */
public class PanelLoginInfo
    extends AbstractTsDialogPanel<ILoginInfo, ITsValidator<ILoginInfo>> {

  /**
   * ID of option {@link #OPDEF_IS_ROLE_USED}.
   */
  public static final String OPID_IS_ROLE_USED = "PanelLoginInfo.isRoleUsed"; //$NON-NLS-1$

  /**
   * Context option: use role, if role not used role field is not shown.
   */
  public static final IDataDef OPDEF_IS_ROLE_USED = DataDef.create( OPID_IS_ROLE_USED, BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_FALSE );

  private static char CHAR_ECHO = '*';

  private Text    textLogin    = null;
  private Text    textPassword = null;
  private Text    textRole     = null;
  private boolean isRoleUsed   = false;

  /**
   * Constructor to be used as a generic panel.
   *
   * @param aParent {@link Composite} - the parent composite
   * @param aContext {@link ITsGuiContext} - the context
   * @param aData &lt;T&gt; - initial data record value, may be <code>null</code>
   * @param aEnviron &lt;E&gt; - the environment, may be <code>null</code>
   * @param aFlags int - ORed dialog configuration flags <code>DF_XXX</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelLoginInfo( Composite aParent, ITsGuiContext aContext, ILoginInfo aData, ITsValidator<ILoginInfo> aEnviron,
      int aFlags ) {
    super( aParent, aContext, aData, aEnviron, aFlags );
    initPanel();
    this.pack();
  }

  /**
   * Constructs panel as {@link TsDialog} content.
   *
   * @param aParent {@link Composite} - the parent composite
   * @param aOwnerDialog {@link TsDialog} - the owner dialog
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelLoginInfo( Composite aParent, TsDialog<ILoginInfo, ITsValidator<ILoginInfo>> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    initPanel();
    this.pack();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void initPanel() {
    isRoleUsed = OPDEF_IS_ROLE_USED.getValue( tsContext().params() ).asBool();
    this.setLayout( new GridLayout( 2, false ) );
    CLabel l;
    // login
    l = new CLabel( this, SWT.CENTER );
    l.setText( STR_TEXT_LOGIN );
    l.setToolTipText( STR_TEXT_LOGIN_D );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false ) );
    textLogin = new Text( this, SWT.SINGLE | SWT.BORDER );
    textLogin.setToolTipText( STR_TEXT_LOGIN_D );
    textLogin.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    textLogin.addModifyListener( notificationModifyListener );
    // password
    l = new CLabel( this, SWT.CENTER );
    l.setText( STR_TEXT_PASSWORD );
    l.setToolTipText( STR_TEXT_PASSWORD_D );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false ) );
    textPassword = new Text( this, SWT.SINGLE | SWT.BORDER );
    textPassword.setToolTipText( STR_TEXT_PASSWORD_D );
    textPassword.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    textPassword.addModifyListener( notificationModifyListener );
    textPassword.setEchoChar( CHAR_ECHO );
    // role
    if( isRoleUsed ) {
      l = new CLabel( this, SWT.CENTER );
      l.setText( STR_TEXT_ROLE );
      l.setToolTipText( STR_TEXT_ROLE_D );
      l.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false ) );
      textRole = new Text( this, SWT.SINGLE | SWT.BORDER );
      textRole.setToolTipText( STR_TEXT_ROLE_D );
      textRole.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
      textRole.addModifyListener( notificationModifyListener );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ILoginInfo aData ) {
    String login = EMPTY_STRING;
    String passowrd = EMPTY_STRING;
    String role = EMPTY_STRING;
    if( aData != null ) {
      login = aData.login();
      passowrd = aData.password();
      role = aData.role();
    }
    textLogin.setText( login );
    textPassword.setText( passowrd );
    if( textRole != null ) {
      textRole.setText( role );
    }
  }

  @Override
  protected ValidationResult validateData() {
    String login = textLogin.getText();
    String passowrd = textPassword.getText();
    String role = EMPTY_STRING;
    if( textRole != null ) {
      role = textLogin.getText();
    }
    ValidationResult vr = LoginInfo.canCreate( login, passowrd, role );
    if( vr.isError() ) {
      return vr;
    }
    ILoginInfo loginInfo = new LoginInfo( login, passowrd, role );
    return ValidationResult.firstNonOk( vr, environ().validate( loginInfo ) );
  }

  @Override
  protected ILoginInfo doGetDataRecord() {
    String login = textLogin.getText();
    String passowrd = textPassword.getText();
    String role = EMPTY_STRING;
    if( textRole != null ) {
      role = textLogin.getText();
    }
    return new LoginInfo( login, passowrd, role );
  }

  /**
   * Invoked {@link ILoginInfo} edit dialog.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aInitVal {@link ILoginInfo} - initial value or {@link ILoginInfo#NONE}
   * @param aValidator {@link ITsValidator}&lt;{@link ILoginInfo}&gt; - the validator ir {@link ITsValidator#PASS}
   * @return {@link ILoginInfo} - edited info or <code>null</code>
   */
  public static final ILoginInfo edit( ITsGuiContext aContext, ILoginInfo aInitVal,
      ITsValidator<ILoginInfo> aValidator ) {
    TsNullArgumentRtException.checkNulls( aContext, aInitVal, aValidator );
    IDialogPanelCreator<ILoginInfo, ITsValidator<ILoginInfo>> dpc = PanelLoginInfo::new;
    TsDialogInfo di = new TsDialogInfo( aContext, STR_DLG_EDIT_LOGIN_INFO, STR_DLG_EDIT_LOGIN_INFO_D );
    di.setMinSizeShellRelative( 10, 10 );
    TsDialog<ILoginInfo, ITsValidator<ILoginInfo>> d = new TsDialog<>( di, aInitVal, aValidator, dpc );
    ITsIconManager iconMgr = aContext.get( ITsIconManager.class );
    d.setTitleImage( iconMgr.loadStdIcon( ICONID_DIALOG_PASSWORD, EIconSize.IS_64X64 ) );
    return d.execData();
  }

}
