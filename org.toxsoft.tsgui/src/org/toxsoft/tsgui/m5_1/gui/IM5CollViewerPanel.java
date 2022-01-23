package org.toxsoft.tsgui.m5_1.gui;

import org.toxsoft.tsgui.m5_1.api.IM5ItemsProvider;

/**
 * Панель просмотра поставляемых {@link IM5ItemsProvider}-ом списка.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public interface IM5CollViewerPanel<T>
    extends IM5CollPanelBase<T> {

  /**
   * Возвращает поставщик элементов.
   *
   * @return {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   */
  IM5ItemsProvider<T> itemsProvider();

  /**
   * Задает поставщик элементов.
   * <p>
   * Смена постащика не приводит к обновлению содержимого или состояния, слудет явно вызвать метод {@link #refresh()}.
   *
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   */
  void setItemsProvider( IM5ItemsProvider<T> aItemsProvider );

}
