package org.toxsoft.core.tsgui.panels.opsedit.group;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Section of options to be edited at once.
 * <p>
 * Before starting
 *
 * @author hazard157
 */
public interface ISectionDef
    extends IStridableParameterized {

  /**
   * Returns definitions of options from {@link #values()} to be edited by user via GUI means.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - known (visble) options definitions
   */
  IStridablesList<IDataDef> defs();

  /**
   * Returns values of the options.
   *
   * @return {@link IOptionSet} - options values
   */
  IOptionSet values();

}
