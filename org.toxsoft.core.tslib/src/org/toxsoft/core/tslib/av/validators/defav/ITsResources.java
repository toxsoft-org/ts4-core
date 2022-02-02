package org.toxsoft.core.tslib.av.validators.defav;

import static org.toxsoft.core.tslib.av.validators.defav.Messages.*;

/**
 * Локадлизуемые ресрсы валидаторов.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link DefaultAvValidator}
   */
  String MSG_ERR_NULL_REF_NOT_ALLOWED = getString( "MSG_ERR_NULL_REF_NOT_ALLOWED" ); //$NON-NLS-1$
  String MSG_ERR_NULL_NOT_ALLOWED     = getString( "MSG_ERR_NULL_NOT_ALLOWED" );     //$NON-NLS-1$
  String FMT_ERR_INCOMPATIBLE_TYPES   = getString( "FMT_ERR_INCOMPATIBLE_TYPES" );   //$NON-NLS-1$

  /**
   * {@link FloatingValidator}
   */
  String FMT_ERR_FLOATING_GE_MAX          = getString( "FMT_ERR_FLOATING_GE_MAX" );          //$NON-NLS-1$
  String FMT_ERR_FLOATING_GT_MAX          = getString( "FMT_ERR_FLOATING_GT_MAX" );          //$NON-NLS-1$
  String FMT_ERR_FLOATING_LE_MIN          = getString( "FMT_ERR_FLOATING_LE_MIN" );          //$NON-NLS-1$
  String FMT_ERR_FLOATING_LT_MIN          = getString( "FMT_ERR_FLOATING_LT_MIN" );          //$NON-NLS-1$
  String MSG_ERR_FLOATING_NAN_NOT_ALLOWED = getString( "MSG_ERR_FLOATING_NAN_NOT_ALLOWED" ); //$NON-NLS-1$
  String MSG_ERR_FLOATING_INF_NOT_ALLOWED = getString( "MSG_ERR_FLOATING_INF_NOT_ALLOWED" ); //$NON-NLS-1$

  /**
   * {@link IntegerValidator}
   */
  String FMT_ERR_INTEGER_GE_MAX = getString( "FMT_ERR_INTEGER_GE_MAX" ); //$NON-NLS-1$
  String FMT_ERR_INTEGER_GT_MAX = getString( "FMT_ERR_INTEGER_GT_MAX" ); //$NON-NLS-1$
  String FMT_ERR_INTEGER_LE_MIN = getString( "FMT_ERR_INTEGER_LE_MIN" ); //$NON-NLS-1$
  String FMT_ERR_INTEGER_LT_MIN = getString( "FMT_ERR_INTEGER_LT_MIN" ); //$NON-NLS-1$

  /**
   * {@link StringValidator}
   */
  String FMT_ERR_NO_STRING_MASK_MATCH = getString( "FMT_ERR_NO_STRING_MASK_MATCH" ); //$NON-NLS-1$
  String FMT_ERR_TOO_LONG_STRING      = getString( "FMT_ERR_TOO_LONG_STRING" );      //$NON-NLS-1$

  /**
   * {@link TimestampValidator}
   */
  String FMT_ERR_TIMESTAMP_GE_MAX = getString( "FMT_ERR_TIMESTAMP_GE_MAX" ); //$NON-NLS-1$
  String FMT_ERR_TIMESTAMP_GT_MAX = getString( "FMT_ERR_TIMESTAMP_GT_MAX" ); //$NON-NLS-1$
  String FMT_ERR_TIMESTAMP_LE_MIN = getString( "FMT_ERR_TIMESTAMP_LE_MIN" ); //$NON-NLS-1$
  String FMT_ERR_TIMESTAMP_LT_MIN = getString( "FMT_ERR_TIMESTAMP_LT_MIN" ); //$NON-NLS-1$

}
