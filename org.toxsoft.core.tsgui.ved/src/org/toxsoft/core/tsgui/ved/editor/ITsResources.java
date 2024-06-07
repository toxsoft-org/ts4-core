package org.toxsoft.core.tsgui.ved.editor;

/**
 * Localizable resources.
 *
 * @author hazard157,vs
 */
interface ITsResources {

  /**
   * {@link VedScreenItemInspector}
   */
  String STR_LABEL_ID   = Messages.getString( "STR_LABEL_ID" );   //$NON-NLS-1$
  String STR_LABEL_ID_D = Messages.getString( "STR_LABEL_ID_D" ); //$NON-NLS-1$
  String STR_BTN_COPY   = Messages.getString( "STR_BTN_COPY" );   //$NON-NLS-1$
  String STR_BTN_COPY_D = Messages.getString( "STR_BTN_COPY_D" ); //$NON-NLS-1$
  String STR_BTN_EDIT   = Messages.getString( "STR_BTN_EDIT" );   //$NON-NLS-1$
  String STR_BTN_EDIT_D = Messages.getString( "STR_BTN_EDIT_D" ); //$NON-NLS-1$

  /**
   * {@link VedEditorUtils}
   */
  String STR_LABEL_VIBP_ID            = Messages.getString( "STR_LABEL_VIBP_ID" );            //$NON-NLS-1$
  String STR_LABEL_VIBP_ID_D          = Messages.getString( "STR_LABEL_VIBP_ID_D" );          //$NON-NLS-1$
  String STR_LABEL_VIBP_NAME          = Messages.getString( "STR_LABEL_VIBP_NAME" );          //$NON-NLS-1$
  String STR_LABEL_VIBP_NAME_D        = Messages.getString( "STR_LABEL_VIBP_NAME_D" );        //$NON-NLS-1$
  String STR_LABEL_VIBP_DESCRIPTION   = Messages.getString( "STR_LABEL_VIBP_DESCRIPTION" );   //$NON-NLS-1$
  String STR_LABEL_VIBP_DESCRIPTION_D = Messages.getString( "STR_LABEL_VIBP_DESCRIPTION_D" ); //$NON-NLS-1$
  String STR_ERR_VIBP_ID_NOT_IDPATH   = Messages.getString( "STR_ERR_VIBP_ID_NOT_IDPATH" );   //$NON-NLS-1$
  String FMT_ERR_VIBP_DUPLICATE_ID    = Messages.getString( "FMT_ERR_VIBP_DUPLICATE_ID" );    //$NON-NLS-1$
  String FMT_DLG_VED_ITEM_BASICS      = Messages.getString( "FMT_DLG_VED_ITEM_BASICS" );      //$NON-NLS-1$
  String FMT_DLG_VED_ITEM_BASICS_D    = Messages.getString( "FMT_DLG_VED_ITEM_BASICS_D" );    //$NON-NLS-1$

  /**
   * {@link PanelCanvasConfig}
   */
  String DLG_T_CANVAS_CFG   = "Конфигурация экрана";
  String STR_MSG_CANVAS_CFG = "Измените требуемые параметры и нажмите \"OK\"";
  String STR_L_WIDTH_PIX    = "Ширина (пикс): ";
  String STR_L_HEIGHT_PIX   = "Высота (пикс): ";
  String STR_G_SIZES        = "Размеры";
  String STR_G_CONVERSIONS  = "Преобразования";

  /**
   * {@link VedPanelActorsList}
   */
  String STR_L_VIEW_ALL       = Messages.getString( "STR_L_VIEW_ALL" );       //$NON-NLS-1$
  String STR_L_VIEW_ALL_D     = Messages.getString( "STR_L_VIEW_ALL_D" );     //$NON-NLS-1$
  String STR_L_VIEW_LINKED    = Messages.getString( "STR_L_VIEW_LINKED" );    //$NON-NLS-1$
  String STR_L_VIEW_LINKED_D  = Messages.getString( "STR_L_VIEW_LINKED_D" );  //$NON-NLS-1$
  String STR_L_VIEW_UNBOUND   = Messages.getString( "STR_L_VIEW_UNBOUND" );   //$NON-NLS-1$
  String STR_L_VIEW_UNBOUND_D = Messages.getString( "STR_L_VIEW_UNBOUND_D" ); //$NON-NLS-1$

  /**
   * {@link VedViselContextMenuManager}
   */
  String STR_M_ALIGNMENT = "Выравнивание";
}
