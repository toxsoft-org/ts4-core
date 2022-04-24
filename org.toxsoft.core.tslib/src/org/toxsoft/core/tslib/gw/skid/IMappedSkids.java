package org.toxsoft.core.tslib.gw.skid;

import java.io.*;

import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * SKID lists mapped on the string keys.
 * <p>
 * This is read-only interface, FIXME
 *
 * @author hazard157
 */
public interface IMappedSkids {

  /**
   * Immutable empty instance singleton.
   */
  IMappedSkids EMPTY = new InternalEmptyMappedSkids();

  /**
   * Returns the map of the SKIDs lists.
   *
   * @return {@link IStringMap}&lt;{@link ISkidList}&gt; - the map of the SKIDs lists
   */
  IStringMap<ISkidList> skidsMap();

}

/**
 * Internal class for {@link IMappedSkids#EMPTY} singleton implementation.
 *
 * @author hazard157
 */
class InternalEmptyMappedSkids
    extends MappedSkids {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IMappedSkids#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IMappedSkids#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IMappedSkids.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  final public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  final public boolean equals( Object obj ) {
    return obj == this;
  }

  @Override
  final public int hashCode() {
    return getClass().getSimpleName().hashCode();
  }

}
