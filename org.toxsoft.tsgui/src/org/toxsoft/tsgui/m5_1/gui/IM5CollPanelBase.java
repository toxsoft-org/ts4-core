package org.toxsoft.tsgui.m5_1.gui;

import org.toxsoft.tsgui.panels.generic.IGenericItemsListPanel;
import org.toxsoft.tslib.coll.IList;

/**
 * Базовый интерфейс панели работы с коллекциями моделированных сущностей.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public interface IM5CollPanelBase<T>
    extends IGenericItemsListPanel<T>, IM5PanelBase<T> {

  /**
   * Возвращает элементы отображаемой коллекции.
   *
   * @return {@link IList}&lt;T&gt; - элементы отображаемой коллекции
   */
  @Override
  IList<T> items();

  /**
   * Обновляет содержимое панели.
   */
  void refresh();

}
