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
 * Min-max double values immutable representation.
 *
 * @author hazard157
 */
public final class DoubleRange
    implements Comparable<DoubleRange>, Serializable {

  private static final long serialVersionUID = -78548258896743718L;

  /**
   * Full range of <code>double</code> numbers.
   */
  public static final DoubleRange FULL = new DoubleRange( Double.MIN_VALUE, Double.MAX_VALUE );

  /**
   * Range (0.0,0.0).
   */
  public static final DoubleRange ZERO = new DoubleRange( 0.0, 0.0 );

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "DoubleRange"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<DoubleRange> KEEPER =
      new AbstractEntityKeeper<>( DoubleRange.class, EEncloseMode.ENCLOSES_BASE_CLASS, ZERO ) {

        @Override
        protected void doWrite( IStrioWriter aSw, DoubleRange aEntity ) {
          aSw.writeDouble( aEntity.minValue() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.maxValue() );
        }

        @Override
        protected DoubleRange doRead( IStrioReader aSr ) {
          double minVal = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double maxVal = aSr.readDouble();
          return new DoubleRange( minVal, maxVal );
        }
      };

  private ITsValidator<Double>       validatorDouble = null; // lazy initialization
  private ITsValidator<IAtomicValue> validatorAv     = null; // lazy initialization

  final double minValue;
  final double maxValue;

  /**
   * Constructor.
   * <p>
   * Ends of the range may be equal.
   * <p>
   * Only finite numbers are allowed!
   *
   * @param aMinValue double - lower end of the range
   * @param aMaxValue double - higher end of the range
   * @throws TsIllegalArgumentRtException max value is less then min value
   * @throws TsIllegalArgumentRtException for any argument {@link Double#isFinite(double)} returns <code>false</code>
   */
  public DoubleRange( double aMinValue, double aMaxValue ) {
    TsIllegalArgumentRtException.checkFalse( Double.isFinite( aMinValue ) );
    TsIllegalArgumentRtException.checkFalse( Double.isFinite( aMaxValue ) );
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
   * @return double - min value
   */
  public double minValue() {
    return minValue;
  }

  /**
   * Return the higher end of the range.
   *
   * @return double - max value
   */
  public double maxValue() {
    return maxValue;
  }

  /**
   * Returns the value "fitted" in range.
   *
   * @param aValue double - the value (may be non-finite)
   * @return double - value always in range
   */
  public double inRange( double aValue ) {
    if( Double.compare( aValue, minValue ) < 0 ) {
      return minValue;
    }
    if( Double.compare( aValue, maxValue ) > 0 ) {
      return maxValue;
    }
    return aValue;
  }

  /**
   * Returns the range width.
   * <p>
   * Minimal width, when {@link #minValue()} == {@link #maxValue()} is 0.0. Note that method throws an exception if
   * {@link #minValue()} == {@link Double#MIN_VALUE} and {@link #maxValue()} == {@link Double#MAX_VALUE}.
   *
   * @return double- the range width
   * @throws TsUnsupportedFeatureRtException this range is max available {@link Double} range
   */
  public double width() {
    TsUnsupportedFeatureRtException.checkTrue( minValue == Double.MIN_VALUE && maxValue == Double.MAX_VALUE );
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
   * @param aValue double - the value
   * @return double - distance between the value and the range
   * @throws TsIllegalArgumentRtException argument is non-finite
   */
  public double distance( double aValue ) {
    TsIllegalArgumentRtException.checkFalse( Double.isFinite( aValue ) );
    if( aValue > maxValue ) {
      return aValue - maxValue;
    }
    if( aValue < minValue ) {
      return aValue - minValue;
    }
    return 0.0;
  }

  /**
   * Returns the {@link Double} value validator checking out of range.
   *
   * @return {@link ITsValidator}&lt;{@link Double}&gt; - {@link Double} validator
   */
  public ITsValidator<Double> validatorDouble() {
    if( validatorDouble == null ) {
      validatorDouble = aValue -> {
        TsNullArgumentRtException.checkNull( aValue );
        return this.validate( aValue.doubleValue() );
      };
    }
    return validatorDouble;
  }

  /**
   * Returns the {@link IAtomicValue} of type {@link EAtomicType#INTEGER} value validator checking out of range.
   *
   * @return {@link ITsValidator}&lt;{@link IAtomicValue}&gt; - {@link IAtomicValue} validator
   */
  public ITsValidator<IAtomicValue> validatorAv() {
    if( validatorAv == null ) {
      validatorAv = aValue -> {
        TsNullArgumentRtException.checkNull( aValue );
        if( aValue.atomicType() != EAtomicType.FLOATING ) {
          return ValidationResult.error( FMT_ERR_NOT_EXPECTED_AT, aValue.atomicType().id(), EAtomicType.FLOATING.id() );
        }
        return this.validate( aValue.asDouble() );
      };
    }
    return validatorAv;
  }

  /**
   * Validates the <code>double</code> value is in range.
   * <p>
   * Performs following checks for error:
   * <ul>
   * <li>value must be finite number, {@link Double#isFinite(double)} == <code>true</code>;</li>
   * <li>value must be in range (inclusive) {@link #minValue()} .. {@link #maxValue()};</li>
   * </ul>
   *
   * @param aValue double - the value
   * @return {@link ValidationResult} - the validation result
   */
  public ValidationResult validate( double aValue ) {
    if( !Double.isFinite( aValue ) ) {
      return ValidationResult.error( FMT_ERR_DOUBLE_NOT_FINITE, Double.valueOf( aValue ) );
    }
    if( aValue > maxValue ) {
      return ValidationResult.error( FMT_ERR_DOUBLE_GT_MAX, Double.valueOf( aValue ), Double.valueOf( minValue ),
          Double.valueOf( maxValue ) );
    }
    if( aValue < minValue ) {
      return ValidationResult.error( FMT_ERR_DOUBLE_LT_MIN, Double.valueOf( aValue ), Double.valueOf( minValue ),
          Double.valueOf( maxValue ) );
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
   * @param aValue double - the value
   * @param aValueName String - name of the value used for message formatting
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValidationResult validateError( double aValue, String aValueName ) {
    TsNullArgumentRtException.checkNull( aValueName );
    if( !Double.isFinite( aValue ) ) {
      return ValidationResult.error( FMT_ERR_NAMED_DOUBLE_NOT_FINITE, aValueName, Double.valueOf( aValue ) );
    }
    if( aValue > maxValue ) {
      return ValidationResult.error( FMT_INV_NAMED_DOUBLE_GT_MAX, aValueName, Double.valueOf( aValue ),
          Double.valueOf( minValue ), Double.valueOf( maxValue ) );
    }
    if( aValue < minValue ) {
      return ValidationResult.error( FMT_INV_NAMED_DOUBLE_LT_MIN, aValueName, Double.valueOf( aValue ),
          Double.valueOf( minValue ), Double.valueOf( maxValue ) );
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
   * @param aValue double - the value
   * @param aValueName String - name of the value used for message formatting
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValidationResult validateWarning( double aValue, String aValueName ) {
    TsNullArgumentRtException.checkNull( aValueName );
    if( !Double.isFinite( aValue ) ) {
      return ValidationResult.warn( FMT_ERR_NAMED_DOUBLE_NOT_FINITE, aValueName, Double.valueOf( aValue ) );
    }
    if( aValue > maxValue ) {
      return ValidationResult.warn( FMT_INV_NAMED_DOUBLE_GT_MAX, aValueName, Double.valueOf( aValue ),
          Double.valueOf( minValue ), Double.valueOf( maxValue ) );
    }
    if( aValue < minValue ) {
      return ValidationResult.warn( FMT_INV_NAMED_DOUBLE_LT_MIN, aValueName, Double.valueOf( aValue ),
          Double.valueOf( minValue ), Double.valueOf( maxValue ) );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Throws an exception if {@link #validate(double)} method fails.
   *
   * @param aValue double - the value
   * @return double - results the argument
   * @throws TsValidationFailedRtException if {@link #validate(double)} returns {@link EValidationResultType#ERROR}
   */
  public double checkInRange( double aValue ) {
    TsValidationFailedRtException.checkError( validate( aValue ) );
    return aValue;
  }

  /**
   * Checks if value is in range.
   *
   * @param aValue double - the value
   * @return boolean - <code>true</code> if value is in range
   */
  public boolean isInRange( double aValue ) {
    return Double.isFinite( aValue ) && aValue >= minValue && aValue <= maxValue;
  }

  /**
   * Determines if value is less than {@link #minValue()}.
   * <p>
   * For non-finite values returns <code>false</code> .
   *
   * @param aValue double - the value to check
   * @return boolean - <code>true</code> when aValue < {@link #minValue()}
   */
  public boolean isLeft( double aValue ) {
    return Double.isFinite( aValue ) && aValue < minValue;
  }

  /**
   * Determines if value is greater than {@link #maxValue()}.
   * <p>
   * For non-finite values returns <code>false</code>
   *
   * @param aValue double - the value to check
   * @return boolean - <code>true</code> when aValue > {@link #maxValue()}
   */
  public boolean isRight( double aValue ) {
    return Double.isFinite( aValue ) && aValue > maxValue;
  }

  /**
   * Determines if this range intersects with the specified range.
   *
   * @param aRange {@link DoubleRange} - range to be checked for intersection
   * @return boolean - <code>true</code> if at least one value is in both ranges
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean intersects( DoubleRange aRange ) {
    TsNullArgumentRtException.checkNull( aRange );
    return ((aRange.minValue <= this.maxValue) && (aRange.maxValue >= this.minValue));
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "(%.3g .. %.3g)", minValue, maxValue ); //$NON-NLS-1$
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
    long dblval = Double.doubleToRawLongBits( minValue );
    result = TsLibUtils.PRIME * result + (int)(dblval ^ (dblval >>> 32));
    dblval = Double.doubleToRawLongBits( maxValue );
    result = TsLibUtils.PRIME * result + (int)(dblval ^ (dblval >>> 32));
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( DoubleRange aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    int c = Double.compare( minValue, aThat.minValue );
    if( c == 0 ) {
      c = Double.compare( maxValue, aThat.maxValue );
    }
    return c;
  }

}
