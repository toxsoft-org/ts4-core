package org.toxsoft.core.tsgui.ved.api.view;

import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages the editor tools {@link IVedEditorTool} assosiated with the screen.
 *
 * @author hazard157
 */
public interface IVedScreenToolsManager {

  /**
   * Returns the active tool ID.
   *
   * @return String - the active tool ID or an empty String if no tool is selected
   */
  String activeToolId();

  /**
   * Activates the tool by ID.
   *
   * @param aToolId String - the tool ID or <code>null</code> or empty String
   * @throws TsItemNotFoundRtException no such tool
   */
  void setActiveTool( String aToolId );

  /**
   * Returns all tools of the screen.
   * <p>
   * List of tools exactly corresponds to the tool providers in libraries {@link IVedLibraryManager#listLibs()}.
   *
   * @return {@link IList}&lt;{@link IVedEditorTool}&gt; - list of existing tools
   */
  IStridablesList<IVedEditorTool> listTools();

  /**
   * Returns {@link #activeToolId()} change eventer.
   *
   * @return {@link IGenericChangeEventer} - active tool change eventer
   */
  IGenericChangeEventer activeToolChangeEventer();

}
