package org.toxsoft.core.tslib.av.temporal;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.time.ITemporalValue;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Атомарное значение в конкретный момент времени.
 * <p>
 * Реализует интерфейс {@link Comparable}&lt;{@link ITemporalAtomicValue}&gt; сортируя по возрастанию
 * {@link #timestamp()}.
 * <p>
 * <b>Внимание:</b> интерфейс не предназначен для раелизации пользователями. Единственная применимая реализация - final
 * класс {@link TemporalAtomicValue}. Тем не менее, интерфейс объявлен как для самодокументируемости понятия "значение в
 * момент времени", так и для использования константы {@link #NULL}, реализованной специальным внутренным классом.
 *
 * @author hazard157
 */
public interface ITemporalAtomicValue
    extends ITemporalValue<IAtomicValue> {

  /**
   * "Нулевое" (остутствующее, не определенное ни в какой момент времени) значение.
   * <p>
   * Фактически, эта константа может, и должна использоваться вместо null. Все методы интерфейса
   * {@link ITemporalAtomicValue} этого экземпляра выбрасывают исключение {@link TsNullObjectErrorRtException}. Если
   * есть подозрение, что полученная ссылка на {@link ITemporalAtomicValue} может быть не опеределена, то сначала надо
   * проверить ссылку простым сравнением <code>interval != {@link ITemporalAtomicValue#NULL}</code>.
   * <p>
   * Безопасано можно использовать Object-методы этого класса {@link #equals(Object)}, {@link #hashCode()},
   * {@link #toString()}, а также {@link Comparable#compareTo(Object)}.
   */
  ITemporalAtomicValue NULL = new InternalNullTemporalAtomicValue();

  /**
   * Возвращает значение в момент времени {@link #timestamp()}.
   *
   * @return {@link IAtomicValue} - значение в заданный момент времени
   */
  @Override
  IAtomicValue value();

}

class InternalNullTemporalAtomicValue
    implements ITemporalAtomicValue, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Метод корректно восстанавливает сериализированный {@link ITemporalAtomicValue#NULL}.
   *
   * @return Object объект {@link ITemporalAtomicValue#NULL}
   * @throws ObjectStreamException это обявление, оно тут не выбрасывается
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITemporalAtomicValue.NULL;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return ITemporalAtomicValue.class.getSimpleName() + ".NULL"; //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) { // отсекаем также и NULL
      return true;
    }
    if( aObj instanceof ITemporalAtomicValue ) {
      ITemporalAtomicValue that = (ITemporalAtomicValue)aObj;
      return that.timestamp() == this.timestamp() && that.value().equals( this.value() );
    }
    return false;
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
    // объект этого класса один - ITemporalValue.NULL. Равен только самому себе, меньше всех остальных
    return aThat == this ? 0 : -1;
  }

  // ------------------------------------------------------------------------------------
  // ITemporalValue
  //

  @Override
  public long timestamp() {
    throw new TsNullObjectErrorRtException( InternalNullTemporalAtomicValue.class );
  }

  @Override
  public IAtomicValue value() {
    throw new TsNullObjectErrorRtException( InternalNullTemporalAtomicValue.class );
  }

}
