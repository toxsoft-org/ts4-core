package org.toxsoft.tsgui.m5_1.ext.fdefs;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.tsgui.m5.ext.fdefs.messages"; //$NON-NLS-1$

  public static String STR_D_DESCRIPTION;
  public static String STR_D_ID;
  public static String STR_D_NAME;
  public static String STR_N_DESCRIPTION;
  public static String STR_N_ID;
  public static String STR_N_NAME;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
