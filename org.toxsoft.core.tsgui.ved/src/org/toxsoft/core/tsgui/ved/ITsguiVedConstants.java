package org.toxsoft.core.tsgui.ved;

import static org.toxsoft.core.tsgui.bricks.actions.ITsActionConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.ved.screen.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsguiVedConstants {

  /**
   * VED specific IDs prefix.
   */
  String VED_ID = TS_ID + ".gui.ved"; //$NON-NLS-1$

  /**
   * VED specific action IDs prefix.
   */
  String VED_ACT_ID = VED_ID + ".act"; //$NON-NLS-1$

  /**
   * The editor palette icon size category ID used in {@link ITsHdpiService}.
   */
  String VED_EDITOR_PALETTE_ICON_SIZE_CATEGORY = VED_ID + ".PaletteIconSizeCategory"; //$NON-NLS-1$

  /**
   * The editor palette icon size category scale factor used in {@link ITsHdpiService#defineIconCategory(String, int)}.
   */
  int VED_EDITOR_PALETTE_ICON_SIZE_SCALE = 1;

  // ------------------------------------------------------------------------------------
  // Misc
  //

  String SCREEN_CFG_FILE_EXT     = "screencfg";                //$NON-NLS-1$
  String SCREEN_CFG_FILE_AST_EXT = "*." + SCREEN_CFG_FILE_EXT; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME     = "ICONID_";                //$NON-NLS-1$
  String ICONID_VED_LOGO               = "ved-logo";               //$NON-NLS-1$
  String ICONID_VED_VISEL              = "ved-visel";              //$NON-NLS-1$
  String ICONID_VED_ACTOR              = "ved-actor";              //$NON-NLS-1$
  String ICONID_VED_COMMAND_ACTOR      = "actors-comanda";         //$NON-NLS-1$
  String ICONID_VED_CMD_FIELD_ACTOR    = "actors-cmd-field";       //$NON-NLS-1$
  String ICONID_VED_RRI_EDIT_ACTOR     = "actors-nsi";             //$NON-NLS-1$
  String ICONID_VED_RTDATA_IMG_ACTOR   = "actors-image";           //$NON-NLS-1$
  String ICONID_VED_INPUT_FIELD_ACTOR  = "actors-field";           //$NON-NLS-1$
  String ICONID_VED_ATR_EDIT_ACTOR     = "actors-attr";            //$NON-NLS-1$
  String ICONID_VED_RT_EDIT_ACTOR      = "actors-Rt";              //$NON-NLS-1$
  String ICONID_VED_RT_BOOL_EDIT_ACTOR = "actors-Rt-bool";         //$NON-NLS-1$
  String ICONID_VED_POPUP_DLG_ACTOR    = "actors-dialog";          //$NON-NLS-1$
  String ICONID_SIMPLE_RECT            = "simple-rect";            //$NON-NLS-1$
  String ICONID_VISEL_CIRCLE_LAMP      = "visel-circle-lamp";      //$NON-NLS-1$
  String ICONID_VISEL_LABEL            = "visel-label";            //$NON-NLS-1$
  String ICONID_VISEL_MULTI_LABEL      = "visel-multi-label";      //$NON-NLS-1$
  String ICONID_VISEL_RECTANGLE        = "visel-rectangle";        //$NON-NLS-1$
  String ICONID_VISEL_LINE             = "visel-line";             //$NON-NLS-1$
  String ICONID_VISEL_BEZIER           = "visel-bezier";           //$NON-NLS-1$
  String ICONID_VISEL_SPLINE           = "visel-spline";           //$NON-NLS-1$
  String ICONID_VISEL_ROUND_RECT       = "visel-round-rect";       //$NON-NLS-1$
  String ICONID_VISEL_BUTTON           = "visel-button";           //$NON-NLS-1$
  String ICONID_VISEL_CHECKBOX         = "visel-checkbox";         //$NON-NLS-1$
  String ICONID_VISEL_GROUPBOX         = "visel-groupbox";         //$NON-NLS-1$
  String ICONID_VISEL_IMAGE            = "visel-image";            //$NON-NLS-1$
  String ICONID_VISEL_BALOON           = "visel-baloon";           //$NON-NLS-1$
  String ICONID_VISEL_LINEAR_GAUGE     = "visel-linear-gauge";     //$NON-NLS-1$
  String ICONID_VED_ACTORS_ENABLED_ON  = "ved-actors-enabled-on";  //$NON-NLS-1$
  String ICONID_VED_ACTORS_ENABLED_OFF = "ved-actors-enabled-off"; //$NON-NLS-1$
  String ICONID_OBJECT_ROTATE_LEFT     = "object-rotate-left";     //$NON-NLS-1$
  String ICONID_OBJECT_ROTATE_ORIGINAL = "object-rotate-original"; //$NON-NLS-1$
  String ICONID_OBJECT_ROTATE_RIGHT    = "object-rotate-right";    //$NON-NLS-1$
  String ICONID_ALL_ITEMS              = "all-items";              //$NON-NLS-1$
  String ICONID_LINKED                 = "linked";                 //$NON-NLS-1$
  String ICONID_NON_LINKED             = "non-linked";             //$NON-NLS-1$
  // Action set - "Alignment"
  String ICONID_ALIGN_LEFT       = "left-align";       //$NON-NLS-1$
  String ICONID_ALIGN_RIGHT      = "right-align";      //$NON-NLS-1$
  String ICONID_ALIGN_TOP        = "top-align";        //$NON-NLS-1$
  String ICONID_ALIGN_BOTTOM     = "bottom-align";     //$NON-NLS-1$
  String ICONID_ALIGN_HOR_CENTER = "hor-center-align"; //$NON-NLS-1$
  String ICONID_ALIGN_VER_CENTER = "ver-center-align"; //$NON-NLS-1$
  String ICONID_SHRINK           = "shrink";           //$NON-NLS-1$
  // Action set - "Common"
  String ICONID_SETTINGS = "settings"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Some useful action definitions
  //

  String ACTID_ENABLE_ACTORS          = VED_ACT_ID + ".EnableActors";         //$NON-NLS-1$
  String ACTID_OBJECT_ROTATE_LEFT     = VED_ACT_ID + ".ObjectRotateLeft";     //$NON-NLS-1$
  String ACTID_OBJECT_ROTATE_ORIGINAL = VED_ACT_ID + ".ObjectRotateOriginal"; //$NON-NLS-1$
  String ACTID_OBJECT_ROTATE_RIGHT    = VED_ACT_ID + ".ObjectRotateRight";    //$NON-NLS-1$

  /**
   * Check action: turns actors ON/OFF (call {@link IVedScreen#setActorsEnabled(boolean)}.
   */
  ITsActionDef ACDEF_ENABLE_ACTORS_CHECK = TsActionDef.ofCheck1( ACTID_ENABLE_ACTORS,
      // unchecked state - actors are off (disabled)
      TSID_NAME, STR_ACT_ENABLE_ACTORS_OFF, //
      TSID_DESCRIPTION, STR_ACT_ENABLE_ACTORS_OFF_D, //
      TSID_ICON_ID, ICONID_VED_ACTORS_ENABLED_OFF, //
      // checked state - actors are on (enabled)
      OPID_CHECKED_TEXT, STR_ACT_ENABLE_ACTORS_ON, //
      OPID_CHECKED_TOOLTIP, STR_ACT_ENABLE_ACTORS_ON_D, //
      OPID_CHECKED_ICON_ID, ICONID_VED_ACTORS_ENABLED_ON //
  );

  ITsActionDef ACDEF_OBJECT_ROTATE_LEFT = TsActionDef.ofPush2( ACTID_OBJECT_ROTATE_LEFT, //
      STR_OBJECT_ROTATE_LEFT, STR_OBJECT_ROTATE_LEFT_D, ICONID_OBJECT_ROTATE_LEFT );

  ITsActionDef ACDEF_OBJECT_ROTATE_ORIGINAL = TsActionDef.ofPush2( ACTID_OBJECT_ROTATE_ORIGINAL, //
      STR_OBJECT_ROTATE_ORIGINAL, STR_OBJECT_ROTATE_ORIGINAL_D, ICONID_OBJECT_ROTATE_ORIGINAL );

  ITsActionDef ACDEF_OBJECT_ROTATE_RIGHT = TsActionDef.ofPush2( ACTID_OBJECT_ROTATE_RIGHT, //
      STR_OBJECT_ROTATE_RIGHT, STR_OBJECT_ROTATE_RIGHT_D, ICONID_OBJECT_ROTATE_RIGHT );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ITsguiVedConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
