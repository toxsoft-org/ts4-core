package org.toxsoft.core.tsgui.ved.zver2.api.cfgdata;

import org.toxsoft.core.tsgui.ved.zver2.api.entity.*;

/**
 * Config data for entities of {@link EVedEntityKind}.
 *
 * @author hazard157
 */
public interface IVedEntityConfig
    extends IVedConfigBase {

  /**
   * Returns the kind of entity.
   *
   * @return {@link EVedEntityKind} - the kind of entity
   */
  EVedEntityKind entityKind();

}
