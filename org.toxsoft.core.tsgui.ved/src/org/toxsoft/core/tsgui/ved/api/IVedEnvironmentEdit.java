package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.ved.api.doc.*;

/**
 * An editable VED environment contains everything that is needed to view and edit VED document.
 *
 * @author hazard157
 */
public interface IVedEnvironmentEdit
    extends IVedEnvironment {

  /**
   * Returns the VED document.
   *
   * @return {@link IVedDocumentEdit} - the ediable VED document
   */
  @Override
  IVedDocumentEdit doc();

}
