package org.toxsoft.tslib.coll.notifier.basis;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.notifier.INotifierList;
import org.toxsoft.tslib.coll.primtypes.*;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TRANSLATE

/**
 * Валидатор изменения линейной коллекции.
 * <p>
 * Линейная коллекция - это фактически список элементов, к которым в библиотеке tslib относятся: {@link IList},
 * {@link IStringList}, {@link IIntList}, {@link ILongList}.
 * <p>
 * Валидатор используется в редактируемых коллекциях с возможностью извещения INotifierXxx. Соответствующие методам
 * проверки правки в коллекции сначала проверят допустимость действия методом canXxx(). Если метод canXxx() вернет
 * ошибку {@link ValidationResult#isError()}=<code>true</code>, то метод редактирования коллекции выбросит исключение
 * {@link TsValidationFailedRtException}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface ITsListValidator<E> {

  /**
   * "Нулевой" валидатор, разрешает все действия.
   */
  @SuppressWarnings( "rawtypes" )
  ITsListValidator NONE = new InternalNoneListValidator();

  /**
   * Можно ли добавить указанный элемент в линейную коллекцию.
   *
   * @param aSource {@link INotifierList}&lt;E&gt; - линейная коллекция, вызвавший валидатор
   * @param aNewItem E - добавляемый элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя добавлять элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValidationResult canAdd( INotifierList<E> aSource, E aNewItem );

  /**
   * Можно ли удалить указанный элемент из линейной коллекции.
   *
   * @param aSource {@link INotifierList}&lt;E&gt; - линейная коллекция, вызвавший валидатор
   * @param aRemovingItem E - удаляемый элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя удалять элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValidationResult canRemove( INotifierList<E> aSource, E aRemovingItem );

  /**
   * Можно ли заменить элемент в линейной коллекции новым элементом.
   * <p>
   * Метод применяется только для линейных коллекции . При замене элементов в линейной коллекции (список), элемент
   * aExistingItem всегда не-null.
   *
   * @param aSource {@link INotifierList}&lt;E&gt; - линейная коллекция, вызвавший валидатор
   * @param aExistingItem E - заменяемый элемент
   * @param aNewItem E - заменяющий элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя заменить элемент
   * @throws TsNullArgumentRtException aNewItem = null
   */
  ValidationResult canReplace( INotifierList<E> aSource, E aExistingItem, E aNewItem );

}

class InternalNoneListValidator<E>
    implements ITsListValidator<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Method correctly returns {@link ITsListValidator#NONE} after deserialization.
   *
   * @return Object - instance {@link ITsListValidator#NONE}
   * @throws ObjectStreamException never thrown by this implementation
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsListValidator.NONE;
  }

  @Override
  public ValidationResult canAdd( INotifierList<E> aSource, E aNewItem ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canRemove( INotifierList<E> aSource, E aRemovingItem ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canReplace( INotifierList<E> aSource, E aExistingItem, E aNewItem ) {
    return ValidationResult.SUCCESS;
  }

}
