package org.toxsoft.core.tslib.math;

import static org.toxsoft.core.tslib.math.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Min-max int values immutable representation.
 *
 * @author hazard157
 */
public final class IntRange
    implements Comparable<IntRange>, Serializable {

  private static final long serialVersionUID = -1466139623166878198L;

  /**
   * Full range of <code>int</code> integers.
   */
  public static final IntRange FULL = new IntRange( Integer.MIN_VALUE, Integer.MAX_VALUE );

  /**
   * Range (0,0).
   */
  public static final IntRange ZERO = new IntRange( 0, 0 );

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "IntRange"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IntRange> KEEPER =
      new AbstractEntityKeeper<>( IntRange.class, EEncloseMode.ENCLOSES_BASE_CLASS, ZERO ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IntRange aEntity ) {
          aSw.writeInt( aEntity.minValue() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.maxValue() );
        }

        @Override
        protected IntRange doRead( IStrioReader aSr ) {
          int minVal = aSr.readInt();
          aSr.ensureSeparatorChar();
          int maxVal = aSr.readInt();
          return new IntRange( minVal, maxVal );
        }
      };

  private ITsValidator<Integer>      validatorInteger = null; // lazy initialization
  private ITsValidator<IAtomicValue> validatorAv      = null; // lazy initialization

  final int minValue;
  final int maxValue;

  /**
   * Constructor.
   * <p>
   * Ends of the range may be equal.
   *
   * @param aMinValue int - lower end of the range
   * @param aMaxValue int - higher end of the range
   * @throws TsIllegalArgumentRtException max value is less then min value
   */
  public IntRange( int aMinValue, int aMaxValue ) {
    TsIllegalArgumentRtException.checkTrue( aMinValue > aMaxValue );
    minValue = aMinValue;
    maxValue = aMaxValue;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Return the lower end of the range.
   *
   * @return int - min value
   */
  public int minValue() {
    return minValue;
  }

  /**
   * Return the higher end of the range.
   *
   * @return int - max value
   */
  public int maxValue() {
    return maxValue;
  }

  /**
   * Returns the value "fitted" in this range.
   *
   * @param aValue int - the value
   * @return int - value always in this range
   */
  public int inRange( int aValue ) {
    if( aValue < minValue ) {
      return minValue;
    }
    if( aValue > maxValue ) {
      return maxValue;
    }
    return aValue;
  }

  /**
   * Returns the range width.
   * <p>
   * Minimal width, when {@link #minValue()} == {@link #maxValue()} is 1. Note that method throws an exception if
   * {@link #minValue()} == {@link Integer#MIN_VALUE} and {@link #maxValue()} == {@link Integer#MAX_VALUE}.
   *
   * @return int - the range width
   * @throws TsUnsupportedFeatureRtException this range is max available {@link Integer} range
   */
  public int width() {
    TsUnsupportedFeatureRtException.checkTrue( minValue == Integer.MIN_VALUE && maxValue == Integer.MAX_VALUE );
    return maxValue - minValue + 1;
  }

  /**
   * Calculates distance between value ang range.
   * <p>
   * Method returns:
   * <ul>
   * <li>0 - value is in range;</li>
   * <li><0 (negative number) - difference between <code>aValue</code> and {@link #minValue()};</li>
   * <li>>0 (positive number) - difference between <code>aValue</code> and {@link #maxValue()};</li>
   * </ul>
   *
   * @param aValue int - the value
   * @return int - distance between the value and the range
   */
  public int distance( int aValue ) {
    if( aValue > maxValue ) {
      return aValue - maxValue;
    }
    if( aValue < minValue ) {
      return aValue - minValue;
    }
    return 0;
  }

  /**
   * Returns the range "fitted" in this range.
   * <p>
   * If argument is totally outside of this range then returned range will have min and max values eqaul to
   * {@link #minValue()} or {@link #maxValue()} of this range.
   * <p>
   * If argument fits in this range the argument is returned.
   *
   * @param aRange {@link IntRange} - the range
   * @return {@link IntRange} - the range always inside of this range
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IntRange inRange( IntRange aRange ) {
    TsNullArgumentRtException.checkNull( aRange );
    if( aRange.minValue >= minValue && aRange.maxValue <= maxValue ) {
      return aRange;
    }
    int minv = inRange( aRange.minValue );
    int maxv = inRange( aRange.maxValue );
    return new IntRange( minv, maxv );
  }

  /**
   * Returns the {@link Integer} value validator checking out of range.
   *
   * @return {@link ITsValidator}&lt;{@link Integer}&gt; - {@link Integer} validator
   */
  public ITsValidator<Integer> validatorInteger() {
    if( validatorInteger == null ) {
      validatorInteger = aValue -> {
        TsNullArgumentRtException.checkNull( aValue );
        return this.validate( aValue.intValue() );
      };
    }
    return validatorInteger;
  }

  /**
   * Returns the {@link IAtomicValue} of type {@link EAtomicType#INTEGER} value validator checking out of range.
   *
   * @return {@link ITsValidator}&lt;{@link IAtomicValue}&gt; - {@link IntRange} validator
   */
  public ITsValidator<IAtomicValue> validatorAv() {
    if( validatorAv == null ) {
      validatorAv = aValue -> {
        TsNullArgumentRtException.checkNull( aValue );
        if( aValue.atomicType() != EAtomicType.INTEGER ) {
          return ValidationResult.error( FMT_ERR_NOT_EXPECTED_AT, aValue.atomicType().id(), EAtomicType.INTEGER.id() );
        }
        return this.validate( aValue.asInt() );
      };
    }
    return validatorAv;
  }

  /**
   * Validates the <code>int</code> value is in range.
   *
   * @param aValue int - the value
   * @return {@link ValidationResult} - the validation result
   */
  public ValidationResult validate( int aValue ) {
    if( aValue > maxValue ) {
      return ValidationResult.error( FMT_ERR_INT_GT_MAX, Integer.valueOf( aValue ), Integer.valueOf( minValue ),
          Integer.valueOf( maxValue ) );
    }
    if( aValue < minValue ) {
      return ValidationResult.error( FMT_ERR_INT_LT_MIN, Integer.valueOf( aValue ), Integer.valueOf( minValue ),
          Integer.valueOf( maxValue ) );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Validates named value against this range.
   * <p>
   * Value name is used to create error string like "'aValueName': value %.2f is out of allowed range %s".
   * <p>
   * If value is out of range or is not finite then returns {@link EValidationResultType#ERROR ERROR}.
   *
   * @param aValue int - the value
   * @param aValueName String - name of the value used for message formatting
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValidationResult validateError( int aValue, String aValueName ) {
    TsNullArgumentRtException.checkNull( aValueName );
    if( aValue > maxValue ) {
      return ValidationResult.error( FMT_INV_NAMED_INT_GT_MAX, aValueName, Integer.valueOf( aValue ),
          Integer.valueOf( minValue ), Integer.valueOf( maxValue ) );
    }
    if( aValue < minValue ) {
      return ValidationResult.error( FMT_INV_NAMED_INT_LT_MIN, aValueName, Integer.valueOf( aValue ),
          Integer.valueOf( minValue ), Integer.valueOf( maxValue ) );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Validates named value against this range.
   * <p>
   * Value name is used to create error string like "'aValueName': value %.2f is out of allowed range %s".
   * <p>
   * If value is out of range returns {@link EValidationResultType#WARNING} and returns
   * {@link EValidationResultType#WARNING} if value is not finite.
   *
   * @param aValue int - the value
   * @param aValueName String - name of the value used for message formatting
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValidationResult validateWarning( int aValue, String aValueName ) {
    TsNullArgumentRtException.checkNull( aValueName );
    if( aValue > maxValue ) {
      return ValidationResult.warn( FMT_INV_NAMED_INT_GT_MAX, aValueName, Integer.valueOf( aValue ),
          Integer.valueOf( minValue ), Integer.valueOf( maxValue ) );
    }
    if( aValue < minValue ) {
      return ValidationResult.warn( FMT_INV_NAMED_INT_LT_MIN, aValueName, Integer.valueOf( aValue ),
          Integer.valueOf( minValue ), Integer.valueOf( maxValue ) );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Throws an exception if {@link #validate(int)} method fails.
   *
   * @param aValue int - the value
   * @return int - results the argument
   * @throws TsValidationFailedRtException if {@link #validate(int)} returns {@link EValidationResultType#ERROR}
   */
  public int checkInRange( int aValue ) {
    TsValidationFailedRtException.checkError( validate( aValue ) );
    return aValue;
  }

  /**
   * Checks if value is in range.
   *
   * @param aValue int - the value
   * @return boolean - <code>true</code> if value is in range
   */
  public boolean isInRange( int aValue ) {
    return aValue >= minValue && aValue <= maxValue;
  }

  /**
   * Determines if value is less than {@link #minValue()}.
   *
   * @param aValue int - the value to check
   * @return boolean - <code>true</code> when aValue < {@link #minValue()}
   */
  public boolean isLeft( int aValue ) {
    return aValue < minValue;
  }

  /**
   * Determines if value is greater than {@link #maxValue()}.
   *
   * @param aValue int - the value to check
   * @return boolean - <code>true</code> when aValue > {@link #maxValue()}
   */
  public boolean isRight( int aValue ) {
    return aValue > maxValue;
  }

  /**
   * Determines if this range intersects with the specified range.
   *
   * @param aRange {@link IntRange} - range to be checked for intersection
   * @return boolean - <code>true</code> if at least one value is in both ranges
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean intersects( IntRange aRange ) {
    TsNullArgumentRtException.checkNull( aRange );
    return ((aRange.minValue <= this.maxValue) && (aRange.maxValue >= this.minValue));
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "(%d...%d)", minValue, maxValue ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IntRange that ) {
      return this.minValue == that.minValue && this.maxValue == that.maxValue;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + minValue;
    result = TsLibUtils.PRIME * result + maxValue;
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( IntRange aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    int c = Integer.compare( minValue, aThat.minValue );
    if( c == 0 ) {
      c = Integer.compare( maxValue, aThat.maxValue );
    }
    return c;
  }

}
