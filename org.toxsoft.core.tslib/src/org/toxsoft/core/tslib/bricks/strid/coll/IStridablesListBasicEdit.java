package org.toxsoft.core.tslib.bricks.strid.coll;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Editable extension of {@link IStridablesList}.
 * <p>
 * This interface adds basic editing methods to {@link IStridablesList}. Editing methods is applicable to any kind of
 * linear collections, including the sorted collections.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public interface IStridablesListBasicEdit<E extends IStridable>
    extends IStridablesList<E>, IListBasicEdit<E>, IStringMapEdit<E> {

  /**
   * Adds new or replaces exiting element in collection.
   * <p>
   * If element with the same identificator as of specified element exists in collection it will be replaced. Otherwise
   * new element will be added to this collection.
   *
   * @param aElem &lt;E&gt; - element to be added to this collection
   * @return int - index of added or replaced element
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  int put( E aElem );

  /**
   * Replaces the existing element by specified new element.
   * <p>
   * If identifiers <code>aId</code> and <code>aElem.id()</code> are different, method checks and if element with
   * identifier <code>aElem.id()</code> already exists in list then exception {@link TsItemAlreadyExistsRtException} is
   * thrown. Thus method guarantees that only one element (with identifier <code>aId</code>) will be replaced.
   *
   * @param aId String - identifier of element already in list
   * @param aElem &lt;E&gt; - element to replace existing one
   * @return &lt;E&gt; - the removed element
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException element with ID aElem.id() already exists (when aElem.id() != aId)
   */
  E replace( String aId, E aElem );

  /**
   * Removes from collection the element by identifier.
   *
   * @param aId String - identifier of element to be removed
   * @return &lt;E&gt; - the removed element, or <code>null</code> if there was no mapping
   * @throws TsNullArgumentRtException argument = null
   */
  E removeById( String aId );

  /**
   * Replaces all elements in this list from source list.
   * <p>
   * This method s declared here to allow compiler to distinguish betwee {@link IMapEdit#setAll(IMap)} and
   * {@link IListEdit#setAll(ITsCollection)} as {@link IStridablesList} is both map and list.
   *
   * @param aSrc {@link IStridablesList}&lt;R&gt; - sourcelist
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( IStridablesList<E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    clear();
    putAll( aSrc );
  }

}
