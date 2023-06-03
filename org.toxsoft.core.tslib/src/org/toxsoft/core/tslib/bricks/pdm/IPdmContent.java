package org.toxsoft.core.tslib.bricks.pdm;

import org.toxsoft.core.tslib.bricks.keeper.*;

/**
 * Persistent content of the {@link IPersistentDataModel}.
 *
 * @author hazard157
 */
public sealed interface IPdmContent
    extends IKeepableEntity permits PdmAbstractContent {

  /**
   * Determines if content is clear.
   *
   * @return boolean - the flag of the clear content
   */
  boolean isClearContent();

}
