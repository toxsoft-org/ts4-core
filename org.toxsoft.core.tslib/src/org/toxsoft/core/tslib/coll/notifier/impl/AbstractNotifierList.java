package org.toxsoft.core.tslib.coll.notifier.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Abstract basic implementation of {@link INotifierList}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public abstract class AbstractNotifierList<E>
    implements INotifierList<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IListEdit<ITsCollectionChangeListener> listeners      = new ElemLinkedBundleList<>();
  private final TsListValidatorsList<E>                validatorsList = new TsListValidatorsList<>();

  private boolean validationEnabled = true;
  private boolean suspended         = false;
  private boolean wasBatchChanges   = false;

  /**
   * Constructor for decendants.
   */
  protected AbstractNotifierList() {
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

  protected void checkAdd( E aNewItem ) {
    if( aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      ValidationResult v = validatorsList.canAdd( this, aNewItem );
      if( v.isError() ) {
        throw new TsValidationFailedRtException( v );
      }
    }
  }

  protected void checkRemove( E aRemovingItem ) {
    if( aRemovingItem == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      ValidationResult v = validatorsList.canRemove( this, aRemovingItem );
      if( v.isError() ) {
        throw new TsValidationFailedRtException( v );
      }
    }
  }

  protected void checkReplace( E aExistingItem, E aNewItem ) {
    if( aExistingItem == null || aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      ValidationResult v = validatorsList.canReplace( this, aExistingItem, aNewItem );
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

  @Override
  public void clearCollectionChangeListeners() {
    listeners.clear();
  }

  // ------------------------------------------------------------------------------------
  // INotifierList
  //

  @Override
  public ValidationResult canAdd( E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    return validatorsList.canAdd( this, aNewItem );
  }

  @Override
  public ValidationResult canRemove( int aIndex ) {
    E oldItem = get( aIndex );
    return validatorsList.canRemove( this, oldItem );
  }

  @Override
  public ValidationResult canReplace( int aIndex, E aNewItem ) {
    E oldItem = get( aIndex );
    TsNullArgumentRtException.checkNull( aNewItem );
    return validatorsList.canReplace( this, oldItem, aNewItem );
  }

  @Override
  public ITsListValidator<E> getListValidator() {
    return validatorsList;
  }

  @Override
  public void addCollectionChangeValidator( ITsListValidator<E> aValidator ) {
    validatorsList.addCollectionChangeValidator( aValidator );
  }

  @Override
  public void removeCollectionChangeValidator( ITsListValidator<E> aValidator ) {
    validatorsList.removeCollectionChangeValidator( aValidator );
  }

}
