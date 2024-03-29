package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.osgi.util.*;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = Messages.class.getName().toLowerCase();

  public static String STR_D_IS_HEADER_SHOWN;
  public static String STR_N_IS_HEADER_SHOWN;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }

}
