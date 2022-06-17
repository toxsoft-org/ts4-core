package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Atomic value of type {@link EAtomicType#VALOBJ} implementation.
 *
 * @author hazard157
 */
class AvValobjImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  private transient String ktor     = null;
  private transient String keeperId = null;
  private transient Object valobj   = null;

  /**
   * Конструктор из объекта-значения для для создания экземпляров методами {@link AvUtils}.
   * <p>
   * Конструктор инициализирует поле {@link #valobj} и оставляет поля {@link #keeperId} и {@link #ktor} равным
   * <code>null</code>, чтобы можно было создавать экземпляры без обязательной регистрации типа объекта-значения в
   * {@link TsValobjUtils};</li>
   *
   * @param aValobj Object - объект-значение,
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException aDataType не типа {@link EAtomicType#VALOBJ}
   */
  AvValobjImpl( Object aValobj ) {
    if( aValobj == null ) {
      throw new TsNullArgumentRtException();
    }
    valobj = aValobj;
    // ktor = null;
    // keeperId = null;
  }

  /**
   * Конструктор для читателся из текстового представления {@link StrioReader}.
   * <p>
   * Конструктор инициализирует поля {@link #keeperId} и {@link #ktor} и оставляет {@link #valobj} равным
   * <code>null</code>, чтобы можно было создавать экземпляры без обязательной регистрации типа объекта-значения в
   * {@link TsValobjUtils};</li>
   *
   * @param aKeeperId String - идентификатор (ИД-путь) хранителя
   * @param aTextInBraces String - текстовое представление, первый и последний символ - обрамляющие скобки
   */
  AvValobjImpl( String aKeeperId, String aTextInBraces ) {
    // =====
    // с целью оптимизации, проверка аргументов отключена, код DvReader гарантирует корректность аргументов
    // @formatter:off
//    StridUtils.checkValidIdPath( aKeeperId );
//    TsNullArgumentRtException.checkNull( aTextInBraces );
//    int len = aTextInBraces.length();
//    TsIllegalArgumentRtException.checkTrue( len >= 2 );
//    switch( aTextInBraces.charAt( 0 ) ) {
//      case CHAR_SET_BEGIN:
//        TsIllegalArgumentRtException.checkTrue( aTextInBraces.charAt( len - 1 ) != CHAR_SET_END );
//        break;
//      case CHAR_ARRAY_BEGIN:
//        TsIllegalArgumentRtException.checkTrue( aTextInBraces.charAt( len - 1 ) != CHAR_ARRAY_END );
//        break;
//      default:
//        throw new TsIllegalArgumentRtException();
//    }
    // @formatter:on
    // =====
    keeperId = aKeeperId;
    ktor = CHAR_VALOBJ_PREFIX + keeperId + aTextInBraces;
  }

  // ------------------------------------------------------------------------------------
  // Сериализация
  //

  private void writeObject( ObjectOutputStream aOut )
      throws IOException {
    aOut.defaultWriteObject();
    getKtor(); // убедимся, что ktor и keeperId инициализированы
    aOut.writeObject( ktor );
    aOut.writeObject( keeperId );
  }

  private void readObject( ObjectInputStream aIn )
      throws IOException,
      ClassNotFoundException {
    aIn.defaultReadObject();
    ktor = (String)aIn.readObject();
    keeperId = (String)aIn.readObject();
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Return the value-object's KTOR text representation.
   *
   * @return String the value-object's KTOR text representation
   * @throws TsRuntimeException the KTOR creation failed
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  String getKtor() {
    if( ktor == null ) {
      keeperId = TsValobjUtils.getKeeperIdByClass( valobj.getClass() );
      StringBuilder sb = new StringBuilder();
      IStrioWriter sw = new StrioWriter( new CharOutputStreamAppendable( sb ) );
      sw.writeChar( CHAR_VALOBJ_PREFIX );
      sw.writeAsIs( keeperId );
      IEntityKeeper keeper = TsValobjUtils.getKeeperById( keeperId );
      if( keeper.isEnclosed() ) {
        keeper.write( sw, valobj );
      }
      else {
        sw.writeChar( CHAR_SET_BEGIN );
        keeper.write( sw, valobj );
        sw.writeChar( CHAR_SET_END );
      }
      ktor = sb.toString();
    }
    return ktor;
  }

  /**
   * Метод для {@link StrioWriter} - записывает это значение в текстовое представление.
   *
   * @param aSw {@link IStrioWriter}
   */
  void write( IStrioWriter aSw ) {
    aSw.writeAsIs( getKtor() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IValueImporter
  //

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public <T> T asValobj() {
    if( valobj == null ) {
      // от ktor отсечем начальный "@keeperId" и получим текстовое представление с обрамляющими скобками
      String s = ktor.substring( keeperId.length() + 1 );
      IEntityKeeper keeper = TsValobjUtils.getKeeperById( keeperId );
      // создаем объект из текстового представления, с учетом того, кто добавил скобки: кипер или писатель в текст
      if( !keeper.isEnclosed() ) {
        s = s.substring( 1, s.length() - 1 );
      }
      valobj = keeper.str2ent( s );
    }
    return (T)valobj;
  }

  // ------------------------------------------------------------------------------------
  // AbstractAtomicValue methods
  //

  @Override
  public final EAtomicType atomicType() {
    return EAtomicType.VALOBJ;
  }

  @Override
  public String asString() {
    if( ktor != null ) {
      return ktor;
    }
    return "@ " + valobj.toString(); //$NON-NLS-1$
  }

  /**
   * Extract valoue-object from atomic value as {@link Comparable} if possible.
   *
   * @param aValue {@link IAtomicValue} - atomic value of {@link EAtomicType#VALOBJ} type
   * @return {@link Comparable} - comparable value-object or null
   */
  @SuppressWarnings( "rawtypes" )
  private static Comparable extractAsComparable( IAtomicValue aValue ) {
    // AvValobjImpl - returns non-null if valobj exists or may be created and it is Comparable
    if( aValue instanceof AvValobjImpl value ) {
      Object vo = value.valobj;
      if( vo == null && value.keeperId != null ) {
        if( TsValobjUtils.findKeeperById( value.keeperId ) != null ) {
          vo = aValue.asValobj();
        }
      }
      if( vo instanceof Comparable cvo ) {
        return cvo;
      }
      return null;
    }
    // AvValobjNullImpl impelemtation - always null
    if( aValue instanceof AvValobjNullImpl ) {
      return null;
    }
    // this may happen when other than AvValobjImpl or AvValobjNullImpl implementation will appear
    throw new TsInternalErrorRtException();
  }

  /**
   * {@inheritDoc} Two VALOBJs equality check is kind of problem. If both values has {@link #ktor} or {@link #valobj}
   * initialized - that's easy. In other case we need to convert ethoer {@link #ktor} to {@link #valobj} or vise versa.
   * However such conversion need the appropriate keeper to be registered. In environments such as server this may not
   * be the case. TODO ???
   */
  @Override
  protected boolean internalEqualsValue( IAtomicValue aThat ) {
    if( aThat instanceof AvValobjImpl that ) {
      // same kind of data in both objects, equality check is simple
      if( this.ktor != null && that.ktor != null ) {
        return this.ktor.equals( that.ktor );
      }
      if( this.valobj != null && that.valobj != null ) {
        return this.valobj.equals( that.valobj );
      }
      // different data, first, let's determine keeperIds
      String thisKeeperId = this.keeperId;
      if( thisKeeperId == null ) {
        this.keeperId = TsValobjUtils.findKeeperIdByClass( this.valobj.getClass() );
      }
      String thatKeeperId = that.keeperId;
      if( thatKeeperId == null ) {
        that.keeperId = TsValobjUtils.findKeeperIdByClass( that.valobj.getClass() );
      }
      // we don't knw about keepers - assume different
      if( thisKeeperId == null && thatKeeperId == null ) {
        return false;
      }
      // different keepers - objects are not equal
      if( !Objects.equals( thisKeeperId, thatKeeperId ) ) {
        return false;
      }
      // same keepers - we prefer to write ktor rather than build valobj
      return Objects.equals( this.getKtor(), that.getKtor() );
    }
    return false;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    Comparable o1 = extractAsComparable( this );
    Comparable o2 = extractAsComparable( aThat );
    if( o1 != null && o2 != null ) {
      if( o1.getClass().equals( o2.getClass() ) ) {
        return o1.compareTo( o2 );
      }
      // valobjs of different classes are considered as equals (as uncomparable values)
      return 0;
    }
    // both nulls are considered as equals (includes uncomparable values)
    if( o1 == null && o2 == null ) {
      return 0;
    }
    // null is considered less than any non-null valobj
    return (o1 == null) ? -1 : 1;
  }

  @Override
  protected int internalValueHashCode() {
    return getKtor().hashCode();
  }

}
