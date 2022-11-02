package org.toxsoft.core.tsgui.ved;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.ved.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

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

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME   = "ICONID_";             //$NON-NLS-1$
  String ICONID_VED_LOGO             = "ved-logo";            //$NON-NLS-1$
  String ICONID_STD_LIB_SHAPES       = "std-lib-shapes";      //$NON-NLS-1$
  String ICONID_TOOL_POINTER         = "tool-pointer";        //$NON-NLS-1$
  String ICONID_COMP_RECTANGLE       = "comp-rectangle";      //$NON-NLS-1$
  String ICONID_COMP_ROUNDRECT       = "comp-roundrect";      //$NON-NLS-1$
  String ICONID_OBJECT_ROLTATE_LEFT  = "object-rotate-left";  //$NON-NLS-1$
  String ICONID_OBJECT_ROLTATE_RIGHT = "object-rotate-right"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions

  // ------------------------------------------------------------------------------------
  // Actions

  String ACTID_OBJECT_ROTATE_LEFT  = "ts.gui.ved.object_rotate_left";  //$NON-NLS-1$
  String ACTID_OBJECT_ROTATE_RIGHT = "ts.gui.ved.object_rotate_right"; //$NON-NLS-1$
  String ACTID_SHIFT_RIGHT         = "ts.gui.ved.shift_right";         //$NON-NLS-1$
  String ACTID_SHIFT_LEFT          = "ts.gui.ved.shift_left";          //$NON-NLS-1$
  String ACTID_SHIFT_UP            = "ts.gui.ved.shift_up";            //$NON-NLS-1$
  String ACTID_SHIFT_DOWN          = "ts.gui.ved.shift_down";          //$NON-NLS-1$

  ITsActionDef ACDEF_OBJECT_ROTATE_LEFT = TsActionDef.ofPush2( ACTID_OBJECT_ROTATE_LEFT, //
      STR_N_OBJECT_ROTATE_LEFT, STR_D_OBJECT_ROTATE_LEFT, ICONID_OBJECT_ROLTATE_LEFT );

  ITsActionDef ACDEF_OBJECT_ROTATE_RIGHT = TsActionDef.ofPush2( ACTID_OBJECT_ROTATE_RIGHT, //
      STR_N_OBJECT_ROTATE_RIGHT, STR_D_OBJECT_ROTATE_RIGHT, ICONID_OBJECT_ROLTATE_RIGHT );

  ITsActionDef ACDEF_SHIFT_RIGHT = TsActionDef.ofPush2( ACTID_SHIFT_RIGHT, //
      STR_N_SHIFT_RIGHT, STR_D_SHIFT_RIGHT, ICONID_NAVIGATE_RIGHT );

  ITsActionDef ACDEF_SHIFT_LEFT = TsActionDef.ofPush2( ACTID_SHIFT_LEFT, //
      STR_N_SHIFT_LEFT, STR_D_SHIFT_LEFT, ICONID_NAVIGATE_LEFT );

  ITsActionDef ACDEF_SHIFT_UP = TsActionDef.ofPush2( ACTID_SHIFT_UP, //
      STR_N_SHIFT_UP, STR_D_SHIFT_UP, ICONID_NAVIGATE_UP );

  ITsActionDef ACDEF_SHIFT_DOWN = TsActionDef.ofPush2( ACTID_SHIFT_DOWN, //
      STR_N_SHIFT_DOWN, STR_D_SHIFT_DOWN, ICONID_NAVIGATE_DOWN );

  // ------------------------------------------------------------------------------------
  // App prefs
  //

  // FIXMEconvert to app preference
  EIconSize LIST_ICON_SIZE = EIconSize.IS_32X32;

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
