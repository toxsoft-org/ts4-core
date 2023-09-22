package org.toxsoft.core.tsgui.ved.zver2.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.zver2.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.zver2.api.comp.*;
import org.toxsoft.core.tsgui.ved.zver2.api.doc.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * VED environment contains everything that is needed to view VED document.
 *
 * @author hazard157
 */
public interface IVedEnvironment
    extends ITsGuiContextable, ICloseable {

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
   * <p>
   * Note: as VED does not uses section data the {@link IVedDocumentData#secitonsData()} is empty.
   *
   * @return {@link IVedDocumentData} - document content data
   */
  IVedDocumentData getDocumentData();

}
