package org.toxsoft.core.tslib.bricks.wub;

import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;

/**
 * {@link IWubUnitDiagnostics} editable implementation.
 *
 * @author hazard157
 */
public class WubUnitDiagnostics
    implements IWubUnitDiagnostics {

  private final IOptionSetEdit varValues = new OptionSet();

  /**
   * Constructor.
   */
  public WubUnitDiagnostics() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IWubUnitDiagnostics
  //

  @Override
  public IOptionSetEdit diagnosticsVariablesValues() {
    return varValues;
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  IWubUnitDiagnostics createCopy() {
    WubUnitDiagnostics diagInfo = new WubUnitDiagnostics();
    diagInfo.diagnosticsVariablesValues().setAll( varValues );
    return diagInfo;
  }

}
