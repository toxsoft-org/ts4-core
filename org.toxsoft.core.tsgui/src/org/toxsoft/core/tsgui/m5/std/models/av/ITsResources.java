package org.toxsoft.core.tsgui.m5.std.models.av;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link DataTypeM5Model}
   */
  String STR_M5M_DATA_TYPE     = Messages.getString( "DataType.STR_M5M_DATA_TYPE" );     //$NON-NLS-1$
  String STR_M5M_DATA_TYPE_D   = Messages.getString( "DataType.STR_M5M_DATA_TYPE_D" );   //$NON-NLS-1$
  String STR_ATOMIC_TYPE       = Messages.getString( "DataType.STR_ATOMIC_TYPE" );       //$NON-NLS-1$
  String STR_ATOMIC_TYPE_D     = Messages.getString( "DataType.STR_ATOMIC_TYPE_D" );     //$NON-NLS-1$
  String STR_FIELD_DATA_TYPE   = Messages.getString( "DataType.STR_FIELD_DATA_TYPE" );   //$NON-NLS-1$
  String STR_FIELD_DATA_TYPE_D = Messages.getString( "DataType.STR_FIELD_DATA_TYPE_D" ); //$NON-NLS-1$
  String STR_CONSTRAINTS       = Messages.getString( "DataType.STR_CONSTRAINTS" );       //$NON-NLS-1$
  String STR_CONSTRAINTS_D     = Messages.getString( "DataType.STR_CONSTRAINTS_D" );     //$NON-NLS-1$

  /**
   * {@link IdValueConstraintM5LifecycleManager}
   */
  String FMT_ERR_CONSTRAINT_ID_EXISTS = Messages.getString( "IdValue.FMT_ERR_CONSTRAINT_ID_EXISTS" ); //$NON-NLS-1$

  /**
   * {@link IdValueM5Model}
   */
  String STR_M5M_IDVAL                  = Messages.getString( "IdValue.STR_M5M_IDVAL" );                  //$NON-NLS-1$
  String STR_M5M_IDVAL_D                = Messages.getString( "IdValue.STR_M5M_IDVAL_D" );                //$NON-NLS-1$
  String STR_IDVAL_ID                   = Messages.getString( "IdValue.STR_IDVAL_ID" );                   //$NON-NLS-1$
  String STR_IDVAL_ID_D                 = Messages.getString( "IdValue.STR_IDVAL_ID_D" );                 //$NON-NLS-1$
  String STR_IDVAL_CONSTR_NAME          = Messages.getString( "IdValue.STR_IDVAL_CONSTR_NAME" );          //$NON-NLS-1$
  String STR_IDVAL_CONSTR_NAME_D        = Messages.getString( "IdValue.STR_IDVAL_CONSTR_NAME_D" );        //$NON-NLS-1$
  String STR_IDVAL_CONSTR_DESCRIPTION   = Messages.getString( "IdValue.STR_IDVAL_CONSTR_DESCRIPTION" );   //$NON-NLS-1$
  String STR_IDVAL_CONSTR_DESCRIPTION_D = Messages.getString( "IdValue.STR_IDVAL_CONSTR_DESCRIPTION_D" ); //$NON-NLS-1$
  String STR_IDVAL_VALUE                = Messages.getString( "IdValue.STR_IDVAL_VALUE" );                //$NON-NLS-1$
  String STR_IDVAL_VALUE_D              = Messages.getString( "IdValue.STR_IDVAL_VALUE_D" );              //$NON-NLS-1$

  /**
   * {@link IdValueConstraintM5EntityPanel}
   */
  String STR_BTN_SELECT_CONSTRAINT         = Messages.getString( "IdValue.STR_BTN_SELECT_CONSTRAINT" );         //$NON-NLS-1$
  String STR_UNKNOWN_CONSTRAINT_INFO_TEXT  = Messages.getString( "IdValue.STR_UNKNOWN_CONSTRAINT_INFO_TEXT" );  //$NON-NLS-1$
  String STR_LABEL_ID                      = STR_IDVAL_ID;
  String STR_LABEL_ID_D                    = STR_IDVAL_ID_D;
  String STR_LABEL_VALUE                   = STR_IDVAL_VALUE;
  String STR_LABEL_VALUE_D                 = STR_IDVAL_VALUE_D;
  String STR_LABEL_KNOWN_CONSTRAINT_INFO   = Messages.getString( "IdValue.STR_LABEL_KNOWN_CONSTRAINT_INFO" );   //$NON-NLS-1$
  String STR_LABEL_KNOWN_CONSTRAINT_INFO_D = Messages.getString( "IdValue.STR_LABEL_KNOWN_CONSTRAINT_INFO_D" ); //$NON-NLS-1$

}
