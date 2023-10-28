package org.toxsoft.core.tsgui.bricks.tin;

import static org.toxsoft.core.tsgui.bricks.tin.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Constants used by {@link ITinWidget}.
 *
 * @author hazard157
 */
public interface ITinWidgetConstants {

  /**
   * The ID of the option {@link #PRMDEF_IS_HIDDEN}.
   */
  String PRMID_IS_HIDDEN = TS_ID + ".gui.TinWidget.isHidden"; //$NON-NLS-1$

  /**
   * The ID of the option {@link #PRMDEF_IS_READ_ONLY}.
   */
  String PRMID_IS_READ_ONLY = TS_ID + ".gui.TinWidget.isReadOnly"; //$NON-NLS-1$

  /**
   * The flag to hide the field (row) in the inspector.
   * <p>
   * Use as an option in {@link ITinFieldInfo#params()}. If option is present and has value <code>true</code> then
   * corresponding field will not be visible to the user in {@link ITinWidget} GUI. However field remain accessible
   * programmatically.
   */
  IDataDef PRMDEF_IS_HIDDEN = DataDef.create( PRMID_IS_HIDDEN, BOOLEAN, //
      TSID_NAME, STR_PRM_IS_HIDDEN, //
      TSID_DESCRIPTION, STR_PRM_IS_HIDDEN_D, //
      TSID_DEFAULT_VALUE, AV_FALSE // by default properties are visible, not hidden
  );

  /**
   * The flag to hide the field (row) in the inspector.
   * <p>
   * Use as an option in {@link ITinFieldInfo#params()}. If option is present and has value <code>true</code> then
   * corresponding field can not be changed by the {@link ITinWidget} GUI. However field remain editable
   * programmatically.
   */
  IDataDef PRMDEF_IS_READ_ONLY = DataDef.create( PRMID_IS_READ_ONLY, BOOLEAN, //
      TSID_NAME, STR_PRM_IS_READ_ONLY, //
      TSID_DESCRIPTION, STR_PRM_IS_READ_ONLY_D, //
      TSID_DEFAULT_VALUE, AV_FALSE // by default properties are editable
  );

}
