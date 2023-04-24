package org.toxsoft.core.txtproj.lib.categs;

import static org.toxsoft.core.txtproj.lib.categs.ICatalogueParamOptions.*;

import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.icons.*;

/**
 * Категория - элемент древовидного каталога.
 *
 * @author hazard157
 * @param <T> - конкретный наследник (реализация) категории (но не каталога)
 */
public interface ICategory<T extends ICategory<T>>
    extends IStridable, IParameterized, Comparable<ICategory<?>>, IIconIdable {

  /**
   * Возвращает локальный (дочерний) идентификатор категории.
   * <p>
   * Для {@link ICatalogue} возвращает пустую строку, для всех остальных категории - ИД-имя.
   *
   * @return String - локальный идентификатор (ИД-имя или пустая строка)
   */
  String localId();

  /**
   * Возвращает каталог, к которому относится категория.
   *
   * @return {@link ICatalogue} - возвращает родительский каталог
   */
  ICatalogue<T> catalogue();

  /**
   * Возвращает родительскую категорию.
   *
   * @return {@link ICategory} - родительская категория или <code>null</code> для {@link ICatalogue}
   */
  ICategory<T> parent();

  /**
   * Возвращает дочерние категории.
   *
   * @return IStridablesList&lt;T&gt; - дочерние категории
   */
  IStridablesList<T> childCategories();

  /**
   * Возвращает все потомки-категории.
   *
   * @return IStridablesList&lt;T&gt; - все потомки-категории
   */
  IStridablesList<T> scionCategories();

  // ------------------------------------------------------------------------------------
  // Inline методы для удобства использования
  //

  /**
   * Определяет, является ли категория листом дерева категории (то есть, у нее отсутствует дочерние категории).
   *
   * @return boolean - признак отсутсвия дочерних категорий
   */
  default boolean isLeaf() {
    return childCategories().isEmpty();
  }

  /**
   * Определяет, является ли категория группой категории (то есть, у нее есть дочерние категории).
   *
   * @return boolean - признак наличия дочерних категорий
   */
  default boolean isGroup() {
    return !childCategories().isEmpty();
  }

  /**
   * Определяет, имеется ли у категории дочерние категории.
   *
   * @return boolean - признак наличия хотя бы одного дочернего категория
   */
  default boolean hasChildren() {
    return !childCategories().isEmpty();
  }

  /**
   * Определяет, запрещено ли категории иметь дочерние категорий.
   *
   * @return boolean - признак, что категория не может иметь дочерные категория
   */
  default boolean canHaveChildren() {
    return OP_CAN_HAVE_CHILDREN.getValue( params() ).asBool();
  }

  /**
   * Определяет, является ли катгория корневой категорией.
   * <p>
   * Корневая категория всегда является каталогом {@link ICatalogue}.
   *
   * @return boolean - признак корневой катгории
   */
  default boolean isRoot() {
    return parent() == null;
  }

}
