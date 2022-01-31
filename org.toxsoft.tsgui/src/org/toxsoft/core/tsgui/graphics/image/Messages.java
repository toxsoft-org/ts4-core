package org.toxsoft.core.tsgui.graphics.image;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.tsgui.graphics.image.messages"; //$NON-NLS-1$
  public static String        FMT_D_THUMB_SIZE;
  public static String        FMT_N_THUMB_SIZE;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }

}
