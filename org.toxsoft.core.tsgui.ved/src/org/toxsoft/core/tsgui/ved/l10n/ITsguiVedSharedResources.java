package org.toxsoft.core.tsgui.ved.l10n;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsguiVedSharedResources {

  /**
   * {@link IVedScreenConstants}
   */
  String STR_IS_ACTIVE          = Messages.getString( "STR_IS_ACTIVE" );          //$NON-NLS-1$
  String STR_IS_ACTIVE_D        = Messages.getString( "STR_IS_ACTIVE_D" );        //$NON-NLS-1$
  String STR_IS_HOVERED         = Messages.getString( "STR_IS_HOVERED" );         //$NON-NLS-1$
  String STR_IS_HOVERED_D       = Messages.getString( "STR_IS_HOVERED_D" );       //$NON-NLS-1$
  String STR_IS_SELECTED        = Messages.getString( "STR_IS_SELECTED" );        //$NON-NLS-1$
  String STR_IS_SELECTED_D      = Messages.getString( "STR_IS_SELECTED_D" );      //$NON-NLS-1$
  String STR_HIDDEN             = Messages.getString( "STR_HIDDEN" );             //$NON-NLS-1$
  String STR_HIDDEN_D           = Messages.getString( "STR_HIDDEN_D" );           //$NON-NLS-1$
  String STR_X                  = Messages.getString( "STR_X" );                  //$NON-NLS-1$
  String STR_X_D                = Messages.getString( "STR_X_D" );                //$NON-NLS-1$
  String STR_Y                  = Messages.getString( "STR_Y" );                  //$NON-NLS-1$
  String STR_Y_D                = Messages.getString( "STR_Y_D" );                //$NON-NLS-1$
  String STR_WIDTH              = Messages.getString( "STR_WIDTH" );              //$NON-NLS-1$
  String STR_WIDTH_D            = Messages.getString( "STR_WIDTH_D" );            //$NON-NLS-1$
  String STR_HEIGHT             = Messages.getString( "STR_HEIGHT" );             //$NON-NLS-1$
  String STR_HEIGHT_D           = Messages.getString( "STR_HEIGHT_D" );           //$NON-NLS-1$
  String STR_TS_FULCRUM         = Messages.getString( "STR_TS_FULCRUM" );         //$NON-NLS-1$
  String STR_TS_FULCRUM_D       = Messages.getString( "STR_TS_FULCRUM_D" );       //$NON-NLS-1$
  String STR_ZOOM               = Messages.getString( "STR_ZOOM" );               //$NON-NLS-1$
  String STR_ZOOM_D             = Messages.getString( "STR_ZOOM_D" );             //$NON-NLS-1$
  String STR_ANGLE              = Messages.getString( "STR_ANGLE" );              //$NON-NLS-1$
  String STR_ANGLE_D            = Messages.getString( "STR_ANGLE_D" );            //$NON-NLS-1$
  String STR_TEXT               = Messages.getString( "STR_TEXT" );               //$NON-NLS-1$
  String STR_TEXT_D             = Messages.getString( "STR_TEXT_D" );             //$NON-NLS-1$
  String STR_HOR_ALIGN          = Messages.getString( "STR_HOR_ALIGN" );          //$NON-NLS-1$
  String STR_HOR_ALIGN_D        = Messages.getString( "STR_HOR_ALIGN_D" );        //$NON-NLS-1$
  String STR_VER_ALIGN          = Messages.getString( "STR_VER_ALIGN" );          //$NON-NLS-1$
  String STR_VER_ALIGN_D        = Messages.getString( "STR_VER_ALIGN_D" );        //$NON-NLS-1$
  String STR_ORIENTATION        = Messages.getString( "STR_ORIENTATION" );        //$NON-NLS-1$
  String STR_ORIENTATION_D      = Messages.getString( "STR_ORIENTATION_D" );      //$NON-NLS-1$
  String STR_BK_COLOR           = Messages.getString( "STR_BK_COLOR" );           //$NON-NLS-1$
  String STR_BK_COLOR_D         = Messages.getString( "STR_BK_COLOR_D" );         //$NON-NLS-1$
  String STR_FG_COLOR           = Messages.getString( "STR_FG_COLOR" );           //$NON-NLS-1$
  String STR_FG_COLOR_D         = Messages.getString( "STR_FG_COLOR_D" );         //$NON-NLS-1$
  String STR_BK_FILL            = Messages.getString( "STR_BK_FILL" );            //$NON-NLS-1$
  String STR_BK_FILL_D          = Messages.getString( "STR_BK_FILL_D" );          //$NON-NLS-1$
  String STR_BORDER_INFO        = Messages.getString( "STR_BORDER_INFO" );        //$NON-NLS-1$
  String STR_BORDER_INFO_D      = Messages.getString( "STR_BORDER_INFO_D" );      //$NON-NLS-1$
  String STR_LINE_INFO          = Messages.getString( "STR_LINE_INFO" );          //$NON-NLS-1$
  String STR_LINE_INFO_D        = Messages.getString( "STR_LINE_INFO_D" );        //$NON-NLS-1$
  String STR_IS_ASPECT_FIXED    = Messages.getString( "STR_IS_ASPECT_FIXED" );    //$NON-NLS-1$
  String STR_IS_ASPECT_FIXED_D  = Messages.getString( "STR_IS_ASPECT_FIXED_D" );  //$NON-NLS-1$
  String STR_ASPECT_RATIO       = Messages.getString( "STR_ASPECT_RATIO" );       //$NON-NLS-1$
  String STR_ASPECT_RATIO_D     = Messages.getString( "STR_ASPECT_RATIO_D" );     //$NON-NLS-1$
  String STR_FONT               = Messages.getString( "STR_FONT" );               //$NON-NLS-1$
  String STR_FONT_D             = Messages.getString( "STR_FONT_D" );             //$NON-NLS-1$
  String STR_TRANSFORM          = Messages.getString( "STR_TRANSFORM" );          //$NON-NLS-1$
  String STR_TRANSFORM_D        = Messages.getString( "STR_TRANSFORM_D" );        //$NON-NLS-1$
  String STR_VISEL_ID           = Messages.getString( "STR_VISEL_ID" );           //$NON-NLS-1$
  String STR_VISEL_ID_D         = Messages.getString( "STR_VISEL_ID_D" );         //$NON-NLS-1$
  String STR_VISEL_PROP_ID      = Messages.getString( "STR_VISEL_PROP_ID" );      //$NON-NLS-1$
  String STR_VISEL_PROP_ID_D    = Messages.getString( "STR_VISEL_PROP_ID_D" );    //$NON-NLS-1$
  String STR_RADIUS             = Messages.getString( "STR_RADIUS" );             //$NON-NLS-1$
  String STR_RADIUS_D           = Messages.getString( "STR_RADIUS_D" );           //$NON-NLS-1$
  String STR_ON_OFF_STATE       = Messages.getString( "STR_ON_OFF_STATE" );       //$NON-NLS-1$
  String STR_ON_OFF_STATE_D     = Messages.getString( "STR_ON_OFF_STATE_D" );     //$NON-NLS-1$
  String STR_CARET_POS          = Messages.getString( "STR_CARET_POS" );          //$NON-NLS-1$
  String STR_CARET_POS_D        = Messages.getString( "STR_CARET_POS_D" );        //$NON-NLS-1$
  String STR_IMAGE_DESCRIPTOR   = Messages.getString( "STR_IMAGE_DESCRIPTOR" );   //$NON-NLS-1$
  String STR_IMAGE_DESCRIPTOR_D = Messages.getString( "STR_IMAGE_DESCRIPTOR_D" ); //$NON-NLS-1$

  String STR_IS_ACTOR_MANDATORY   = Messages.getString( "STR_IS_ACTOR_MANDATORY" );   //$NON-NLS-1$
  String STR_IS_ACTOR_MANDATORY_D = Messages.getString( "STR_IS_ACTOR_MANDATORY_D" ); //$NON-NLS-1$

  String STR_LEFT_INDENT     = Messages.getString( "STR_LEFT_INDENT" );     //$NON-NLS-1$
  String STR_LEFT_INDENT_D   = Messages.getString( "STR_LEFT_INDENT_D" );   //$NON-NLS-1$
  String STR_TOP_INDENT      = Messages.getString( "STR_TOP_INDENT" );      //$NON-NLS-1$
  String STR_TOP_INDENT_D    = Messages.getString( "STR_TOP_INDENT_D" );    //$NON-NLS-1$
  String STR_RIGHT_INDENT    = Messages.getString( "STR_RIGHT_INDENT" );    //$NON-NLS-1$
  String STR_RIGHT_INDENT_D  = Messages.getString( "STR_RIGHT_INDENT_D" );  //$NON-NLS-1$
  String STR_BOTTOM_INDENT   = Messages.getString( "STR_BOTTOM_INDENT" );   //$NON-NLS-1$
  String STR_BOTTOM_INDENT_D = Messages.getString( "STR_BOTTOM_INDENT_D" ); //$NON-NLS-1$

  String STR_HOVERED   = Messages.getString( "STR_HOVERED" );   //$NON-NLS-1$
  String STR_HOVERED_D = Messages.getString( "STR_HOVERED_D" ); //$NON-NLS-1$

  /**
   * {@link VedAbstractActor}
   */
  String FMT_ERR_NO_MANDATORY_ACTOR_PROP = Messages.getString( "FMT_ERR_NO_MANDATORY_ACTOR_PROP" ); //$NON-NLS-1$

  /**
   * {@link VedAbstractVisel}
   */
  String FMT_ERR_NO_MANDATORY_VISEL_PROP = Messages.getString( "FMT_ERR_NO_MANDATORY_VISEL_PROP" ); //$NON-NLS-1$

  /**
   * {@link VedAbstractItem}
   */
  String STR_WARN_DUPLICATE_DIPOSAL = Messages.getString( "STR_WARN_DUPLICATE_DIPOSAL" ); //$NON-NLS-1$

  /**
   * {@link EVedItemKind}
   */
  String STR_ITEMKIND_VISEL   = Messages.getString( "STR_ITEMKIND_VISEL" );   //$NON-NLS-1$
  String STR_ITEMKIND_VISEL_D = Messages.getString( "STR_ITEMKIND_VISEL_D" ); //$NON-NLS-1$
  String STR_ITEMKIND_ACTOR   = Messages.getString( "STR_ITEMKIND_ACTOR" );   //$NON-NLS-1$
  String STR_ITEMKIND_ACTOR_D = Messages.getString( "STR_ITEMKIND_ACTOR_D" ); //$NON-NLS-1$

  /**
   * General
   */
  String STR_ITEM_ID             = Messages.getString( "STR_ITEM_ID" );             //$NON-NLS-1$
  String STR_ITEM_ID_D           = Messages.getString( "STR_ITEM_ID_D" );           //$NON-NLS-1$
  String STR_ITEM_FACTORY_ID     = Messages.getString( "STR_ITEM_FACTORY_ID" );     //$NON-NLS-1$
  String STR_ITEM_FACTORY_ID_D   = Messages.getString( "STR_ITEM_FACTORY_ID_D" );   //$NON-NLS-1$
  String STR_ITEM_FACTORY_NAME   = Messages.getString( "STR_ITEM_FACTORY_NAME" );   //$NON-NLS-1$
  String STR_ITEM_FACTORY_NAME_D = Messages.getString( "STR_ITEM_FACTORY_NAME_D" ); //$NON-NLS-1$
  String STR_ITEM_NAME           = Messages.getString( "STR_ITEM_NAME" );           //$NON-NLS-1$
  String STR_ITEM_NAME_D         = Messages.getString( "STR_ITEM_NAME_D" );         //$NON-NLS-1$
  String STR_ITEM_DESCRIPTION    = Messages.getString( "STR_ITEM_DESCRIPTION" );    //$NON-NLS-1$
  String STR_ITEM_DESCRIPTION_D  = Messages.getString( "STR_ITEM_DESCRIPTION_D" );  //$NON-NLS-1$

  /**
   * M5-models
   */
  String STR_TMI_ITEM_BY_KIND   = Messages.getString( "STR_TMI_ITEM_BY_KIND" );   //$NON-NLS-1$
  String STR_TMI_ITEM_BY_KIND_D = Messages.getString( "STR_TMI_ITEM_BY_KIND_D" ); //$NON-NLS-1$

  /**
   * implementation classes
   */
  String FMT_ERR_VED_ITEM_CREATION_INV_INDEX = Messages.getString( "FMT_ERR_VED_ITEM_CREATION_INV_INDEX" ); //$NON-NLS-1$
  String FMT_ERR_VED_ITEM_CREATION_DUP_ID    = Messages.getString( "FMT_ERR_VED_ITEM_CREATION_DUP_ID" );    //$NON-NLS-1$
  String FMT_ERR_VED_ITEM_UNKNOWN_FACTORY    = Messages.getString( "FMT_ERR_VED_ITEM_UNKNOWN_FACTORY" );    //$NON-NLS-1$
  String FMT_WARN_CANT_REMOVE_ABSENT_ITEM    = Messages.getString( "FMT_WARN_CANT_REMOVE_ABSENT_ITEM" );    //$NON-NLS-1$

}
