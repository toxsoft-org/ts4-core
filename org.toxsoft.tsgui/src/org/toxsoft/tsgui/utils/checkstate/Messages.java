package org.toxsoft.tsgui.utils.checkstate;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.tsgui.utils.checkstate.messages"; //$NON-NLS-1$

  public static String STR_N_CHECKED;
  public static String STR_D_CHECKED;
  public static String STR_N_GRAYED;
  public static String STR_D_GRAYED;
  public static String STR_N_UNCHECKED;
  public static String STR_D_UNCHECKED;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
