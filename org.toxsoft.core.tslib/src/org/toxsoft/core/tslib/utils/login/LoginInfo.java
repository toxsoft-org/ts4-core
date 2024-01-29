package org.toxsoft.core.tslib.utils.login;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.core.tslib.utils.login.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILoginInfo} immutable implementation.
 *
 * @author hazard157
 */
public final class LoginInfo
    implements ILoginInfo, Serializable {

  private static final long serialVersionUID = 2680646976644587565L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "LoginInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ILoginInfo> KEEPER =
      new AbstractEntityKeeper<>( ILoginInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ILoginInfo aEntity ) {
          aSw.writeAsIs( aEntity.login() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.password() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.role() );
        }

        @Override
        protected ILoginInfo doRead( IStrioReader aSr ) {
          String login = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String password = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          String role = aSr.readQuotedString();
          return new LoginInfo( login, password, role );
        }
      };

  private final String login;
  private final String password;
  private final String role;

  /**
   * Constructor.
   *
   * @param aLogin String - the user name, the login (an IDpath)
   * @param aPassword String - arbitrary password, may be an empty string
   * @param aRole String - the role (an IDpath) or an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canCreate(String, String, String)}
   */
  public LoginInfo( String aLogin, String aPassword, String aRole ) {
    TsNullArgumentRtException.checkNull( aLogin );
    TsIllegalArgumentRtException.checkTrue( aLogin.length() == 0, MSG_ERR_EMPTY_LOGIN );
    TsIllegalArgumentRtException.checkFalse( StridUtils.isValidIdPath( aLogin ), MSG_ERR_INVALID_LOGIN, aLogin );
    login = aLogin;
    password = TsNullArgumentRtException.checkNull( aPassword );
    role = TsNullArgumentRtException.checkNull( aRole );
  }

  /**
   * Constructor with an empty role.
   *
   * @param aLogin String - the user name, the login (an IDpath)
   * @param aPassword String - arbitrary password, may be an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canCreate(String, String, String)}
   */
  public LoginInfo( String aLogin, String aPassword ) {
    this( aLogin, aPassword, EMPTY_STRING );
  }

  /**
   * Copy constructor.
   *
   * @param aSrc ILoginInfo - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public LoginInfo( ILoginInfo aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    login = StridUtils.checkValidIdPath( aSrc.login() );
    password = TsNullArgumentRtException.checkNull( aSrc.password() );
    role = TsNullArgumentRtException.checkNull( aSrc.role() );
  }

  /**
   * Pacake-private constructor for {@link ILoginInfo#NONE}.
   */
  LoginInfo() {
    login = EMPTY_STRING;
    password = EMPTY_STRING;
    role = EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // ILoginInfo
  //

  @Override
  public String login() {
    return login;
  }

  @Override
  public String password() {
    return password;
  }

  @Override
  public String role() {
    return role;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "%s password='%s', role='%s'", login, password, role ); //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + login.hashCode();
    result = PRIME * result + password.hashCode();
    result = PRIME * result + role.hashCode();
    return result;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    // equality check will be performed on IAtomicValue because tslib does not creates IAtomicValue instances
    if( aThat instanceof LoginInfo that ) {
      return this.login.equals( that.login ) && this.password.equals( that.password ) && this.role.equals( that.role );
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Checks if {@link LoginInfo} instance can be created with the specified arguments.
   *
   * @param aLogin String - the user name, the login (an IDpath)
   * @param aPassword String - arbitrary password, may be an empty string
   * @param aRole String - the role (an IDpath) or an empty string
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult canCreate( String aLogin, String aPassword, String aRole ) {
    TsNullArgumentRtException.checkNulls( aLogin, aPassword, aRole );
    if( aLogin.isEmpty() ) {
      return ValidationResult.error( MSG_ERR_EMPTY_LOGIN );
    }
    if( !StridUtils.isValidIdPath( aLogin ) ) {
      return ValidationResult.error( MSG_ERR_INVALID_LOGIN, aLogin );
    }
    if( !aRole.isEmpty() && !StridUtils.isValidIdPath( aRole ) ) {
      return ValidationResult.error( MSG_ERR_INVALID_ROLE, aLogin );
    }
    return ValidationResult.SUCCESS;
  }
}
