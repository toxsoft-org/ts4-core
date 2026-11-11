package org.toxsoft.core.tslib.bricks.coopcomp;

import org.toxsoft.core.tslib.bricks.wub.Messages;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  String STR_CCS_CREATED       = Messages.getString( "STR_CCS_CREATED" );       //$NON-NLS-1$
  String STR_CCS_CREATED_D     = Messages.getString( "STR_CCS_CREATED_D" );     //$NON-NLS-1$
  String STR_CCS_INITIALIZED   = Messages.getString( "STR_CCS_INITIALIZED" );   //$NON-NLS-1$
  String STR_CCS_INITIALIZED_D = Messages.getString( "STR_CCS_INITIALIZED_D" ); //$NON-NLS-1$
  String STR_CCS_WORKING       = Messages.getString( "STR_CCS_WORKING" );       //$NON-NLS-1$
  String STR_CCS_WORKING_D     = Messages.getString( "STR_CCS_WORKING_D" );     //$NON-NLS-1$
  String STR_CCS_STOPPING      = Messages.getString( "STR_CCS_STOPPING" );      //$NON-NLS-1$
  String STR_CCS_STOPPING_D    = Messages.getString( "STR_CCS_STOPPING_D" );    //$NON-NLS-1$
  String STR_CCS_DESTROYED     = Messages.getString( "STR_CCS_DESTROYED" );     //$NON-NLS-1$
  String STR_CCS_DESTROYED_D   = Messages.getString( "STR_CCS_DESTROYED_D" );   //$NON-NLS-1$

  /**
   * {@link TsCooperativeContainer}
   */
  String FMT_ERR_ALL_UNITS_INIT_FAILED   = Messages.getString( "FMT_ERR_ALL_UNITS_INIT_FAILED" );   //$NON-NLS-1$
  String FMT_WARN_SOME_UNITS_INIT_FAILED = Messages.getString( "FMT_WARN_SOME_UNITS_INIT_FAILED" ); //$NON-NLS-1$
  String FMT_WARN_UNIT_NOT_ADDED         = Messages.getString( "FMT_WARN_UNIT_NOT_ADDED" );         //$NON-NLS-1$

}
