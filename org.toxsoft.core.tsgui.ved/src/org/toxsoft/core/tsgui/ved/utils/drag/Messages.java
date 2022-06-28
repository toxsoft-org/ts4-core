package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.osgi.util.*;

public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
  public static String        STR_D_DRAG_CANCELED;
  public static String        STR_D_DRAG_FINISHED;
  public static String        STR_D_DRAG_START;
  public static String        STR_D_DRAGGING;
  public static String        STR_N_DRAG_CANCELED;
  public static String        STR_N_DRAG_FINISHED;
  public static String        STR_N_DRAG_START;
  public static String        STR_N_DRAGGING;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
