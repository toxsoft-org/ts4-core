package org.toxsoft.tsgui.rcp;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.tsgui.rcp.valed.ValedFile;
import org.toxsoft.tsgui.valed.api.IValedControlFactoriesRegistry;

/**
 * Library initialization quant.
 *
 * @author hazard157
 */
public class QuantTsGuiRcp
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantTsGuiRcp() {
    super( QuantTsGuiRcp.class.getSimpleName() );
    // HERE any Valobj registrations
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    IValedControlFactoriesRegistry vcfReg = aAppContext.get( IValedControlFactoriesRegistry.class );
    vcfReg.registerFactory( ValedFile.FACTORY );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
