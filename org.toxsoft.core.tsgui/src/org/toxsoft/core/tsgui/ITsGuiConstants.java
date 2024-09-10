package org.toxsoft.core.tsgui;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;

/**
 * TsGui common constants.
 *
 * @author hazard157
 */
public interface ITsGuiConstants {

  /**
   * ID prefix for TsGui constants.
   */
  String TSGUI_ID = TS_ID + ".gui"; //$NON-NLS-1$

  /**
   * Full ID prefix for TsGui constants.
   */
  String TSGUI_FULL_ID = TS_FULL_ID + ".gui"; //$NON-NLS-1$

  /**
   * TsGUI M5 models ID prefix.
   * <p>
   * TODO ID prefix from {@link IM5Constants#M5_ID} has to be changed on this one.
   */
  String TSGUI_M5_ID = TSGUI_ID + ".m5"; //$NON-NLS-1$

}
