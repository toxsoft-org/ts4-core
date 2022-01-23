package org.toxsoft.tslib.coll.notifier.impl;

import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.notifier.INotifierList;
import org.toxsoft.tslib.coll.notifier.basis.ITsListValidator;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Adater for {@link ITsListValidator} implementations.
 * <p>
 * All methods simply returns {@link ValidationResult#SUCCESS}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class TsListValidatorAdapter<E>
    implements ITsListValidator<E> {

  @Override
  public ValidationResult canAdd( INotifierList<E> aSource, E aNewItem ) {
    if( aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canRemove( INotifierList<E> aSource, E aRemovingItem ) {
    if( aRemovingItem == null ) {
      throw new TsNullArgumentRtException();
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canReplace( INotifierList<E> aSource, E aExistingItem, E aNewItem ) {
    if( aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    return ValidationResult.SUCCESS;
  }

}
