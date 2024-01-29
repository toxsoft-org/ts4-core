package org.toxsoft.core.tslib.utils.login;

/**
 * Authentication information for logging into any system.
 * <p>
 * Authentication information includes:
 * <ul>
 * <li>{@link #login()} - user name, must be an IDpath;</li>
 * <li>{@link #password()} - password, may be an empty string;</li>
 * <li>{@link #role()} - role, maybe an empty string.</li>
 * </ul>
 * The login/password/role formats may be programmatically checked with
 * {@link LoginInfo#canCreate(String, String, String)} according to the rules above.
 *
 * @author hazard157
 */
public sealed interface ILoginInfo
    permits LoginInfo {

  /**
   * Empty authentication information - all fields are an empty strings.
   */
  ILoginInfo NONE = new LoginInfo();

  /**
   * Return the user name, the login..
   *
   * @return String - login (an IDpath) or an empty string for {@link ILoginInfo#NONE}
   */
  String login();

  /**
   * Returns the login password.
   *
   * @return String - arbitrary password, may be an empty string
   */
  String password();

  /**
   * Returns the user's login role.
   *
   * @return String - the role (an IDpath) or an empty string
   */
  String role();

}
