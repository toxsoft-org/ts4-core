package org.toxsoft.tsgui.m5_1.ext.enums;

import org.eclipse.osgi.util.NLS;

class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.tsgui.m5.ext.enums.messages"; //$NON-NLS-1$

  public static String STR_D_JAVA_NAME;
  public static String STR_D_ORDINAL;
  public static String STR_N_JAVA_NAME;
  public static String STR_N_ORDINAL;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }

}
