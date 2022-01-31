package org.toxsoft.core.tslib.utils.valobj;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String FMT_ERR_NO_KEEPER_FOR_CLASS = "No registered keeper exists for value objects of class '%s'";
  String FMT_ERR_NO_KEEPER_BY_ID     = "No registered keeper exist for keeper identifier '%s'";
  String FMT_ERR_NO_KEEPER_BY_CLASS  = "No registered keeper exist for the entity class '%s'";
  String FMT_ERR_TEXT_START_NOT_AT   = "Fimbed text value must starts by '%c' character instead of '%c'";

}
