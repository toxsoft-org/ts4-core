package org.toxsoft.core.tslib.coll.notifier.impl;

import static org.toxsoft.core.tslib.coll.helpers.ECrudOp.*;

import java.util.*;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Notification and validation wrapper over {@link IMapEdit}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public class NotifierMapEditWrapper<K, E>
    extends AbstractNotifierMap<K, E>
    implements INotifierMapEdit<K, E> {

  private static final long serialVersionUID = -4643864976306267057L;

  private final IMapEdit<K, E> source;

  /**
   * Constructor.
   *
   * @param aSource {@link IMapEdit} - the map to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierMapEditWrapper( IMapEdit<K, E> aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // AbstractNotifierMap
  //

  @Override
  public void fireItemByIndexChangeEvent( int aIndex ) {
    fireChangedEvent( EDIT, source.keys().get( aIndex ) );
  }

  @Override
  public void fireItemByRefChangeEvent( Object aItem ) {
    // nop
  }

  @Override
  public void fireItemByKeyChangeEvent( K aKey ) {
    if( keys().hasElem( aKey ) ) {
      fireChangedEvent( EDIT, aKey );
    }
  }

  @Override
  public boolean hasElem( E aElem ) {
    return source.hasElem( aElem );
  }

  @Override
  public boolean hasKey( K aKey ) {
    return source.hasKey( aKey );
  }

  @Override
  public E findByKey( K aKey ) {
    return source.findByKey( aKey );
  }

  @Override
  public IList<K> keys() {
    return source.keys();
  }

  @Override
  public IList<E> values() {
    return source.values();
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
  // IMapEdit
  //

  @Override
  public E put( K aKey, E aElem ) {
    TsNullArgumentRtException.checkNulls( aElem );
    TsValidationFailedRtException.checkError( canPut( aKey, aElem ) );
    E oldValue = source.findByKey( aKey );
    if( !Objects.equals( oldValue, aElem ) ) {
      source.put( aKey, aElem );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aKey );
    }
    return oldValue;
  }

  @Override
  public E removeByKey( K aKey ) {
    TsValidationFailedRtException.checkError( canRemove( aKey ) );
    E value = source.removeByKey( aKey );
    if( value != null ) {
      fireChangedEvent( REMOVE, aKey );
    }
    return value;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( source.keys().isEmpty() ) {
      return;
    }
    source.clear();
    fireChangedEvent( LIST, null );
  }

}
