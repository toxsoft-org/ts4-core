package org.toxsoft.core.tsgui.bricks.combicond;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * {@link ICombiCondParamsPanel} configuration constants.
 * <p>
 * This options may be passed to the {@link CombiCondParamsPanel} constructor as {@link ITsGuiContext#params()}.
 *
 * @author hazard157
 */
public interface ICombiCondParamsPanelConstants {

  /**
   * Prefix of the option IDs.
   */
  String ID_PREFIX = "CombiCondParamsPanel"; //$NON-NLS-1$

  /**
   * Determines if formula editor line is placed at panel top or bottom.
   * <p>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN BOOLEAN}
   * <p>
   * <i>Usage:</i> <code>true</code> - formula line is at top of panel, <code>false</code> - at bottom.
   * <p>
   * <i>Default value:</i> <code>true</code> (at top)
   */
  IDataDef OPDEF_IS_FORMULA_EDITOR_AT_TOP = DataDef.create( ID_PREFIX + ".IsFormulaEditorAtTop", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Determines if validation message pane is present in the panel.
   * <p>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN BOOLEAN}
   * <p>
   * <i>Usage:</i> Validation message pane is {@link ValidationResultPanel} displaying the value, returned by
   * {@link ICombiCondParamsPanel#canGetEntity()} on each editing. When option is <code>true</code> - validation message
   * pane is present, <code>false</code> - no pane.
   * <p>
   * <i>Default value:</i> <code>false</code> (no validation message panel)
   */
  IDataDef OPDEF_IS_VALIDATION_PANE = DataDef.create( ID_PREFIX + ".IsValidationPane", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Determines if validation message pane is placed at panel top or bottom.
   * <p>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN BOOLEAN}
   * <p>
   * <i>Usage:</i> <code>true</code> - validation message pane is at top of panel, <code>false</code> - at bottom. If
   * both validation message pane and formula line are at the same side, validation message pane is placed above formula
   * line.
   * <p>
   * <i>Default value:</i> <code>true</code> (at top)
   */
  IDataDef OPDEF_IS_VALIDATION_PANE_AT_TOP = DataDef.create( ID_PREFIX + ".IsValidationPanesAtTop", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

}
