package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;

/**
 * VED environment contains everything that is needed to view VED document.
 *
 * @author hazard157
 */
public interface IVedEnvironment
    extends ITsGuiContextable {

  /**
   * Returns the VED framework that created this environment.
   *
   * @return {@link IVedFramework} - parent framework
   */
  IVedFramework vedFramework();

  /**
   * Returns the VED document.
   *
   * @return {@link IVedDocumentEdit} - the VED document
   */
  IVedDocument doc();

  /**
   * Returns the screen manager - the visualization means of the VED document.
   *
   * @return {@link IVedScreenManager} - screens manager
   */
  IVedScreenManager screenManager();

  /**
   * Sets the contnetnt of the document and updates all viewes (screens).
   *
   * @param aData {@link IVedDocumentData} - document content data
   */
  void setDocumentData( IVedDocumentData aData );

  /**
   * Returns the current content data of the document.
   *
   * @return {@link IVedDocumentData} - document content data
   */
  IVedDocumentData getDocumentData();

}
