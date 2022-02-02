package org.toxsoft.core.tslib.av.impl;

import java.io.Serializable;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.errors.AvTypeCastRtException;
import org.toxsoft.core.tslib.utils.TsLibUtils;

/**
 * Basic implementation of the {@link IAtomicValue}.
 *
 * @author hazard157
 */
public abstract  class AbstractAtomicValue
    implements IAtomicValue, Serializable {

  private static final long serialVersionUID = 158158L;

  // ------------------------------------------------------------------------------------
  // Interface IAtomicValue
  //

  @Override
  public abstract EAtomicType atomicType();

  @Override
  public boolean isAssigned() {
    return true;
  }

  @Override
  public boolean asBool() {
    throw new AvTypeCastRtException();
  }

  @Override
  public int asInt() {
    throw new AvTypeCastRtException();
  }

  @Override
  public long asLong() {
    throw new AvTypeCastRtException();
  }

  @Override
  public float asFloat() {
    throw new AvTypeCastRtException();
  }

  @Override
  public double asDouble() {
    throw new AvTypeCastRtException();
  }

  @Override
  public abstract String asString();

  @Override
  public <T> T asValobj() {
    throw new AvTypeCastRtException();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return asString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    // equality check will be performed on IAtomicValue because tslib does not creates IAtomicValue instances
    if( aThat instanceof IAtomicValue that ) {
      if( atomicType() == that.atomicType() ) {
        return internalCompareValue( that ) == 0;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + atomicType().hashCode();
    result = TsLibUtils.PRIME * result + internalValueHashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( IAtomicValue aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    // different types will be sorted in the order of constants declaration in EAtomicType
    if( atomicType() != aThat.atomicType() ) {
      return atomicType().ordinal() - aThat.atomicType().ordinal();
    }
    // data type constraints does not affects on comparison!
    return internalCompareValue( aThat );
  }

  // ------------------------------------------------------------------------------------
  // abstract methods
  //

  /**
   * Descendant must compare this and <code>aThat</code> objects values.
   * <p>
   * Argument is never the same as this object so <code>aThat != this</code> condition is always true.
   *
   * @param aThat {@link IAtomicValue} - the object to be compared, has the same atomic type as this object
   * @return int - comparison result as stated in {@link Comparable#compareTo(Object)}
   */
  protected abstract int internalCompareValue( IAtomicValue aThat );

  /**
   * Descendant must return hash code oth it's value.
   *
   * @return int - hash code oth the value
   */
  protected abstract int internalValueHashCode();

}
