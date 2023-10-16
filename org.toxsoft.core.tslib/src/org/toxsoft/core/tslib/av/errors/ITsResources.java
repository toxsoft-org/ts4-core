package org.toxsoft.core.tslib.av.errors;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String LOG_STR_ERR_STD_DATA_LOSS        = "Loss of data due to reduced representation accuracy";
  String LOG_STR_ERR_STD_TYPE_CAST        = "Cannot convert one atomic type to another";
  String LOG_FMT_ERR_CANT_ASSIGN          = "A variable of atomic type %s cannot be assigned a value of type %s";
  String LOG_STR_ERR_STD_UNASSIGNED_VALUE = "This value is not assigned";

}
