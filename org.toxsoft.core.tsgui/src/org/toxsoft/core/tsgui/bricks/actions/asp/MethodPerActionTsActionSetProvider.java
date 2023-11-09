package org.toxsoft.core.tsgui.bricks.actions.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsActionSetProvider} abstract implementation with method per action.
 *
 * @author hazard157
 */
public class MethodPerActionTsActionSetProvider
    extends AbstractTsActionSetProvider {

  /**
   * Determines the current boolean state of something.
   * <p>
   * Used as ENABLED and CHECKED state providers when defining the action.
   *
   * @author hazard157
   */
  public interface IBooleanState {

    /**
     * Always <code>true</code> state singleton.
     */
    IBooleanState ALWAY_TRUE = () -> true;

    /**
     * Always <code>false</code> state singleton.
     */
    IBooleanState ALWAY_FALSE = () -> false;

    /**
     * Returns the current state.
     *
     * @return boolean - the current state
     */
    boolean isState();

  }

  private final IListEdit<ITsActionDef>           allActionDefs = new ElemArrayList<>();
  private final IStridablesListEdit<ITsActionDef> actDefs       = new StridablesList<>();

  private final IStringMapEdit<Runnable>      runMap   = new StringMap<>();
  private final IStringMapEdit<IBooleanState> enaMap   = new StringMap<>();
  private final IStringMapEdit<IBooleanState> checkMap = new StringMap<>();

  /**
   * Constructor for subclass.
   */
  public MethodPerActionTsActionSetProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsActionSetProvider
  //

  @Override
  public void doHandleAction( String aActionId ) {
    Runnable r = runMap.findByKey( aActionId );
    if( r != null ) {
      r.run();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsActionSetProvider
  //

  @Override
  public IList<ITsActionDef> listAllActionDefs() {
    return allActionDefs;
  }

  @Override
  public IStridablesList<ITsActionDef> listHandledActionDefs() {
    return actDefs;
  }

  @Override
  public boolean isActionKnown( String aActionId ) {
    return actDefs.keys().hasElem( aActionId );
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    IBooleanState s = enaMap.findByKey( aActionId );
    if( s != null ) {
      return s.isState();
    }
    return true;
  }

  @Override
  public boolean isActionChecked( String aActionId ) {
    IBooleanState s = checkMap.findByKey( aActionId );
    if( s != null ) {
      return s.isState();
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API for subclass
  //

  /**
   * Defines action and adds to {@link #listHandledActionDefs()}.
   *
   * @param aDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - the action executor
   * @param aEnaState {@link IBooleanState} - determines if action is enables
   * @param aCheckState {@link IBooleanState} - determines if action is checked
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void defineAction( ITsActionDef aDef, Runnable aRunner, IBooleanState aEnaState, IBooleanState aCheckState ) {
    TsNullArgumentRtException.checkNulls( aDef, aRunner, aEnaState, aCheckState );
    if( aDef.isSeparator() ) {
      defineSeparator();
      return;
    }
    allActionDefs.add( aDef );
    actDefs.add( aDef );
    runMap.put( aDef.id(), aRunner );
    enaMap.put( aDef.id(), aEnaState );
    checkMap.put( aDef.id(), aCheckState );
  }

  /**
   * Adds the separator.
   */
  public void defineSeparator() {
    allActionDefs.add( ACDEF_SEPARATOR );
  }

  /**
   * Defines always unchecked action and adds to {@link #listHandledActionDefs()}.
   *
   * @param aDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - the action executor
   * @param aEnaState {@link IBooleanState} - determines if action is enables
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void defineAction( ITsActionDef aDef, Runnable aRunner, IBooleanState aEnaState ) {
    defineAction( aDef, aRunner, aEnaState, IBooleanState.ALWAY_FALSE );
  }

  /**
   * Defines always enabled and unchecked action and adds to {@link #listHandledActionDefs()}.
   *
   * @param aDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - the action executor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void defineAction( ITsActionDef aDef, Runnable aRunner ) {
    defineAction( aDef, aRunner, IBooleanState.ALWAY_TRUE, IBooleanState.ALWAY_FALSE );
  }

}
