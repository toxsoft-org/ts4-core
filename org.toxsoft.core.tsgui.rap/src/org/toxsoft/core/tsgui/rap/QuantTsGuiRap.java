package org.toxsoft.core.tsgui.rap;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;

/**
 * Library initialization quant.
 *
 * @author hazard157
 */
public class QuantTsGuiRap
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantTsGuiRap() {
    super( QuantTsGuiRap.class.getSimpleName() );
    // HERE any Valobj registrations
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // IValedControlFactoriesRegistry vcfReg = aWinContext.get( IValedControlFactoriesRegistry.class );
    // vcfReg.registerFactory( ValedFile.FACTORY );
    // vcfReg.registerFactory( ValedAvStringFile.FACTORY );
    // vcfReg.registerFactory( ValedAvValobjFile.FACTORY );
  }

}
