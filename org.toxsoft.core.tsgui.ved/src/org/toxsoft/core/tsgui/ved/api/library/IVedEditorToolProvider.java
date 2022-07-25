package org.toxsoft.core.tsgui.ved.api.library;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Editor tool factory.
 *
 * @author hazard157
 */
public interface IVedEditorToolProvider
    extends IStridableParameterized, IIconIdable {

  /**
   * Returns the owner library ID.
   *
   * @return String - the owner library ID (an IDpath)
   */
  String libraryId();

  /**
   * returns ID of the tools group.
   *
   * @return String - group ID or an empty string for no grouping
   */
  String groupId();

  /**
   * Creates {@link IVedEditorTool} implementation for the specified screen.
   *
   * @param aEnvironment {@link IVedEnvironment} - the VED framefork environment
   * @param aScreen {@link IVedScreen} - one of the screens in environment
   * @return {@link IVedEditorTool} - created instance
   */
  IVedEditorTool createTool( IVedEnvironment aEnvironment, IVedScreen aScreen );

}
