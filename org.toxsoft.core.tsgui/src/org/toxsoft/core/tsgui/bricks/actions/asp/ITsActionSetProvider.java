package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Declared actions executor, action set provider (frequently referred as ASP).
 * <p>
 * Declares executed actions in {@link #listHandledActionDefs()}, executes (handles) actions in method
 * {@link #handleAction(String)}. Methods {@link #isActionEnabled(String)} and {@link #isActionChecked(String)} together
 * with {@link #actionsStateEventer()} allows to change visual representation of the actions (such as tool buttons, menu
 * items) to represent enabled and checked states.
 * <p>
 * ASPs are typically provided by modules (subsystems) as a facade to access their functionality. For example, the
 * undo/redo manager may provide two ASPs: for actions "Undo"/"Redo" and separate ASP for the single action "Show
 * undo/redo stack dialog".
 * <p>
 * ASP is designed to build GUI components like tool bars and menus.
 *
 * @author hazard157
 */
public sealed interface ITsActionSetProvider
    extends ITsActionHandler permits AbstractTsActionSetProvider,SeparatorTsActionSetProvider {

  @Override
  void handleAction( String aActionId );

  /**
   * Returns all actions including (separators and handled ones) by this handler.
   * <p>
   * Action IDs not present in {@link #listHandledActionIds()} are ignored.
   *
   * @return {@link IList}&lt;{@link ITsActionDef}&gt; - the list of all actions definitions
   */
  IList<ITsActionDef> listAllActionDefs();

  /**
   * Returns all actions handled by this handler.
   *
   * @return {@link IStridablesList}&lt;{@link ITsActionDef}&gt; - the list of handled actions
   */
  IStridablesList<ITsActionDef> listHandledActionDefs();

  /**
   * Determines if action is known to the provider.
   *
   * @param aActionId String - the action ID
   * @return boolean - <code>true</code> if action is in {@link #listHandledActionDefs()}
   */
  boolean isActionKnown( String aActionId );

  /**
   * Determines if action enabled state is on.
   * <p>
   * For action IDs not present in {@link #listHandledActionIds()} returns <code>true</code>.
   *
   * @param aActionId String - the action ID
   * @return boolean - <code>true</code> if action is enabled
   */
  boolean isActionEnabled( String aActionId );

  /**
   * Determines if action checked state is on.
   * <p>
   * For action IDs not present in {@link #listHandledActionIds()} returns <code>false</code>.
   *
   * @param aActionId String - the action ID
   * @return boolean - <code>true</code> if action is checked
   */
  boolean isActionChecked( String aActionId );

  /**
   * Generates event when any action enabled and/or checked state changes.
   * <p>
   * Also the event is generated at the end of the method {@link #handleAction(String)} call for the known (that is
   * actually handled) actions.
   *
   * @return {@link IGenericChangeEventer} - actions state eventer
   */
  IGenericChangeEventer actionsStateEventer();

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience
  //

  @SuppressWarnings( "javadoc" )
  default IStringList listHandledActionIds() {
    return listHandledActionDefs().ids();
  }

}
