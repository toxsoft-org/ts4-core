package org.toxsoft.core.tslib.gw.skid;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISkidList} implementation.
 *
 * @author hazard157
 */
public class SkidList
    implements ISkidList, IListEdit<Skid>, IKeepableEntity, Serializable {

  /**
   * FIXME add caching of the generated lists
   */

  private static final long serialVersionUID = 157157L;

  private final IListEdit<Skid> list;

  /**
   * Constructor.
   */
  public SkidList() {
    list = new ElemArrayList<>();
  }

  /**
   * Copy constructor.
   *
   * @param aSource ITsCollection&lt;{@link Skid}&gt; - список элементов - источник
   * @throws TsNullArgumentRtException aSource = null
   */
  public SkidList( ITsCollection<Skid> aSource ) {
    list = new ElemArrayList<>( aSource );
  }

  /**
   * Constructor.
   *
   * @param aElems {@link Skid}[] - initial content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkidList( Skid... aElems ) {
    list = new ElemArrayList<>( aElems );
  }

  /**
   * Constructor.
   *
   * @param aSource Collection&lt;{@link Skid}&gt; - initial content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkidList( Collection<Skid> aSource ) {
    list = new ElemArrayList<>( aSource );
  }

  private SkidList( IListEdit<Skid> aList, @SuppressWarnings( "unused" ) boolean FooParam ) {
    list = aList;
  }

  /**
   * Static constructor uses source list.
   *
   * @param aList {@link IListEdit} - the list used for SKIDs storing
   * @return {@link SkidList} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException not implements {@link ITsFastIndexListTag}
   */
  public static SkidList createDirect( IListEdit<Skid> aList ) {
    TsNullArgumentRtException.checkNull( aList );
    TsIllegalArgumentRtException.checkFalse( aList instanceof ITsFastIndexListTag );
    return new SkidList( aList, false );
  }

  /**
   * Creates non-empty {@link SkidList} with specified number of {@link Skid#NONE} elements.
   *
   * @param aCount int - number of elements
   * @return {@link SkidList} - the created instance
   * @throws TsIllegalArgumentRtException argument is 0 or negative number
   */
  public static SkidList createNones( int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aCount < 1 );
    SkidList sl = new SkidList();
    for( int i = 0; i < aCount; i++ ) {
      sl.add( Skid.NONE );
    }
    return sl;
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( Skid aElem ) {
    return list.indexOf( aElem );
  }

  @Override
  public Skid get( int aIndex ) {
    return list.get( aIndex );
  }

  @Override
  public boolean hasElem( Skid aElem ) {
    return list.hasElem( aElem );
  }

  @Override
  public Iterator<Skid> iterator() {
    return list.iterator();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public Skid[] toArray( Skid[] aSrcArray ) {
    return list.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  // ------------------------------------------------------------------------------------
  // IListEdit
  //

  @Override
  public int remove( Skid aElem ) {
    return list.remove( aElem );
  }

  @Override
  public Skid removeByIndex( int aIndex ) {
    return list.removeByIndex( aIndex );
  }

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    list.removeRangeByIndex( aIndex, aCount );
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public int add( Skid aElem ) {
    return list.add( aElem );
  }

  @Override
  public void addAll( Skid... aArray ) {
    list.addAll( aArray );
  }

  @Override
  public void addAll( ITsCollection<Skid> aElemList ) {
    list.addAll( aElemList );
  }

  @Override
  public void addAll( Collection<Skid> aElemColl ) {
    list.addAll( aElemColl );
  }

  @Override
  public void setAll( ITsCollection<Skid> aColl ) {
    list.setAll( aColl );
  }

  @Override
  public void setAll( Collection<Skid> aColl ) {
    list.setAll( aColl );
  }

  @Override
  public void setAll( Skid... aElems ) {
    list.setAll( aElems );
  }

  @Override
  public Skid set( int aIndex, Skid aElem ) {
    return list.set( aIndex, aElem );
  }

  @Override
  public void insert( int aIndex, Skid aElem ) {
    list.insert( aIndex, aElem );
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<Skid> aElemList ) {
    list.insertAll( aIndex, aElemList );
  }

  @Override
  public void insertAll( int aIndex, Collection<Skid> aElemColl ) {
    list.insertAll( aIndex, aElemColl );
  }

  @Override
  public void insertAll( int aIndex, Skid... aArray ) {
    list.insertAll( aIndex, aArray );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return list.toString();
  }

  @Override
  public int hashCode() {
    return list.hashCode();
  }

  @Override
  public boolean equals( Object obj ) {
    return list.equals( obj );
  }

  // ------------------------------------------------------------------------------------
  // ISkidList
  //

  @Override
  public IStringList classIds() {
    IStringListBasicEdit classIds = new SortedStringLinkedBundleList( DEFAULT_BUNDLE_CAPACITY, false );
    for( Skid s : list ) {
      classIds.add( s.classId() );
    }
    return classIds;
  }

  @Override
  public IList<Skid> listSkidsOfClass( String aClassId ) {
    TsNullArgumentRtException.checkNull( aClassId );
    IListEdit<Skid> ll = new ElemLinkedBundleList<>();
    for( Skid skid : list ) {
      if( skid.classId().equals( aClassId ) ) {
        ll.add( skid );
      }
    }
    return ll;
  }

  @Override
  public IStringList listStridsOfClass( String aClassId ) {
    TsNullArgumentRtException.checkNull( aClassId );
    IStringListEdit ll = new StringLinkedBundleList();
    for( Skid skid : list ) {
      if( skid.classId().equals( aClassId ) ) {
        ll.add( skid.strid() );
      }
    }
    return ll;
  }

  @Override
  public Skid findDuplicateSkid() {
    if( list.isEmpty() ) {
      return null;
    }
    for( int i = 0, n = list.size(); i < n; i++ ) {
      Skid skid = list.get( i );
      for( int j = i + 1; j < n; j++ ) {
        if( skid.equals( list.get( j ) ) ) {
          return skid;
        }
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aSw ) {
    Skid.KEEPER.writeColl( aSw, list, false );
  }

  @Override
  public void read( IStrioReader aSr ) {
    Skid.KEEPER.readColl( aSr, list );
  }

}
