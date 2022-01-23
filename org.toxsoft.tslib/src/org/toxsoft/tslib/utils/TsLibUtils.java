package org.toxsoft.tslib.utils;

import org.toxsoft.tslib.av.IAtomicValue;

/**
 * General purpose utility constants and methods.
 *
 * @author hazard157
 */
public final class TsLibUtils {

  /**
   * Simply the empty string.
   */
  public static final String EMPTY_STRING = ""; //$NON-NLS-1$

  /**
   * Empty immutable array of {@link Object} elements.
   */
  public static final Object[] EMPTY_ARRAY_OF_OBJECTS = {};

  /**
   * Empty immutable array of {@link String} elements.
   */
  public static final String[] EMPTY_ARRAY_OF_STRINGS = {};

  /**
   * Empty immutable array of <code>int</code> elements.
   */
  public static final int[] EMPTY_ARRAY_OF_INTS = {};

  /**
   * Empty immutable array of <code>long</code> elements.
   */
  public static final long[] EMPTY_ARRAY_OF_LONGS = {};

  /**
   * Empty immutable array of {@link Integer} elements.
   */
  public static final Integer[] EMPTY_ARRAY_OF_INT_OBJS = {};

  /**
   * Empty immutable array of {@link Long} elements.
   */
  public static final Long[] EMPTY_ARRAY_OF_LONG_OBJS = {};

  /**
   * Empty atomic values array.
   */
  public static final IAtomicValue[] EMPTY_AV_ARRAY = {};

  /**
   * Initial value for hash code calculation.
   * <p>
   * For usage description see {@link #PRIME}.
   *
   * @see #PRIME
   */
  public static final int INITIAL_HASH_CODE = 1;

  /**
   * Prime number to be used as multiplier when adding hash-code of next field of object.
   * <p>
   * Example of {@link Object#hashCode()} implementation:
   *
   * <pre>
   * ...
   * &#064;Override
   * public int hashCode() {
   *   int result = INITIAL_HASH_CODE;
   *   result = PRIME * result + <b>objectField</b>.hashCode();
   *   result = PRIME * result + (<b>booleanField</b> ? 1 : 0);
   *   result = PRIME * result + <b>intField</b>;
   *   result = PRIME * result + (int)(<b>longField</b> ^ (<b>longField</b> >>> 32));
   *   int fltval = Float.floatToRawIntBits( <b>floatField</b> );
   *   result = PRIME * result + fltval;
   *   long dblval = Double.doubleToRawLongBits( <b>doubleField</b> );
   *   result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
   *   result = PRIME * result + <b>stringField</b>.hashCode();
   *   return result;
   * }
   * </pre>
   */
  public static final int PRIME = 31;

  /**
   * Compares two {@link Comparable} references, possibly <code>null</code>.
   * <p>
   * If both references are non-null then returns {@link Comparable#compareTo(Object)}. Both <code>null</code> return 0.
   * Otherwise <code>null</code> reference is "less" than other and method returns +1 or -1.
   *
   * @param <V> - comparable type of the references {@link Comparable}
   * @param aThis &lt;V&gt; - first (left) object to compare
   * @param aThat &lt;V&gt; - second (right) object to compare
   * @return int - comparison result
   */
  public static <V extends Comparable<V>> int compare( V aThis, V aThat ) {
    if( aThis != null && aThat != null ) {
      return aThis.compareTo( aThat );
    }
    if( aThis != null ) {
      return +1;
    }
    if( aThat != null ) {
      return -1;
    }
    return 0;
  }

  /**
   * Prohibition of subclass creation.
   */
  private TsLibUtils() {
    // nop
  }

}
