package org.toxsoft.core.tslib.bricks.wub;

import static org.toxsoft.core.tslib.bricks.wub.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.wub.IWubConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IWubBox} implementation.
 *
 * @author hazard157
 */
public final class WubBox
    extends AbstractWubUnit
    implements IWubBox {

  private final IStridablesListEdit<AbstractWubUnit> unitsList = new StridablesList<>();
  private final IStringMapEdit<WubUnitStatistics>    unitStats = new StringMap<>();

  private final IStringMapEdit<Long> unitQueryStopTimeMsecs = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aId String - the box unit ID (an IDpath)
   * @param aParame {@link IOptionSet} - unit creation parameters including name and description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public WubBox( String aId, IOptionSet aParame ) {
    super( aId, aParame );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Queries to stop the unit and updates {@link #unitQueryStopTimeMsecs} if not stopped immediately.
   * <p>
   * Already stopped or stopping units are ignored.
   *
   * @param aUnitId String - ID of unit to stop
   * @param aTimeMsecs long - current time in milliseconds
   * @return boolean - <code>true</code> if unit was stopped immediately and it has to be removed
   */
  private boolean internalQueryStopUnit( String aUnitId, long aTimeMsecs ) {
    AbstractWubUnit unit = unitsList.getByKey( aUnitId );
    if( unit.state().isSameOrAfter( EWubUnitState.STOP_QUERIED ) ) {
      return false;
    }
    boolean wasStopped = unit.queryStop();
    if( !wasStopped ) { // unit is stopping, state will be monitored in internalCheckUnitsStopping()
      unitQueryStopTimeMsecs.put( aUnitId, Long.valueOf( aTimeMsecs ) );
    }
    return wasStopped;
  }

  /**
   * Removes unit this the internal lists.
   *
   * @param aUnitId String - ID of unit to remove
   */
  private void internalRemoveUnit( String aUnitId ) {
    AbstractWubUnit unit = unitsList.findByKey( aUnitId );
    if( unit != null ) {
      unitsList.removeById( aUnitId );
      unitStats.removeByKey( aUnitId );
      unitQueryStopTimeMsecs.removeByKey( aUnitId );
    }
  }

  /**
   * Checks unit for stopped state or stopping timeout and removes stopped units from the box.
   * <p>
   * Stopping units with timeout are destroyed and removed.
   */
  private void internalCheckUnitsStopping() {
    if( unitQueryStopTimeMsecs.isEmpty() ) { // no units are stopping now
      return;
    }
    // check stopping and stopped units
    long time = System.currentTimeMillis();
    long timeoutMsecs = OPDEF_UNIT_STOPPING_TIMEOUT_MSECS.getValue( params() ).asLong();
    IStringListEdit toRemove = new StringArrayList();
    for( IWubUnit u : unitsList ) {
      if( u.state() == EWubUnitState.STOPPED ) {
        toRemove.add( u.id() );
        continue;
      }
      if( u.state() == EWubUnitState.STOP_QUERIED ) {
        long passedTime = time - unitQueryStopTimeMsecs.getByKey( u.id() ).longValue();
        if( passedTime > timeoutMsecs ) {
          u.destroy();
          toRemove.add( u.id() );
        }
      }
    }
    for( String unitId : toRemove ) {
      internalRemoveUnit( unitId );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractWubUnit
  //

  @Override
  protected ValidationResult doInit( ITsContextRo aEnviron ) {
    ValidationResult result = ValidationResult.SUCCESS;
    IStringListEdit toRemove = new StringArrayList();
    // initialize units
    long time = System.currentTimeMillis();
    long nanos = System.nanoTime();
    for( AbstractWubUnit u : unitsList ) {
      ValidationResult vr = u.init( aEnviron );
      if( !vr.isError() ) {
        unitStats.getByKey( u.id() ).setStartTime( time, nanos );
      }
      else {
        toRemove.add( u.id() );
      }
    }
    // remove failed units
    if( !toRemove.isEmpty() ) {
      for( String unitId : toRemove ) {
        internalRemoveUnit( unitId );
      }
      if( unitsList.isEmpty() ) { // all units failed to init - consider as error
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
    for( IWubUnit u : unitsList ) {
      u.start();
    }
  }

  @Override
  protected void doDoJob() {
    for( IWubUnit u : unitsList ) {
      u.doJob();
    }
    internalCheckUnitsStopping();
  }

  @Override
  protected boolean doQueryStop() {
    long time = System.currentTimeMillis();
    for( String unitId : unitsList.keys() ) {
      internalQueryStopUnit( unitId, time );
    }
    internalCheckUnitsStopping();
    return unitsList.isEmpty();
  }

  @Override
  protected boolean doStopping() {
    // invoke all units
    for( IWubUnit u : unitsList ) {
      if( u.state() == EWubUnitState.STOP_QUERIED ) {
        u.doJob();
      }
    }
    internalCheckUnitsStopping();
    return unitsList.isEmpty();
  }

  @Override
  protected void doDestroy() {
    for( IWubUnit u : unitsList ) {
      if( u.state() != EWubUnitState.STOPPED ) {
        u.destroy();
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IWubBox
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IStridablesList<IWubUnit> unitsList() {
    return (IStridablesList)unitsList;
  }

  @Override
  public IWubUnitStatistics getUnitStatistics( String aUnitId ) {
    return unitStats.getByKey( aUnitId ).createCopy();
  }

  @Override
  public ValidationResult addUnit( AbstractWubUnit aUnit ) {
    TsNullArgumentRtException.checkNull( aUnit );
    TsIllegalArgumentRtException.checkTrue( aUnit.state() != EWubUnitState.CREATED );
    TsIllegalArgumentRtException.checkTrue( unitsList.hasKey( aUnit.id() ) );
    switch( state() ) {
      case CREATED: {
        unitsList.add( aUnit );
        unitStats.put( aUnit.id(), new WubUnitStatistics() );
        return ValidationResult.SUCCESS;
      }
      case INITED: {
        ValidationResult vr = aUnit.init( environ() );
        if( !vr.isError() ) {
          unitsList.add( aUnit );
          WubUnitStatistics statInfo = new WubUnitStatistics();
          unitStats.put( aUnit.id(), statInfo );
          long time = System.currentTimeMillis();
          long nanos = System.nanoTime();
          aUnit.start();
          statInfo.setStartTime( time, nanos );
        }
        return vr;
      }
      case STARTED: {
        ValidationResult vr = aUnit.init( environ() );
        if( !vr.isError() ) {
          unitsList.add( aUnit );
          WubUnitStatistics statInfo = new WubUnitStatistics();
          unitStats.put( aUnit.id(), statInfo );
          long time = System.currentTimeMillis();
          long nanos = System.nanoTime();
          aUnit.start();
          statInfo.setStartTime( time, nanos );
        }
        return vr;
      }
      case STOP_QUERIED:
      case STOPPED: { // do nothing
        return ValidationResult.warn( FMT_WARN_UNIT_NOT_ADDED, aUnit.id() );
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public void removeUnit( String aUnitId ) {
    AbstractWubUnit unit = unitsList.findByKey( aUnitId );
    if( unit == null ) {
      return;
    }
    if( unit.state().isSameOrAfter( EWubUnitState.STOP_QUERIED ) ) { // unit is already stopped or stopping now
      return;
    }
    if( internalQueryStopUnit( aUnitId, System.currentTimeMillis() ) ) {
      internalRemoveUnit( aUnitId );
    }
  }

}
