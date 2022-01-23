package org.toxsoft.tslib.bricks.time;

import org.toxsoft.tslib.coll.IListBasicEdit;

/**
 * Редатируемое расширение {@link ITimedList}.
 *
 * @author goga
 * @param <T> - конкретный тип сущности с меткой времени
 */
public interface ITimedListEdit<T extends ITimestampable>
    extends ITimedList<T>, IListBasicEdit<T> {

  // добавляются методы из IListBasicEdit

}
