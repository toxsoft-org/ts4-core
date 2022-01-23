package org.toxsoft.tsgui.m5_1.impl.gui.mpc;

import org.toxsoft.tsgui.m5_1.api.IM5Bunch;
import org.toxsoft.tsgui.m5_1.api.IM5LifecycleManager;

/**
 * Компонента просмотра и редактирования списка моделированных сущностей.
 * <p>
 * TODO описать
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMultiPaneModownComponent<T>
    extends IMultiPaneComponent<T> {

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

  /**
   * Возвращает значения, которыми заполнается диалог создания нового элемента.
   *
   * @return {@link IM5Bunch} - начальные значения создания эелемнта или <code>null</code>
   */
  IM5Bunch<T> getItemCreationInitialValues();

  /**
   * Задает {@link #getItemCreationInitialValues()}.
   *
   * @param aInitialValues {@link IM5Bunch} - начальные значения создания эелемнта или <code>null</code>
   */
  void setItemCreationInitialValues( IM5Bunch<T> aInitialValues );

}
