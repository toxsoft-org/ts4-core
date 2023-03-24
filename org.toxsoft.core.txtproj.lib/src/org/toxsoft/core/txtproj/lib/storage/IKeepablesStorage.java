package org.toxsoft.core.txtproj.lib.storage;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Хранилище сериализуемых с помощью {@link IEntityKeeper} сущностей.
 * <p>
 * Конкретная реализация может хранить сущности в файлах и директориях, в проекте {@link ITsProject}, в реестре, в
 * сервере S5 и т.п.
 *
 * @author hazard157
 */
public interface IKeepablesStorage
    extends IKeepablesStorageRo, ITsClearable {

  /**
   * Записывает объект в хранилище.
   *
   * @param <T> - конкретный тип объектов
   * @param aId String - уникальный в рамках хранилища идентификатор (ИД-путь) раздела
   * @param aItem &lt;T&gt; - записываемый объект
   * @param aKeeper {@link IEntityKeeper} - хранитель элементов
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIoRtException ошибка записи в хранилище
   */
  <T> void writeItem( String aId, T aItem, IEntityKeeper<T> aKeeper );

  /**
   * Записывает коллекцию объектов.
   *
   * @param <T> - конкретный тип объектов
   * @param aId String - уникальный в рамках хранилища идентификатор (ИД-путь) раздела
   * @param aColl {@link ITsCollection}&lt;T&gt - записываемая коллекция
   * @param aKeeper {@link IEntityKeeper} - хранитель элементов
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   * @throws TsIoRtException ошибка записи в хранилище
   */
  <T> void writeColl( String aId, ITsCollection<T> aColl, IEntityKeeper<T> aKeeper );

  /**
   * Удаляет раздел из хранилища.
   * <p>
   * Если такого раздела нет в хранилище, метод ничего не делает.
   *
   * @param aId String - идентификатор удалаемого раздела
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void removeSection( String aId );

}
