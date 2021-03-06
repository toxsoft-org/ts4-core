package org.toxsoft.core.tsgui.ved.utils.drag;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщения о "перетаскивании" 2d-объектов с возможностью приостановки и
 * возобновления генерации.
 * <p>
 *
 * @author vs
 */
public interface IVedDragEventProducer {

  /**
   * Добавляет слушатель изменения.
   * <p>
   * Если слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IVedDragListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addDragListener( IVedDragListener aListener );

  /**
   * Удаляет слушатель изменения.
   * <p>
   * Если слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IVedDragListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeDragListener( IVedDragListener aListener );

}
