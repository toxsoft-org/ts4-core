package org.toxsoft.core.txtproj.mws;

import java.util.*;

public class Messages {

  private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

  private Messages() {
  }

  public static String getString( String key ) {
    try {
      return RESOURCE_BUNDLE.getString( key );
    }
    catch( MissingResourceException e ) {
      return '!' + key + '!';
    }
  }
}
