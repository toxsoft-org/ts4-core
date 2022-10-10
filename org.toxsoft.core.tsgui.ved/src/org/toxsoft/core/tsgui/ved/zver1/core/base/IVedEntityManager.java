package org.toxsoft.core.tsgui.ved.zver1.core.base;

import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages the VED entity instances of the same type.
 *
 * @author hazard157
 * @param <T> - the type of the entity
 */
public interface IVedEntityManager<T extends IVedEntity> {

  /**
   * Returns the entities.
   *
   * @return {@link INotifierStridablesListEdit}&lt;{@link IVedEntity}&gt; - the entities list
   */
  INotifierStridablesList<T> items();

  /**
   * Adds new entity to the end of list {@link #items()}.
   *
   * @param aComponent &lt;T&gt; - entity to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException entity with the same ID already exists
   */
  void addComponent( T aComponent );

  /**
   * Adds the component to the specified position in list {@link #items()}.
   *
   * @param aIndex int - index of the element to insert (in range 0..list size)
   * @param aComponent {@link IVedEntity} - entity to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException entity with the same ID already exists
   */
  void insertComponent( int aIndex, IVedEntity aComponent );

  /**
   * Removes the specified entity.
   * <p>
   * Unexistant ID is ignored.
   *
   * @param aEntityId String - ID of entity to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeComponent( String aEntityId );

}
