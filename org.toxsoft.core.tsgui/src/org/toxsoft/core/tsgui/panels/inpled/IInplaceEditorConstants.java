package org.toxsoft.core.tsgui.panels.inpled;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * {@link IInplaceEditorPanel} configuration constants.
 *
 * @author hazard157
 */
public interface IInplaceEditorConstants {

  /**
   * Option IDs prefix.
   */
  String ID_PREFIX = TS_ID + ".gui.inpled"; //$NON-NLS-1$

  /**
   * Editing mode border thickness in pixels.<br>
   * Type: {@link EAtomicType#INTEGER}<br>
   * Default value: 3
   */
  IDataDef OPDEF_BORDER_THICKNESS = DataDef.create( ID_PREFIX + ".BorderThickness", INTEGER, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avInt( 3 ) //
  );

  /**
   * Editing mode border thickness in pixels.<br>
   * Type: {@link EAtomicType#VALOBJ} - {@link EBorderLayoutPlacement} <br>
   * Default value: {@link RGB}(255,0,0) (the same as {@link ETsColor#RED})
   */
  IDataDef OPDEF_BORDER_COLOR = DataDef.create( ID_PREFIX + ".BorderColor", VALOBJ, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avValobj( ETsColor.RED.rgb() ) //
  );

  /**
   * Flags to use validation (show panel and validate on every change).<br>
   * Type: {@link EAtomicType#BOOLEAN}<br>
   * Default value: <code>true</code>
   */
  IDataDef OPDEF_IS_VALIDATION_USED = DataDef.create( ID_PREFIX + ".IsValidationUsed", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

}
