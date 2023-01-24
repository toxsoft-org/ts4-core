package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Atomic value of type {@link EAtomicType#VALOBJ} implementation.
 * <p>
 * There is two way of creating instances of this class:
 * <ul>
 * <li>for value-objects of registered keepers by the constructor {@link AvValobjImpl#AvValobjImpl(Object)}. In such a
 * case field {@link #valobj} is initalized while {@link #keeperId} and {@link #ktor} remains <code>null</code>;</li>
 * <li>for read KTOR textual representetion by the constructor {@link AvValobjImpl#AvValobjImpl(String, String)}. In
 * this case {@link #keeperId} and {@link #ktor} are initilized while {@link #valobj} remain <code>null</code>.</li>
 * </ul>
 * <p>
 * {@link Serializable} serialization is supported for any value-objects. This instance is serilized as KTRO
 * representation {@link String}.
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
   * Constructor creates instance from value-object (for {@link AvUtils#avValobj(Object)}.
   *
   * @param aValobj Object - the value-object with registered keeper
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  AvValobjImpl( Object aValobj ) {
    if( aValobj == null ) {
      throw new TsNullArgumentRtException();
    }
    // =====
    // argument checking is commented out for optimization purposes.
    // It's assumed that instance is created only for vale-objects with registered keepers
    // @formatter:off
//    if( TsValobjUtils.findKeeperByClass( aValobj.getClass() ) == null ) {
//      throw new TsIllegalArgumentRtException();
//    }
    // @formatter:on
    // =====
    valobj = aValobj;
    // ktor = null;
    // keeperId = null;
  }

  /**
   * Constructor creates instance from keeper ID and KTOR representation text.
   *
   * @param aKeeperId String - the keeper ID
   * @param aTextInBraces String - the KTRO representation in braces
   */
  AvValobjImpl( String aKeeperId, String aTextInBraces ) {
    // =====
    // argument checking is commented out for optimization purposes.
    // It is assumed that caller checks for KTOR validity
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
  // Serialization
  //

  private void writeObject( ObjectOutputStream aOut )
      throws IOException {
    aOut.defaultWriteObject();
    getKtor(); // initialize #ktor an #keeperId
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
  // package API
  //

  /**
   * Returns the value-object's full KTOR text representation.
   * <p>
   * Intensively used side effect ensures that {@link #ktor} and {@link #keeperId} fields are initialized.
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
   * Method for {@link StrioWriter} - writes full KTOR representation to the stream.
   *
   * @param aSw {@link IStrioWriter}
   */
  void write( IStrioWriter aSw ) {
    aSw.writeAsIs( getKtor() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractAtomicValue
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

  @Override
  public final EAtomicType atomicType() {
    return EAtomicType.VALOBJ;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Two VALOBJs equality check is kind of problem. If both values has {@link #ktor} or {@link #valobj} initialized -
   * that's easy. In other case we need to convert ethoer {@link #ktor} to {@link #valobj} or vise versa. However such
   * conversion need the appropriate keeper to be registered. In environments such as server this may not be the case.
   * TODO ???
   */
  @Override
  protected boolean internalEqualsValue( IAtomicValue aThat ) {
    if( aThat instanceof AvValobjImpl that ) {
      // check equality for #ktor fields if initilized in both instance
      if( this.ktor != null && that.ktor != null ) {
        return this.ktor.equals( that.ktor );
      }
      // check equality for #valobj fields if initilized in both instance
      if( this.valobj != null && that.valobj != null ) {
        return this.valobj.equals( that.valobj );
      }
      /**
       * Here we are when one instance has #valobj field initialized and other instance has #ktor and #keeperId fields.
       * <br>
       * We'll init unexisting #ktor field and compare KTORs. Other way of creating #valobj may not work in environments
       * when #kkeperId is not registered.
       */
      // ensure and check equality via #ktor fields
      return this.getKtor().equals( that.getKtor() );
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

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String asString() {
    if( ktor != null ) {
      return ktor;
    }
    return "@ " + valobj.toString(); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Extract value-object from atomic value as {@link Comparable} if possible.
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
    // this may happen only if other than AvValobjImpl or AvValobjNullImpl implementation exists
    throw new TsInternalErrorRtException();
  }

}
