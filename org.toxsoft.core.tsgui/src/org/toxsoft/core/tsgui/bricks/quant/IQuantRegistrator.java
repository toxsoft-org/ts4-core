package org.toxsoft.core.tsgui.bricks.quant;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Quant registration interface - any unit with initialization and finalization handling.
 *
 * @author hazard157
 */
public interface IQuantRegistrator {

  /**
   * Adds the child quant/
   * <p>
   * Child quants are initialized after parent one and closed before parent.
   *
   * @param aQuant {@link IQuant} - quant to be registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void registerQuant( IQuant aQuant );

}
