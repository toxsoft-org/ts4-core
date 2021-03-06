package org.toxsoft.core.tslib.utils.errors;

/**
 * Localizable resources.
 * <p>
 * Note: these error messages may be translated for differen locales however this is NOT recommended. These exceptions
 * designate programming errors and must be processes by developer programmers. There is absolutely no guarantee that
 * developers understand localisation language.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String MSG_ERR_NULL_ARGUMENT       = "Impermissible null argument";
  String MSG_ERR_ILLEGAL_ARG         = "Illegal argument";
  String ERR_MSG_ITEM_NOT_FOUND      = "Specified element not found";
  String MSG_ERR_ITEM_ALREADY_EXIST  = "Specified element already exists";
  String FMT_MSG_NULL_OBJECT_ERROR   = "Access to Null (special case) instance of class %s";
  String MSG_ERR_NULL_OBJECT_ERROR   = "Access to Null (special case) instance";
  String ERR_MSG_INTERNAL_ERROR      = "Internal error - inconsistent state or invalid logic";
  String ERR_MSG_FILE_IO_EXCEPTION   = "Error acessing file or directory";
  String ERR_MSG_ILLEGAL_STATE       = "Object has illegal state for this method";
  String ERR_MSG_UNSUPPORTED_FEATURE = "Declared method is not appicable for this instance";
  String MSG_FMT_EX_CAUSE_CLASS      = "\n   Exception: ";
  String MSG_FMT_EX_MESSAGE          = "\n     Message: ";
  String ERR_MSG_NOT_ALL_ENUMS_USED  = "Not all enumeratable constants were considered";
  String ERR_MSG_UNDER_DEVELOPMENT   = "This part of program in under development";
  String ERR_MSG_REMOTE              = "Error accessing remote (network) resource";
  String FMT_ERR_ARG_IS_NOT_LIST     = "IList implementation was expected as object: %s";
  String FMT_ERR_LIST_HAS_INV_ITEM   = "Only items of class %s were expected in the list";

}
