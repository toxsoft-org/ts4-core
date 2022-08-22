package org.toxsoft.core.tsgui.bricks.qtree;

import java.util.*;

import org.eclipse.osgi.util.*;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = Messages.class.getName().toLowerCase();

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

  private Messages() {
  }

  public static String getString( String key ) {
    try {
      return RESOURCE_BUNDLE.getString( key );
    }
    catch( @SuppressWarnings( "unused" ) MissingResourceException ex ) {
      return '!' + key + '!';
    }
  }

}
