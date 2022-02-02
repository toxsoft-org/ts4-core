package org.toxsoft.core.tslib.av.validators.defav;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.av.validators.defav.ITsResources.*;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;

/**
 * Валидатор типа {@link EAtomicType#TIMESTAMP}.
 *
 * @author hazard157
 */
class TimestampValidator
    implements ITsValidator<IAtomicValue> {

  private IOptionSet constraints;

  public TimestampValidator( IOptionSet aConstraints ) {
    constraints = aConstraints;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    long value = aValue.asLong();
    Long valueL = Long.valueOf( value );
    if( !constraints.isNull( TSID_MAX_EXCLUSIVE ) ) {
      long limit = constraints.getTime( TSID_MAX_EXCLUSIVE );
      if( value >= limit ) {
        return ValidationResult.error( FMT_ERR_TIMESTAMP_GE_MAX, valueL, Long.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MAX_INCLUSIVE ) ) {
      long limit = constraints.getTime( TSID_MAX_INCLUSIVE );
      if( value > limit ) {
        return ValidationResult.error( FMT_ERR_TIMESTAMP_GT_MAX, valueL, Long.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MIN_EXCLUSIVE ) ) {
      long limit = constraints.getTime( TSID_MIN_EXCLUSIVE );
      if( value <= limit ) {
        return ValidationResult.error( FMT_ERR_TIMESTAMP_LE_MIN, valueL, Long.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MIN_INCLUSIVE ) ) {
      long limit = constraints.getTime( TSID_MIN_INCLUSIVE );
      if( value < limit ) {
        return ValidationResult.error( FMT_ERR_TIMESTAMP_LT_MIN, valueL, Long.valueOf( limit ) );
      }
    }
    return ValidationResult.SUCCESS;
  }
}