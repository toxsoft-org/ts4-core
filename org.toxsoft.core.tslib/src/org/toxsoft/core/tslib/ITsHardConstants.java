package org.toxsoft.core.tslib;

import org.toxsoft.core.tslib.utils.*;

/**
 * Constants with fixed and persistent values.
 *
 * @author hazard157
 */
public interface ITsHardConstants {

  /**
   * IDpath short prefix of all tslib specific identifiers.
   */
  String TS_ID = "ts"; //$NON-NLS-1$

  /**
   * IDpath long prefix (including domain name) of all tslib specific identifiers.
   */
  String TS_FULL_ID = "org.toxsoft"; //$NON-NLS-1$

  /**
   * Sequential version number of the ToxSoft platform.
   * <p>
   * The This constant is defined starting from the version 4 of the platform.
   *
   * @since 4
   */
  int TS_PLATFORM_VERSION = 4;

  /**
   * TS platform version.
   * <p>
   * This version has only one meaningful part - {@link TsVersion#verMajor()}. Other parts are not changed during the
   * platform lifecycle.
   */
  TsVersion TS_VERSION = new TsVersion( TS_PLATFORM_VERSION, 0 );

}
