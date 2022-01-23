package org.toxsoft.tsgui.m5.std.fields;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

@SuppressWarnings( "javadoc" )
public class Messages {

  private static final String BUNDLE_NAME = "org.toxsoft.tsgui.m5.std.fields.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

  private Messages() {
  }

  public static String getString( String aKey ) {
    try {
      return RESOURCE_BUNDLE.getString( aKey );
    }
    catch( @SuppressWarnings( "unused" ) MissingResourceException e ) {
      return '!' + aKey + '!';
    }
  }

}
