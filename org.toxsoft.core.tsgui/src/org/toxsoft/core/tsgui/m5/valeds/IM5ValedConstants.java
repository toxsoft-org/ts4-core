package org.toxsoft.core.tsgui.m5.valeds;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * M5 specific VALEDs related constants.
 *
 * @author hazard157
 */
public interface IM5ValedConstants {

  /**
   * ID of {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF}.
   */
  String M5_VALED_REFID_FIELD_DEF = IM5Constants.M5_ID + ".valed.ctxref.M5FieldDef"; //$NON-NLS-1$

  /**
   * ID of {@link IM5ValedConstants#M5_VALED_REFDEF_MASTER_OBJ}.
   */
  String M5_VALED_REFID_MASTER_OBJ = IM5Constants.M5_ID + ".valed.ctxref.MasterObject"; //$NON-NLS-1$

  /**
   * ID of {@link IM5ValedConstants#M5_VALED_OPDEF_WIDGET_TYPE_ID}.
   */
  String M5_VALED_OPID_WIDGET_TYPE_ID = IM5Constants.M5_ID + ".valed.op.WidgetTypeId"; //$NON-NLS-1$

  /**
   * Context reference to store {@link IM5FieldDef} for {@link IValedControl} field editors.
   */
  @SuppressWarnings( "rawtypes" )
  ITsGuiContextRefDef<IM5FieldDef> M5_VALED_REFDEF_FIELD_DEF = TsGuiContextRefDef.create( M5_VALED_REFID_FIELD_DEF, //
      IM5FieldDef.class, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  // ------------------------------------------------------------------------------------
  // VALED widget types

  /**
   * Inplace editing widget may contain some controls to edit single modown value. <br>
   * This is widget type ID to be specified in {@link #M5_VALED_OPDEF_WIDGET_TYPE_ID}.
   */
  String M5VWTID_INPLACE = "Inplace"; //$NON-NLS-1$

  /**
   * Generic text line with button to invoke value editor dialog. <br>
   * Type of invoked dialog.<br>
   * This is widget type ID to be specified in {@link #M5_VALED_OPDEF_WIDGET_TYPE_ID}.
   */
  String M5VWTID_TEXT = "Text"; //$NON-NLS-1$

  /**
   * The radio buttons to select one value for from lookup ones. <br>
   * This is widget type ID to be specified in {@link #M5_VALED_OPDEF_WIDGET_TYPE_ID}.
   */
  // String M5VWTID_RADIO = "Radio"; //$NON-NLS-1$

  /**
   * The check buttons to select set of value from few lookup ones. <br>
   * This is widget type ID to be specified in {@link #M5_VALED_OPDEF_WIDGET_TYPE_ID}.
   */
  // String M5VWTID_CHECK_BOX = "CheckBox"; //$NON-NLS-1$

  /**
   * Combo box with drop-down list to select one value for few lookup ones. <br>
   * This is widget type ID to be specified in {@link #M5_VALED_OPDEF_WIDGET_TYPE_ID}.
   */
  String M5VWTID_COMBO = "Combo"; //$NON-NLS-1$

  /**
   * Table with editing toolbar contains list of linked objects. <br>
   * This is widget type ID to be specified in {@link #M5_VALED_OPDEF_WIDGET_TYPE_ID}.
   */
  String M5VWTID_TABLE = "Table"; //$NON-NLS-1$

  /**
   * Context reference to store master-object for {@link IValedControl} field editors.
   */
  ITsGuiContextRefDef<Object> M5_VALED_REFDEF_MASTER_OBJ = TsGuiContextRefDef.create( M5_VALED_REFID_MASTER_OBJ, //
      Object.class, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

  /**
   * Context option specifies widget used for value editing.
   * <p>
   * Editors for different kinds lineked object field editors supports different widgets for eidting. For example for
   * single lookup field following widgets may be used: radio buttons, drop-down combobox, text-line with selection
   * button, etc. This option containg ID of widget type. Each kind of field editors supports own set off applicable
   * widgets.
   * <p>
   * For any kind some default type exists which is used if this option is not specified of type ID is unknown.
   * <p>
   * Some common tyype IDs are predefined here in <code><b>M5VWTID_</b>XXX</code> constants.
   */
  IDataDef M5_VALED_OPDEF_WIDGET_TYPE_ID = DataDef.create( M5_VALED_OPID_WIDGET_TYPE_ID, STRING, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY, //
      TSID_IS_MANDATORY, AV_FALSE //
  );

}
