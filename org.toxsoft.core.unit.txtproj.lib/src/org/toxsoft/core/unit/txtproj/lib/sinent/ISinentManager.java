package org.toxsoft.core.unit.txtproj.lib.sinent;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.INotifierStridablesList;
import org.toxsoft.core.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.unit.txtproj.lib.IProjDataUnit;

/**
 * Менеджер управления ЖЦ и хранения {@link IStridable} сущностей (элементов) с XxxInfo описанием.
 *
 * @author hazard157
 * @param <E> - тип (класс) сущности
 * @param <F> - тип (класс) информации о сущности
 */
public interface ISinentManager<E extends ISinentity<F>, F>
    extends IProjDataUnit {

  /**
   * Возвращает список элементов.
   *
   * @return {@link INotifierStridablesList}&lt;E&gt; - список элементов
   */
  INotifierStridablesList<E> items();

  /**
   * Создает элемент (сущность).
   *
   * @param aId String - идентификатор (ИД-путь) элемента
   * @param aInfo F - описание элемента
   * @return E - созданный элемент
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException проверка {@link #canCreateItem(String, Object)} вернула ошибку
   */
  E createItem( String aId, F aInfo );

  /**
   * Проверяет, можно ли создать новый элементы.
   * <p>
   * Возвращает ошибку {@link EValidationResultType#ERROR}, если:
   * <ul>
   * <li>aId не ИД путь;</li>
   * <li>элемент с таким идентификатором уже существует</li>
   * </ul>
   * <p>
   * Допонительно, в зависимости от сути сущностей и/или способа хранения в коллекции, может возвращает другие ошибки
   * или предупреждения.
   *
   * @param aId String - идентификатор (ИД-путь) элемента
   * @param aInfo F - описание элемента
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canCreateItem( String aId, F aInfo );

  /**
   * Редактирует существующийй элемент.
   *
   * @param aOldId String - идентификатор существующего элемента
   * @param aId String - новый идентификатор (ИД-путь) элемента (может совпадать со старым)
   * @param aInfo F - описание элемента
   * @return E - отредкатированный или вновь созданный элемент
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException проверка {@link #canEditItem(String, String, Object)} вернула ошибку
   */
  E editItem( String aOldId, String aId, F aInfo );

  /**
   * Проверяет, можно ли редактировать существующий элементы.
   * <p>
   * Возвращает ошибку {@link EValidationResultType#ERROR}, если:
   * <ul>
   * <li>aId не ИД путь;</li>
   * <li>элемент с идентификатором aOldId не существует</li>
   * <li>при изменении идентификатора, элемент с новым идентификатором aId уже существует</li>
   * </ul>
   * <p>
   * Допонительно, в зависимости от сути сущностей и/или способа хранения в коллекции, может возвращает другие ошибки
   * или предупреждения.
   *
   * @param aOldId String - идентификатор существующего элемента
   * @param aId String - новый идентификатор (ИД-путь) элемента (может совпадать со старым)
   * @param aInfo F - описание элемента
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canEditItem( String aOldId, String aId, F aInfo );

  /**
   * Удаляет элемент.
   * <p>
   * Если нет такого элемента, метод ничего не делает.
   *
   * @param aId String - идентификатор удаляемого элемента
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException проверка {@link #canRemoveItem(String)} вернула ошибку
   */
  void removeItem( String aId );

  /**
   * Проверяет, можно ли редактировать существующий элементы.
   * <p>
   * Возвращает предупреждение {@link EValidationResultType#WARNING}, если:
   * <ul>
   * <li>элемент с таким идентификатором не существует</li>
   * </ul>
   * <p>
   * Допонительно, в зависимости от сути сущностей и/или способа хранения в коллекции, может возвращает другие ошибки
   * или предупреждения.
   *
   * @param aId String - идентификатор удаляемого элемента
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValidationResult canRemoveItem( String aId );

}
