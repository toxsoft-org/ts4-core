package org.toxsoft.tsgui.m5_1.impl.gui.viewers.helpers;

import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.m5_1.api.helpers.IM5Getter;
import org.toxsoft.tsgui.m5_1.impl.gui.viewers.IM5CollectionViewer;
import org.toxsoft.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.primtypes.IStringMap;
import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Менеджер управления столбцами в просмотрщике {@link IM5CollectionViewer}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IM5ColumnManager<T> {

  /**
   * Возвращает карту всех колонок таблицы, упорядоченной по начальному расположению.
   * <p>
   * Колонка по индексу получается из списка {@link IStringMapEdit#values()}, соответственно, методом
   * {@link IList#indexOf(Object)} этого же списка получается индекс коноки в таблице.
   *
   * @return IStringMap&lt;{@link IM5Column}&gt; - карта "ИД поля модели" - "столбец таблицы"
   */
  IStringMap<IM5Column<T>> columns();

  /**
   * Создает колонку и добавлет ее в конец списка колонок.
   *
   * @param aFieldId String - идентификатор добавляемого поля
   * @return {@link IM5Column} - созданная колонка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такого поля в модели {@link IM5Model}
   * @throws TsItemAlreadyExistsRtException это поле уже добавлено
   */
  IM5Column<T> add( String aFieldId );

  /**
   * Создает колонку и добавлет ее в конец списка колонок.
   * <p>
   * Для колонки задается поставщик отображения (название и значок) ячейки, что позволяет реализовывать поля, связанные
   * с конкретным контекстом отображения. Например, поле "последовательный номер", нумерация которого идет всегда по
   * возрастанию, в не зависимости от выбранного столюца сортировки.
   *
   * @param <V> - field value type
   * @param aFieldId String - идентификатор добавляемого поля
   * @param aGetter {@link IM5Getter} - поставщик значения поля, замещающий поставщик от описания поля
   * @param aValueVisuals {@link ITsVisualsProvider}&lt;V&gt; - the field value representation means
   * @return {@link IM5Column} - созданная колонка
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException нет такого поля в модели {@link IM5Model}
   * @throws TsItemAlreadyExistsRtException это поле уже добавлено
   */
  <V> IM5Column<T> add( String aFieldId, IM5Getter<T, V> aGetter, ITsVisualsProvider<V> aValueVisuals );

  /**
   * Создает колонку и добавлет ее в конец списка колонок.
   *
   * @param aField {@link IStridable} - идентификатор добавляемого поля
   * @return {@link IM5Column} - созданная колонка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такого поля в модели {@link IM5Model}
   * @throws TsItemAlreadyExistsRtException это поле уже добавлено
   */
  IM5Column<T> add( IStridable aField );

  /**
   * Удалет колонку из таблицы.
   *
   * @param aFieldId String - идентификатор удаляемого поля
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такого поля в модели {@link IM5Model}
   */
  void remove( String aFieldId );

}
