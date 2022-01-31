package org.toxsoft.core.tslib.av.temporal;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.time.impl.TemporalValueBase;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Неизменяемая реализация {@link ITemporalAtomicValue}.
 * 
 * @author hazard157
 */
public final class TemporalAtomicValue
    extends TemporalValueBase<IAtomicValue>
    implements ITemporalAtomicValue {

  private static final long serialVersionUID = 157157L;

  /**
   * Конструктор.
   * 
   * @param aTimestamp long - момент времени
   * @param aValue {@link IAtomicValue} - значение
   * @throws TsNullArgumentRtException aValue = null
   */
  public TemporalAtomicValue( long aTimestamp, IAtomicValue aValue ) {
    super( aTimestamp, TsNullArgumentRtException.checkNull( aValue ) );
  }

}
