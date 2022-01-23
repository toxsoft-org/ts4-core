package org.toxsoft.unit.txtproj.core.categs.pdu;

import org.toxsoft.unit.txtproj.core.IProjDataUnit;
import org.toxsoft.unit.txtproj.core.categs.ICatalogue;

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
