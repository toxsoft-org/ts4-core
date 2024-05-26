package org.toxsoft.core.tslib.bricks.wub;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The container manages lifecycle and work of the components {@link IWubUnit}.
 * <p>
 * Note: WUB box itself is a {@link IWubUnit}. As for common WUB unit, method {@link #params()} returns the container
 * creation parameters.
 *
 * @author hazard157
 */
public sealed interface IWubBox
    extends IWubUnit
    permits WubBox {

  /**
   * Returns the list of units contained in this box.
   * <p>
   * Returned list may may contain not all units added with {@link #addUnit(AbstractWubUnit)}. During initialization the
   * failed units are removed from the list.
   *
   * @return {@link IStridablesList}&lt;{@link IWubUnit}&gt; - the list of units
   */
  IStridablesList<IWubUnit> unitsList();

  /**
   * Returns the statistics of the unit.
   * <p>
   * Calling method before box is started may return senseless values.
   *
   * @param aUnitId String - the unit ID
   * @return {@link IWubUnitStatistics} - statistics information about unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException unit not found in box
   */
  IWubUnitStatistics getUnitStatistics( String aUnitId );

  /**
   * Add unit to the box, initializes and starts immediately.
   * <p>
   * The method brings the unit into compliance with the state of the box:
   * <ul>
   * <li>{@link EWubUnitState#CREATED} - adds unit to {@link #unitsList()};</li>
   * <li>{@link EWubUnitState#INITED} - initializes and on success adds unit to {@link #unitsList()};</li>
   * <li>{@link EWubUnitState#STARTED} - initializes and on success starts and adds unit to {@link #unitsList()};</li>
   * <li>{@link EWubUnitState#STOP_QUERIED} - method does nothing;</li>
   * <li>{@link EWubUnitState#STOPPED} - method does nothing.</li>
   * </ul>
   *
   * @param aUnit {@link AbstractWubUnit} - the unit to start
   * @return {@link ValidationResult} - result of initialization or {@link ValidationResult#SUCCESS}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument unit is not in state {@link EWubUnitState#CREATED}
   * @throws TsItemAlreadyExistsRtException unit with the same ID is already in the box
   */
  ValidationResult addUnit( AbstractWubUnit aUnit );

  /**
   * Stops and removes unit from the box.
   * <p>
   * Method request unit stop and returns immediately. Actual stopping and unit removal may take a long time.
   * <p>
   * Invoking method for already stopping units or units not contained in the box has no effect.
   *
   * @param aUnitId String - the ID of unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeUnit( String aUnitId );

  //
  //
  // TODO do we need the API below ???
  //
  // void pauseUnit( String aUnitId );
  // boolean isUnitPaused( String aUnitId );
  // void resumeUnit( String aUnitId );

}
