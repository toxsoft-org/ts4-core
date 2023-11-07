package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

public sealed interface ITsActionSetProvider
    extends ITsActionHandler
    permits AbstractTsActionSetProvider, SeparatorTsActionSetProvider {

  @Override
  void handleAction( String aActionId );

  /**
   * Returns all actions handled by this handler.
   * <p>
   * Action IDs not present in {@link #listHandledActionIds()} are ignored.
   *
   * @return {@link IStridablesList}&lt;{@link ITsActionDef}&gt; - the list of known actions
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
   * For unknown actions returns <code>true</code>.
   *
   * @param aActionId String - the action ID
   * @return boolean - <code>true</code> if action is enabled
   */
  boolean isActionEnabled( String aActionId );

  /**
   * Determines if action checked state is on.
   * <p>
   * For unknown actions returns <code>false</code>.
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
