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
   * @param aInfo IOptionSet - параметры элемента
   * @return E - созданный элемент
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла {@link IStriparManagerValidator#canCreateItem(String, IOptionSet)}
   */
  E createItem( String aId, IOptionSet aInfo );

  /**
   * Редактирует существующийй элемент.
   * <p>
   * Аргумент aInfo может содержать только измененные параметры, старые останутся без изменений.
   * <p>
   * Обратите внимание, что изменение идентификатора приводит к созданию нового элемента взамен существующего.
   *
   * @param aOldId String - идентификатор существующего элемента
   * @param aId String - новый идентификатор (ИД-путь) элемента (может совпадать со старым)
   * @param aInfo IOptionSet - параметры элемента
   * @return E - отредкатированный или вновь созданный элемент
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла
   *           {@link IStriparManagerValidator#canEditItem(String, String, IOptionSet)}
   */
  E editItem( String aOldId, String aId, IOptionSet aInfo );

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
