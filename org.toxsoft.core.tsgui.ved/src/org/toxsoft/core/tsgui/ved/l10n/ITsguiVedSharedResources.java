package org.toxsoft.core.tsgui.ved.l10n;

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
   * {@link IVedFrameworkConstants}
   */
  String STR_IS_ACTIVE       = Messages.getString( "STR_IS_ACTIVE" );         //$NON-NLS-1$
  String STR_IS_ACTIVE_D     = Messages.getString( "STR_IS_ACTIVE_D" );       //$NON-NLS-1$
  String STR_IS_HOVERED      = Messages.getString( "STR_IS_HOVERED" );        //$NON-NLS-1$
  String STR_IS_HOVERED_D    = Messages.getString( "STR_IS_HOVERED_D" );      //$NON-NLS-1$
  String STR_IS_SELECTED     = Messages.getString( "STR_IS_SELECTED" );       //$NON-NLS-1$
  String STR_IS_SELECTED_D   = Messages.getString( "STR_IS_SELECTED_D" );     //$NON-NLS-1$
  String STR_HIDDEN          = Messages.getString( "STR_HIDDEN" );            //$NON-NLS-1$
  String STR_HIDDEN_D        = Messages.getString( "STR_HIDDEN_D" );          //$NON-NLS-1$
  String STR_X               = Messages.getString( "STR_X" );                 //$NON-NLS-1$
  String STR_X_D             = Messages.getString( "STR_X_D" );               //$NON-NLS-1$
  String STR_Y               = Messages.getString( "STR_Y" );                 //$NON-NLS-1$
  String STR_Y_D             = Messages.getString( "STR_Y_D" );               //$NON-NLS-1$
  String STR_WIDTH           = Messages.getString( "STR_WIDTH" );             //$NON-NLS-1$
  String STR_WIDTH_D         = Messages.getString( "STR_WIDTH_D" );           //$NON-NLS-1$
  String STR_HEIGHT          = Messages.getString( "STR_HEIGHT" );            //$NON-NLS-1$
  String STR_HEIGHT_D        = Messages.getString( "STR_HEIGHT_D" );          //$NON-NLS-1$
  String STR_FULCRUM         = Messages.getString( "STR_FULCRUM" );           //$NON-NLS-1$
  String STR_FULCRUM_D       = Messages.getString( "STR_FULCRUM_D" );         //$NON-NLS-1$
  String STR_BK_COLOR        = Messages.getString( "STR_BK_COLOR" );          //$NON-NLS-1$
  String STR_BK_COLOR_D      = Messages.getString( "STR_BK_COLOR_D" );        //$NON-NLS-1$
  String STR_FG_COLOR        = Messages.getString( "STR_FG_COLOR" );          //$NON-NLS-1$
  String STR_FG_COLOR_D      = Messages.getString( "STR_FG_COLOR_D" );        //$NON-NLS-1$
  String STR_FONT            = Messages.getString( "STR_FONT" );              //$NON-NLS-1$
  String STR_FONT_D          = Messages.getString( "STR_FONT_D" );            //$NON-NLS-1$
  String STR_TRANSFORM       = Messages.getString( "STR_N_TRANSFORM" );       //$NON-NLS-1$
  String STR_TRANSFORM_D     = Messages.getString( "STR_D_TRANSFORM_D" );     //$NON-NLS-1$
  String STR_VISEL_ID        = Messages.getString( "STR_N_VISEL_ID" );        //$NON-NLS-1$
  String STR_VISEL_ID_D      = Messages.getString( "STR_D_VISEL_ID_D" );      //$NON-NLS-1$
  String STR_VISEL_PROP_ID   = Messages.getString( "STR_N_VISEL_PROP_ID" );   //$NON-NLS-1$
  String STR_VISEL_PROP_ID_D = Messages.getString( "STR_D_VISEL_PROP_ID_D" ); //$NON-NLS-1$

  /**
   * {@link VedAbstractItem}
   */
  String STR_WARN_DUPLICATE_DIPOSAL = Messages.getString( "STR_WARN_DUPLICATE_DIPOSAL" ); //$NON-NLS-1$

  /**
   * {@link VedEnvironment}
   */
  String FMT_ERR_ITEM_ALREADY_EXISTS  = Messages.getString( "FMT_ERR_ITEM_ALREADY_EXISTS" );  //$NON-NLS-1$
  String FMT_WARN_UNKNON_ITEM_FACTORY = Messages.getString( "FMT_WARN_UNKNON_ITEM_FACTORY" ); //$NON-NLS-1$
  String FMT_ERR_CAN_CREATE_ITEM      = Messages.getString( "FMT_ERR_CAN_CREATE_ITEM" );      //$NON-NLS-1$

  /**
   * {@link EVedItemKind}
   */
  String STR_ITEMKIND_VISEL   = Messages.getString( "STR_ITEMKIND_VISEL" );   //$NON-NLS-1$
  String STR_ITEMKIND_VISEL_D = Messages.getString( "STR_ITEMKIND_VISEL_D" ); //$NON-NLS-1$
  String STR_ITEMKIND_ACTOR   = Messages.getString( "STR_ITEMKIND_ACTOR" );   //$NON-NLS-1$
  String STR_ITEMKIND_ACTOR_D = Messages.getString( "STR_ITEMKIND_ACTOR_D" ); //$NON-NLS-1$

  /**
   * M5-models
   */
  String STR_ITEM_ID            = Messages.getString( "STR_ITEM_ID" );            //$NON-NLS-1$
  String STR_ITEM_ID_D          = Messages.getString( "STR_ITEM_ID_D" );          //$NON-NLS-1$
  String STR_ITEM_FACTORY_ID    = Messages.getString( "STR_ITEM_FACTORY_ID" );    //$NON-NLS-1$
  String STR_ITEM_FACTORY_ID_D  = Messages.getString( "STR_ITEM_FACTORY_ID_D" );  //$NON-NLS-1$
  String STR_ITEM_NAME          = Messages.getString( "STR_ITEM_NAME" );          //$NON-NLS-1$
  String STR_ITEM_NAME_D        = Messages.getString( "STR_ITEM_NAME_D" );        //$NON-NLS-1$
  String STR_ITEM_DESCRIPTION   = Messages.getString( "STR_ITEM_DESCRIPTION" );   //$NON-NLS-1$
  String STR_ITEM_DESCRIPTION_D = Messages.getString( "STR_ITEM_DESCRIPTION_D" ); //$NON-NLS-1$

}
