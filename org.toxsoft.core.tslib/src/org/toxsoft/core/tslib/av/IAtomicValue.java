package org.toxsoft.core.tslib.av;

import java.io.*;

import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The value of the specified atomic type.
 * <p>
 * This is lowest level interface, which already represents the realization of an abstraction "data value". Any method
 * asXxx() is a way to transfer the values of the real (red) world to the Java programming code (blue world).
 * <p>
 * Any two values may be compared to each other - this interface extends {@link Comparable}.
 * <p>
 * Note: this interface is not intended to be implemented by users. The only reason it is <b>not</b> declared as
 * <code>sealed interface</code> is that OSGi implementation does not allow to have <code>sealed</code> interface and
 * <code>permit</code> class in different packages.
 *
 * @author hazard157
 */
public interface IAtomicValue
    extends Comparable<IAtomicValue> {

  /**
   * Unassigned value.
   * <p>
   * This is the only instance of {@link IAtomicValue} with {@link #atomicType()} == {@link EAtomicType#NONE}.
   * <p>
   * All methods <code>asXxx()</code> (except of {@link #asString()}) throws an exception
   * {@link AvUnassignedValueRtException}.
   */
  IAtomicValue NULL = new InternalNoneAtomicValue();

  /**
   * Returns atomic type of this value.
   *
   * @return {@link EAtomicType} - the atomic type
   */
  EAtomicType atomicType();

  /**
   * Determines if implementation contains any value.
   * <p>
   * Reading from unassigned {@link IAtomicValue} throws an {@link AvUnassignedValueRtException}.
   * <p>
   * The only tslib implementation on {@link IAtomicValue} with unassigned value is singleton {@link IAtomicValue#NULL}.
   *
   * @return boolean - <code>true</code> if any kind of value exists in this object
   */
  boolean isAssigned();

  /**
   * Returns data as boolean value.
   *
   * @return boolean - boolean value
   * @throws AvTypeCastRtException value is not of requested type
   * @throws AvUnassignedValueRtException no value, {@link #isAssigned()} = <code>false</code>
   */
  boolean asBool();

  /**
   * Returns data as 32-bit integer value.
   *
   * @return int - 32-bit integer value
   * @throws AvTypeCastRtException value is not of requested type
   * @throws AvUnassignedValueRtException no value, {@link #isAssigned()} = <code>false</code>
   */
  int asInt();

  /**
   * Returns data as 64-bit integer value.
   *
   * @return long - 64-bit integer value
   * @throws AvTypeCastRtException value is not of requested type
   * @throws AvUnassignedValueRtException no value, {@link #isAssigned()} = <code>false</code>
   */
  long asLong();

  /**
   * Returns data as 32-bit floating point value.
   *
   * @return float - 32-bit floating value
   * @throws AvTypeCastRtException value is not of requested type
   * @throws AvUnassignedValueRtException no value, {@link #isAssigned()} = <code>false</code>
   */
  float asFloat();

  /**
   * Returns data as 64-bit floating point value.
   *
   * @return double - 64-bit floating value
   * @throws AvTypeCastRtException value is not of requested type
   * @throws AvUnassignedValueRtException no value, {@link #isAssigned()} = <code>false</code>
   */
  double asDouble();

  /**
   * Returns string representation of data value.
   *
   * @return String - string representation of data value, never is <code>null</code>
   */
  String asString();

  /**
   * Returns requested value-object representation of the data value.
   * <p>
   * For {@link EAtomicType#VALOBJ} returns stored value-object. For all other atomic types returns the respective
   * object (such as {@link IAtomicValue#NULL} for {@link EAtomicType#NONE}, {@link Boolean}, {@link Float},
   * {@link Double}, etc).
   *
   * @param <T> - expected type of the value-object
   * @return &lt;T&gt; - value-object, may be <code>null</code>
   * @throws AvTypeCastRtException value is not of requested type
   * @throws TsItemNotFoundRtException no keeper was registered for value-object class
   * @throws ClassCastException the value-object is not of expected type &lt;T&gt;
   */
  <T> T asValobj();

}

final class InternalNoneAtomicValue
    implements IAtomicValue, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Method correctly deserializes {@link IAtomicValue#NULL} value.
   *
   * @return {@link Object} - {@link IAtomicValue#NULL}
   * @throws ObjectStreamException is declared but newer thrown by this method
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IAtomicValue.NULL;
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.NONE;
  }

  @Override
  public boolean isAssigned() {
    return false;
  }

  @Override
  public boolean asBool() {
    throw new AvUnassignedValueRtException();
  }

  @Override
  public double asDouble() {
    throw new AvUnassignedValueRtException();
  }

  @Override
  public float asFloat() {
    throw new AvUnassignedValueRtException();
  }

  @Override
  public int asInt() {
    throw new AvUnassignedValueRtException();
  }

  @Override
  public long asLong() {
    throw new AvUnassignedValueRtException();
  }

  @Override
  public String asString() {
    return EAtomicType.NONE.id();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Object asValobj() {
    throw new AvUnassignedValueRtException();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public int hashCode() {
    return TsLibUtils.INITIAL_HASH_CODE;
  }

  @Override
  public boolean equals( Object obj ) {
    return obj == this;
  }

  @Override
  public String toString() {
    return "NONE"; //$NON-NLS-1$
  }

  @Override
  public int compareTo( IAtomicValue o ) {
    if( o == null ) {
      throw new NullPointerException();
    }
    if( o == this ) {
      return 0;
    }
    return -1; // this "value" is "less" than any other
  }

}
