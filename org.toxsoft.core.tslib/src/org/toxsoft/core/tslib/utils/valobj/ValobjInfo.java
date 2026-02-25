package org.toxsoft.core.tslib.utils.valobj;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Additional human-readable information about value-objects.
 * <p>
 * This optional information may be registered with value-object keepers in {@link TsValobjUtils}.
 *
 * @author hazard157
 * @param name String - the short name
 * @param description String - the detailed description
 */
public record ValobjInfo( String name, String description ) {

  /**
   * Singleton of empty information, may be used when info is not known.
   */
  public static ValobjInfo EMPTY = new ValobjInfo( EMPTY_STRING, EMPTY_STRING );

  /**
   * Constructor.
   *
   * @param name String - the short name
   * @param description String - the detailed description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValobjInfo( String name, String description ) {
    TsNullArgumentRtException.checkNulls( name, description );
    this.name = name;
    this.description = description;
  }

}
