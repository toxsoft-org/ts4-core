package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.CharOutputStreamAppendable;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioReader;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioWriter;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

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

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    // TODO is it possible to sort VALOBJs? how to do it?
    return 0;
  }

  @Override
  protected int internalValueHashCode() {
    return getKtor().hashCode();
  }

}
