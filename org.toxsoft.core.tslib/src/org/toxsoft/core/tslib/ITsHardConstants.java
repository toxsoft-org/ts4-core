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
   * TS platform version.
   */
  TsVersion TS_VERSION = new TsVersion( 4, 0 );

}
