package org.toxsoft.core.tsgui.bricks.stdevents;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщение
 * {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)}.
 *
 * @author hazard157
 * @param <E> - конкретный тип элементов
 */
public interface ITsSelectionChangeEventProducer<E> {

  /**
   * Задает слушатель.
   * <p>
   * Если такой слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsSelectionChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addTsSelectionListener( ITsSelectionChangeListener<E> aListener );

  /**
   * Удаляет слушатель.
   * <p>
   * Если такой слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsSelectionChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeTsSelectionListener( ITsSelectionChangeListener<E> aListener );

}
