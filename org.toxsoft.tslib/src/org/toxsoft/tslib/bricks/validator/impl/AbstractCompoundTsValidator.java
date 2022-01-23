package org.toxsoft.tslib.bricks.validator.impl;

import org.toxsoft.tslib.bricks.validator.*;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Abstract implementation of {@link ITsCompoundValidator}.
 * <p>
 * Usually you do not need this class, use {@link TsCompoundValidator#create(boolean, boolean)} instead.
 *
 * @author hazard157
 * @param <V> - checked entity (value) class
 */
public abstract class AbstractCompoundTsValidator<V>
    implements ITsCompoundValidator<V> {

  private final IListEdit<ITsValidator<V>> allValidators      = new ElemArrayList<>();
  private final IListEdit<ITsValidator<V>> disabledValidators = new ElemArrayList<>();

  // null-> enabled list was changed, will be updated in enabledValidators()
  private IListEdit<ITsValidator<V>> enabledValidators = null;

  /**
   * Пустой конструктор.
   */
  protected AbstractCompoundTsValidator() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ICompoundTsValidator
  //

  @Override
  final public IListEdit<ITsValidator<V>> listValidators() {
    return allValidators;
  }

  @Override
  final public void addValidator( ITsValidator<V> aValidator ) {
    TsNullArgumentRtException.checkNull( aValidator );
    if( !allValidators.hasElem( aValidator ) ) {
      allValidators.add( aValidator );
      enabledValidators = null;
    }
  }

  @Override
  final public void removeValidator( ITsValidator<V> aValidator ) {
    if( allValidators.remove( aValidator ) >= 0 ) {
      disabledValidators.remove( aValidator );
      enabledValidators = null;
    }
  }

  @Override
  public void muteValidator( ITsValidator<V> aValidator ) {
    if( allValidators.hasElem( aValidator ) ) {
      disabledValidators.add( aValidator );
      enabledValidators = null;
    }
  }

  @Override
  public boolean isValidatorMuted( ITsValidator<V> aValidator ) {
    return disabledValidators.hasElem( aValidator );
  }

  @Override
  public void unmuteValidator( ITsValidator<V> aValidator ) {
    if( disabledValidators.remove( aValidator ) >= 0 ) {
      enabledValidators = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // For descendants
  //

  protected IListEdit<ITsValidator<V>> enabledValidators() {
    if( enabledValidators == null ) {
      enabledValidators = new ElemArrayList<>( allValidators.size() );
      for( ITsValidator<V> v : allValidators ) {
        if( !isValidatorMuted( v ) ) {
          enabledValidators.add( v );
        }
      }
    }
    return enabledValidators;
  }

  // ------------------------------------------------------------------------------------
  // To be implemented
  //

  @Override
  abstract public ValidationResult validate( V aValue );

}
