package org.toxsoft.core.tsgui.panels.vecboard;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = Messages.class.getName().toLowerCase();

  public static String STR_N_LK_BORDER;
  public static String STR_D_LK_BORDER;
  public static String STR_N_LK_LADDER;
  public static String STR_D_LK_LADDER;
  public static String STR_N_LK_ROW;
  public static String STR_D_LK_ROW;
  public static String STR_N_LK_SASH;
  public static String STR_D_LK_SASH;
  public static String STR_N_LK_TABS;
  public static String STR_D_LK_TABS;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
