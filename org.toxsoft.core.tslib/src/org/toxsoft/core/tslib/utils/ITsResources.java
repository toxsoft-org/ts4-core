package org.toxsoft.core.tslib.utils;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link ELockState}
   */
  String STR_N_LOCK_NONE  = "No lock";
  String STR_D_LOCK_NONE  = "Resource is not locked";
  String STR_N_LOCK_READ  = "Read";
  String STR_D_LOCK_READ  = "Resource is locked for reading - writing is prohibited";
  String STR_N_LOCK_WRITE = "Write";
  String STR_D_LOCK_WRITE = "Resource is locked for writing and thus is not accessible";

  /**
   * {@link ESortOrder}
   */
  String STR_N_SORT_NONE       = "None";
  String STR_D_SORT_NONE       = "No sorting";
  String STR_N_SORT_ASCENDING  = "Ascending";
  String STR_D_SORT_ASCENDING  = "Items are sorted in ascending (lowest to highest) value";
  String STR_N_SORT_DESCENDING = "Descending";
  String STR_D_SORT_DESCENDING = "Items are sorted in descending order (highest to lowest) value";

  /**
   * {@link TsTestUtils}
   */
  String MSG_ENTER_TO_CONTINUE = "Press ENTER to continue";
  String MSG_ERROR_MSG_PREFIX  = "ERROR: ";

  /**
   * {@link TsVersion}
   */
  String ERR_MSG_INV_VERSION_STRING_FORMAT = Messages.getString( "ERR_MSG_INV_VERSION_STRING_FORMAT" ); //$NON-NLS-1$

}
