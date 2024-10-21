package org.toxsoft.core.tslib.av.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String MSG_ERR_INT_VALUE_OVER_MAX_LONG      = Messages.getString( "MSG_ERR_INT_VALUE_OVER_MAX_LONG" );      //$NON-NLS-1$
  String MSG_ERR_INV_NUMERICAL_AV_TEXT_FORMAT = Messages.getString( "MSG_ERR_INV_NUMERICAL_AV_TEXT_FORMAT" ); //$NON-NLS-1$
  String MSG_ERR_INV_HEX_NUL_AV_TEXT_FORMAT   = Messages.getString( "MSG_ERR_INV_HEX_NUL_AV_TEXT_FORMAT" );   //$NON-NLS-1$
  String FMT_ERR_INV_AV_TEXT                  = Messages.getString( "FMT_ERR_INV_AV_TEXT" );                  //$NON-NLS-1$
  String FMT_ERR_ENTITY_AV_NOT_STRING_TYPE    = Messages.getString( "FMT_ERR_ENTITY_AV_NOT_STRING_TYPE" );    //$NON-NLS-1$
  String FMT_ERR_INV_DATA_DEF_HELPER_CLASS    = Messages.getString( "FMT_ERR_INV_DATA_DEF_HELPER_CLASS" );    //$NON-NLS-1$
  String FMT_ERR_INV_DATADEF_CONSTRUCTOR      = Messages.getString( "FMT_ERR_INV_DATADEF_CONSTRUCTOR" );      //$NON-NLS-1$
  String FMT_ERR_INV_DATATYPE_CONSTRUCTOR     = Messages.getString( "FMT_ERR_INV_DATATYPE_CONSTRUCTOR" );     //$NON-NLS-1$
  String FMT_ERR_NO_SUITABLE_CONSTRUCTORS     = Messages.getString( "FMT_ERR_NO_SUITABLE_CONSTRUCTORS" );     //$NON-NLS-1$
  String FMT_ERR_INV_EMPTY_CONSTRUCTOR        = Messages.getString( "FMT_ERR_INV_EMPTY_CONSTRUCTOR" );        //$NON-NLS-1$
  String FMT_ERR_NO_MANDATORY_OP              = Messages.getString( "FMT_ERR_NO_MANDATORY_OP" );              //$NON-NLS-1$

  // FIXME String MSG_ERR_INT_VALUE_OVER_MAX_LONG = "Integer value is greater than max allowed value " + Long.MAX_VALUE;
  // FIXME String FMT_ERR_ENTITY_AV_NOT_STRING_TYPE = "Type must be " + AvUtils.DDID_STRING + " rather than %s";

}
