package org.toxsoft.core.tslib.gw.gwid;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.gw.skid.ISkidList;

/**
 * Список {@link Gwid}-ов расширяет {@link IList}, и дает дополнительные возможности.
 * <p>
 * Реализован как интерфес "только-для-чтения" и клсаа {@link GwidList} для редактирования.
 *
 * @author hazard157
 */
public interface IGwidList
    extends IList<Gwid> {

  /**
   * Всегда пустой список.
   */
  IGwidList EMPTY = new GwidList();

  /**
   * Создает и возвращает список всех классов, которые встречаются в элементах списка.
   *
   * @return {@link IStringList} - список идентификаторов классов в списке
   */
  IStringList listClassIds();

  /**
   * Returns the SKIDs of all objects in all concrete GWIDs in list.
   * <p>
   * Returned list does not contains duplicated values.
   *
   * @return {@link ISkidList} - SKIDs list
   */
  ISkidList objIds();

  // TODO какие еще методы могут оказаться полезными?

}
