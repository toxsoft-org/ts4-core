package org.toxsoft.core.tsgui.ved.m5;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.toxsoft.core.tsgui.ved.screen.items.*;

/**
 * Ved M5-modeling constants.
 *
 * @author hazard157
 */
public interface IVedM5Constants {

  /**
   * The {@link IVedItem} model ID.
   */
  String MID_VED_ITEM = VED_ID + ".VedItem"; //$NON-NLS-1$

  /**
   * The {@link IVedItem} model ID.
   */
  String MID_VED_VISEL = VED_ID + ".Visel"; //$NON-NLS-1$

  /**
   * The {@link IVedItem} model ID.
   */
  String MID_VED_ACTOR = VED_ID + ".Actor"; //$NON-NLS-1$

  /**
   * ID of the field corresponding to {@link IVedItem#factoryId()}.
   */
  String FID_FACTORY_ID = "factoryId"; //$NON-NLS-1$

  /**
   * ID of the field display name of the factory by {@link IVedItem#factoryId()}.
   */
  String FID_FACTORY_NAME = "factoryName"; //$NON-NLS-1$

}
