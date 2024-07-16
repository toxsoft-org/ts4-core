package org.toxsoft.core.tslib.coll.helpers;

import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IListReorderer} implementation calls {@link #doProcessActualChange(IList)}.
 *
 * @author hazard157
 * @param <E> - the type of elements the underlying list
 * @param <L> - the type of the underlying editable list
 */
public class ListReorderer2<E, L extends IListEdit<E>>
    implements IListReorderer<E> {

  private final L list;

  /**
   * Creates the reorderer, associated to the list.
   *
   * @param aSource &lt;L&gt; - the underlying list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ListReorderer2( L aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    list = aSource;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  protected int calcJump() {
    int size = list.size();
    if( size > 10 ) {
      return size / 10;
    }
    if( size > 2 ) {
      return 2;
    }
    return 1;
  }

  private IList<E> makeSnapshot() {
    IListEdit<E> ll = new ElemArrayList<>( list.size() );
    for( E e : list ) {
      ll.add( e );
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // IListReorderer
  //

  @Override
  public L list() {
    return list;
  }

  @Override
  public boolean swap( int aIndex1, int aIndex2 ) {
    if( aIndex1 < 0 || aIndex2 < 0 || aIndex1 >= list.size() || aIndex2 >= list.size() ) {
      throw new TsIllegalArgumentRtException();
    }
    if( aIndex1 == aIndex2 ) {
      return false;
    }
    IList<E> snaphot = makeSnapshot();
    E tmp = list.get( aIndex1 );
    list.set( aIndex1, list.get( aIndex2 ) );
    list.set( aIndex2, tmp );
    doProcessActualChange( snaphot );
    return true;
  }

  @Override
  public boolean move( int aOldIndex, int aNewIndex ) {
    if( aOldIndex < 0 || aNewIndex < 0 || aOldIndex >= list.size() || aNewIndex >= list.size() ) {
      throw new TsIllegalArgumentRtException();
    }
    if( aOldIndex == aNewIndex ) {
      return false;
    }
    IList<E> snaphot = makeSnapshot();
    E tmp = list.removeByIndex( aOldIndex );
    list.insert( aNewIndex, tmp );
    doProcessActualChange( snaphot );
    return true;
  }

  @Override
  public boolean move( int aOldIndex, ETsCollMove aDir ) {
    if( aOldIndex < 0 || aOldIndex >= list.size() ) {
      throw new TsIllegalArgumentRtException();
    }
    TsNullArgumentRtException.checkNull( aDir );
    int newIndex;
    switch( aDir ) {
      case FIRST:
        newIndex = 0;
        break;
      case PREV:
        newIndex = aOldIndex - 1;
        if( newIndex < 0 ) {
          newIndex = 0;
        }
        break;
      case NEXT:
        newIndex = aOldIndex + 1;
        if( newIndex >= list.size() ) {
          newIndex = list.size() - 1;
        }
        break;
      case LAST:
        newIndex = list.size() - 1;
        break;
      case MIDDLE:
        newIndex = list.size() / 2;
        break;
      case JUMP_PREV:
        newIndex = aOldIndex - calcJump();
        if( newIndex < 0 ) {
          newIndex = 0;
        }
        break;
      case JUMP_NEXT:
        newIndex = aOldIndex + calcJump();
        if( newIndex >= list.size() ) {
          newIndex = list.size() - 1;
        }
        break;
      case NONE:
        newIndex = aOldIndex;
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return move( aOldIndex, newIndex );
  }

  @Override
  public boolean swap( E aElem1, E aElem2 ) {
    int index1 = list.indexOf( aElem1 );
    int index2 = list.indexOf( aElem2 );
    if( index1 < 0 || index2 < 0 ) {
      throw new TsItemNotFoundRtException();
    }
    return swap( index1, index2 );
  }

  @Override
  public boolean move( E aElem, int aNewIndex ) {
    int oldIndex = list.indexOf( aElem );
    if( oldIndex < 0 ) {
      throw new TsItemNotFoundRtException();
    }
    return move( oldIndex, aNewIndex );
  }

  @Override
  public boolean move( E aElem, ETsCollMove aDir ) {
    int oldIndex = list.indexOf( aElem );
    if( oldIndex < 0 ) {
      throw new TsItemNotFoundRtException();
    }
    return move( oldIndex, aDir );
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public void sort( Comparator<E> aComparator ) {
    TsNullArgumentRtException.checkNull( aComparator );
    if( list.size() > 1 && !(list instanceof ITsSortedCollectionTag) ) {
      IList<E> snapshot = makeSnapshot();
      Object[] array = list.toArray();
      Arrays.sort( array, 0, array.length, (Comparator)aComparator );
      list.setAll( (E[])array );
      doProcessActualChange( snapshot );
    }
  }

  // ------------------------------------------------------------------------------------
  // To implement/override
  //

  /**
   * Called after list was actually changed.
   * <p>
   * Does nothing in the base class, there is no need to call superclass method when overriding.
   *
   * @param aOld {@link IList}&lt;E&gt; - the list content order before change
   */
  protected void doProcessActualChange( IList<E> aOld ) {
    // nop
  }

}
