package org.toxsoft.core.tslib.bricks.filebound;

import java.io.*;

import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * Validates the content to file binding change actions.
 *
 * @author hazard157
 */
public interface IKeepedContentFileBoundValidator {

  /**
   * Checks if content reset (cleared)..
   *
   * @param aSource {@link IKeepedContentFileBound} - the event source
   * @return {@link ValidationResult} - check result
   */
  ValidationResult canClear( IKeepedContentFileBound aSource );

  /**
   * Checks if content can be loaded from the specified file.
   *
   * @param aSource {@link IKeepedContentFileBound} - the event source
   * @param aFile {@link File} - file to load content from
   * @return {@link ValidationResult} - check result
   */
  ValidationResult canOpen( IKeepedContentFileBound aSource, File aFile );

  /**
   * Checks if content can be saved to {@link IKeepedContentFileBound#file()}.
   *
   * @param aSource {@link IKeepedContentFileBound} - the event source
   * @return {@link ValidationResult} - check result
   */
  ValidationResult canSave( IKeepedContentFileBound aSource );

  /**
   * Checks if content can be saved to another file.
   *
   * @param aSource {@link IKeepedContentFileBound} - the event source
   * @param aFile {@link File} - other file to save content to
   * @return {@link ValidationResult} - check result
   */
  ValidationResult canSaveAs( IKeepedContentFileBound aSource, File aFile );

}
