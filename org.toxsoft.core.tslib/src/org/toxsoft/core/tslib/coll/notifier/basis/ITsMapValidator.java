package org.toxsoft.core.tslib.coll.notifier.basis;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.INotifierStridablesListEdit;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.notifier.INotifierMap;
import org.toxsoft.core.tslib.coll.primtypes.ILongMapEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

// TRANSLATE

/**
 * Map editing validator.
 * <p>
 * Валидатор используется в редактируемых коллекциях с возможностью извещения INotifierXxx. Соответствующие методам
 * проверки правки в коллекции сначала проверят допустимость действия методом canXxx(). Если метод canXxx() вернет
 * ошибку {@link ValidationResult#isError()}=<code>true</code>, то метод редактирования коллекции выбросит исключение
 * {@link TsValidationFailedRtException}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public interface ITsMapValidator<K, E> {

  /**
   * "Нулевой" валидатор, разрешает все действия.
   */
  @SuppressWarnings( "rawtypes" )
  ITsMapValidator NONE = new InternalNullKeyedCollectionChangeValidator();

  /**
   * Можно ли заменить элемент в ассоциативной коллекции (в карте) новым элементом.
   * <p>
   * Для коллекции, где ключом служит примитивный тип, aKey следует приводит к соответствующему классу (например, для
   * {@link ILongMapEdit} это будет {@link Long}.
   *
   * @param aSource {@link INotifierMap}&lt;K,E&gt; - ассоциативная коллекция, вызвавший валидатор
   * @param aKey K - ключ в ассоциативной коллекции
   * @param aExistingItem E - заменяемый элемент или null, если нет элемента с ключом aKey
   * @param aNewItem E - заменяющий элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя добавить/заменить элемент
   * @throws TsNullArgumentRtException aKey или aNewItem = null
   */
  ValidationResult canPut( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem );

  /**
   * Можно ли удалить элемент из ассоциативной коллекции (карты).
   * <p>
   * Для коллекции, где ключом служит примитивный тип, aKey следует приводит к соответствующему классу (например, для
   * {@link ILongMapEdit} это будет {@link Long}.
   * <p>
   * Перед вызовом метода <b>не</b> проверяется существование элемента с таким ключом, то есть, аргумент-ключ может
   * ссылаться на отсутствующий в ассоциативной коллекции элемент.
   *
   * @param aSource {@link INotifierMap}&lt;K,E&gt; - ассоциативная коллекция, вызвавший валидатор
   * @param aKey K - ключ удаляемого элемента, не бывает null
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя удалить элемент
   * @throws TsNullArgumentRtException aKey = null
   */
  ValidationResult canRemove( INotifierMap<K, E> aSource, K aKey );

  /**
   * Можно ли добавить указанный элемент в коллекцию.
   * <p>
   * В некоторых ассоциативных коллекциях коллекциях (в частности, в наследниках {@link IStridablesList}), наряду с
   * <code>"put"</code> (добавить или заменить), определено действие <code>"add"</code> (строго заменить). Методы ведут
   * себя одинаково, отличие только в том, что в {@link INotifierStridablesListEdit} <code>"add"</code> вызывает этот
   * метод для проверки, а <code>"put"</code> - {@link #canPut(INotifierMap, Object, Object, Object)}, что позволяет,
   * например запретить добавление нового элемента при существующем старом.
   *
   * @param aSource {@link INotifierMap}&lt;K,E&gt; - ассоциативная коллекция, вызвавший валидатор
   * @param aKey K - ключ в ассоциативной коллекции
   * @param aExistingItem E - заменяемый элемент или null, если нет элемента с ключом aKey
   * @param aNewItem E - заменяющий элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя добавлять элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValidationResult canAdd( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem );

}

class InternalNullKeyedCollectionChangeValidator<K, E>
    implements ITsMapValidator<K, E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Method correctly returns {@link ITsMapValidator#NONE} after deserialization.
   *
   * @return Object - instance {@link ITsMapValidator#NONE}
   * @throws ObjectStreamException never thrown by this implementation
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsMapValidator.NONE;
  }

  @Override
  public ValidationResult canPut( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canRemove( INotifierMap<K, E> aSource, K aKey ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canAdd( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem ) {
    return ValidationResult.SUCCESS;
  }
}
