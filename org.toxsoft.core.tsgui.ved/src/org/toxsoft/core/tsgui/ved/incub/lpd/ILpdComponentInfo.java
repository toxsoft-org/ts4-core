package org.toxsoft.core.tsgui.ved.incub.lpd;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;

/**
 * Information about live panel component to be stored.
 *
 * @author hazard157
 */
public interface ILpdComponentInfo {

  /**
   * Returns namespace that provided the component.
   *
   * @return IdChain - the namespace (as chain of IDpaths)
   */
  IdChain namespace();

  /**
   * Returns component kind ID.
   * <p>
   * Kind ID identifies the component provider in the namespace.
   *
   * @return String - the factory ID (an IDpath)
   */
  String componentKindId();

  /**
   * Returns the values of the component properties.
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
