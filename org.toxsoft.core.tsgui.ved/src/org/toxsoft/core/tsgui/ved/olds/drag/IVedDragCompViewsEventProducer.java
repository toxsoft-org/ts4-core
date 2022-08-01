package org.toxsoft.core.tsgui.ved.olds.drag;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщение об изменении положения фигур посредством перетаскивания.
 * <p>
 *
 * @author vs
 */
public interface IVedDragCompViewsEventProducer {

  /**
   * Добавляет слушатель изменения.
   * <p>
   * Если слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IVedDragObjectsListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addVedDragCompViewsEventListener( IVedDragObjectsListener aListener );

  /**
   * Удаляет слушатель изменения.
   * <p>
   * Если слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IVedDragObjectsListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeVedDragCompViewsEventListener( IVedDragObjectsListener aListener );

  /**
   * Приостанавливает генерацию сообщений.
   * <p>
   * Если генерация уже приостановлена, метод ничего не делает.
   */
  void pauseFiring();

  /**
   * Продолжает генерацию сообщений, приостановленной ранее методом {@link #pauseFiring()}.
   * <p>
   * Если генерация не приостановлена, метод ничего не делает.
   *
   * @param aFireDelayed boolean - признак однократной генерации сообщения, если во время паузы были запросы на
   *          извещения
   */
  void resumeFiring( boolean aFireDelayed );

}
