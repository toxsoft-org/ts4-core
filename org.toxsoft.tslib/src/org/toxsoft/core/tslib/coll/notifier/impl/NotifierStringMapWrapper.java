package org.toxsoft.core.tslib.coll.notifier.impl;

import static org.toxsoft.core.tslib.coll.helpers.ECrudOp.*;

import java.util.Iterator;
import java.util.Objects;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IMap;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.core.tslib.coll.notifier.INotifierStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Notification and validation wrapper over {@link IStringMap}.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class NotifierStringMapWrapper<E>
    extends AbstractNotifierMap<String, E>
    implements INotifierStringMapEdit<E> {

  private static final long serialVersionUID = 157157L;

  private final IStringMapEdit<E> source;

  /**
   * Constructor.
   *
   * @param aSource {@link IStringMapEdit} - the map to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierStringMapWrapper( IStringMapEdit<E> aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // ITsNotifierCollection
  //

  @Override
  public void fireItemByIndexChangeEvent( int aIndex ) {
    fireChangedEvent( EDIT, source.keys().get( aIndex ) );
  }

  @Override
  public void fireItemByRefChangeEvent( Object aItem ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // INotifierMap
  //

  @Override
  public void fireItemByKeyChangeEvent( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    String key = aKey;
    if( keys().hasElem( key ) ) {
      fireChangedEvent( EDIT, key );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return source.hasElem( aElem );
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  @Override
  public int size() {
    return source.size();
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( String aKey ) {
    return source.hasKey( aKey );
  }

  @Override
  public E findByKey( String aKey ) {
    return source.findByKey( aKey );
  }

  @Override
  public IStringList keys() {
    return source.keys();
  }

  @Override
  public IList<E> values() {
    return source.values();
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( !isEmpty() ) {
      for( String k : source.keys() ) {
        canRemove( k );
      }
      source.clear();
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
  //

  @Override
  public E put( String aKey, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    E existingItem = source.findByKey( aKey );
    if( Objects.equals( aElem, existingItem ) ) {
      return existingItem;
    }
    checkPut( aKey, existingItem, aElem );
    E e = source.put( aKey, aElem );
    if( existingItem != null ) {
      fireChangedEvent( ECrudOp.EDIT, aElem );
    }
    else {
      fireChangedEvent( ECrudOp.CREATE, aElem );
    }
    return e;
  }

  @Override
  public E removeByKey( String aKey ) {
    checkRemove( aKey );
    E e = source.removeByKey( aKey );
    if( e != null ) {
      fireChangedEvent( ECrudOp.REMOVE, e );
    }
    return e;
  }

  @Override
  public void putAll( IMap<String, ? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    if( !aSrc.isEmpty() ) {
      for( String key : aSrc.keys() ) {
        checkPut( key, source.findByKey( key ), aSrc.getByKey( key ) );
      }
      source.putAll( aSrc );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void setAll( IMap<String, ? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    for( String key : aSrc.keys() ) {
      checkPut( key, null, aSrc.getByKey( key ) );
    }
    source.setAll( aSrc );
    fireChangedEvent( ECrudOp.LIST, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object obj ) {
    return source.equals( obj );
  }

  @Override
  public int hashCode() {
    return source.hashCode();
  }

}
