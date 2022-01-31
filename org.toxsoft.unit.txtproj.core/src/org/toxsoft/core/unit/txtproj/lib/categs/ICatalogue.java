package org.toxsoft.core.unit.txtproj.lib.categs;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.events.ITsEventer;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.validator.ITsValidationSupport;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.basis.ITsClearable;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Каталог - дерево категории.
 * <p>
 * Древовидность категорий обеспечиватся форматом ИД-путей - идентификаторов {@link ICategory#id()}. Доечние категории
 * добавляют локальный ИД-имя идентификатор {@link ICategory#localId()} к ИД-путь идентификатору родителя. Корневой
 * категорией является сам каталог {@link ICatalogue}, у которого идентификатор - пустая строка.
 * <p>
 * Несмотря на то, что каталог является {@link IStridable}, его идентификатор {@link #id()} - пустая строка! Также
 * всегда пустые строки {@link #nmName()} и {@link #description()}.
 *
 * @author hazard157
 * @param <T> - конкретный наследник (реализация) категори (но не каталога)
 */
public interface ICatalogue<T extends ICategory<T>>
    extends ICategory<T>, ITsClearable {

  // FIXME добавить поддержку known IOptionInfo опции для:
  // 1) M5-модели
  // 2) не сохранять значения по умолчанию
  // 3) использовать, если совпадают с известными опциями типа ICON_NAME, IS_LEAF и т.п.

  /**
   * Создает категорию.
   *
   * @param aParentId String - идентификатор (ИД-путь или пустая строка) родительской категории
   * @param aLocalId String - локальный идентификатор (ИД-путь) создаваемой категории
   * @param aParams {@link IOptionSet} - параметры созданваемой категории
   * @return &lt;T&gt; - созданная категория
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка
   *           {@link ICatalogueEditValidator#canCreateCategory(String, String, IOptionSet)}
   */
  T createCategory( String aParentId, String aLocalId, IOptionSet aParams );

  /**
   * Редактирует свойства категории.
   *
   * @param aId String - идентификатор редактируемой категории
   * @param aParams {@link IOptionSet} - новые параметры категории
   * @return &lt;T&gt; - отредктированная категория
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка
   *           {@link ICatalogueEditValidator#canEditCategory(String, IOptionSet)}
   */
  T editCategory( String aId, IOptionSet aParams );

  /**
   * Изменяет лоальный идентификатор категории, и соответственно, всех ее потомков.
   * <p>
   * В результате правки все ссылки на измененные катгории становятся недействительными.
   *
   * @param aId String - идентификатор редактируемой категории
   * @param aNewLocalId - новый локальный идентификатор
   * @return &lt;T&gt; - отредктированная категория
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка
   *           {@link ICatalogueEditValidator#canChangeCaregoryLocalId(String, String)}
   */
  T changeCaregoryLocalId( String aId, String aNewLocalId );

  /**
   * Удаляет категорию.
   *
   * @param aId String - идентификатор удаляемой категории
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка {@link ICatalogueEditValidator#canRemoveCategory(String)}
   */
  void removeCategory( String aId );

  /**
   * Находит категорию по идентификатору.
   *
   * @param aCatgoryId string - идентификатор искомой категории
   * @return {@link ICategory} - найденная категория или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ICategory<T> findCategory( String aCatgoryId );

  /**
   * Возвращает категорию по идентификатору.
   *
   * @param aCatgoryId string - идентификатор искомой категории
   * @return {@link ICategory} - найденная категория
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException нет такой категории
   */
  ICategory<T> getCategory( String aCatgoryId );

  /**
   * Возвращает запрошенные потомки, начиная с корня - указанной категории.
   *
   * @param aCategoryId String - идентификатор корневой категории поиска, пустая строка - начать с каталога
   * @param aIsSelf boolean - признак включения корневой категории список (она будет первой в списке)
   * @param aIsGroups boolean - признак включения групп (то есть, категории, имеющих дочерние категории)
   * @param aIsLeafs boolean - признак включения листьев (то есть, категории без дочерних категории)
   * @return IStridablesList&lt;T&gt; - запрошенные потомки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException нет такой категории
   */
  IStridablesList<T> listScionCategories( String aCategoryId, boolean aIsSelf, boolean aIsGroups, boolean aIsLeafs );

  /**
   * Возвращает средство работы с событиями редактирования.
   *
   * @return {@link ITsEventer} - средство работы с событиями
   */
  ITsEventer<ICatalogueChangeListener> eventer();

  /**
   * Возвращает средсва валидации вызовов методов редактирования.
   *
   * @return {@link ITsValidationSupport} - поддержка валидации
   */
  ITsValidationSupport<ICatalogueEditValidator> svs();

}
