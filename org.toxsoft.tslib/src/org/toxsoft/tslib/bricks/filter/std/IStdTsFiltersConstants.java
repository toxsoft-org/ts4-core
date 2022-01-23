package org.toxsoft.tslib.bricks.filter.std;

import org.toxsoft.tslib.ITsHardConstants;
import org.toxsoft.tslib.bricks.filter.ITsSingleFilterFactory;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;

/**
 * Персистентные константы стандартных фильтров.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public interface IStdTsFiltersConstants {

  /**
   * Префикс (ИД-путь) идентификаторов стандартных фильтров.
   */
  String STD_FILTERID_ID_PREFIX = ITsHardConstants.TS_ID + "filter.std";

  /**
   * Возвращает список фабрик, известных в этом пакете.
   *
   * @return IStridablesList&lt;{@link ITsSingleFilterFactory}&gt; - список фабрик стандартных фильтров
   */
  default IStridablesList<ITsSingleFilterFactory<?>> listStandardFactories() {
    IStridablesListEdit<ITsSingleFilterFactory<?>> ll = new StridablesList<>( //

    );
    return ll;
  }

}
