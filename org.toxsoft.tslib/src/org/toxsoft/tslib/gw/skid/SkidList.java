package org.toxsoft.tslib.gw.skid;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.toxsoft.tslib.bricks.keeper.IKeepableEntity;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.tslib.coll.impl.*;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.coll.primtypes.IStringListBasicEdit;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link ISkidList}.
 *
 * @author hazard157
 */
public class SkidList
    implements ISkidList, IListEdit<Skid>, IKeepableEntity, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Минимально допустимое количество элементов в связке.
   */
  public static final int MIN_BUNDLE_CAPACITY = 4;

  /**
   * максимально допустимое количество элементов в связке.
   */
  public static final int MAX_BUNDLE_CAPACITY = 268435456; // 2 ^ 28

  private static final int DEFAULT_BUNDLE_CAPACITY = 32;

  private final IListEdit<Skid> list;

  /**
   * Конструктор.
   */
  public SkidList() {
    list = new ElemArrayList<>();
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource ITsCollection&lt;{@link Skid}&gt; - список элементов - источник
   * @throws TsNullArgumentRtException aSource = null
   */
  public SkidList( ITsCollection<Skid> aSource ) {
    list = new ElemArrayList<>( aSource );
  }

  /**
   * Создает список с начальным содержимым набора или массива aElems.
   *
   * @param aElems {@link Skid}[] - массив элементов
   * @throws TsNullArgumentRtException любой элемент = null
   */
  public SkidList( Skid... aElems ) {
    list = new ElemArrayList<>( aElems );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource Collection&lt;{@link Skid}&gt; - итератор по набору элементов
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException любой элемент aSource = null
   */
  public SkidList( Collection<Skid> aSource ) {
    list = new ElemArrayList<>( aSource );
  }

  private SkidList( IListEdit<Skid> aList, @SuppressWarnings( "unused" ) boolean FooParam ) {
    list = aList;
  }

  /**
   * Создает список с прямым запоминанием ссылки на список.
   *
   * @param aList {@link IListEdit} - используемыфй список
   * @return {@link SkidList} - созданный список
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргмуент не реализует {@link ITsFastIndexListTag}
   */
  public static SkidList createDirect( IListEdit<Skid> aList ) {
    TsNullArgumentRtException.checkNull( aList );
    TsIllegalArgumentRtException.checkFalse( aList instanceof ITsFastIndexListTag );
    return new SkidList( aList, false );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IList
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
  // Реализация интерфейса IListEdit
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
  // Реализация интерфейса ISkidList
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
  public IList<Skid> listObjSkidsOfClass( String aClassId ) {
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
  // Реализация интерфейса IKeepableEntity
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
