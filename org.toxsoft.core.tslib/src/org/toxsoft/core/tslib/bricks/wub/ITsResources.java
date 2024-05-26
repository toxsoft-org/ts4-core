package org.toxsoft.core.tslib.bricks.wub;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link EWubUnitState}
   */
  String STR_EWUS_CREATED        = Messages.getString( "STR_EWUS_CREATED" );        //$NON-NLS-1$
  String STR_EWUS_CREATED_D      = Messages.getString( "STR_EWUS_CREATED_D" );      //$NON-NLS-1$
  String STR_EWUS_INITED         = Messages.getString( "STR_EWUS_INITED" );         //$NON-NLS-1$
  String STR_EWUS_INITED_D       = Messages.getString( "STR_EWUS_INITED_D" );       //$NON-NLS-1$
  String STR_EWUS_STARTED        = Messages.getString( "STR_EWUS_STARTED" );        //$NON-NLS-1$
  String STR_EWUS_STARTED_D      = Messages.getString( "STR_EWUS_STARTED_D" );      //$NON-NLS-1$
  String STR_EWUS_STOP_QUERIED   = Messages.getString( "STR_EWUS_STOP_QUERIED" );   //$NON-NLS-1$
  String STR_EWUS_STOP_QUERIED_D = Messages.getString( "STR_EWUS_STOP_QUERIED_D" ); //$NON-NLS-1$
  String STR_EWUS_STOPPED        = Messages.getString( "STR_EWUS_STOPPED" );        //$NON-NLS-1$
  String STR_EWUS_STOPPED_D      = Messages.getString( "STR_EWUS_STOPPED_D" );      //$NON-NLS-1$

  /**
   * {@link WubBox}
   */
  String FMT_ERR_ALL_UNITS_INIT_FAILED   = Messages.getString( "FMT_ERR_ALL_UNITS_INIT_FAILED" );   //$NON-NLS-1$
  String FMT_WARN_SOME_UNITS_INIT_FAILED = Messages.getString( "FMT_WARN_SOME_UNITS_INIT_FAILED" ); //$NON-NLS-1$
  String FMT_WARN_UNIT_NOT_ADDED         = Messages.getString( "FMT_WARN_UNIT_NOT_ADDED" );         //$NON-NLS-1$

}
