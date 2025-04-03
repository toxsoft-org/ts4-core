package org.toxsoft.core.tslib.av.validators.defav;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * Валидатор типа {@link EAtomicType#VALOBJ}.
 *
 * @author hazard157
 */
class ValobjValidator
    implements ITsValidator<IAtomicValue> {

  @SuppressWarnings( "unused" )
  private IOptionSet constraints;

  public ValobjValidator( IOptionSet aConstraints ) {
    constraints = aConstraints;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {

    /**
     * TODO if TSID_KEEPER_ID exists WARN check if keeper is not accessible
     */

    return ValidationResult.SUCCESS;
  }

}
