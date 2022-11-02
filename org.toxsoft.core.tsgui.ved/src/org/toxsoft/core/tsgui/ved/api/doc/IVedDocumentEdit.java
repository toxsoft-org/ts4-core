package org.toxsoft.core.tsgui.ved.api.doc;

import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.basis.*;

/**
 * VED document data model of entities created from {@link IVedDocumentData}.
 *
 * @author hazard157
 */
public interface IVedDocumentEdit
    extends IVedDocument, //
    IPropertable, // allow to edit properties
    ITsClearable, // to implement "New" command
    IGenericChangeEventCapable // inform about any user edits
{

  /**
   * Returns the components.
   *
   * @return {@link IVedEntityManagerEdit}&lt;{@link IVedComponent}&gt; - manager of components
   */
  @Override
  IVedEntityManagerEdit<IVedComponent> components();

  /**
   * Returns the tailors.
   *
   * @return {@link IVedEntityManagerEdit}&lt;{@link IVedTailor}&gt; - manager of tailors
   */
  @Override
  IVedEntityManagerEdit<IVedTailor> tailors();

  /**
   * Returns the actors.
   *
   * @return {@link IVedEntityManagerEdit}&lt;{@link IVedActor}&gt; - manager of actors
   */
  @Override
  IVedEntityManagerEdit<IVedActor> actors();

}
