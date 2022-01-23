package org.toxsoft.tsgui.m5_2.gui.panels;

import org.toxsoft.tsgui.m5_2.IM5ItemsProvider;
import org.toxsoft.tslib.coll.IList;

/**
 * Панель просмотра поставляемых {@link IM5ItemsProvider}-ом списка.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public interface IM5CollLookupsPanel<T>
    extends IM5CollPanelBase<T> {

  /**
   * Задает элементы для просмотра / редактирования.
   *
   * @param aItems {@link IList}&lt;T&gt; - коллекция элементов для просмотра / редактирования
   */
  void setItems( IList<T> aItems );

  /**
   * Возвращает поставщик справочных элементов.
   *
   * @return {@link IM5ItemsProvider} - поставщик справочных элементов, может быть <code>null</code>
   */
  IM5ItemsProvider<T> lookupProvider();

  /**
   * Задает поставщик справочных элементов.
   *
   * @param aLookupProvider {@link IM5ItemsProvider} - поставщик справочных элементов, может быть <code>null</code>
   */
  void setLookupProvider( IM5ItemsProvider<T> aLookupProvider );

}
