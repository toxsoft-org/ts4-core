package org.toxsoft.core.tsgui.chart.api;

/**
 * Localizable resources.
 *
 * @author vs
 */
interface ITsResources {

  /**
   * {@link EGraphicRenderingKind}
   */
  String STR_GRK_LINE     = Messages.getString( "STR_GRK_LINE" );     //$NON-NLS-1$
  String STR_GRK_LINE_D   = Messages.getString( "STR_GRK_LINE_D" );   //$NON-NLS-1$
  String STR_GRK_LADDER   = Messages.getString( "STR_GRK_LADDER" );   //$NON-NLS-1$
  String STR_GRK_LADDER_D = Messages.getString( "STR_GRK_LADDER_D" ); //$NON-NLS-1$

  /**
   * {@link EDisplayFormat}
   */
  String STR_EDF_AS_INTEGER    = Messages.getString( "STR_EDF_AS_INTEGER" );    //$NON-NLS-1$
  String STR_EDF_AS_INTEGER_D  = Messages.getString( "STR_EDF_AS_INTEGER_D" );  //$NON-NLS-1$
  String STR_EDF_ONE_DIGIT     = Messages.getString( "STR_EDF_ONE_DIGIT" );     //$NON-NLS-1$
  String STR_EDF_ONE_DIGIT_D   = Messages.getString( "STR_EDF_ONE_DIGIT_D" );   //$NON-NLS-1$
  String STR_EDF_TWO_DIGIT     = Messages.getString( "STR_EDF_TWO_DIGIT" );     //$NON-NLS-1$
  String STR_EDF_TWO_DIGIT_D   = Messages.getString( "STR_EDF_TWO_DIGIT_D" );   //$NON-NLS-1$
  String STR_EDF_THREE_DIGIT   = Messages.getString( "STR_EDF_THREE_DIGIT" );   //$NON-NLS-1$
  String STR_EDF_THREE_DIGIT_D = Messages.getString( "STR_EDF_THREE_DIGIT_D" ); //$NON-NLS-1$
  String STR_EDF_AS_FLOAT      = Messages.getString( "STR_EDF_AS_FLOAT" );      //$NON-NLS-1$
  String STR_EDF_AS_FLOAT_D    = Messages.getString( "STR_EDF_AS_FLOAT_D" );    //$NON-NLS-1$

