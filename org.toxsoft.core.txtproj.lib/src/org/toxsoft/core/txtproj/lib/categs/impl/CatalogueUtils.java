package org.toxsoft.core.txtproj.lib.categs.impl;

import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.txtproj.lib.categs.ICatalogue;
import org.toxsoft.core.txtproj.lib.categs.ICategory;

/**
 * Вспомогательные методы работы с категориями {@link ICategory} и каталогами {@link ICatalogue}.
 *
 * @author hazard157
 */
public class CatalogueUtils {

  /**
   * Извлекает идентификатор родительской категории из указанного идентификатора категории.
   * <p>
   * Если аргумент пустая строка (то есть, идентификатор каталога {@link ICatalogue#id()}, то возвращает
   * <code>null</code>.
   *
   * @param aCategoryId String - идентфикатор категории
   * @return String - идентификатор родительской категории или пустая строка или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент не ИД-путь и не пустая строка
   */
  public static final String extractParentId( String aCategoryId ) {
    TsNullArgumentRtException.checkNull( aCategoryId );
    if( aCategoryId.isEmpty() ) {
      return null;
    }
    return StridUtils.removeTailingIdNames( aCategoryId, 1 );
  }

  /**
   * Извлекает локальный идентификатор категории из указанного идентификатора категории.
   * <p>
   * Если аргумент пустая строка (то есть, идентификатор каталога {@link ICatalogue#id()}, то возвращает пустую строку.
   *
   * @param aCategoryId String - идентфикатор категории
   * @return String - локальный идентификатор категории или пустая строка
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент не ИД-путь и не пустая строка
   */
  public static final String extractLocalId( String aCategoryId ) {
    TsNullArgumentRtException.checkNull( aCategoryId );
    if( aCategoryId.isEmpty() ) {
      return TsLibUtils.EMPTY_STRING;
    }
    return StridUtils.getLast( aCategoryId );
  }

  /**
   * Создает идентификатор дочерней категории.
   *
   * @param aParentId String - идентификатор родительской категории (ИД-путь или пустая строка)
   * @param aLocalId String - локальный идентификатор (ИД-имя) дочерней категории
   * @return String - идентификатор дочерней категории
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aParentId не ИД-путь и не пустая строка
   * @throws TsIllegalArgumentRtException aLocalId не ИД-путь
   */
  public static final String makeCategoryId( String aParentId, String aLocalId ) {
    TsNullArgumentRtException.checkNull( aParentId );
    StridUtils.checkValidIdName( aLocalId );
    if( aParentId.isEmpty() ) {
      return aLocalId;
    }
    return StridUtils.makeIdPath( aParentId, aLocalId );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private CatalogueUtils() {
    // nop
  }

}
