package org.toxsoft.core.tsgui.ved.utils.drag;

/**
 * "Генератор" событий о перетаскивании объектов.
 * <p>
 *
 * @author vs
 */
public interface IVedDragObjectsEventProducer {

  /**
   * Добавляет слушателя события перетаскивания объектов.
   *
   * @param aListener IVedDragObjectsListener - слушателя события перетаскивания объектов
   */
  void addVedDragObjectsListener( IVedDragObjectsListener aListener );

  /**
   * Удаляет слушателя события перетаскивания объектов.
   *
   * @param aListener IVedDragObjectsListener - слушателя события перетаскивания объектов
   */
  void removeVedDragObjectsListener( IVedDragObjectsListener aListener );
}