  /**
   * {@link ETimeUnit}
   */
  String STR_ETU_DAY      = Messages.getString( "STR_ETU_DAY" );      //$NON-NLS-1$
  String STR_ETU_DAY_D    = Messages.getString( "STR_ETU_DAY_D" );    //$NON-NLS-1$
  String STR_ETU_HOUR01   = Messages.getString( "STR_ETU_HOUR01" );   //$NON-NLS-1$
  String STR_ETU_HOUR01_D = Messages.getString( "STR_ETU_HOUR01_D" ); //$NON-NLS-1$
  String STR_ETU_HOUR02   = Messages.getString( "STR_ETU_HOUR02" );   //$NON-NLS-1$
  String STR_ETU_HOUR02_D = Messages.getString( "STR_ETU_HOUR02_D" ); //$NON-NLS-1$
  String STR_ETU_HOUR04   = Messages.getString( "STR_ETU_HOUR04" );   //$NON-NLS-1$
  String STR_ETU_HOUR04_D = Messages.getString( "STR_ETU_HOUR04_D" ); //$NON-NLS-1$
  String STR_ETU_HOUR08   = Messages.getString( "STR_ETU_HOUR08" );   //$NON-NLS-1$
  String STR_ETU_HOUR08_D = Messages.getString( "STR_ETU_HOUR08_D" ); //$NON-NLS-1$
  String STR_ETU_HOUR12   = Messages.getString( "STR_ETU_HOUR12" );   //$NON-NLS-1$
  String STR_ETU_HOUR12_D = Messages.getString( "STR_ETU_HOUR12_D" ); //$NON-NLS-1$
  String STR_ETU_MIN01    = Messages.getString( "STR_ETU_MIN01" );    //$NON-NLS-1$
  String STR_ETU_MIN01_D  = Messages.getString( "STR_ETU_MIN01_D" );  //$NON-NLS-1$
  String STR_ETU_MIN05    = Messages.getString( "STR_ETU_MIN05" );    //$NON-NLS-1$
  String STR_ETU_MIN05_D  = Messages.getString( "STR_ETU_MIN05_D" );  //$NON-NLS-1$
  String STR_ETU_MIN10    = Messages.getString( "STR_ETU_MIN10" );    //$NON-NLS-1$
  String STR_ETU_MIN10_D  = Messages.getString( "STR_ETU_MIN10_D" );  //$NON-NLS-1$
  String STR_ETU_MIN15    = Messages.getString( "STR_ETU_MIN15" );    //$NON-NLS-1$
  String STR_ETU_MIN15_D  = Messages.getString( "STR_ETU_MIN15_D" );  //$NON-NLS-1$
  String STR_ETU_MIN20    = Messages.getString( "STR_ETU_MIN20" );    //$NON-NLS-1$
  String STR_ETU_MIN20_D  = Messages.getString( "STR_ETU_MIN20_D" );  //$NON-NLS-1$
  String STR_ETU_MIN30    = Messages.getString( "STR_ETU_MIN30" );    //$NON-NLS-1$
  String STR_ETU_MIN30_D  = Messages.getString( "STR_ETU_MIN30_D" );  //$NON-NLS-1$
  String STR_ETU_SEC01    = Messages.getString( "STR_ETU_SEC01" );    //$NON-NLS-1$
  String STR_ETU_SEC01_D  = Messages.getString( "STR_ETU_SEC01_D" );  //$NON-NLS-1$
  String STR_ETU_SEC02    = Messages.getString( "STR_ETU_SEC02" );    //$NON-NLS-1$
  String STR_ETU_SEC02_D  = Messages.getString( "STR_ETU_SEC02_D" );  //$NON-NLS-1$
  String STR_ETU_SEC03    = Messages.getString( "STR_ETU_SEC03" );    //$NON-NLS-1$
  String STR_ETU_SEC03_D  = Messages.getString( "STR_ETU_SEC03_D" );  //$NON-NLS-1$
  String STR_ETU_SEC05    = Messages.getString( "STR_ETU_SEC05" );    //$NON-NLS-1$
  String STR_ETU_SEC05_D  = Messages.getString( "STR_ETU_SEC05_D" );  //$NON-NLS-1$
  String STR_ETU_SEC10    = Messages.getString( "STR_ETU_SEC10" );    //$NON-NLS-1$
  String STR_ETU_SEC10_D  = Messages.getString( "STR_ETU_SEC10_D" );  //$NON-NLS-1$
  String STR_ETU_SEC15    = Messages.getString( "STR_ETU_SEC15" );    //$NON-NLS-1$
  String STR_ETU_SEC15_D  = Messages.getString( "STR_ETU_SEC15_D" );  //$NON-NLS-1$
  String STR_ETU_SEC20    = Messages.getString( "STR_ETU_SEC20" );    //$NON-NLS-1$
  String STR_ETU_SEC20_D  = Messages.getString( "STR_ETU_SEC20_D" );  //$NON-NLS-1$
  String STR_ETU_SEC30    = Messages.getString( "STR_ETU_SEC30" );    //$NON-NLS-1$
  String STR_ETU_SEC30_D  = Messages.getString( "STR_ETU_SEC30_D" );  //$NON-NLS-1$
  String STR_ETU_WEEK     = Messages.getString( "STR_ETU_WEEK" );     //$NON-NLS-1$
  String STR_ETU_WEEK_D   = Messages.getString( "STR_ETU_WEEK_D" );   //$NON-NLS-1$
  String STR_ETU_YEAR     = Messages.getString( "STR_ETU_YEAR" );     //$NON-NLS-1$
  String STR_ETU_YEAR_D   = Messages.getString( "STR_ETU_YEAR_D" );   //$NON-NLS-1$

}
