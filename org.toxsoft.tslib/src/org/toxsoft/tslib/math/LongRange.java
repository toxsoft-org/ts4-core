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
 * Min-max long values immutable representation.
 *
 * @author hazard157
 */
public final class LongRange
    implements Comparable<LongRange>, Serializable {

  private static final long serialVersionUID = 8639994626129869569L;

  /**
   * Full range of <code>long</code> integers.
   */
  public static final LongRange FULL = new LongRange( Long.MIN_VALUE, Long.MAX_VALUE );

  /**
   * Range (0,0).
   */
  public static final LongRange ZERO = new LongRange( 0L, 0L );

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "LongRange"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<LongRange> KEEPR =
      new AbstractEntityKeeper<>( LongRange.class, EEncloseMode.ENCLOSES_BASE_CLASS, ZERO ) {

        @Override
        protected void doWrite( IStrioWriter aSw, LongRange aEntity ) {
          aSw.writeLong( aEntity.minValue() );
          aSw.writeSeparatorChar();
          aSw.writeLong( aEntity.maxValue() );
        }

        @Override
        protected LongRange doRead( IStrioReader aSr ) {
          long minVal = aSr.readLong();
          aSr.ensureSeparatorChar();
          long maxVal = aSr.readLong();
          return new LongRange( minVal, maxVal );
        }
      };

  private ITsValidator<Long> validatorLong = new ITsValidator<>() {

    @Override
    public ValidationResult validate( Long aValue ) {
      TsNullArgumentRtException.checkNull( aValue );
      return LongRange.this.validate( aValue.longValue() );
    }

  };

  private ITsValidator<IAtomicValue> validatorAv = new ITsValidator<>() {

    @Override
    public ValidationResult validate( IAtomicValue aValue ) {
      TsNullArgumentRtException.checkNull( aValue );
      if( aValue.atomicType() != EAtomicType.INTEGER ) {
        return ValidationResult.error( FMT_ERR_AV_NOT_INTEGER, aValue.atomicType().id() );
      }
      return LongRange.this.validate( aValue.asLong() );
    }

  };

  final long minValue;
  final long maxValue;

  /**
   * Constructor.
   * <p>
   * Ends of the range may be equal.
   *
   * @param aMinValue long - lower end of the range
   * @param aMaxValue long - higher end of the range
   * @throws TsIllegalArgumentRtException max value is less then min value
   */
  public LongRange( long aMinValue, long aMaxValue ) {
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
   * @return long - min value
   */
  public long minValue() {
    return minValue;
  }

  /**
   * Return the higher end of the range.
   *
   * @return long - max value
   */
  public long maxValue() {
    return maxValue;
  }

  /**
   * Returns the value "fitted" in range.
   *
   * @param aValue long - the value
   * @return long - value always in range
   */
  public long inRange( long aValue ) {
    if( aValue < minValue ) {
      return minValue;
    }
    if( aValue > maxValue ) {
      return maxValue;
    }
    return aValue;
  }

  /**
   * Returns the {@link Long} value validator checking out of range.
   *
   * @return {@link ITsValidator}&lt;{@link Long}&gt; - {@link Long} validator
   */
  public ITsValidator<Long> validatorLong() {
    return validatorLong;
  }

  /**
   * Returns the {@link IAtomicValue} of type {@link EAtomicType#INTEGER} value validator checking out of range.
   *
   * @return {@link ITsValidator}&lt;{@link IAtomicValue}&gt; - {@link IAtomicValue} validator
   */
  public ITsValidator<IAtomicValue> validatorAv() {
    return validatorAv;
  }

  /**
   * Validates the <code>long</code> value is in reange.
   *
   * @param aValue long - the value
   * @return {@link ValidationResult} - the validation result
   */
  public ValidationResult validate( long aValue ) {
    if( aValue > maxValue ) {
      return ValidationResult.error( FMT_ERR_LONG_GT_MAX, Long.valueOf( aValue ), Long.valueOf( minValue ),
          Long.valueOf( maxValue ) );
    }
    if( aValue < maxValue ) {
      return ValidationResult.error( FMT_ERR_LONG_LT_MIN, Long.valueOf( aValue ), Long.valueOf( minValue ),
          Long.valueOf( maxValue ) );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Throws an excepion if {@link #validate(long)} method fails.
   *
   * @param aValue long - the value
   * @return long - retuts the argument
   * @throws TsValidationFailedRtException if {@link #validate(long)} returns {@link EValidationResultType#ERROR}
   */
  public long checkInRange( long aValue ) {
    TsValidationFailedRtException.checkError( validate( aValue ) );
    return aValue;
  }

  /**
   * Checks if value is in range.
   *
   * @param aValue long - the value
   * @return boolean - <code>true</code> if value is in range
   */
  public boolean isInRange( long aValue ) {
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
    result = TsLibUtils.PRIME * result + (int)(minValue ^ (minValue >>> 32));
    result = TsLibUtils.PRIME * result + (int)(maxValue ^ (maxValue >>> 32));
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( LongRange aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    if( aThat == this ) {
      return 0;
    }
    int c = Long.compare( minValue, aThat.minValue );
    if( c == 0 ) {
      c = Long.compare( maxValue, aThat.maxValue );
    }
    return c;
  }

}
