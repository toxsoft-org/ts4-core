package org.toxsoft.core.unit.txtproj.lib.categs.pdu;

import org.toxsoft.core.unit.txtproj.lib.IProjDataUnit;
import org.toxsoft.core.unit.txtproj.lib.categs.ICatalogue;

/**
 * Компонента файла проекта для хранения одного каталога.
 *
 * @author hazard157
 */
public interface ICatalogueProjDataUnit
    extends IProjDataUnit {

  /**
   * Возвращает каталог, хранимый в файле проекта.
   *
   * @return {@link ICatalogue} - каталог
   */
  ICatalogue<?> catalogue();

}
