package org.toxsoft.core.tslib.bricks.wub;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Diagnostics information about unit.
 * <p>
 * Note: diagnostics information {@link IWubUnitDiagnostics} is created and maintained by the WUB unit itself while
 * statistics information {@link IWubUnitStatistics} are created and maintained by the WUB box.
 *
 * @author hazard157
 */
public interface IWubUnitDiagnostics {

  /**
   * Returns current values of the unit diagnostics variables.
   *
   * @return {@link IOptionSet} - unit diagnostics variables.
   */
  IOptionSet diagnosticsVariablesValues();

  // TODO more diagnostics API has to be developed !!!

}
