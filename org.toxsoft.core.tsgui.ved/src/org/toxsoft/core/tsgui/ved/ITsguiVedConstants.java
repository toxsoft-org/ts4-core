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
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME     = "ICONID_";                //$NON-NLS-1$
  String ICONID_VED_LOGO               = "ved-logo";               //$NON-NLS-1$
  String ICONID_VED_VISEL              = "ved-visel";              //$NON-NLS-1$
  String ICONID_VED_ACTOR              = "ved-actor";              //$NON-NLS-1$
  String ICONID_SIMPLE_RECT            = "simple-rect";            //$NON-NLS-1$
  String ICONID_VISEL_CIRCLE_LAMP      = "visel-circle-lamp";      //$NON-NLS-1$
  String ICONID_VISEL_LABEL            = "visel-label";            //$NON-NLS-1$
  String ICONID_VISEL_RECTANGLE        = "visel-rectangle";        //$NON-NLS-1$
  String ICONID_VISEL_ROUND_RECT       = "visel-round-rect";       //$NON-NLS-1$
  String ICONID_VISEL_BUTTON           = "visel-button";           //$NON-NLS-1$
  String ICONID_VED_ACTORS_ENABLED_ON  = "ved-actors-enablem-on";  //$NON-NLS-1$
  String ICONID_VED_ACTORS_ENABLED_OFF = "ved-actors-enablem-off"; //$NON-NLS-1$
  String ICONID_OBJECT_ROTATE_LEFT     = "object-rotate-left";     //$NON-NLS-1$
  String ICONID_OBJECT_ROTATE_RIGHT    = "object-rotate-right";    //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Some useful action definitions
  //

  String ACTID_ENABLE_ACTORS       = VED_ACT_ID + ".EnableActors";      //$NON-NLS-1$
  String ACTID_OBJECT_ROTATE_LEFT  = VED_ACT_ID + ".ObjectRotateLeft";  //$NON-NLS-1$
  String ACTID_OBJECT_ROTATE_RIGHT = VED_ACT_ID + ".ObjectRotateRight"; //$NON-NLS-1$

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
