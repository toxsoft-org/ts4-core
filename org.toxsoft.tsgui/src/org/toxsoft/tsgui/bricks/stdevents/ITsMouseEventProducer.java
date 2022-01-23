package org.toxsoft.tsgui.bricks.stdevents;

import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщение {@link ITsMouseListener}.
 *
 * @author goga
 * @param <S> - тип источника сообщения
 */
public interface ITsMouseEventProducer<S> {

  /**
   * Добавляет слушатель.
   * <p>
   * Если такой слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsMouseListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addTsMouseListener( ITsMouseListener<S> aListener );

  /**
   * Удаляет слушатель.
   * <p>
   * Если такой слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsMouseListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeTsMouseListener( ITsMouseListener<S> aListener );

}
