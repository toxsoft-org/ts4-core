package org.toxsoft.core.tsgui.chart.api;

import org.eclipse.osgi.util.*;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.core.tsgui.chart.api.messages"; //$NON-NLS-1$
  public static String        ITgResources_E_D_GRK_LADDER;
  public static String        ITgResources_E_D_GRK_LINE;
  public static String        ITgResources_E_N_GRK_LADDER;
  public static String        ITgResources_E_N_GRK_LINE;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
