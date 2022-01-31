package org.toxsoft.core.tslib.coll.primtypes;

import java.io.ObjectStreamException;

import org.toxsoft.core.tslib.coll.IMap;
import org.toxsoft.core.tslib.coll.primtypes.impl.ImmutableStringMap;

/**
 * A collection that maps {@link String} keys to values.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public interface IStringMap<E>
    extends IMap<String, E> {

  /**
   * Singleton of always empty uneditable (immutable) map.
   */
  @SuppressWarnings( "rawtypes" )
  IStringMapEdit EMPTY = new InternalNullStringMap();

  /**
   * Returns ordered list of all keys as {@link IStringList}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  IStringList keys();

}

/**
 * Internal class for {@link IStringMap#EMPTY} singleton implementation.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
class InternalNullStringMap<E>
    extends ImmutableStringMap<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IStringMap#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IStringMap#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IStringMap.EMPTY;
  }

  // Object methods are implemented in parent class

}
