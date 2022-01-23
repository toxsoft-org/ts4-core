package org.toxsoft.tsgui.bricks.stdevents;

import org.eclipse.swt.widgets.Event;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщение {@link ITsKeyEventListener#onTsKeyEvent(Object, Event)} по
 * отжатию клавиш клавиатуры.
 *
 * @author goga
 */
public interface ITsKeyUpEventProducer {

  /**
   * Добавляет слушатель отжатия клавиш.
   * <p>
   * Если такой слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsKeyEventListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addTsKeyUpListener( ITsKeyEventListener aListener );

  /**
   * Удаляет слушатель отжатия клавиш.
   * <p>
   * Если такой слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsKeyEventListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeTsKeyUpListener( ITsKeyEventListener aListener );

}
