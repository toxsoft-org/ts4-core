package org.toxsoft.tsgui.m5_1.impl.gui.viewers.helpers;

import org.toxsoft.tsgui.m5_1.api.IM5Bunch;
import org.toxsoft.tsgui.m5_1.impl.gui.viewers.IM5CollectionViewer;
import org.toxsoft.tslib.bricks.filter.ITsFilter;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Управление фильтрацией строк в просмотрщиках {@link IM5CollectionViewer}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IM5FilterManager<T> {

  /**
   * Возвращает признак того, что к просмотрщику применен фильтр.
   *
   * @return boolean - признак того, что к просмотрщику применен фильтр<br>
   *         <b>true</b> - просмотрщик отфильтрован, в ней потенциально показана только часть элементов из модели
   *         данных;<br>
   *         <b>false</b> - нет фильтра, все элементы модели данных показаны.
   */
  boolean isFiltered();

  /**
   * Возвращает отфильтрованные элементы.
   *
   * @return {@link IList}&lt;T&gt; - список отфильтрованных элементов
   */
  IList<T> items();

  /**
   * Задает фильтр для отбора части элементов исходной коллекции для показа в просмотрщике.
   * <p>
   * Обратите внимание, что при применении фильтра количество элементов в просмотрщике становиться меньше, чем
   * количество элментов в исходной коллекции. Таким образом, не все элементы {@link IM5CollectionViewer#items()} видны
   * в просмотрщике. Видимые элементы перечислены в {@link #items()}.
   * <p>
   * Для отмены фильтрации следует задать фильтр {@link ITsFilter#ALL}.
   *
   * @param aFilter {@link ITsFilter} - фильтр для отбора элементов исходной коллекции
   *          {@link IM5CollectionViewer#items()}
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setFilter( ITsFilter<IM5Bunch<T>> aFilter );

  /**
   * Возвращает текущий фильтр.
   *
   * @return {@link ITsFilter} - текущий (возможно пустой) фильтр, но никогда не null
   */
  ITsFilter<IM5Bunch<T>> getFilter();

}
