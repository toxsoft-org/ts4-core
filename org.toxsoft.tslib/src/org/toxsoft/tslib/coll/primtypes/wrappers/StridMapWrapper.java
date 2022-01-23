package org.toxsoft.tslib.coll.primtypes.wrappers;

import java.util.Iterator;

import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Wrapper over {@link IStringMapEdit} allowing only IDpath/IDname keys.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class StridMapWrapper<E>
    implements IStringMapEdit<E> {

  private final IStringMapEdit<E> source;
  private final boolean           olyIdNames;

  /**
   * Constructor.
   *
   * @param aSource {@link IStringMapEdit}&lt;E&gt; - the wrapper map
   * @param aOnlyIdNames boolean - determines if only IDnames are allowed as keys
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StridMapWrapper( IStringMapEdit<E> aSource, boolean aOnlyIdNames ) {
    source = TsNullArgumentRtException.checkNull( aSource );
    olyIdNames = aOnlyIdNames;
  }

  /**
   * Constructor for IDpath keyed map.
   *
   * @param aSource {@link IStringMapEdit}&lt;E&gt; - the wrapper map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StridMapWrapper( IStringMapEdit<E> aSource ) {
    this( aSource, false );
  }

  @Override
  public IStringList keys() {
    return source.keys();
  }

  @Override
  public boolean hasKey( String aKey ) {
    return source.hasKey( aKey );
  }

  @Override
  public E findByKey( String aKey ) {
    return source.findByKey( aKey );
  }

  @Override
  public IList<E> values() {
    return source.values();
  }

  @Override
  public boolean hasElem( E aElem ) {
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

  @Override
  public E put( String aKey, E aElem ) {
    if( olyIdNames ) {
      StridUtils.checkValidIdName( aKey );
    }
    else {
      StridUtils.checkValidIdPath( aKey );
    }
    return source.put( aKey, aElem );
  }

  @Override
  public E removeByKey( String aKey ) {
    return source.removeByKey( aKey );
  }

  @Override
  public void clear() {
    source.clear();
  }

}
