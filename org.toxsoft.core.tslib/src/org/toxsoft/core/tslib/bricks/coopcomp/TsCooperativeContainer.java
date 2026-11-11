package org.toxsoft.core.tslib.bricks.coopcomp;

import static org.toxsoft.core.tslib.bricks.coopcomp.ITsCoopCompConstants.*;
import static org.toxsoft.core.tslib.bricks.coopcomp.ITsResources.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCooperativeContainer} implementation.
 *
 * @author mvk
 */
public class TsCooperativeContainer
    extends AbstractTsStridableComponent
    implements ITsCooperativeContainer {

  private final ITsCollectionEdit<AbstractTsCooperativeComponent> componentsStorage;

  private final IMapEdit<AbstractTsCooperativeComponent, Long> unitQueryStopTimeMsecs = new ElemMap<>();

  /**
   * Constructor.
   *
   * @param aId String - entity ID (an IDpath)
   * @param aName String - short name
   * @param aDescription String - description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsCooperativeContainer( String aId, String aName, String aDescription ) {
    super( new Stridable( aId, aName, aDescription ) );
    componentsStorage = new ElemLinkedList<>();
  }

  /**
   * Constructor.
   *
   * @param aId String - entity ID (an IDpath)
   * @param aName String - short name
   * @param aDescription String - description
   * @param aStorage {@link ITsCollectionEdit} component storage
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsCooperativeContainer( String aId, String aName, String aDescription,
      ITsCollectionEdit<AbstractTsCooperativeComponent> aStorage ) {
    super( new Stridable( aId, aName, aDescription ) );
    componentsStorage = aStorage;
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsCooperativeComponent
  //

  @Override
  protected ValidationResult doInit( ITsContextRo aEnviron ) {
    ValidationResult result = ValidationResult.SUCCESS;
    IListEdit<AbstractTsCooperativeComponent> toRemove = new ElemLinkedList<>();
    // initialize componentsStorage
    for( AbstractTsCooperativeComponent c : componentsStorage ) {
      ValidationResult vr = c.init( aEnviron );
      if( vr.isError() ) {
        toRemove.add( c );
      }
    }
    // remove failed units
    if( !toRemove.isEmpty() ) {
      for( AbstractTsCooperativeComponent c : toRemove ) {
        internalRemoveComponent( c );
      }
      if( componentsStorage.isEmpty() ) { // all units failed to init - consider as error
        result = ValidationResult.error( FMT_ERR_ALL_UNITS_INIT_FAILED, Integer.valueOf( toRemove.size() ) );
      }
      else { // only some units failed - consider as warning
        result = ValidationResult.warn( FMT_WARN_SOME_UNITS_INIT_FAILED, toRemove.toString() );
      }
    }
    return result;
  }

  @Override
  protected void doStart() {
    for( AbstractTsCooperativeComponent c : new ElemArrayList<>( componentsStorage ) ) {
      c.start();
    }
  }

  @Override
  protected void doDoJob() {
    for( AbstractTsCooperativeComponent c : new ElemArrayList<>( componentsStorage ) ) {
      c.doJob();
    }
    internalCheckUnitsStopping();
  }

  @Override
  protected boolean doQueryStop() {
    long time = System.currentTimeMillis();
    for( AbstractTsCooperativeComponent c : new ElemArrayList<>( componentsStorage ) ) {
      internalQueryStopUnit( c, time );
    }
    internalCheckUnitsStopping();
    return componentsStorage.isEmpty();
  }

  @Override
  protected boolean doStopping() {
    // invoke all componentsStorage
    for( AbstractTsCooperativeComponent c : new ElemArrayList<>( componentsStorage ) ) {
      if( c.compState() == ETsCoopCompState.STOPPING ) {
        c.doJob();
      }
    }
    internalCheckUnitsStopping();
    return componentsStorage.isEmpty();
  }

  @Override
  protected void doDestroy() {
    for( AbstractTsCooperativeComponent c : new ElemArrayList<>( componentsStorage ) ) {
      if( c.compState() != ETsCoopCompState.DESTROYED ) {
        c.destroy();
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCooperativeContainer
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsCooperativeComponent> componentList() {
    return (IStridablesList)componentsStorage;
  }

  @Override
  public ValidationResult addComponent( AbstractTsCooperativeComponent aComponent ) {
    TsNullArgumentRtException.checkNull( aComponent );
    TsIllegalArgumentRtException.checkTrue( aComponent.compState() != ETsCoopCompState.CREATED );
    TsIllegalArgumentRtException.checkTrue( componentsStorage.hasElem( aComponent ) );
    switch( compState() ) {
      case CREATED: {
        componentsStorage.add( aComponent );
        return ValidationResult.SUCCESS;
      }
      case INITIALIZED: {
        ValidationResult vr = aComponent.init( compInitArgs() );
        if( !vr.isError() ) {
          componentsStorage.add( aComponent );
          aComponent.start();
        }
        return vr;
      }
      case WORKING: {
        ValidationResult vr = aComponent.init( compInitArgs() );
        if( !vr.isError() ) {
          componentsStorage.add( aComponent );
          aComponent.start();
        }
        return vr;
      }
      case STOPPING:
      case DESTROYED: { // do nothing
        return ValidationResult.warn( FMT_WARN_UNIT_NOT_ADDED, aComponent.toString() );
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public void removeComponent( AbstractTsCooperativeComponent aComponent ) {
    TsNullArgumentRtException.checkNull( aComponent );
    if( aComponent.compState().isSameOrAfter( ETsCoopCompState.STOPPING ) ) { // unit is already stopped or stopping now
      return;
    }
    if( internalQueryStopUnit( aComponent, System.currentTimeMillis() ) ) {
      internalRemoveComponent( aComponent );
    }
  }

  @Override
  void internalSetToFinalState() {
    // the container as a component is always ready for reuse
    compState = ETsCoopCompState.INITIALIZED;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Queries to stop the unit and updates {@link #unitQueryStopTimeMsecs} if not stopped immediately.
   * <p>
   * Already stopped or stopping units are ignored.
   *
   * @param aComponent AbstractTsCooperativeComponent - component
   * @param aTimeMsecs long - current time in milliseconds
   * @return boolean - <code>true</code> if unit was stopped immediately and it has to be removed
   */
  private boolean internalQueryStopUnit( AbstractTsCooperativeComponent aComponent, long aTimeMsecs ) {
    if( aComponent.compState().isSameOrAfter( ETsCoopCompState.STOPPING ) ) {
      return false;
    }
    boolean wasStopped = aComponent.queryStop();
    if( !wasStopped ) {
      long currTime = System.currentTimeMillis();
      unitQueryStopTimeMsecs.put( aComponent, Long.valueOf( currTime ) );
    }
    return wasStopped;
  }

  /**
   * Removes unit this the internal lists.
   *
   * @param aComponent AbstractTsCooperativeComponent component
   */
  private void internalRemoveComponent( AbstractTsCooperativeComponent aComponent ) {
    componentsStorage.remove( aComponent );
  }

  /**
   * Checks unit for stopped state or stopping timeout and removes stopped units from the box.
   * <p>
   * Stopping units with timeout are destroyed and removed.
   */
  private void internalCheckUnitsStopping() {
    // check stopping and stopped units
    long time = System.currentTimeMillis();
    long timeoutMsecs = OPDEF_COMPONENT_STOPPING_TIMEOUT_MSECS.getValue( compInitArgs().params() ).asLong();
    IListEdit<AbstractTsCooperativeComponent> toRemove = new ElemLinkedList<>();
    for( AbstractTsCooperativeComponent c : componentsStorage ) {
      if( c.compState() == ETsCoopCompState.DESTROYED ) {
        c.destroy();
        toRemove.add( c );
        continue;
      }
      if( c.compState() == ETsCoopCompState.STOPPING ) {
        long passedTime = time - unitQueryStopTimeMsecs.getByKey( c ).longValue();
        if( passedTime > timeoutMsecs ) {
          c.destroy();
          toRemove.add( c );
        }
      }
    }
    for( AbstractTsCooperativeComponent c : toRemove ) {
      internalRemoveComponent( c );
    }
  }
}
