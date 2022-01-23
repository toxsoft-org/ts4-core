package org.toxsoft.tsgui.m5;

import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextRefDef;
import org.toxsoft.tsgui.bricks.ctx.impl.TsGuiContextRefDef;
import org.toxsoft.tsgui.graphics.EHorAlignment;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tslib.ITsHardConstants;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.bricks.strid.IStridable;

/**
 * M6 GUI modelling framework constants.
 *
 * @author hazard157
 */
public interface IM5Constants {

  /**
   * The framework identifiers prefix.
   */
  String M5_ID = ITsHardConstants.TS_ID + ".m5"; //$NON-NLS-1$

  /**
   * Hints bit for {@link IM5FieldDef#flags()}: the field is read-only.
   */
  int M5FF_READ_ONLY = 0x0001;

  /**
   * Hints bit for {@link IM5FieldDef#flags()}: the field is invariant, it can be set at creation but not edited after.
   */
  int M5FF_INVARIANT = 0x0002;

  /**
   * Hints bit for {@link IM5FieldDef#flags()}: the field will be present as column in table/tree viewes.
   */
  int M5FF_COLUMN = 0x0010;

  /**
   * Hints bit for {@link IM5FieldDef#flags()}: the field will be present in details viewes.
   */
  int M5FF_DETAIL = 0x0020;

  /**
   * Hints bit for {@link IM5FieldDef#flags()}: the field is hidden and not shown in auto-generated GUI.
   */
  int M5FF_HIDDEN = 0x0040;

  /**
   * ID for fields defs of {@link IStridable#id()} and maybe othed IDs.
   */
  String FID_ID = TSID_ID;

  /**
   * ID for fields defs of {@link IStridable#nmName()} and mayby other names.
   */
  String FID_NAME = TSID_NAME;

  /**
   * ID for fields defs of {@link IStridable#description()}.
   */
  String FID_DESCRIPTION = TSID_DESCRIPTION;

  /**
   * ID of {@link #M5_OPDEF_COLUMN_ALIGN}.
   */
  String M5_OPID_COLUMN_ALIGN = M5_ID + ".hints.ColumnAlignment"; //$NON-NLS-1$

  /**
   * ID of {@link #M5_REFDEF_FIELD_DEF}.
   */
  String M5_REFID_FIELD_DEF = M5_ID + ".M5FieldDef"; //$NON-NLS-1$

  /**
   * Option to store column alignment hint.
   */
  IDataDef M5_OPDEF_COLUMN_ALIGN = DataDef.create( M5_OPID_COLUMN_ALIGN, VALOBJ, //
      TSID_KEEPER_ID, EHorAlignment.KEEPER_ID, //
      TSID_IS_MANDATORY, AV_FALSE, //
      TSID_DEFAULT_VALUE, avValobj( EHorAlignment.LEFT ) //
  );

  /**
   * Context reference to store {@link IM5FieldDef} for {@link IValedControl} field editor.
   */
  @SuppressWarnings( "rawtypes" )
  ITsGuiContextRefDef<IM5FieldDef> M5_REFDEF_FIELD_DEF = TsGuiContextRefDef.create( M5_REFID_FIELD_DEF, //
      IM5FieldDef.class, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

}
