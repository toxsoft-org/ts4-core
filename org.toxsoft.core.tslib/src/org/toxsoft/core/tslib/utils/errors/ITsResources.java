package org.toxsoft.core.tslib.utils.errors;

/**
 * Localizable resources.
 * <p>
 * Note: these error messages may be translated for different locales however this is NOT recommended. These exceptions
 * designate programming errors and must be processes by developer programmers. There is absolutely no guarantee that
 * developers understand localization language. Using English language is especially actual for log messages.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String LOG_STR_ERR_NULL_ARGUMENT           = "Impermissible null argument";
  String LOG_STR_ERR_ILLEGAL_ARG             = "Illegal argument";
  String LOG_STR_ERR_ITEM_NOT_FOUND          = "Specified element not found";
  String LOG_STR_ERR_ITEM_ALREADY_EXIST      = "Specified element already exists";
  String LOG_FMT_ERR_NULL_OBJECT_ERROR       = "Access to Null (special case) instance of class %s";
  String LOG_STR_ERR_NULL_OBJECT_ERROR       = "Access to Null (special case) instance";
  String LOG_STR_ERR_INTERNAL_ERROR          = "Internal error - inconsistent state or invalid logic";
  String LOG_STR_ERR_FILE_IO_EXCEPTION       = "Error acessing file or directory";
  String LOG_STR_ERR_ILLEGAL_STATE           = "Object has illegal state for this method";
  String LOG_STR_ERR_UNSUPPORTED_FEATURE     = "Declared method is not appicable for this instance";
  String LOG_STR_ERR_NOT_ALL_ENUMS_USED      = "Not all enumeratable constants were considered";
  String LOG_STR_ERR_UNDER_DEVELOPMENT       = "This part of program in under development";
  String LOG_STR_ERR_REMOTE                  = "Error accessing remote (network) resource";
  String LOG_FMT_ERR_ARG_IS_NOT_LIST         = "IList implementation was expected as object: %s";
  String LOG_FMT_ERR_LIST_HAS_INV_ITEM       = "Only items of class %s were expected in the list";
  String LOG_STR_ERR_INV_INDEX_NEGATIVE      = "Invalid index: negative index";
  String LOG_STR_ERR_INV_INDEX_ON_EMPTY_COLL = "Invalid index: can't use index on an empty collection";
  String LOG_FMT_ERR_INV_INDEX_OUT_OF_RANGE  = "Invalid index: out of allowed range 0 .. %d";

}
