package org.toxsoft.tslib.bricks.strid.coll.impl;

import java.util.Collection;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListBasicEdit;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.tslib.bricks.strid.coll.notifier.INotifierStridablesListEdit;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Оболочка над списком {@link IStridablesListBasicEdit} с извещением об изменениях при редактировании.
 *
 * @author hazard157
 * @param <E> - тип хранимых элементов
 */
public class NotifierStridablesListEditWrapper<E extends IStridable>
    extends NotifierStridablesListBasicEditWrapper<E>
    implements INotifierStridablesListEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Creates source wrapper instance.
   *
   * @param aSource {@link IStridablesListEdit}&lt;E&gt; - the wrapped list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierStridablesListEditWrapper( IStridablesListEdit<E> aSource ) {
    super( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  IStridablesListEdit<E> source() {
    return (IStridablesListEdit<E>)source;
  }

  // ------------------------------------------------------------------------------------
  // IListEdit
  //

  @Override
  public E set( int aIndex, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    checkPut( aElem.id(), source.findByKey( aElem.id() ), aElem );
    E e = source().set( aIndex, aElem );
    fireChangedEvent( ECrudOp.EDIT, aElem.id() );
    return e;
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    checkPut( aElem.id(), null, aElem );
    source().insert( aIndex, aElem );
    fireChangedEvent( ECrudOp.CREATE, aElem.id() );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void insertAll( int aIndex, E... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    if( aArray.length != 0 ) {
      for( E e : aArray ) {
        checkPut( e.id(), source.findByKey( e.id() ), e );
      }
      source().insertAll( aIndex, aArray );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( aColl.isEmpty() ) {
      return;
    }
    for( E e : aColl ) {
      checkPut( e.id(), source.findByKey( e.id() ), e );
    }
    source().insertAll( aIndex, aColl );
    fireChangedEvent( ECrudOp.LIST, null );
  }

  @Override
  public void insertAll( int aIndex, Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( !aColl.isEmpty() ) {
      for( E e : aColl ) {
        checkPut( e.id(), source.findByKey( e.id() ), e );
      }
      source().insertAll( aIndex, aColl );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

}
