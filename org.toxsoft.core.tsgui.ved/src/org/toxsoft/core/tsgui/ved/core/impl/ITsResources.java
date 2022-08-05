package org.toxsoft.core.tsgui.ved.core.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link VedAbstractComponentProvider}
   */
  String FMT_ERR_INV_PROP_TYPE = Messages.getString( "FMT_ERR_INV_PROP_TYPE" ); //$NON-NLS-1$

  /**
   * {@link VedDataModel}
   */
  String FMT_ERR_DUP_COMP_ID = Messages.getString( "FMT_ERR_DUP_COMP_ID" ); //$NON-NLS-1$

  /**
   * Log messages does not need to be localized.
   */
  String FMT_LOG_WARN_NO_COMP_PROVIDER = "No provider found for component (libId=%s, kindId=%s)"; //$NON-NLS-1$

}
