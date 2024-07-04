package org.toxsoft.core.tsgui.graphics.image;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link AbstractTsImageSourceKind}
   */
  String FMT_ERR_INV_KIND           = Messages.getString( "FMT_ERR_INV_KIND" );           //$NON-NLS-1$
  String STR_DLG_C_IMAGE_DESCR_EDIT = Messages.getString( "STR_DLG_C_IMAGE_DESCR_EDIT" ); //$NON-NLS-1$
  String FMT_DLG_T_IMAGE_DESCR_EDIT = Messages.getString( "FMT_DLG_T_IMAGE_DESCR_EDIT" ); //$NON-NLS-1$

  /**
   * {@link EThumbSize}
   */
  String FMT_THUMB_SIZE   = Messages.getString( "FMT_THUMB_SIZE" );   //$NON-NLS-1$
  String FMT_THUMB_SIZE_D = Messages.getString( "FMT_THUMB_SIZE_D" ); //$NON-NLS-1$

  /**
   * {@link TsImageSourceKindNone}
   */
  String STR_SRCKIND_NONE   = Messages.getString( "STR_SRCKIND_NONE" );   //$NON-NLS-1$
  String STR_SRCKIND_NONE_D = Messages.getString( "STR_SRCKIND_NONE_D" ); //$NON-NLS-1$

  /**
   * {@link TsImageSourceKindPlugin}
   */
  String STR_SRCKIND_PLUGIN         = Messages.getString( "STR_SRCKIND_PLUGIN" );         //$NON-NLS-1$
  String STR_SRCKIND_PLUGIN_D       = Messages.getString( "STR_SRCKIND_PLUGIN_D" );       //$NON-NLS-1$
  String STR_PLUGIN_PLUGIN_ID       = Messages.getString( "STR_PLUGIN_PLUGIN_ID" );       //$NON-NLS-1$
  String STR_PLUGIN_PLUGIN_ID_D     = Messages.getString( "STR_PLUGIN_PLUGIN_ID_D" );     //$NON-NLS-1$
  String STR_PLUGIN_RESOURCE_PATH   = Messages.getString( "STR_PLUGIN_RESOURCE_PATH" );   //$NON-NLS-1$
  String STR_PLUGIN_RESOURCE_PATH_D = Messages.getString( "STR_PLUGIN_RESOURCE_PATH_D" ); //$NON-NLS-1$
  String MSG_ERR_NO_PLUGIN_ID       = Messages.getString( "MSG_ERR_NO_PLUGIN_ID" );       //$NON-NLS-1$
  String MSG_ERR_NO_RESOURCE_PATH   = Messages.getString( "MSG_ERR_NO_RESOURCE_PATH" );   //$NON-NLS-1$
  String FMT_ERR_INV_RESOURCE_URL   = Messages.getString( "FMT_ERR_INV_RESOURCE_URL" );   //$NON-NLS-1$
  String FMT_ERR_NO_RESOURCE_BY_URL = Messages.getString( "FMT_ERR_NO_RESOURCE_BY_URL" ); //$NON-NLS-1$

  /**
   * {@link TsImageSourceKindTsIcon}
   */
  String STR_SRCKIND_TSICON     = Messages.getString( "STR_SRCKIND_TSICON" );     //$NON-NLS-1$
  String STR_SRCKIND_TSICON_D   = Messages.getString( "STR_SRCKIND_TSICON_D" );   //$NON-NLS-1$
  String STR_TSICON_ICON_ID     = Messages.getString( "STR_TSICON_ICON_ID" );     //$NON-NLS-1$
  String STR_TSICON_ICON_ID_D   = Messages.getString( "STR_TSICON_ICON_ID_D" );   //$NON-NLS-1$
  String STR_TSICON_ICON_SIZE   = Messages.getString( "STR_TSICON_ICON_SIZE" );   //$NON-NLS-1$
  String STR_TSICON_ICON_SIZE_D = Messages.getString( "STR_TSICON_ICON_SIZE_D" ); //$NON-NLS-1$

  /**
   * {@link TsImageSourceKindUrl}
   */
  String STR_SRCKIND_URL       = Messages.getString( "STR_SRCKIND_URL" );       //$NON-NLS-1$
  String STR_SRCKIND_URL_D     = Messages.getString( "STR_SRCKIND_URL_D" );     //$NON-NLS-1$
  String STR_FILE_URL_STRING   = Messages.getString( "STR_FILE_URL_STRING" );   //$NON-NLS-1$
  String STR_FILE_URL_STRING_D = Messages.getString( "STR_FILE_URL_STRING_D" ); //$NON-NLS-1$

  /**
   * {@link ThumbSizeableDropDownMenuCreator}
   */
  String FMT_N_ORIGINAL_SIZE            = "Исходный (%s)";
  String FMT_D_ORIGINAL_SIZE            = "Восстановить размер миниатюр по умочанию %s";
  String STR_N_THUMB_SIZEABLE_ZOOM_MENU = "Размер миниатюр";
  String STR_D_THUMB_SIZEABLE_ZOOM_MENU = "Меню управления размерами миниатюр";
  String STR_N_THUMB_SIZEABLE_ZOOM_IN   = "Увеличить";
  String STR_D_THUMB_SIZEABLE_ZOOM_IN   = "Увеличение размера миниатюр";
  String STR_N_THUMB_SIZEABLE_ZOOM_OUT  = "Уменьшить";
  String STR_D_THUMB_SIZEABLE_ZOOM_OUT  = "Уменьшение размера миниатюр";

}
