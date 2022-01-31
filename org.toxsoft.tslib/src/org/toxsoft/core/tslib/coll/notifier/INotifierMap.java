package org.toxsoft.core.tslib.coll.notifier;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.INotifierStridablesListEdit;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.IMap;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.IIntMap;
import org.toxsoft.core.tslib.coll.primtypes.ILongMapEdit;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Any map with ability to be a data model.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public interface INotifierMap<K, E>
    extends IMap<K, E>, ITsNotifierCollection {

  // TRANSLATE

  /**
   * Проверяет, можно ли добавить (заменить) элемент в ассоциативной коллекции (в карте) новым элементом.
   * <p>
   * Для коллекции, где ключом служит примитивный тип, aKey следует приводит к соответствующему классу (например, для
   * {@link ILongMapEdit} это будет {@link Long}.
   *
   * @param aKey K - ключ в ассоциативной коллекции
   * @param aNewItem E - новый элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя добавить/заменить элемент
   * @throws TsNullArgumentRtException aKey или aNewItem = null
   */
  ValidationResult canPut( K aKey, E aNewItem );

  /**
   * Проверяет, можно ли удалить элемент из ассоциативной коллекции (карты).
   * <p>
   * Для коллекции, где ключом служит примитивный тип, aKey следует приводит к соответствующему классу (например, для
   * {@link ILongMapEdit} это будет {@link Long}.
   *
   * @param aKey K - ключ удаляемого элемента
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя удалить элемент
   * @throws TsNullArgumentRtException aKey = null
   */
  ValidationResult canRemove( K aKey );

  /**
   * Проверяет, можно ли добавить указанный элемент в коллекцию.
   * <p>
   * Существуют ассоциативные коллекции типизированных сущностей, которые в себе содержат свои ключи. Например, к таким
   * сущностьям относятся наследники {@link IStridable} или entity-классы, соответствующие записями в СУБД. Для таких
   * коллекции (в частности, {@link INotifierStridablesListEdit}), наряду с <code>"put"</code> (добавить или заменить),
   * определено действие <code>"add"</code> (строго добавить - при существовании элемента выбросить исключение).
   * Соответственно появляется этот метод {@link #canAdd(Object, Object)}.
   *
   * @param aKey K - ключ в ассоциативной коллекции
   * @param aNewItem E - новый элемент
   * @return {@link ValidationResult} - результат проверки, ошибка {@link ValidationResult#isError()}=<code>true</code>
   *         означает, что нельзя добавлять элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValidationResult canAdd( K aKey, E aNewItem );

  /**
   * Пользователь этим методом извещает коллекцию о том, что изменилось содержимое элемент (без изменения ссылки).
   * <p>
   * Данный метод приводит к генерации сообщения
   * {@link ITsCollectionChangeListener#onCollectionChanged(Object, ECrudOp, Object)} с кодом операции
   * {@link ECrudOp#EDIT}.
   * <p>
   * Для карт, ключом которым служат примитивные типы, тип аргумента приводится к соответствующему ссылочному типу
   * (например, для {@link IIntMap} aKey имеет тип {@link Integer}). Этот метод применим также и к
   * {@link IStridablesList}, в этом случае aKey приводится к {@link String}.
   *
   * @param aKey K - ключ изменившегося элемента в карте (следует привести к типу ключа в карте)
   * @throws TsNullArgumentRtException аргумент = null
   * @throws ClassCastException тип аргумента не соответствует ожидаемому
   * @throws TsItemNotFoundRtException нет элемента с таким ключом
   */
  void fireItemByKeyChangeEvent( K aKey );

  /**
   * Возвращает валидатор проверки воможнсти внесения изменений в коллецию.
   *
   * @return {@link ITsMapValidator} - валидатор изменении, по умолчанию разрешающий все
   */
  ITsMapValidator<K, E> getMapValidator();

  /**
   * Add map modification validator.
   * <p>
   * If validator is already added, method soes nothing.
   *
   * @param aValidator {@link ITsMapValidator} - modifications validator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addCollectionChangeValidator( ITsMapValidator<K, E> aValidator );

  /**
   * Removes the validator.
   * <p>
   * If validator was not added, method soes nothing.
   *
   * @param aValidator {@link ITsMapValidator} - modifications validator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeCollectionChangeValidator( ITsMapValidator<K, E> aValidator );

}
