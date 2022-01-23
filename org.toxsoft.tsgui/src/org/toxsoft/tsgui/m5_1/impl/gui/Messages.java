package org.toxsoft.tsgui.m5_1.impl.gui;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.tsgui.m5.impl.gui.messages"; //$NON-NLS-1$

  public static String ITsResources_FMT_CANT_SET_FIELD_VALUE;
  public static String ITsResources_FMT_ERR_FIELD_VALIDATION_FAIL;
  public static String ITsResources_FMT_ERR_NO_FACTORY_IN_PARAMS;
  public static String ITsResources_MSG_ERR_NO_FACTORIES_REGISTRY_IN_CONTEXT;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
