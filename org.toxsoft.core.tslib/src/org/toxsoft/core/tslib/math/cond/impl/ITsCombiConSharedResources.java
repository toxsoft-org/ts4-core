package org.toxsoft.core.tslib.math.cond.impl;

import org.toxsoft.core.tslib.math.cond.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsCombiConSharedResources {

  /**
   * {@link ITsCombiCondInfoBuilder}
   */
  String FMT_ERR_NO_TPOIC_MANAGER = Messages.getString( "FMT_ERR_NO_TPOIC_MANAGER" ); //$NON-NLS-1$

  /**
   * {@link TsCombiCondInfoBuilder}
   */
  String MSG_ERR_NO_CONDS_SPECIFIED_YET = Messages.getString( "MSG_ERR_NO_CONDS_SPECIFIED_YET" ); //$NON-NLS-1$
  String FMT_ERR_UNSPECIFIED_CONDS_QTTY = Messages.getString( "FMT_ERR_UNSPECIFIED_CONDS_QTTY" ); //$NON-NLS-1$
  String FMT_ERR_UNUSED_CONDS_QTTY      = Messages.getString( "FMT_ERR_UNUSED_CONDS_QTTY" );      //$NON-NLS-1$

  /**
   * {@link TsSingleCondType}
   */
  String FMT_ERR_INV_COND_TYPE_ID = Messages.getString( "FMT_ERR_INV_COND_TYPE_ID" ); //$NON-NLS-1$

  /**
   * {@link TsConditionsTopicManager}
   */
  String FMT_ERR_UNKNOWN_COND_TYPE_ID = Messages.getString( "FMT_ERR_UNKNOWN_COND_TYPE_ID" ); //$NON-NLS-1$

}
