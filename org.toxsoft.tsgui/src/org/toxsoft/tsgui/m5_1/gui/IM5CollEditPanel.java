package org.toxsoft.tsgui.m5_1.gui;

import org.toxsoft.tsgui.m5_1.api.IM5LifecycleManager;

/**
 * Панель редактирования коллекции.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public interface IM5CollEditPanel<T>
    extends IM5CollViewerPanel<T> {

  /**
   * Возвращает менеджер жизненного цикла сущнсотей.
   *
   * @return {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  IM5LifecycleManager<T> lifecycleManager();

  /**
   * Задает менеджер ЖЦ.
   * <p>
   * Смена менеджера не приводит к обновлению содержимого или состояния, слудет явно вызвать метод {@link #refresh()}.
   *
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager );

}
