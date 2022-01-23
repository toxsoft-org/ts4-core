package org.toxsoft.tslib.coll.notifier;

import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.notifier.basis.ITsListValidator;
import org.toxsoft.tslib.coll.notifier.basis.ITsNotifierCollection;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TRANSLATE

/**
 * Любая линейная коллекция, которая извещает об изменениях.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface INotifierList<E>
    extends IList<E>, ITsNotifierCollection {

  /**
   * Проверяет, можно ли добавить указанный элемент в линейную коллекцию.
   *
   * @param aNewItem E - добавляемый элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя добавлять элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValidationResult canAdd( E aNewItem );

  /**
   * Проверяет, можно ли удалить указанный элемент из линейной коллекции.
   *
   * @param aIndex int - индекс удаляемого элемента
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя удалять элемент
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aIndex выходит за допустимые пределы
   */
  ValidationResult canRemove( int aIndex );

  /**
   * Проверяет, можно ли заменить элемент в линейной коллекции новым элементом.
   *
   * @param aIndex int - индекс заменяемого элемента
   * @param aNewItem E - заменяющий элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя заменить элемент
   * @throws TsNullArgumentRtException aNewItem = null
   * @throws TsIllegalArgumentRtException aIndex выходит за допустимые пределы
   */
  ValidationResult canReplace( int aIndex, E aNewItem );

  /**
   * Возвращает валидатор проверки воможнсти внесения изменений в коллецию.
   *
   * @return {@link ITsListValidator} - валидатор изменении, по умолчанию разрешающий все
   */
  ITsListValidator<E> getListValidator();

  /**
   * Adds the validator.
   * <p>
   * If validator is already added, method soes nothing.
   *
   * @param aValidator {@link ITsListValidator} - the validator
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void addCollectionChangeValidator( ITsListValidator<E> aValidator );

  /**
   * Removes the validator.
   * <p>
   * If validator was not added, method soes nothing.
   *
   * @param aValidator {@link ITsListValidator} - the validator
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void removeCollectionChangeValidator( ITsListValidator<E> aValidator );

}
