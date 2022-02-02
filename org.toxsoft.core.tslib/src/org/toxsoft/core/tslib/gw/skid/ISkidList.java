package org.toxsoft.core.tslib.gw.skid;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Список идентификаторов объектов.
 *
 * @author hazard157
 */
public interface ISkidList
    extends IList<Skid> {

  /**
   * FIXME maybe agregate, not extend IList ??
   */

  /**
   * Всегда пустой список.
   */
  ISkidList EMPTY = new SkidList();

  /**
   * Создает и возвращает список всех классов, которые встречаются в идентификаторах объектов.
   *
   * @return {@link IStringList} - список идентификаторов классов в списке
   */
  IStringList classIds();

  /**
   * Возвращает все идентификаторы объектов заданного класса, которые содержатся в этом спсике.
   *
   * @param aClassId String - идентификатор класса
   * @return {@link IList}&lt;{@link Skid}&gt; - список идентификаторов объектов указанного класса
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  IList<Skid> listObjSkidsOfClass( String aClassId );

  /**
   * Находит идентификатор, который встречается два или более раз в списке.
   *
   * @return {@link Skid} - первый найденный идентификатор или <code>null</code> если нет повторяющихся элементов
   */
  Skid findDuplicateSkid();

}
