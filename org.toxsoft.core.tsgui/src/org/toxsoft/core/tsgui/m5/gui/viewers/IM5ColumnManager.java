package org.toxsoft.core.tsgui.m5.gui.viewers;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5CollectionViewer} columns manager.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5ColumnManager<T> {

  /**
   * Returns the map of columns ordered by initial placemnet.
   *
   * @return IStringMap&lt;{@link IM5Column}&gt; - the map "modefl field ID" - "viewer column"
   */
  IStringMap<IM5Column<T>> columns();

  // TODO TRANSLATE

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
   * возрастанию, вне зависимости от выбранного столбца сортировки.
   *
   * @param aFieldId String - идентификатор добавляемого поля
   * @param aGetter {@link IM5Getter} - поставщик значения поля, замещающий поставщик от описания поля
   * @return {@link IM5Column} - созданная колонка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такого поля в модели {@link IM5Model}
   * @throws TsItemAlreadyExistsRtException это поле уже добавлено
   */
  IM5Column<T> add( String aFieldId, IM5Getter<T, ?> aGetter );

  /**
   * Removes column by the field ID.
   *
   * @param aFieldId String - the field ID of column to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such field in the model {@link IM5CollectionViewer#model()}
   */
  void remove( String aFieldId );

}
