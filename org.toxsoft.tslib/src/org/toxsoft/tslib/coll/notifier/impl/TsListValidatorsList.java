package org.toxsoft.tslib.coll.notifier.impl;

import static org.toxsoft.tslib.bricks.validator.ValidationResult.*;

import java.io.Serializable;

import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.notifier.INotifierList;
import org.toxsoft.tslib.coll.notifier.basis.ITsListValidator;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Compound validator validating modifications based on principle "first error" - "last warning".
 * <p>
 * Calls validators in the order of {@link #addCollectionChangeValidator(ITsListValidator)} calls and returns first
 * error or last warning. If no erors or warning were encountered returns {@link ValidationResult#SUCCESS}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class TsListValidatorsList<E>
    implements ITsListValidator<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IListEdit<ITsListValidator<E>> list = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public TsListValidatorsList() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Adds the validator.
   * <p>
   * If validator is already added, method does nothing.
   *
   * @param aValidator {@link ITsListValidator} - the validator
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public void addCollectionChangeValidator( ITsListValidator<E> aValidator ) {
    if( !list.hasElem( aValidator ) ) {
      list.add( aValidator );
    }
  }

  /**
   * Removes the validator.
   * <p>
   * If validator was not added, method does nothing.
   *
   * @param aValidator {@link ITsListValidator} - the validator
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public void removeCollectionChangeValidator( ITsListValidator<E> aValidator ) {
    list.remove( aValidator );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsListValidator
  //

  @Override
  public ValidationResult canAdd( INotifierList<E> aSource, E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    ValidationResult result = ValidationResult.SUCCESS;
    for( ITsListValidator<E> c : list ) {
      ValidationResult r = c.canAdd( aSource, aNewItem );
      result = lastNonOk( result, r );
    }
    return result;
  }

  @Override
  public ValidationResult canRemove( INotifierList<E> aSource, E aRemovingItem ) {
    TsNullArgumentRtException.checkNull( aRemovingItem );
    ValidationResult result = ValidationResult.SUCCESS;
    for( ITsListValidator<E> c : list ) {
      ValidationResult r = c.canRemove( aSource, aRemovingItem );
      result = lastNonOk( result, r );
    }
    return result;
  }

  @Override
  public ValidationResult canReplace( INotifierList<E> aSource, E aExistingItem, E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    ValidationResult result = ValidationResult.SUCCESS;
    for( ITsListValidator<E> c : list ) {
      ValidationResult r = c.canReplace( aSource, aExistingItem, aNewItem );
      result = lastNonOk( result, r );
    }
    return result;
  }

}
