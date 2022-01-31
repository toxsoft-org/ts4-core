package org.toxsoft.core.tsgui.bricks.stdevents;

import org.eclipse.swt.widgets.Event;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщение {@link ITsKeyEventListener#onTsKeyEvent(Object, Event)} по
 * нажатию клавиш клавиатуры.
 *
 * @author goga
 */
public interface ITsKeyDownEventProducer {

  /**
   * Задает слушатель нажатия клавиш.
   * <p>
   * Если такой слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsKeyEventListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addTsKeyDownListener( ITsKeyEventListener aListener );

  /**
   * Удаляет слушатель нажатия клавиш.
   * <p>
   * Если такой слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link ITsKeyEventListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeTsKeyDownListener( ITsKeyEventListener aListener );

}
