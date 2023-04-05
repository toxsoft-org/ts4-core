package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Manage {@link IStridable} and {@link IParameterized} entities.
 *
 * @author hazard157
 * @param <E> - stridable parameterized entity type
 */
public interface IStriparManager<E extends IStridable & IParameterized>
    extends IProjDataUnit {

  // TODO TRANSLATE

  /**
   * Возвращает список элементов.
   *
   * @return {@link IStridablesList}&lt;E&gt; - список элементов
   */
  IStridablesList<E> items();

  /**
   * Создает элемент (сущность).
   *
   * @param aId String - идентификатор (ИД-путь) элемента
   * @param aParams IOptionSet - параметры элемента
   * @return E - созданный элемент
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла {@link IStriparManagerValidator#canCreateItem(String, IOptionSet)}
   */
  E createItem( String aId, IOptionSet aParams );

  /**
   * Edits the ID and/or parameters of the item.
   * <p>
   * Argument <code>aParams</code> may contain only options to be changed/added, or event be an empty set.
   * <p>
   * Returned instance may be an update existing instance or the new instance.
   *
   * @param aOldId String - the ID of the existing item to change
   * @param aId String - item new ID (an IDpath), may be the same as old ID
   * @param aParams IOptionSet - changed parameters values
   * @return &lt;E&gt; - an edited items (may be newly created instance)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation failed
   */
  E editItem( String aOldId, String aId, IOptionSet aParams );

  /**
   * Changes only parameters of the item without changing the ID.
   * <p>
   * Argument <code>aParams</code> may contain only options to be changed/added, or event be an empty set.
   * <p>
   * Returned instance may be an update existing instance or the new instance.
   *
   * @param aId String - existing item ID (an IDpath)
   * @param aParams IOptionSet - changed parameters values
   * @return &lt;E&gt; - an edited items (may be newly created instance)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation failed
   */
  E editItem( String aId, IOptionSet aParams );

  /**
   * Удаляет элемент.
   * <p>
   * Если нет такого элемента, метод ничего не делает.
   *
   * @param aId String - идентификатор удаляемого элемента
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException не прошла {@link IStriparManagerValidator#canRemoveItem(String)}
   */
  void removeItem( String aId );

  /**
   * Возвращает средство работы с событиями редактирования.
   *
   * @return {@link IStriparManagerListener} - средство работы с событиями
   */
  ITsEventer<IStriparManagerListener> eventer();

  /**
   * Возвращает средство предварительной валидации выполнения методов.
   *
   * @return {@link ITsValidationSupport}&lt;{@link IStriparManagerValidator}&gt; - средство валидации службы
   */
  ITsValidationSupport<IStriparManagerValidator> svs();

}
