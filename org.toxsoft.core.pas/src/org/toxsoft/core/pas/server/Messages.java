package org.toxsoft.core.pas.server;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

@SuppressWarnings( "javadoc" )
public class Messages {

  private static final String BUNDLE_NAME = "ru.toxsoft.tslib.pas.server.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

  private Messages() {
  }

  @SuppressWarnings( "unused" )
  public static String getString( String key ) {
    try {
      return RESOURCE_BUNDLE.getString( key );
    }
    catch( MissingResourceException e ) {
      return '!' + key + '!';
    }
  }
}
