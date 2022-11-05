package org.toxsoft.core.tsgui.graphics.lines;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link ETsBorderKind}
   */
  String STR_N_BK_NONE   = Messages.getString( "STR_N_BK_NONE" );   //$NON-NLS-1$
  String STR_D_BK_NONE   = Messages.getString( "STR_D_BK_NONE" );   //$NON-NLS-1$
  String STR_N_BK_SINGLE = Messages.getString( "STR_N_BK_SINGLE" ); //$NON-NLS-1$
  String STR_D_BK_SINGLE = Messages.getString( "STR_D_BK_SINGLE" ); //$NON-NLS-1$
  String STR_N_BK_DOUBLE = Messages.getString( "STR_N_BK_DOUBLE" ); //$NON-NLS-1$
  String STR_D_BK_DOUBLE = Messages.getString( "STR_D_BK_DOUBLE" ); //$NON-NLS-1$

  /**
   * {@link ETsLineCapStyle}
   */
  String STR_D_LCS_FLAT   = Messages.getString( "STR_D_LCS_FLAT" );   //$NON-NLS-1$
  String STR_N_LCS_FLAT   = Messages.getString( "STR_N_LCS_FLAT" );   //$NON-NLS-1$
  String STR_D_LCS_ROUND  = Messages.getString( "STR_D_LCS_ROUND" );  //$NON-NLS-1$
  String STR_N_LCS_ROUND  = Messages.getString( "STR_N_LCS_ROUND" );  //$NON-NLS-1$
  String STR_D_LCS_SQUARE = Messages.getString( "STR_D_LCS_SQUARE" ); //$NON-NLS-1$
  String STR_N_LCS_SQUARE = Messages.getString( "STR_N_LCS_SQUARE" ); //$NON-NLS-1$

  /**
   * {@link ETsLineJoinStyle}
   */
  String STR_D_LJS_ROUND = Messages.getString( "STR_D_LJS_ROUND" ); //$NON-NLS-1$
  String STR_N_LJS_ROUND = Messages.getString( "STR_N_LJS_ROUND" ); //$NON-NLS-1$
  String STR_D_LJS_BEVEL = Messages.getString( "STR_D_LJS_BEVEL" ); //$NON-NLS-1$
  String STR_N_LJS_BEVEL = Messages.getString( "STR_N_LJS_BEVEL" ); //$NON-NLS-1$
  String STR_D_LJS_MITER = Messages.getString( "STR_D_LJS_MITER" ); //$NON-NLS-1$
  String STR_N_LJS_MITER = Messages.getString( "STR_N_LJS_MITER" ); //$NON-NLS-1$

  /**
   * {@link ETsLineType}
   */
  String STR_N_LT_SOLID      = Messages.getString( "STR_N_LT_SOLID" );      //$NON-NLS-1$
  String STR_D_LT_SOLID      = Messages.getString( "STR_D_LT_SOLID" );      //$NON-NLS-1$
  String STR_N_LT_DASH       = Messages.getString( "STR_N_LT_DASH" );       //$NON-NLS-1$
  String STR_D_LT_DASH       = Messages.getString( "STR_D_LT_DASH" );       //$NON-NLS-1$
  String STR_N_LT_DOT        = Messages.getString( "STR_N_LT_DOT" );        //$NON-NLS-1$
  String STR_D_LT_DOT        = Messages.getString( "STR_D_LT_DOT" );        //$NON-NLS-1$
  String STR_N_LT_DASHDOT    = Messages.getString( "STR_N_LT_DASHDOT" );    //$NON-NLS-1$
  String STR_D_LT_DASHDOT    = Messages.getString( "STR_D_LT_DASHDOT" );    //$NON-NLS-1$
  String STR_N_LT_DASHDOTDOT = Messages.getString( "STR_N_LT_DASHDOTDOT" ); //$NON-NLS-1$
  String STR_D_LT_DASHDOTDOT = Messages.getString( "STR_D_LT_DASHDOTDOT" ); //$NON-NLS-1$
  String STR_N_LT_CUSTOM     = Messages.getString( "STR_N_LT_CUSTOM" );     //$NON-NLS-1$
  String STR_D_LT_CUSTOM     = Messages.getString( "STR_D_LT_CUSTOM" );     //$NON-NLS-1$

  /**
   * {@link ETsOutlineKind}
   */
  String STR_N_OK_NONE   = Messages.getString( "STR_N_OK_NONE" );   //$NON-NLS-1$
  String STR_D_OK_NONE   = Messages.getString( "STR_D_OK_NONE" );   //$NON-NLS-1$
  String STR_N_OK_SIMPLE = Messages.getString( "STR_N_OK_SIMPLE" ); //$NON-NLS-1$
  String STR_D_OK_SIMPLE = Messages.getString( "STR_D_OK_SIMPLE" ); //$NON-NLS-1$

  /**
   * {@link TsBorderInfo}
   */
  String STR_N_BI_KIND         = Messages.getString( "STR_N_BI_KIND" );         //$NON-NLS-1$
  String STR_D_BI_KIND         = Messages.getString( "STR_D_BI_KIND" );         //$NON-NLS-1$
  String STR_N_BI_LEFT_RGBA    = Messages.getString( "STR_N_BI_LEFT_RGBA" );    //$NON-NLS-1$
  String STR_D_BI_LEFT_RGBA    = Messages.getString( "STR_D_BI_LEFT_RGBA" );    //$NON-NLS-1$
  String STR_N_BI_RIGHT_RGBA   = Messages.getString( "STR_N_BI_RIGHT_RGBA" );   //$NON-NLS-1$
  String STR_D_BI_RIGHT_RGBA   = Messages.getString( "STR_D_BI_RIGHT_RGBA" );   //$NON-NLS-1$
  String STR_N_BI_LINE_INFO    = Messages.getString( "STR_N_BI_LINE_INFO" );    //$NON-NLS-1$
  String STR_D_BI_LINE_INFO    = Messages.getString( "STR_D_BI_LINE_INFO" );    //$NON-NLS-1$
  String STR_N_BI_PAINT_LEFT   = Messages.getString( "STR_N_BI_PAINT_LEFT" );   //$NON-NLS-1$
  String STR_D_BI_PAINT_LEFT   = Messages.getString( "STR_D_BI_PAINT_LEFT" );   //$NON-NLS-1$
  String STR_N_BI_PAINT_TOP    = Messages.getString( "STR_N_BI_PAINT_TOP" );    //$NON-NLS-1$
  String STR_D_BI_PAINT_TOP    = Messages.getString( "STR_D_BI_PAINT_TOP" );    //$NON-NLS-1$
  String STR_N_BI_PAINT_RIGHT  = Messages.getString( "STR_N_BI_PAINT_RIGHT" );  //$NON-NLS-1$
  String STR_D_BI_PAINT_RIGHT  = Messages.getString( "STR_D_BI_PAINT_RIGHT" );  //$NON-NLS-1$
  String STR_N_BI_PAINT_BOTTOM = Messages.getString( "STR_N_BI_PAINT_BOTTOM" ); //$NON-NLS-1$
  String STR_D_BI_PAINT_BOTTOM = Messages.getString( "STR_D_BI_PAINT_BOTTOM" ); //$NON-NLS-1$

}
