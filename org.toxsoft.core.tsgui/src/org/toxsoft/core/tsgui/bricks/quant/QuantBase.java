package org.toxsoft.core.tsgui.bricks.quant;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Quant manager is default implementation of the {@link IQuant} with main goal to initialize child quants.
 *
 * @author hazard157
 */
public class QuantBase
    extends AbstractQuant {

  /**
   * Constructor.
   *
   * @param aQuantName String - non-blank quant name, must be uinque for all quants in application
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is blank string
   */
  public QuantBase( String aQuantName ) {
    super( aQuantName );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
