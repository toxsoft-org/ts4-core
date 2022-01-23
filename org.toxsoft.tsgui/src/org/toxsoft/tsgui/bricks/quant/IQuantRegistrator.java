package org.toxsoft.tsgui.bricks.quant;

import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Quant registration interface - any unit with initialization and finalization handling.
 *
 * @author hazard157
 */
public interface IQuantRegistrator {

  /**
   * Добавляет дочерний квант.
   * <p>
   * Доерние кванты инициализируются после родительского в порядке их регистрации и заврываются в обратном порядке, до
   * завершения родительского кванта.
   *
   * @param aQuant {@link IQuant} - регистрируемый квант
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsIllegalStateRtException попытка добавить квант после инициализации
   */
  void registerQuant( IQuant aQuant );

}
