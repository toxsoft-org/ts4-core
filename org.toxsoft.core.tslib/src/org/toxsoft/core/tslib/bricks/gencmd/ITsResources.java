package org.toxsoft.core.tslib.bricks.gencmd;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link GenericCommandDef}
   */
  String FMT_ERR_UNKNOWN_ARG = Messages.getString( "FMR_ERR_UNKNOWN_ARG" ); //$NON-NLS-1$

  /**
   * {@link ExecutableCommandSetProvider}
   */
  String FMT_ERR_UNKNOWN_CMD_ID = Messages.getString( "FMT_ERR_UNKNOWN_CMD_ID" ); //$NON-NLS-1$

  /**
   * {@link IGenericCommandConstants}
   */
  String STR_COMMAND_RESULT            = Messages.getString( "STR_COMMAND_RESULT" );            //$NON-NLS-1$
  String STR_COMMAND_RESULT_D          = Messages.getString( "STR_COMMAND_RESULT_D" );          //$NON-NLS-1$
  String STR_IS_UNKNOWN_ARGS_ALLOWED   = Messages.getString( "STR_IS_UNKNOWN_ARGS_ALLOWED" );   //$NON-NLS-1$
  String STR_IS_UNKNOWN_ARGS_ALLOWED_D = Messages.getString( "STR_IS_UNKNOWN_ARGS_ALLOWED_D" ); //$NON-NLS-1$

}
