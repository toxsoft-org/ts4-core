package org.toxsoft.core.pas.tj.impl;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
interface ITsResources {

  /**
   * {@link TsJsonValueStorage}
   */
  String FMR_ERR_UNKNOWN_TOCKEN = Messages.getString( "ITsResources.FMR_ERR_UNKNOWN_TOCKEN" );                 //$NON-NLS-1$
  String MSG_ERR_INT_OVER_LONG  = Messages.getString( "ITsResources.MSG_ERR_INT_OVER_LONG" ) + Long.MAX_VALUE; //$NON-NLS-1$
  String MSG_ERR_INV_NUM_FORMAT = Messages.getString( "ITsResources.MSG_ERR_INV_NUM_FORMAT" );                 //$NON-NLS-1$

}
