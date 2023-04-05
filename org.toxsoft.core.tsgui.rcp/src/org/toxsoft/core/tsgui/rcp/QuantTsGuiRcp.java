package org.toxsoft.core.tsgui.rcp;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.rcp.valed.*;
import org.toxsoft.core.tsgui.valed.api.*;

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
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IValedControlFactoriesRegistry vcfReg = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfReg.registerFactory( ValedFile.FACTORY );
    vcfReg.registerFactory( ValedAvStringFile.FACTORY );
    vcfReg.registerFactory( ValedAvValobjFile.FACTORY );
  }

}
