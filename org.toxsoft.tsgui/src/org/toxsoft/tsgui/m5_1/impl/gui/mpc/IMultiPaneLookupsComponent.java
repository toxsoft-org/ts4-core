package org.toxsoft.tsgui.m5_1.impl.gui.mpc;

import org.toxsoft.tsgui.m5_1.api.helpers.IM5LookupProvider;
import org.toxsoft.tslib.coll.IList;

/**
 * Пногозонная компонента для работы со списком элементов, поставляемых справочником.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMultiPaneLookupsComponent<T>
    extends IMultiPaneComponent<T> {

  /**
   * Задает список отображаемых элементов.
   *
   * @param aItems {@link IList}&lt;T&gt; - список отображаемых элементов
   */
  void setItems( IList<T> aItems );

  /**
   * Возвращает поставщик справочных сущностей.
   *
   * @return {@link IM5LookupProvider} - поставщик справочных сущностей, может быть <code>null</code>
   */
  IM5LookupProvider<T> lookupProvider();

  /**
   * Задает поставщик справочных сущностей.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - поставщик справочных сущностей, может быть <code>null</code>
   */
  void setLookupProvider( IM5LookupProvider<T> aLookupProvider );

}
