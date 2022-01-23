package org.toxsoft.tslib.av.validators.defav;

import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.tslib.av.validators.defav.ITsResources.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.bricks.validator.ValidationResult;

/**
 * Валидатор типа {@link EAtomicType#INTEGER}.
 *
 * @author hazard157
 */
class IntegerValidator
    implements ITsValidator<IAtomicValue> {

  private IOptionSet constraints;

  public IntegerValidator( IOptionSet aConstraints ) {
    constraints = aConstraints;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    long value = aValue.asLong();
    Long valueL = Long.valueOf( value );
    if( !constraints.isNull( TSID_MAX_EXCLUSIVE ) ) {
      long limit = constraints.getLong( TSID_MAX_EXCLUSIVE );
      if( value >= limit ) {
        return ValidationResult.error( FMT_ERR_INTEGER_GE_MAX, valueL, Long.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MAX_INCLUSIVE ) ) {
      long limit = constraints.getLong( TSID_MAX_INCLUSIVE );
      if( value > limit ) {
        return ValidationResult.error( FMT_ERR_INTEGER_GT_MAX, valueL, Long.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MIN_EXCLUSIVE ) ) {
      long limit = constraints.getLong( TSID_MIN_EXCLUSIVE );
      if( value <= limit ) {
        return ValidationResult.error( FMT_ERR_INTEGER_LE_MIN, valueL, Long.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MIN_INCLUSIVE ) ) {
      long limit = constraints.getLong( TSID_MIN_INCLUSIVE );
      if( value < limit ) {
        return ValidationResult.error( FMT_ERR_INTEGER_LT_MIN, valueL, Long.valueOf( limit ) );
      }
    }
    return ValidationResult.SUCCESS;
  }

}