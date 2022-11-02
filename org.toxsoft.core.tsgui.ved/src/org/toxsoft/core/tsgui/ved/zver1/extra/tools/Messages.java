package org.toxsoft.core.tsgui.ved.zver1.extra.tools;

import java.util.*;

class Messages {

  private static final String BUNDLE_NAME = Messages.class.getName().toLowerCase();

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

  private Messages() {
  }

  public static String getString( String key ) {
    try {
      return RESOURCE_BUNDLE.getString( key );
    }
    catch( @SuppressWarnings( "unused" ) MissingResourceException e ) {
      return '!' + key + '!';
    }
  }

}
