package org.toxsoft.core.tslib.bricks.time.impl;

import java.io.Serializable;

import org.toxsoft.core.tslib.bricks.time.ITemporalValue;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Базовый класс для реализации наследников интерфейса {@link ITemporalValue}.
 *
 * @author hazard157
 * @param <E> - конкретный тип значения во времени
 */
public class TemporalValueBase<E>
    implements ITemporalValue<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final long timestamp;
  private final E    value;

  /**
   * Конструктор для наследников.
   * <p>
   * Конструктор не проверят aValue на null. Если (как должно быть чаще всего) наследник не допускает null, то он жлжен
   * делать проверку на null и выбрасывать исключение {@link TsNullArgumentRtException}.
   *
   * @param aTimestamp long - момент времени
   * @param aValue &lt;E&gt; - значение
   */
  protected TemporalValueBase( long aTimestamp, E aValue ) {
    timestamp = aTimestamp;
    value = aValue;
  }

  // ------------------------------------------------------------------------------------
  // ITemporalValue
  //

  @Override
  final public long timestamp() {
    return timestamp;
  }

  @Override
  final public E value() {
    return value;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return TimeUtils.timestampToString( timestamp ) + ' ' + (value == null ? "<<null>>" : value.toString());
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof TemporalValueBase ) {
      TemporalValueBase<?> that = (TemporalValueBase<?>)aObj;
      if( timestamp == that.timestamp ) {
        // если один из value null, то вернем true только когда ОБА null
        if( this.value == null || that.value == null ) {
          return this.value == that.value;
        }
        return this.value.equals( that.value );
      }
      return false;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + (int)(timestamp ^ (timestamp >>> 32));
    if( value != null ) {
      result = TsLibUtils.PRIME * result + value.hashCode();
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @SuppressWarnings( "rawtypes" )
  @Override
  public int compareTo( ITemporalValue aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return Long.compare( timestamp, aThat.timestamp() );
  }

}
