package org.toxsoft.core.tslib.av.validators.defav;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.av.validators.defav.ITsResources.*;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;

/**
 * Валидатор типа {@link EAtomicType#FLOATING}.
 *
 * @author hazard157
 */
class FloatingValidator
    implements ITsValidator<IAtomicValue> {

  private IOptionSet constraints;

  public FloatingValidator( IOptionSet aConstraints ) {
    constraints = aConstraints;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    double value = aValue.asDouble();
    Double valueL = Double.valueOf( value );
    if( !constraints.isNull( TSID_MAX_EXCLUSIVE ) ) {
      double limit = constraints.getDouble( TSID_MAX_EXCLUSIVE );
      if( value >= limit ) {
        return ValidationResult.error( FMT_ERR_FLOATING_GE_MAX, valueL, Double.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MAX_INCLUSIVE ) ) {
      double limit = constraints.getDouble( TSID_MAX_INCLUSIVE );
      if( value > limit ) {
        return ValidationResult.error( FMT_ERR_FLOATING_GT_MAX, valueL, Double.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MIN_EXCLUSIVE ) ) {
      double limit = constraints.getDouble( TSID_MIN_EXCLUSIVE );
      if( value <= limit ) {
        return ValidationResult.error( FMT_ERR_FLOATING_LE_MIN, valueL, Double.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_MIN_INCLUSIVE ) ) {
      double limit = constraints.getDouble( TSID_MIN_INCLUSIVE );
      if( value < limit ) {
        return ValidationResult.error( FMT_ERR_FLOATING_LT_MIN, valueL, Double.valueOf( limit ) );
      }
    }
    if( !constraints.isNull( TSID_IS_NAN_ALLOWED ) && !constraints.getBool( TSID_IS_NAN_ALLOWED ) ) {
      if( Double.isNaN( value ) ) {
        return ValidationResult.error( MSG_ERR_FLOATING_NAN_NOT_ALLOWED );
      }
    }
    if( !constraints.isNull( TSID_IS_INF_ALLOWED ) && !constraints.getBool( TSID_IS_INF_ALLOWED ) ) {
      if( Double.isInfinite( value ) ) {
        return ValidationResult.error( MSG_ERR_FLOATING_INF_NOT_ALLOWED );
      }
    }
    return ValidationResult.SUCCESS;
  }
}