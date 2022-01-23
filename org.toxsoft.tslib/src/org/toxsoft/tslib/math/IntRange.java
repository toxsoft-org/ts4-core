package org.toxsoft.tslib.math;

import static org.toxsoft.tslib.math.ITsResources.*;

import java.io.Serializable;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper.EEncloseMode;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.bricks.validator.*;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
  public static final IEntityKeeper<IntRange> KEEPR =
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

  private ITsValidator<Integer> validatorInteger = new ITsValidator<>() {

    @Override
    public ValidationResult validate( Integer aValue ) {
      TsNullArgumentRtException.checkNull( aValue );
      return IntRange.this.validate( aValue.intValue() );
    }

  };

  private ITsValidator<IAtomicValue> validatorAv = new ITsValidator<>() {

    @Override
    public ValidationResult validate( IAtomicValue aValue ) {
      TsNullArgumentRtException.checkNull( aValue );
      if( aValue.atomicType() != EAtomicType.INTEGER ) {
        return ValidationResult.error( FMT_ERR_AV_NOT_INTEGER, aValue.atomicType().id() );
      }
      return IntRange.this.validate( aValue.asInt() );
    }

  };

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
    return validatorInteger;
  }

  /**
   * Returns the {@link IAtomicValue} of type {@link EAtomicType#INTEGER} value validator checking out of range.
   *
   * @return {@link ITsValidator}&lt;{@link IAtomicValue}&gt; - {@link IntRange} validator
   */
  public ITsValidator<IAtomicValue> validatorAv() {
    return validatorAv;
  }

  /**
   * Validates the <code>int</code> value is in reange.
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
   * Throws an excepion if {@link #validate(int)} method fails.
   *
   * @param aValue int - the value
   * @return int - retuts the argument
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
    // equality check will be performed on IntRange because tslib does not creates IntRange instances
    if( aThat instanceof IntRange ) {
      IntRange that = (IntRange)aThat;
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
