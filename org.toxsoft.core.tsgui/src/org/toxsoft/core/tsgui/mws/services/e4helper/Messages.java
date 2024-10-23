package org.toxsoft.core.tsgui.mws.services.e4helper;

import java.util.*;

@SuppressWarnings( "javadoc" )
public class Messages {

  private static final String BUNDLE_NAME = Messages.class.getName().toLowerCase();

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
