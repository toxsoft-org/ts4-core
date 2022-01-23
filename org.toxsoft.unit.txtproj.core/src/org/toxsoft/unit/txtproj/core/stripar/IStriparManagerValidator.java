package org.toxsoft.unit.txtproj.core.stripar;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Влидатор редактирования {@link IStriparManager}.
 *
 * @author hazard157
 */
public interface IStriparManagerValidator {

  /**
   * Проверяет врозможность создания элемента.
   *
   * @param aId String - идентификатор (ИД-путь) элемента
   * @param aInfo IOptionSet - описание элемента
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canCreateItem( String aId, IOptionSet aInfo );

  /**
   * Проверяет возможность редактирования существующийй элемента.
   *
   * @param aOldId String - идентификатор существующего элемента
   * @param aId String - новый идентификатор (ИД-путь) элемента (может совпадать со старым)
   * @param aInfo IOptionSet - описание элемента
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canEditItem( String aOldId, String aId, IOptionSet aInfo );

  /**
   * Проверяет возможность удаления элемента.
   *
   * @param aId String - идентификатор удаляемого элемента
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValidationResult canRemoveItem( String aId );

}
