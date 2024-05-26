package org.toxsoft.core.tslib.bricks.wub;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * {@link IWubUnitStatistics} editable implementation.
 *
 * @author hazard157
 */
class WubUnitStatistics
    implements IWubUnitStatistics {

  private final IOptionSetEdit varValues = new OptionSet();

  private long startTime  = 0L;
  private long startNanos = Long.MIN_VALUE;

  /**
   * Constructor.
   */
  public WubUnitStatistics() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IWubUnitStatistics
  //

  @Override
  public long startTime() {
    return startTime;
  }

  @Override
  public long startTimeNanos() {
    return startNanos;
  }

  @Override
  public IOptionSetEdit statisticsVariablesValues() {
    return varValues;
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  void setStartTime( long aStartTime, long aStartNanos ) {
    startTime = aStartTime;
    startNanos = aStartNanos;
  }

  IWubUnitStatistics createCopy() {
    WubUnitStatistics statInfo = new WubUnitStatistics();
    statInfo.startTime = startTime;
    statInfo.startNanos = startNanos;
    statInfo.statisticsVariablesValues().setAll( varValues );
    return statInfo;
  }

}
