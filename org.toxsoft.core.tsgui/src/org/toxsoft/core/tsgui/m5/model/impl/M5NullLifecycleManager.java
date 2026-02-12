package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Special case lLM does not allows anything.
 * <p>
 * Instances are useful when required master object is <code>null</code> (not yet retrieved) but <code>null</code> LM
 * may cause an exception.Especially "NULL" LMs may be used while constructing panels before they are shown and master
 * entity is set.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5NullLifecycleManager<T>
    implements IM5LifecycleManager<T> {

  private static final ValidationResult ERROR_VR = ValidationResult.error( "Null M5Lifecycle manager" );
  private final IM5Model<T>             model;

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model} - the model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5NullLifecycleManager( IM5Model<T> aModel ) {
    model = TsNullArgumentRtException.checkNull( aModel );
  }

  @Override
  public IM5Model<T> model() {
    return model;
  }

  @Override
  public boolean isCrudOpAllowed( ECrudOp aOp ) {
    return false;
  }

  @Override
  public <M> M master() {
    return null;
  }

  @Override
  public IM5ItemsProvider<T> itemsProvider() {
    return IM5ItemsProvider.EMPTY;
  }

  @Override
  public IM5BunchEdit<T> createNewItemValues() {
    return new M5BunchEdit<>( model() );
  }

  @Override
  public ValidationResult canCreate( IM5Bunch<T> aValues ) {
    return ERROR_VR;
  }

  @Override
  public T create( IM5Bunch<T> aValues ) {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ValidationResult canEdit( IM5Bunch<T> aValues ) {
    return ERROR_VR;
  }

  @Override
  public T edit( IM5Bunch<T> aValues ) {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ValidationResult canRemove( T aEntity ) {
    return ERROR_VR;
  }

  @Override
  public void remove( T aEntity ) {
    throw new TsUnsupportedFeatureRtException();
  }

}
