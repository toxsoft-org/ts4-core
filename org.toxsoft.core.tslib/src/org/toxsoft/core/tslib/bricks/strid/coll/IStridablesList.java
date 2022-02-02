package org.toxsoft.core.tslib.bricks.strid.coll;

import java.io.ObjectStreamException;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.ImmutableStridablesList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;

/**
 * Collection of {@link IStridable} entityes of one scope.
 * <p>
 * This is mixed collection - both list and map, beacuse {@link IStridable} entities of one scope have natural uniquie
 * key {@link IStridable#id()}.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public interface IStridablesList<E extends IStridable>
    extends IList<E>, IMap<String, E> {

  /**
   * Singleton of always empty uneditable (immutable) list.
   */
  @SuppressWarnings( "rawtypes" )
  IStridablesListEdit EMPTY = new InternalNullStridablesList();

  /**
   * Retuns the keys {@link #keys()} as {@link IStringList}.
   *
   * @return IStringList - ordered list of identifiers of {@link IStridable} elements in this collection
   */
  IStringList ids();

}

/**
 * Internal class for {@link IList#EMPTY} singleton implementation.
 *
 * @author hazard157
 * @param <E> - the type of {@link IStridable} elements in this collection
 */
class InternalNullStridablesList<E extends IStridable>
    extends ImmutableStridablesList<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IStridablesList#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IStridablesList#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IStridablesList.EMPTY;
  }

}
