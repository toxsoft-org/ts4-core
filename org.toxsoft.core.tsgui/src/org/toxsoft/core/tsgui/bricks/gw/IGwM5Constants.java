package org.toxsoft.core.tsgui.bricks.gw;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;

/**
 * M5-modeling constants of the Green World entities.
 *
 * @author hazard157
 */
public interface IGwM5Constants {

  /**
   * The ID of the {@link Skid} M5-model {@link SkidM5Model}.
   */
  String MID_SKID = M5_ID + ".Skid"; //$NON-NLS-1$

  /**
   * The ID of the {@link Gwid} M5-model {@link GwidM5Model}.
   */
  String MID_GWID = M5_ID + ".Gwid"; //$NON-NLS-1$

  /**
   * ID of the attribute M5-field {@link Skid#classId()} and {@link Gwid#classId()}.
   */
  String FID_CLASS_ID = "classId"; //$NON-NLS-1$

  /**
   * ID of the attribute M5-field {@link Skid#strid()} and {@link Gwid#strid()}.
   */
  String FID_OBJ_STRID = "objStrid"; //$NON-NLS-1$

  /**
   * ID of the attribute M5-field {@link Gwid#kind()}.
   */
  String FID_GWID_KIND = "gwidKind"; //$NON-NLS-1$

  /**
   * ID of the attribute M5-field {@link Gwid#propId()}.
   */
  String FID_PROP_ID = "propId"; //$NON-NLS-1$

  /**
   * ID of the attribute M5-field {@link Gwid#subPropId()}.
   */
  String FID_SUB_PROP_ID = "subPropId"; //$NON-NLS-1$

  /**
   * Id of M5-field of type {@link Gwid}.
   * <p>
   * For {@link Skid} entities returns the entity itself. For {@link Gwid} entities returns {@link Gwid#skid()}.
   */
  String FID_SKID = "skid"; //$NON-NLS-1$

  /**
   * Id of M5-field of type {@link Gwid}.
   * <p>
   * For {@link Gwid} entities returns the entity itself.
   */
  String FID_GWID = "gwid"; //$NON-NLS-1$

}
