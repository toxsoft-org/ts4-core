package org.toxsoft.core.tsgui.panels.opsedit;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Items to be used when editing several {@link IOpsSetter}s in one panel.
 *
 * @author hazard157
 */
public interface IOpsetsKitItemDef
    extends IStridableParameterized {

  /**
   * Returns the definitions of the known options in this item.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   */
  IStridablesList<IDataDef> optionDefs();

}
