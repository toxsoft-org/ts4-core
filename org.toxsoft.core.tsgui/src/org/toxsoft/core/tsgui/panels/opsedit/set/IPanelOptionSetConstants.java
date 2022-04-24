package org.toxsoft.core.tsgui.panels.opsedit.set;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;

/**
 * Constants used by the panel {@link IPanelOptionSetEdit}.
 *
 * @author hazard157
 */
public interface IPanelOptionSetConstants {

  /**
   * IDs prefix.
   */
  String ID_PREFIX = TS_ID + ".PanelOptionSet"; //$NON-NLS-1$

  /**
   * Param: the flag to use checkboxes marking options to be lost (not included in {@link IOptionSet}).<br>
   * Type: {@link EAtomicType#BOOLEAN}<br>
   * Usage: if a flag is set, then there will be checkboxes on the right of the option editors. By checking the box, you
   * can remove the corresponding option from the IOptionSet (option will be <i>lost</i>).<br>
   * This parameter is used only at the time of panel creation, subsequent changes to the parameter are ignored.<br>
   * Default value: <code>false</code>
   */
  IDataDef OPDEF_IS_LOST_OPTION_CHECKBOXES = DataDef.create( ID_PREFIX + ".IsLostOptionCheckboxes", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Param: the flag to exclude unknown options from the {@link IOptionSet}.<br>
   * Type: {@link EAtomicType#BOOLEAN}<br>
   * Usage: if the flag is set, the options not listed in {@link IPanelOptionSetView#listOpionDefs()} will be removeed
   * from the IOptionSet (options will be <i>lost</i>). While when the flag is reset all unknown options will remain in
   * option set but not displayed in the panel.<br>
   * This parameter is used only at the time of panel creation, subsequent changes to the parameter are ignored.<br>
   * Default value: <code>false</code>
   */
  IDataDef OPDEF_IS_UNKNOWN_OPTIONS_LOST = DataDef.create( ID_PREFIX + ".IsUnknownOptionsLost", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

}
