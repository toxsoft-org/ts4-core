package org.toxsoft.tslib.bricks.validator.impl;

import org.toxsoft.tslib.bricks.validator.ITsValidationSupport;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;

// TODO TRANSLATE

/**
 * Базовая реализация {@link ITsValidationSupport}.
 *
 * @author hazard157
 * @param <V> - конкретный итерфейс валидатора
 */
public abstract class AbstractTsValidationSupport<V>
    implements ITsValidationSupport<V> {

  private final IListEdit<V> validators       = new ElemArrayList<>();
  private final IListEdit<V> pausedValidators = new ElemArrayList<>();

  /**
   * Конструктор.
   */
  public AbstractTsValidationSupport() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
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
  // Реализация интерфейса ISkValidationSupport
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
