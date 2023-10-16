package org.toxsoft.core.tsgui.graphics;

import org.toxsoft.core.tsgui.bricks.tin.tti.Messages;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link EBorderType}
   */
  String STR_N_BT_NONE        = "Без рамки";
  String STR_D_BT_NONE        = "Без рамки";
  String STR_N_BT_LINE        = "Линия";
  String STR_D_BT_LINE        = "Линия";
  String STR_N_BT_ECTHED      = "Вдавленная линия";
  String STR_D_BT_ECTHED      = "Вдавленная линия";
  String STR_N_BT_CONVEX      = "Выпуклая линия";
  String STR_D_BT_CONVEX      = "Выпуклая линия";
  String STR_N_BT_BEVEL_INNER = "Вдавленная область";
  String STR_D_BT_BEVEL_INNER = "Вдавленная область";
  String STR_N_BT_BEVEL_OUTER = "Выпуклая область";
  String STR_D_BT_BEVEL_OUTER = "Выпуклая область";

  /**
   * {@link EHorAlignment}
   */
  String STR_N_HA_LEFT   = "Влево";
  String STR_D_HA_LEFT   = "Влево";
  String STR_N_HA_CENTER = "По центру";
  String STR_D_HA_CENTER = "По центру";
  String STR_N_HA_RIGHT  = "Вправо";
  String STR_D_HA_RIGHT  = "Вправо";
  String STR_N_HA_FILL   = "В ширину";
  String STR_D_HA_FILL   = "В ширину";

  /**
   * {@link ETsOrientation}
   */
  String STR_N_TSO_HORIZONTAL = "Горизонтально";
  String STR_D_TSO_HORIZONTAL = "Горизонтально";
  String STR_N_TSO_VERTICAL   = "Вертикально";
  String STR_D_TSO_VERTICAL   = "Вертикально";

  /**
   * {@link EVerAlignment}
   */
  String STR_VA_TOP      = "Вверх";
  String STR_VA_TOP_D    = "Вверх";
  String STR_VA_CENTER   = "По центру";
  String STR_VA_CENTER_D = "По центру";
  String STR_VA_BOTTOM   = "Вниз";
  String STR_VA_BOTTOM_D = "Вниз";
  String STR_VA_FILL     = "В высоту";
  String STR_VA_FILL_D   = "В высоту";

  /**
   * {@link ETsFulcrum}
   */
  String STR_ETF_BOTTOM_CENTER   = Messages.getString( "ETsFulcrum.STR_ETF_BOTTOM_CENTER" );   //$NON-NLS-1$
  String STR_ETF_BOTTOM_CENTER_D = Messages.getString( "ETsFulcrum.STR_ETF_BOTTOM_CENTER_D" ); //$NON-NLS-1$
  String STR_ETF_CENTER          = Messages.getString( "ETsFulcrum.STR_ETF_CENTER" );          //$NON-NLS-1$
  String STR_ETF_CENTER_D        = Messages.getString( "ETsFulcrum.STR_ETF_CENTER_D" );        //$NON-NLS-1$
  String STR_ETF_LEFT_BOTTOM     = Messages.getString( "ETsFulcrum.STR_ETF_LEFT_BOTTOM" );     //$NON-NLS-1$
  String STR_ETF_LEFT_BOTTOM_D   = Messages.getString( "ETsFulcrum.STR_ETF_LEFT_BOTTOM_D" );   //$NON-NLS-1$
  String STR_ETF_LEFT_CENTER     = Messages.getString( "ETsFulcrum.STR_ETF_LEFT_CENTER" );     //$NON-NLS-1$
  String STR_ETF_LEFT_CENTER_D   = Messages.getString( "ETsFulcrum.STR_ETF_LEFT_CENTER_D" );   //$NON-NLS-1$
  String STR_ETF_LEFT_TOP        = Messages.getString( "ETsFulcrum.STR_ETF_LEFT_TOP" );        //$NON-NLS-1$
  String STR_ETF_LEFT_TOP_D      = Messages.getString( "ETsFulcrum.STR_ETF_LEFT_TOP_D" );      //$NON-NLS-1$
  String STR_ETF_RIGHT_BOTTOM    = Messages.getString( "ETsFulcrum.STR_ETF_RIGHT_BOTTOM" );    //$NON-NLS-1$
  String STR_ETF_RIGHT_BOTTOM_D  = Messages.getString( "ETsFulcrum.STR_ETF_RIGHT_BOTTOM_D" );  //$NON-NLS-1$
  String STR_ETF_RIGHT_CENTER    = Messages.getString( "ETsFulcrum.STR_ETF_RIGHT_CENTER" );    //$NON-NLS-1$
  String STR_ETF_RIGHT_CENTER_D  = Messages.getString( "ETsFulcrum.STR_ETF_RIGHT_CENTER_D" );  //$NON-NLS-1$
  String STR_ETF_RIGHT_TOP       = Messages.getString( "ETsFulcrum.STR_ETF_RIGHT_TOP" );       //$NON-NLS-1$
  String STR_ETF_RIGHT_TOP_D     = Messages.getString( "ETsFulcrum.STR_ETF_RIGHT_TOP_D" );     //$NON-NLS-1$
  String STR_ETF_TOP_CENTER      = Messages.getString( "ETsFulcrum.STR_ETF_TOP_CENTER" );      //$NON-NLS-1$
  String STR_ETF_TOP_CENTER_D    = Messages.getString( "ETsFulcrum.STR_ETF_TOP_CENTER_D" );    //$NON-NLS-1$

  /**
   * {@link ITsGraphicsConstants}
   */
  String STR_COLOR_COMPONENT   = Messages.getString( "STR_COLOR_COMPONENT" );   //$NON-NLS-1$ aAAA
  String STR_COLOR_COMPONENT_D = Messages.getString( "STR_COLOR_COMPONENT_D" ); //$NON-NLS-1$ aAAA
  String STR_COLOR_RGB         = Messages.getString( "STR_COLOR_RGB" );         //$NON-NLS-1$ aAAA
  String STR_COLOR_RGB_D       = Messages.getString( "STR_COLOR_RGB_D" );       //$NON-NLS-1$ aAAA
  String STR_COLOR_RGBA        = Messages.getString( "STR_COLOR_RGBA" );        //$NON-NLS-1$ aAAA
  String STR_COLOR_RGBA_D      = Messages.getString( "STR_COLOR_RGBA_D" );      //$NON-NLS-1$ aAAA
  String STR_DT_D2POINT        = Messages.getString( "STR_DT_D2POINT" );        //$NON-NLS-1$ aAAA
  String STR_DT_D2POINT_D      = Messages.getString( "STR_DT_D2POINT_D" );      //$NON-NLS-1$ aAAA
  String STR_DT_D2CONVERSION   = Messages.getString( "STR_DT_D2CONVERSION" );   //$NON-NLS-1$ aAAA
  String STR_DT_D2CONVERSION_D = Messages.getString( "STR_DT_D2CONVERSION_D" ); //$NON-NLS-1$ aAAA
  String STR_TS_FILL_INFO      = Messages.getString( "STR_TS_FILL_INFO" );      //$NON-NLS-1$ aAAA
  String STR_TS_FILL_INFO_D    = Messages.getString( "STR_TS_FILL_INFO_D" );    //$NON-NLS-1$ aAAA
  String STR_TS_BORDER_INFO    = Messages.getString( "STR_TS_BORDER_INFO" );    //$NON-NLS-1$ aAAA
  String STR_TS_BORDER_INFO_D  = Messages.getString( "STR_TS_BORDER_INFO_D" );  //$NON-NLS-1$ aAAA

}
