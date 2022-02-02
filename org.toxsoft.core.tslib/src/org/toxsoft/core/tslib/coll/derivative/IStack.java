package org.toxsoft.core.tslib.coll.derivative;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.basis.ITsClearable;
import org.toxsoft.core.tslib.coll.basis.ITsSizeRestrictableCollection;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Стек объектов.
 *
 * @author goga
 * @version $id$
 * @param <E> - тип хранимых элементов
 */
public interface IStack<E>
    extends IList<E>, ITsClearable, ITsSizeRestrictableCollection {

  /**
   * Помещает в стек заданный элемент.
   *
   * @param aElem E - элемент, помещаемый в стек
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException (для стека с ограничением размера) стек уже полный
   */
  void push( E aElem );

  /**
   * Выталкивает элемент с вершины стека.
   *
   * @return E - элемент с вершины стека
   * @throws TsIllegalStateRtException стек пустой
   */
  E pop();

  /**
   * Выталкивает элемент с вершины стека или возвращает null.
   *
   * @return E - элемент с вершины стека или null, если стек пустой
   */
  E popOrNull();

  /**
   * Возвращает элемент с вершины стека, не выталкивая его.
   *
   * @return E - элемент с вершины стека
   * @throws TsIllegalStateRtException стек пустой
   */
  E peek();

  /**
   * Возвращает элемент (или null) с вершины стека, не выталкивая его.
   *
   * @return E - элемент с вершины стека или null, если стек пустой
   */
  E peekOrNull();

}
