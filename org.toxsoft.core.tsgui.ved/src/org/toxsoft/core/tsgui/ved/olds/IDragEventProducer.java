package org.toxsoft.core.tsgui.ved.olds;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщения о "перетаскивании" 2d-объектов с возможностью приостановки и
 * возобновления генерации.
 * <p>
 *
 * @author vs
 */
public interface IDragEventProducer {

  /**
   * Добавляет слушатель изменения.
   * <p>
   * Если слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IDragListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addDragListener( IDragListener aListener );

  /**
   * Удаляет слушатель изменения.
   * <p>
   * Если слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IDragListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeDragListener( IDragListener aListener );

}
