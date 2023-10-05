package org.toxsoft.core.tslib.coll.notifier.basis;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Collection change listener.
 *
 * @author hazard157
 */
public interface ITsCollectionChangeListener {

  /**
   * This method is called when collection changes
   * <p>
   * For {@link ECrudOp#LIST} operations <code>aItem = null</code>, for othe operations <code>aItem</code> identifies
   * the element:
   * <ul>
   * <li>for maps {@link IMap} it contains the key, not the value. For maps with primitive type keys <b>aItem</b> is
   * corresponding object type (eg for {@link IIntMap} it has {@link Integer} type);</li>
   * <li>for linear collections {@link IList} of primitive type <b>aItem</b> is correponding object type (eg for
   * {@link ILongList} it has {@link Long} type)</li>
   * <li>for other linear collections {@link IList} <code>aItem</code> is changed element itself.</li>
   * </ul>
   * For {@link ECrudOp#EDIT} operations aItem is new element or for maps - the key of the changed element.
   *
   * @param aSource {@link Object} - the source of the event
   * @param aOp {@link ECrudOp} - the kind of the operation over the collection
   * @param aItem Object - changed key/element or <code>null</code> for {@link ECrudOp#LIST}
   */
  void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem );

}
