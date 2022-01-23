package org.toxsoft.unit.txtproj.core.categs;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Валидатор, реализующий стратегию зарета редактирования каталога.
 *
 * @author hazard157
 */
public interface ICatalogueEditValidator {

  /**
   * Проверяет, можно ли создать категорию.
   *
   * @param aParentId String - идентификатор (ИД-путь или пустая строка) родительской категории
   * @param aLocalId String - локальный идентификатор (ИД-путь) создаваемой категории
   * @param aParams {@link IOptionSet} - параметры созданваемой категории
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canCreateCategory( String aParentId, String aLocalId, IOptionSet aParams );

  /**
   * Проверяет, можно ли редактировать категорию.
   *
   * @param aId String - идентификатор редактируемой категории
   * @param aParams {@link IOptionSet} - новые параметры категории
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canEditCategory( String aId, IOptionSet aParams );

  /**
   * Проверяет, можно ли изменить локальный идентификатор категории, и соответственно, всех ее потомков.
   *
   * @param aId String - идентификатор редактируемой категории
   * @param aNewLocalId - новый локальный идентификатор
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canChangeCaregoryLocalId( String aId, String aNewLocalId );

  /**
   * Проверяет, можно ли удалить категорию.
   *
   * @param aId String - идентификатор удаляемой категории
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canRemoveCategory( String aId );

}
