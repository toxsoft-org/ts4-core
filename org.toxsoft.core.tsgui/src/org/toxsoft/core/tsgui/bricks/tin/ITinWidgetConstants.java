package org.toxsoft.core.tsgui.bricks.tin;

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

  String PRMID_IS_HIDDEN = TS_ID + ".gui.TinWidget.isHidden"; //$NON-NLS-1$

  IDataDef PRMDEF_IS_HIDDEN = DataDef.create( PRMID_IS_HIDDEN, BOOLEAN, //
      TSID_NAME, "Hidden?", //
      TSID_DESCRIPTION, "This property is hidden from user (is not visible in object inspector)", //
      TSID_DEFAULT_VALUE, AV_FALSE // by default properties are visible, not hidden
  );

}
