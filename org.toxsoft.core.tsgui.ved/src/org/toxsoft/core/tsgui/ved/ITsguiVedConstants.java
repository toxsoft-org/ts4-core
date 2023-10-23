package org.toxsoft.core.tsgui.ved;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;

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
   * The editor palette icon size category ID used in {@link ITsHdpiService}.
   */
  String VED_EDITOR_PALETTE_ICON_SIZE_CATEGORY = VED_ID + ".PaletteIconSizeCategory"; //$NON-NLS-1$

  /**
   * The editor palette icon size category scale factor used in {@link ITsHdpiService#defineIconCategory(String, int)}.
   */
  int VED_EDITOR_PALETTE_ICON_SIZE_SCALE = 1;

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";           //$NON-NLS-1$
  String ICONID_VED_LOGO           = "ved-logo";          //$NON-NLS-1$
  String ICONID_VED_VISEL          = "ved-visel";         //$NON-NLS-1$
  String ICONID_VED_ACTOR          = "ved-actor";         //$NON-NLS-1$
  String ICONID_SIMPLE_RECT        = "simple-rect";       //$NON-NLS-1$
  String ICONID_VISEL_CIRCLE_LAMP  = "visel-circle-lamp"; //$NON-NLS-1$
  String ICONID_VISEL_LABEL        = "visel-label";       //$NON-NLS-1$
  String ICONID_VISEL_RECTANGLE    = "visel-rectangle";   //$NON-NLS-1$
  String ICONID_VISEL_ROUND_RECT   = "visel-round-rect";  //$NON-NLS-1$

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
