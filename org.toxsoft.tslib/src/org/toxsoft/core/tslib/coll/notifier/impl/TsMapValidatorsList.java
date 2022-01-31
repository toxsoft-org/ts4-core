package org.toxsoft.core.tslib.coll.notifier.impl;

import static org.toxsoft.core.tslib.bricks.validator.ValidationResult.*;

import java.io.Serializable;

import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.notifier.INotifierMap;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsMapValidator;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Compound validator validates modifications based on principle "first error" - "last warning".
 * <p>
 * Calls validators in the order of {@link #addCollectionChangeValidator(ITsMapValidator)} calls and returns first error
 * or last warning. If no erors or warning were encountered returns {@link ValidationResult#SUCCESS}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public class TsMapValidatorsList<K, E>
    implements ITsMapValidator<K, E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IListEdit<ITsMapValidator<K, E>> list = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public TsMapValidatorsList() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Adds the validator.
   * <p>
   * If validator is already added, method does nothing.
   *
   * @param aValidator {@link ITsMapValidator} - the validator
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public void addCollectionChangeValidator( ITsMapValidator<K, E> aValidator ) {
    if( !list.hasElem( aValidator ) ) {
      list.add( aValidator );
    }
  }

  /**
   * Removes the validator.
   * <p>
   * If validator was not added, method does nothing.
   *
   * @param aValidator {@link ITsMapValidator} - the validator
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public void removeCollectionChangeValidator( ITsMapValidator<K, E> aValidator ) {
    list.remove( aValidator );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsMapValidator
  //

  @Override
  public ValidationResult canPut( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem ) {
    TsNullArgumentRtException.checkNulls( aKey, aNewItem );
    ValidationResult result = ValidationResult.SUCCESS;
    for( ITsMapValidator<K, E> c : list ) {
      ValidationResult r = c.canPut( aSource, aKey, aExistingItem, aNewItem );
      result = lastNonOk( result, r );
    }
    return result;
  }

  @Override
  public ValidationResult canRemove( INotifierMap<K, E> aSource, K aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    ValidationResult result = ValidationResult.SUCCESS;
    for( ITsMapValidator<K, E> c : list ) {
      ValidationResult r = c.canRemove( aSource, aKey );
      result = lastNonOk( result, r );
    }
    return result;
  }

  @Override
  public ValidationResult canAdd( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem ) {
    TsNullArgumentRtException.checkNulls( aKey, aNewItem );
    ValidationResult result = ValidationResult.SUCCESS;
    for( ITsMapValidator<K, E> c : list ) {
      ValidationResult r = c.canAdd( aSource, aKey, aExistingItem, aNewItem );
      result = lastNonOk( result, r );
    }
    return result;
  }

}
