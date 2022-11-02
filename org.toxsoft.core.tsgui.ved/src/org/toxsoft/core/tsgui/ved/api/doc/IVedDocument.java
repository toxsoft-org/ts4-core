package org.toxsoft.core.tsgui.ved.api.doc;

import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tslib.av.props.*;

/**
 * VED document data model of entities created from {@link IVedDocumentData}.
 *
 * @author hazard157
 */
public interface IVedDocument
    extends IPropertableRo {

  /**
   * Returns the components.
   *
   * @return {@link IVedEntityManager}&lt;{@link IVedComponent}&gt; - manager of components
   */
  IVedEntityManager<IVedComponent> components();

  /**
   * Returns the tailors.
   *
   * @return {@link IVedEntityManager}&lt;{@link IVedTailor}&gt; - manager of tailors
   */
  IVedEntityManager<IVedTailor> tailors();

  /**
   * Returns the actors.
   *
   * @return {@link IVedEntityManager}&lt;{@link IVedActor}&gt; - manager of actors
   */
  IVedEntityManager<IVedActor> actors();

}
