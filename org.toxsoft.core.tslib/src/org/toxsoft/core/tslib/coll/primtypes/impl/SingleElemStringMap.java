package org.toxsoft.core.tslib.coll.primtypes.impl;

import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Immutable implementation of {@link IStringMap} containing one element.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class SingleElemStringMap<E>
    implements IStringMap<E> {

  private final IStringList keysList;
  private final IList<E>    valuesList;

  /**
   * Constructor.
   *
   * @param aKey String - the key
   * @param aValue &lt;E&gt; - the value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SingleElemStringMap( String aKey, E aValue ) {
    keysList = new SingleStringList( aKey );
    valuesList = new SingleItemList<>( aValue );
  }

  @Override
  public boolean hasKey( String aKey ) {
    return keysList.hasElem( aKey );
  }

  @Override
  public E findByKey( String aKey ) {
    if( hasKey( aKey ) ) {
      return valuesList.first();
    }
    return null;
  }

  @Override
  public IList<E> values() {
    return valuesList;
  }

  @Override
  public boolean hasElem( E aElem ) {
    return valuesList.hasElem( aElem );
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return valuesList.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return valuesList.toArray();
  }

  @Override
  public Iterator<E> iterator() {
    return valuesList.iterator();
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public IStringList keys() {
    return keysList;
  }

}
