package org.toxsoft.core.pas.tj.impl;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Professional
 */
public class Messages {

  private static final String BUNDLE_NAME = "org.toxsoft.core.pas.tj.impl.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

  private Messages() {
  }

  @SuppressWarnings( { "javadoc", "unused" } )
  public static String getString( String key ) {
    try {
      return RESOURCE_BUNDLE.getString( key );
    }
    catch( MissingResourceException e ) {
      return '!' + key + '!';
    }
  }
}
