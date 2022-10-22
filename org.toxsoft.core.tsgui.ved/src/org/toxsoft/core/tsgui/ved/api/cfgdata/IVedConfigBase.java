package org.toxsoft.core.tsgui.ved.api.cfgdata;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Base interface configuration data of single VED entity or other propartable object.
 *
 * @author hazard157
 */
public interface IVedConfigBase
    extends IStridable {

  /**
   * Returns the values of the properties.
   *
   * @return {@link IOptionSet} - the properies values
   */
  IOptionSet propValues();

  /**
   * Returns the values of the external data.
   *
   * @return {@link IOptionSet} - the external data
   */
  IOptionSet extdata();

}
