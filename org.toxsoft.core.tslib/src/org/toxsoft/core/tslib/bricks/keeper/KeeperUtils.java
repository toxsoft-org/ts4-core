package org.toxsoft.core.tslib.bricks.keeper;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods for {@link IKeepableEntity}.
 *
 * @author hazard157
 */
public class KeeperUtils {

  /**
   * Writes textual representation of entity to the string.
   *
   * @param <T> - entity type
   * @param aEntity &lt;T&gt; - the entity
   * @return String - KTOR textual representation
   */
  public static <T extends IKeepableEntity> String keepableEntityToString( T aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    StringBuilder sb = new StringBuilder();
    ICharOutputStream chOut = new CharOutputStreamAppendable( sb );
    IStrioWriter sw = new StrioWriter( chOut );
    aEntity.write( sw );
    return sb.toString();
  }

  /**
   * Reads entity content from the KTOR string.
   *
   * @param <T> - entity type
   * @param aKtor String - KTOR textual representation
   * @param aEntity &lt;T&gt; - the entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T extends IKeepableEntity> void stringToKeepableEntity( String aKtor, T aEntity ) {
    TsNullArgumentRtException.checkNulls( aKtor, aEntity );
    ICharInputStream chIn = new CharInputStreamString( aKtor );
    IStrioReader sr = new StrioReader( chIn );
    aEntity.read( sr );
  }

  /**
   * No subclassing.
   */
  private KeeperUtils() {
    // nop
  }

}
