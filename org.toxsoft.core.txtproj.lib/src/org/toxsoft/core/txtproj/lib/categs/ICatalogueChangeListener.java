package org.toxsoft.core.txtproj.lib.categs;

import org.toxsoft.core.tslib.coll.helpers.ECrudOp;

/**
 * Слушатель изменений каталога.
 *
 * @author hazard157
 */
public interface ICatalogueChangeListener {

  /**
   * Вызывается при изменении в каталоге.
   * <p>
   * В зависимости от вида события, идентификат категории имеет следующий смысл:
   * <ul>
   * <li>{@link ECrudOp#CREATE} - идентификатор добавленной категории, которая, очевидно не имеет детей;</li>
   * <li>{@link ECrudOp#EDIT} - означает, что у указанной категории поменись параметры {@link ICategory#params()}, имя
   * {@link ICategory#nmName()} или описание {@link ICategory#description()}. Изменений в иерархии категорий не
   * было;</li>
   * <li>{@link ECrudOp#REMOVE} - удалена указанная категория со всеми дочерними категориями;</li>
   * <li>{@link ECrudOp#LIST} - было несколько изменений сразу в поддереве категорий, начиная с указанной категории.
   * Если изменился весь каталог (например, был загружен с хранилища), то идентификатор категории имеет идентификатор
   * каталога {@link ICatalogue#id()}, то есть, будет пустой строкой;</li>
   * </ul>
   *
   * @param aCatalogue {@link ICatalogue} - источник события
   * @param aOp {@link ECrudOp} - вид события
   * @param aCategoryId String - идентификатор категории (ИД-путь или пустая строка для корневой категории)
   */
  void onCatalogueChanged( ICatalogue<?> aCatalogue, ECrudOp aOp, String aCategoryId );

}
