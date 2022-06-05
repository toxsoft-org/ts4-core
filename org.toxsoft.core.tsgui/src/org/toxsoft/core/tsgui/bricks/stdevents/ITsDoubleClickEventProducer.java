package org.toxsoft.core.tsgui.bricks.stdevents;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщение {@link ITsDoubleClickListener#onTsDoubleClick(Object, Object)}.
 *
 * @author hazard157
 * @param <E> - конкретный тип элементов
 */
public interface ITsDoubleClickEventProducer<E> {

  /**
   * Добавляет слушатель.
   * <p>
   * Если такой слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsDoubleClickListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addTsDoubleClickListener( ITsDoubleClickListener<E> aListener );

  /**
   * Удаляет слушатель.
   * <p>
   * Если такой слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsDoubleClickListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeTsDoubleClickListener( ITsDoubleClickListener<E> aListener );

}
