package org.toxsoft.core.txtproj.lib.storage;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Read-only methods of the {@link IKeepablesStorage} interface.
 *
 * @author hazard157
 */
public interface IKeepablesStorageRo {

  /**
   * Determines if the storage contains the section with specified ID.
   *
   * @param aId String - the section ID
   * @return boolean - <code>true</code> if the section with specified ID exists
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean hasSection( String aId );

  // TODO TRANSLATE

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

}
