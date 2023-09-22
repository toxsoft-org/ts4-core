package org.toxsoft.core.tsgui.ved.zver2.api.doc;

import org.toxsoft.core.tsgui.ved.zver2.api.entity.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;

/**
 * Entities list manager for {@link IVedDocument} data model.
 *
 * @author hazard157
 * @param <T> - entities Java type
 */
public interface IVedEntityManager<T extends IVedEntity> {

  /**
   * Returns the kind of managed entities.
   *
   * @return {@link EVedEntityKind} - the kind of entities
   */
  EVedEntityKind entityKind();

  /**
   * Returns the entities.
   *
   * @return {@link INotifierStridablesListEdit}&lt;T&gt; - list of the entities
   */
  INotifierStridablesList<T> items();

}
