package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * {@link ITsActionSetProvider} base implementation.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractTsActionSetProvider
    implements ITsActionSetProvider {

  private final GenericChangeEventer genericChangeEventer;

  /**
   * Constructor.
   */
  public AbstractTsActionSetProvider() {
    genericChangeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsActionSetProvider
  //

  @Override
  final public void handleAction( String aActionId ) {
    if( listHandledActionIds().hasElem( aActionId ) ) {
      doHandleAction( aActionId );
      doAfterActionHandled( aActionId );
      actionsStateEventer().fireChangeEvent();
    }
  }

  @Override
  final public GenericChangeEventer actionsStateEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * The subclass must perform action.
   *
   * @param aActionId String - the action ID, guaranteed to be in the {@link #listHandledActionIds()}
   */
  protected abstract void doHandleAction( String aActionId );

  /**
   * Subclass may perform additional processing after action was handled.
   * <p>
   * Does nothing in the base class, there is no need to call superclass method when overriding.
   *
   * @param aActionId String - the action ID, guaranteed to be in the {@link #listHandledActionIds()}
   */
  protected void doAfterActionHandled( String aActionId ) {
    // nop
  }

}
