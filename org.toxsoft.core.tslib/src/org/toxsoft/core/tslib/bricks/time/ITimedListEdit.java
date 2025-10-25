package org.toxsoft.core.tslib.bricks.time;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редатируемое расширение {@link ITimedList}.
 *
 * @author hazard157
 * @param <T> - the type of elements in this collection
 */
public interface ITimedListEdit<T extends ITimestampable>
    extends ITimedList<T>, IListBasicEdit<T> {

  // добавляются методы из IListBasicEdit

  /**
   * Replaces (if any) first element with exactly same timestamp.
   * <p>
   * If there is no element with timestamp orof argument method is the same as {@link #add(Object)}.
   *
   * @param aElem &lt;T&gt; - new value for specified timestamp
   * @return int - index of added (or already existed) element
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  int replaceByTimestamp( T aElem );

}
