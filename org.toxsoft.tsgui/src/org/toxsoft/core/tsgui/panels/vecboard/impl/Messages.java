package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = Messages.class.getName().toLowerCase();

  public static String FMT_ERR_CONTROL_ALREADY_EXIST_AT_BORDER_PLACEMENT;
  public static String MSG_ERR_NO_BOADR_LAYOUT;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
