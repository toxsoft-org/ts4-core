package org.toxsoft.core.tsgui.ved.api.entity;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Base interface of all VED entities.
 *
 * @author hazard157
 */
public interface IVedEntityCfg
    extends IStridableParameterized {

  /**
   * Returns the kind of provider entities.
   *
   * @return {@link EVedEntityKind} - the kind of entities
   */
  EVedEntityKind entityKind();

  /**
   * Returns the values of the entity properties.
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
