package org.toxsoft.core.tslib.bricks.validator.impl;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * {@link ITsValidationSupport} base implementation.
 *
 * @author hazard157
 * @param <V> - concrete validator interface
 */
public abstract class AbstractTsValidationSupport<V>
    implements ITsValidationSupport<V> {

  private final IListEdit<V> validators       = new ElemArrayList<>();
  private final IListEdit<V> pausedValidators = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public AbstractTsValidationSupport() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  protected IList<V> validatorsList() {
    IListEdit<V> vv = new ElemArrayList<>( validators.size() );
    for( V v : validators ) {
      if( !pausedValidators.hasElem( v ) ) {
        vv.add( v );
      }
    }
    return vv;
  }

  // ------------------------------------------------------------------------------------
  // ITsValidationSupport
  //

  @Override
  public abstract V validator();

  @Override
  public void addValidator( V aValidator ) {
    if( !validators.hasElem( aValidator ) ) {
      validators.add( aValidator );
    }
  }

  @Override
  public void removeValidator( V aValidator ) {
    validators.remove( aValidator );
  }

  @Override
  public void pauseValidator( V aValidator ) {
    if( !pausedValidators.hasElem( aValidator ) ) {
      pausedValidators.add( aValidator );
    }
  }

  @Override
  public void resumeValidator( V aValidator ) {
    pausedValidators.remove( aValidator );
  }

}
