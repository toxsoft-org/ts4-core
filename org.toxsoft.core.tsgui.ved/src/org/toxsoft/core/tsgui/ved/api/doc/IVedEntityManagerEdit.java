package org.toxsoft.core.tsgui.ved.api.doc;

import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Entities list manager for {@link IVedDocument} data model.
 *
 * @author hazard157
 * @param <T> - entities Java type
 */
public interface IVedEntityManagerEdit<T extends IVedEntity>
    extends IVedEntityManager<T>, ITsClearable, IGenericChangeEventCapable {

  /**
   * Adds new entity to the end of list {@link #items()}.
   * <p>
   * Simply calls {@link #insertEntity(int, IVedEntity)} to insert entity at the end of {@link #items()}.
   *
   * @param aEntity &lt;T&gt; - entity to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException kind of entity does not matches {@link #entityKind()}
   * @throws TsItemAlreadyExistsRtException entity with such ID already exists
   */
  default void addEntity( T aEntity ) {
    insertEntity( items().size(), aEntity );
  }

  /**
   * Adds the entity to the specified position in list {@link #items()}.
   *
   * @param aIndex int - index of the element to insert (in range 0..list size)
   * @param aEntity &lt;T&gt; - entity to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException index out of range
   * @throws TsIllegalArgumentRtException kind of entity does not matches {@link #entityKind()}
   * @throws TsItemAlreadyExistsRtException entity with such ID already exists
   */
  void insertEntity( int aIndex, T aEntity );

  /**
   * Removes the specified entity.
   * <p>
   * Unexistant ID is ignored.
   *
   * @param aEntityId String - entity ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeEntity( String aEntityId );

  /**
   * Returns entities list {@link #items()} reorderer.
   *
   * @return {@link IListReorderer}&lt;T&gt; - compnents list reorderer
   */
  IListReorderer<T> itemsReorderer();

}
