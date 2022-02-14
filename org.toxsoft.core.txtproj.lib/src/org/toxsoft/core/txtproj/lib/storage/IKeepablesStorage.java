package org.toxsoft.core.txtproj.lib.storage;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.StrioRtException;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.basis.ITsClearable;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.ITsProject;

/**
 * Хранилище сериализуемых с помощью {@link IEntityKeeper} сущностей.
 * <p>
 * Конкретная реализация может хранить сущности в файлах и директориях, в проекте {@link ITsProject}, в реестре, в
 * сервере S5 и т.п.
 *
 * @author hazard157
 */
public interface IKeepablesStorage
    extends ITsClearable {

  /**
   * Определяет, если в хранилище раздел с указанным идентификатором.
   *
   * @param aId String - искомый идентификатор
   * @return boolean - признак существования указанного раздела
   */
  boolean hasSection( String aId );

  /**
   * Загружает объект из разедла.
   * <p>
   * Если раздела с указанным идентификатором нет в хранилище, метод возвращает аргумент aDefault.
   *
   * @param <T> - конкретный тип объектов
   * @param aId String - уникальный в рамках хранилища идентификатор (ИД-путь) раздела
   * @param aKeeper {@link IEntityKeeper} - хранитель элементов
   * @param aDefault &lt;T&gt - значение, возвращаемое при отсутствии раздела, может быть <code>null</code>
   * @return &lt;T&gt - загруженный объект или aDefulat
   * @throws TsNullArgumentRtException любой (кроме aDefault) аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   * @throws TsIoRtException ошибка чтения из хранилища
   * @throws StrioRtException неверный формат хранения или в разеле хранятся другие сущности
   */
  <T> T readItem( String aId, IEntityKeeper<T> aKeeper, T aDefault );

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
   * Загружает коллекцию объектов.
   * <p>
   * Если раздела с указанным идентификатором нет в хранилище, метод возвращает пустой список.
   *
   * @param <T> - конкретный тип объектов
   * @param aId String - уникальный в рамках хранилища идентификатор (ИД-путь) раздела
   * @param aKeeper {@link IEntityKeeper} - хранитель элементов
   * @return {@link IList}&lt;T&gt - загруженный список
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   * @throws TsIoRtException ошибка чтения из хранилища
   * @throws StrioRtException неверный формат хранения или в разеле хранятся другие сущности
   */
  <T> IList<T> readColl( String aId, IEntityKeeper<T> aKeeper );

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
