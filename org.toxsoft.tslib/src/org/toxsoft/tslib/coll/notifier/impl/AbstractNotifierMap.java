package org.toxsoft.tslib.coll.notifier.impl;

import java.io.Serializable;

import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.notifier.INotifierMap;
import org.toxsoft.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.tslib.coll.notifier.basis.ITsMapValidator;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TRANSLATE

/**
 * Abstract basic implementation of {@link INotifierMap}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public abstract class AbstractNotifierMap<K, E>
    implements INotifierMap<K, E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IListEdit<ITsCollectionChangeListener> listeners      = new ElemArrayList<>();
  private final TsMapValidatorsList<K, E>              validatorsList = new TsMapValidatorsList<>();

  private boolean validationEnabled = true;
  private boolean suspended         = false;
  private boolean wasBatchChanges   = false;

  /**
   * Constructor for decendants.
   */
  protected AbstractNotifierMap() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Methods for descendants
  //

  protected void fireChangedEvent( ECrudOp aOp, Object aItem ) {
    if( suspended ) {
      wasBatchChanges = true;
      return;
    }
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsCollectionChangeListener> ll = new ElemArrayList<>( listeners );
    for( ITsCollectionChangeListener l : ll ) {
      l.onCollectionChanged( this, aOp, aItem );
    }
  }

  protected void checkPut( K aKey, E aExistingItem, E aNewItem ) {
    if( aKey == null || aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      ValidationResult v = validatorsList.canPut( this, aKey, aExistingItem, aNewItem );
      if( v.isError() ) {
        throw new TsValidationFailedRtException( v );
      }
    }
  }

  protected void checkRemove( K aKey ) {
    if( aKey == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      ValidationResult v = validatorsList.canRemove( this, aKey );
      if( v.isError() ) {
        throw new TsValidationFailedRtException( v );
      }
    }
  }

  protected void checkAdd( K aKey, E aExistingItem, E aNewItem ) {
    if( aKey == null || aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      ValidationResult v = validatorsList.canAdd( this, aKey, aExistingItem, aNewItem );
      if( v.isError() ) {
        throw new TsValidationFailedRtException( v );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsPausabeEventProducer
  //

  @Override
  public void pauseFiring() {
    suspended = true;
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    if( !suspended ) {
      return;
    }
    suspended = false;
    if( wasBatchChanges ) {
      if( aFireDelayed ) {
        fireChangedEvent( ECrudOp.LIST, null );
      }
      wasBatchChanges = false;
    }
  }

  @Override
  public boolean isFiringPaused() {
    return suspended;
  }

  @Override
  public boolean isPendingEvents() {
    return wasBatchChanges;
  }

  @Override
  public void resetPendingEvents() {
    wasBatchChanges = false;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollectionChangeEventProducer
  //

  @Override
  public void addCollectionChangeListener( ITsCollectionChangeListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeCollectionChangeListener( ITsCollectionChangeListener aListener ) {
    listeners.remove( aListener );
  }

  @Override
  public void fireBatchChangeEvent() {
    fireChangedEvent( ECrudOp.LIST, null );
  }

  @Override
  public boolean isValidationEnabled() {
    return validationEnabled;
  }

  @Override
  public void setValidationEnabled( boolean aEnabled ) {
    validationEnabled = aEnabled;
  }

  // ------------------------------------------------------------------------------------
  // INotifierMap
  //

  @Override
  public ValidationResult canPut( K aKey, E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    E oldItem = findByKey( aKey );
    return validatorsList.canPut( this, aKey, oldItem, aNewItem );
  }

  @Override
  public ValidationResult canRemove( K aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return validatorsList.canRemove( this, aKey );
  }

  @Override
  public ValidationResult canAdd( K aKey, E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    E oldItem = findByKey( aKey );
    return validatorsList.canAdd( this, aKey, oldItem, aNewItem );
  }

  @Override
  public ITsMapValidator<K, E> getMapValidator() {
    return validatorsList;
  }

  @Override
  public void addCollectionChangeValidator( ITsMapValidator<K, E> aValidator ) {
    validatorsList.addCollectionChangeValidator( aValidator );
  }

  @Override
  public void removeCollectionChangeValidator( ITsMapValidator<K, E> aValidator ) {
    validatorsList.removeCollectionChangeValidator( aValidator );
  }

}
