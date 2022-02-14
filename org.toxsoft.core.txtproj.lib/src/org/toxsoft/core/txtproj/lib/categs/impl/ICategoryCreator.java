package org.toxsoft.core.txtproj.lib.categs.impl;

import org.toxsoft.core.tslib.av.opset.IOptionSet;

/**
 * Интерфейс создания переопределенных классов категории.
 *
 * @author hazard157
 */
public interface ICategoryCreator {

  /**
   * Создает категорию (просто вызывает конструктор).
   *
   * @param aCatalogue {@link Catalogue} - родительский каталог
   * @param aId String - полный идентификатор (ИД-путь) категории
   * @param aParams {@link IOptionSet} - значения параметров {@link Category#params()}
   * @return &lt;T&gt; - созданный экземпляр
   */
  Category<?> create( Catalogue<?> aCatalogue, String aId, IOptionSet aParams );

}
