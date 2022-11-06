package org.toxsoft.core.tslib.utils.plugins.impl;

import java.util.*;

@SuppressWarnings( { "javadoc", "unused" } )
public class Messages {

  private static final String BUNDLE_NAME = "ru.toxsoft.tslib.plugins.impl.messages"; //$NON-NLS-1$

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
