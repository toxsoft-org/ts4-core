package org.toxsoft.core.tsgui.ved.tools_old.palette2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Смешиваемый интерфейс классов, генерирующих сообщения для {@link IVedToolSelectionListener}.
 *
 * @author vs
 */
public interface IVedToolSelectionEventProducer {

  /**
   * Добавляет слушатель изменения.
   * <p>
   * Если слушатель уже зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IVedToolSelectionListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void addToolSelectionListener( IVedToolSelectionListener aListener );

  /**
   * Удаляет слушатель изменения.
   * <p>
   * Если слушатель не зарегистрирован, метод ничего не делает.
   *
   * @param aListener {@link IVedToolSelectionListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeToolSelectionListener( IVedToolSelectionListener aListener );
}
