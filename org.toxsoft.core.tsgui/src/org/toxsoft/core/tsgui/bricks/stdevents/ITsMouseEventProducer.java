package org.toxsoft.core.tsgui.bricks.stdevents;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщение {@link ITsMouseListener}.
 *
 * @author goga
 */
public interface ITsMouseEventProducer {

  /**
   * Добавляет слушатель.
   * <p>
   * Если такой слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsMouseListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addTsMouseListener( ITsMouseListener aListener );

  /**
   * Удаляет слушатель.
   * <p>
   * Если такой слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsMouseListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeTsMouseListener( ITsMouseListener aListener );

}
