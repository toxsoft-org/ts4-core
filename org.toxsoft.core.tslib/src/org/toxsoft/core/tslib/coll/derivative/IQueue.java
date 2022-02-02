package org.toxsoft.core.tslib.coll.derivative;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.basis.ITsClearable;
import org.toxsoft.core.tslib.coll.basis.ITsSizeRestrictableCollection;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Очередь объектов.
 *
 * @author hazard157
 * @param <E> - тип хранимых элементов
 */
public interface IQueue<E>
    extends IList<E>, ITsClearable, ITsSizeRestrictableCollection {

  /**
   * Добавляет элемент в хвост очереди.
   * <p>
   * В отличие от {@link #offerTail(Object)}, если очередь полная, выбрасывает исключение
   * {@link TsIllegalStateRtException}.
   *
   * @param aElem E - добавляемый элемент
   * @return всегда <b>true</b>
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException очередь переполнена
   */
  boolean putTail( E aElem );

  /**
   * Добавляет элемент в хвост очереди.
   * <p>
   * В отличие от {@link #putTail(Object)}, если очередь полная, просто возвращает <b>false</b>.
   *
   * @param aElem E - добавляемый элемент
   * @return всегда <b>true</b>
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException очередь переполнена
   */
  boolean offerTail( E aElem );

  /**
   * Забирает (удаляет из очереди и возвращает) элемент из головы очереди.
   * <p>
   * В отличие от {@link #getHeadOrNull()}, если очередь пустая, выбрасывает исключение
   * {@link TsIllegalStateRtException}.
   *
   * @return E - очередной элемент очереди
   * @throws TsIllegalStateRtException очередь пуста
   */
  E getHead();

  /**
   * Забирает (удаляет из очереди и возвращает) элемент из головы очереди.
   * <p>
   * В отличие от {@link #getHead()}, если очередь пустая, возвращает null.
   *
   * @return E - очередной элемент очереди или null
   */
  E getHeadOrNull();

  /**
   * Возвращает (не удаляя из очереди) элемент из головы очереди.
   * <p>
   * В отличие от {@link #peekHeadOrNull()}, если очередь пустая, выбрасывает исключение
   * {@link TsIllegalStateRtException}.
   *
   * @return E - очередной элемент очереди
   * @throws TsIllegalStateRtException очередь пуста
   */
  E peekHead();

  /**
   * Возвращает (не удаляя из очереди) элемент из головы очереди.
   * <p>
   * В отличие от {@link #peekHead()}, если очередь пустая, возвращает null.
   *
   * @return E - очередной элемент очереди или null
   */
  E peekHeadOrNull();

}
