package org.toxsoft.core.tslib.bricks.coopcomp;

import static org.toxsoft.core.tslib.bricks.coopcomp.ITsResources.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Possible state of the {@link ITsCooperativeComponent}.
 * <p>
 * Each state comment describes <b><i>allowed methods</i></b> meaning methods of {@link ITsCooperativeComponent} allowed
 * to be called in the state. Any other method, except {@link ITsCooperativeComponent#compState()} will throw an
 * exception {@link TsIllegalStateRtException}. <b>From state</b> and <b>To state</b> sections describe previous and
 * next possible states of the component.
 * <p>
 * Any implementation of this interface must be subclassed from {@link AbstractTsCoopCompMultiUse} or
 * {@link AbstractTsCoopCompSingleUse} allowing multiple or single start/stop sequences respectively.
 *
 * @author hazard157
 */
public enum ETsCoopCompState
    implements IStridable {

  /**
   * Initial state of the component, immediately after constructor.
   * <p>
   * <i>Allowed methods:</i>
   * <ul>
   * <li>{@link ITsCooperativeComponent#init(ITsContextRo) init()} - initializes to state {@link #INITIALIZED};</li>
   * <li>{@link ITsCooperativeComponent#destroy() destroy()} - kills to state {@link #DESTROYED}.</li>
   * </ul>
   * <p>
   * <i>From state(s):</i> ---<br>
   * <i>To state(s):</i> {@link #INITIALIZED}, {@link #DESTROYED}<br>
   */
  CREATED( "created", STR_CCS_CREATED, STR_CCS_CREATED_D ), //$NON-NLS-1$

  /**
   * Component was initialized with {@link ITsCooperativeComponent#init(ITsContextRo) init()}.
   * <p>
   * Initialized component is ready to start working. For multi-use components this method may be called several times
   * in sequence, however it does not seems to have much sense. Anyway, multi-use components allows to re-initialize
   * component with different arguments after stop.
   * <p>
   * <i>Allowed methods:</i>
   * <ul>
   * <li>{@link ITsCooperativeComponent#init(ITsContextRo) init()} - only for multi-use components;</li>
   * <li>{@link ITsCooperativeComponent#start() start()} - starts working in state {@link #WORKING};</li>
   * <li>{@link ITsCooperativeComponent#queryStop() queryStop()} - request component to stop working;</li>
   * <li>{@link ITsCooperativeComponent#isStopped() isStopped()} - determines if stopping has finished;</li>
   * <li>{@link ITsCooperativeComponent#destroy() destroy()} - kills to state {@link #DESTROYED}.</li>
   * </ul>
   * <p>
   * <i>From state(s):</i> {@link #CREATED}, {@link #STOPPING}<br>
   * <i>To state(s):</i> {@link #WORKING}, {@link #DESTROYED}<br>
   */
  INITIALIZED( "initialized", STR_CCS_INITIALIZED, STR_CCS_INITIALIZED_D ), //$NON-NLS-1$

  /**
   * Started by {@link ITsCooperativeComponent#start() start()} and working now.
   * <p>
   * This is the state the component was created for.
   * <p>
   * <i>Allowed methods:</i>
   * <ul>
   * <li>{@link ITsCooperativeComponent#doJob() doJob()} - mandatory to call for component to work;</li>
   * <li>{@link ITsCooperativeComponent#queryStop() queryStop()} - request component to stop working;</li>
   * <li>{@link ITsCooperativeComponent#destroy() destroy()} - kills to state {@link #DESTROYED}.</li>
   * </ul>
   * <p>
   * <i>From state(s):</i> {@link #INITIALIZED}<br>
   * <i>To state(s):</i> {@link #STOPPING}, {@link #DESTROYED}<br>
   */
  WORKING( "working", STR_CCS_WORKING, STR_CCS_WORKING_D ), //$NON-NLS-1$

  /**
   * {@link ITsCooperativeComponent#queryStop() queryStop()} was called and component is finishing work.
   * <p>
   * WHen component is stopping, container must continue to call {@link ITsCooperativeComponent#doJob() doJob()} for
   * component to proceed with stopping. If component has stopped may be determined by either
   * {@link ITsCooperativeComponent#isStopped() isStopped()} or better {@link ITsCooperativeComponent#compState()
   * componentState()}. After stop the component state is either {@link #DESTROYED} (single-use component) or
   * {@link #INITIALIZED} (for multi-use component).
   * <p>
   * <i>Allowed methods:</i>
   * <ul>
   * <li>{@link ITsCooperativeComponent#doJob() doJob()} - mandatory to call for component to finish stopping;</li>
   * <li>{@link ITsCooperativeComponent#isStopped() isStopped()} - determines if stopping has finished;</li>
   * <li>{@link ITsCooperativeComponent#destroy() destroy()} - kills to state {@link #DESTROYED}.</li>
   * </ul>
   * <p>
   * <i>From state(s):</i> {@link #WORKING}<br>
   * <i>To state(s):</i> {@link #INITIALIZED}, {@link #DESTROYED}<br>
   */
  STOPPING( "stopping", STR_CCS_STOPPING, STR_CCS_STOPPING_D ), //$NON-NLS-1$

  /**
   * {@link ITsCooperativeComponent#destroy() destroy()} was called and component becomes unusable.
   * <p>
   * <i>Allowed methods:</i>
   * <ul>
   * <li>{@link ITsCooperativeComponent#isStopped() isStopped()} - determines if stopping has finished.</li>
   * </ul>
   * <p>
   * <i>From state(s):</i> {@link #CREATED}, {@link #INITIALIZED}, {@link #WORKING}, {@link #STOPPING}<br>
   * <i>To state(s):</i> ---<br>
   */
  DESTROYED( "destroyed", STR_CCS_DESTROYED, STR_CCS_DESTROYED_D ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ETsCoopCompState"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ETsCoopCompState> KEEPER = new StridableEnumKeeper<>( ETsCoopCompState.class );

  private static IStridablesListEdit<ETsCoopCompState> list = null;

  private final String id;
  private final String name;
  private final String description;

  ETsCoopCompState( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsCoopCompState} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsCoopCompState> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsCoopCompState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsCoopCompState getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsCoopCompState} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsCoopCompState findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsCoopCompState item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsCoopCompState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsCoopCompState getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
