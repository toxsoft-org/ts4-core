package org.toxsoft.tslib.gw.gwid;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.toxsoft.tslib.bricks.keeper.*;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper.EEncloseMode;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.impl.SortedStringLinkedBundleList;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.coll.primtypes.IStringListBasicEdit;
import org.toxsoft.tslib.gw.skid.ISkidList;
import org.toxsoft.tslib.gw.skid.SkidList;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link IGwidList}.
 *
 * @author hazard157
 */
public class GwidList
    implements IGwidList, IListEdit<Gwid>, IKeepableEntity, Serializable {

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "GwidList"; //$NON-NLS-1$

  /**
   * Keeper singleton (indenting keeeper).
   */
  public static final IEntityKeeper<IGwidList> KEEPER_INDENTED =
      new AbstractEntityKeeper<>( IGwidList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, IGwidList.EMPTY ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IGwidList aEntity ) {
          Gwid.KEEPER.writeColl( aSw, aEntity, true );
        }

        @Override
        protected IGwidList doRead( IStrioReader aSr ) {
          IListEdit<Gwid> ll = Gwid.KEEPER.readColl( aSr );
          return GwidList.createDirect( ll );
        }

      };

  /**
   * Keeper singleton (non-indenting keeeper).
   */
  public static final IEntityKeeper<IGwidList> KEEPER =
      new AbstractEntityKeeper<>( IGwidList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, IGwidList.EMPTY ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IGwidList aEntity ) {
          Gwid.KEEPER.writeColl( aSw, aEntity, false );
        }

        @Override
        protected IGwidList doRead( IStrioReader aSr ) {
          IListEdit<Gwid> ll = Gwid.KEEPER.readColl( aSr );
          return GwidList.createDirect( ll );
        }

      };

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

  private final IListEdit<Gwid> list;

  /**
   * Конструктор.
   */
  public GwidList() {
    list = new ElemArrayList<>();
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ITsCollection}&lt;{@link Gwid}&gt; - список элементов - источник
   * @throws TsNullArgumentRtException aSource = null
   */
  public GwidList( ITsCollection<Gwid> aSource ) {
    list = new ElemArrayList<>( aSource );
  }

  /**
   * Создает список с начальным содержимым набора или массива aElems.
   *
   * @param aElems {@link Gwid}[] - массив элементов
   * @throws TsNullArgumentRtException любой элемент = null
   */
  public GwidList( Gwid... aElems ) {
    list = new ElemArrayList<>( aElems );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource Collection&lt;{@link Gwid}&gt; - итератор по набору элементов
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException любой элемент aSource = null
   */
  public GwidList( Collection<Gwid> aSource ) {
    list = new ElemArrayList<>( aSource );
  }

  private GwidList( IListEdit<Gwid> aList, @SuppressWarnings( "unused" ) boolean FooParam ) {
    list = aList;
  }

  /**
   * Создает список с прямым запоминанием ссылки на список.
   *
   * @param aList {@link IListEdit} - используемыфй список
   * @return {@link GwidList} - созданный список
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргмуент не реализует {@link ITsFastIndexListTag}
   */
  public static GwidList createDirect( IListEdit<Gwid> aList ) {
    TsNullArgumentRtException.checkNull( aList );
    TsIllegalArgumentRtException.checkFalse( aList instanceof ITsFastIndexListTag );
    return new GwidList( aList, false );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IList
  //

  @Override
  public int indexOf( Gwid aElem ) {
    return list.indexOf( aElem );
  }

  @Override
  public Gwid get( int aIndex ) {
    return list.get( aIndex );
  }

  @Override
  public boolean hasElem( Gwid aElem ) {
    return list.hasElem( aElem );
  }

  @Override
  public Iterator<Gwid> iterator() {
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
  public Gwid[] toArray( Gwid[] aSrcArray ) {
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
  public int remove( Gwid aElem ) {
    return list.remove( aElem );
  }

  @Override
  public Gwid removeByIndex( int aIndex ) {
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
  public int add( Gwid aElem ) {
    return list.add( aElem );
  }

  @Override
  public void addAll( Gwid... aArray ) {
    list.addAll( aArray );
  }

  @Override
  public void addAll( ITsCollection<Gwid> aElemList ) {
    list.addAll( aElemList );
  }

  @Override
  public void addAll( Collection<Gwid> aElemColl ) {
    list.addAll( aElemColl );
  }

  @Override
  public void setAll( ITsCollection<Gwid> aColl ) {
    list.setAll( aColl );
  }

  @Override
  public void setAll( Collection<Gwid> aColl ) {
    list.setAll( aColl );
  }

  @Override
  public void setAll( Gwid... aElems ) {
    list.setAll( aElems );
  }

  @Override
  public Gwid set( int aIndex, Gwid aElem ) {
    return list.set( aIndex, aElem );
  }

  @Override
  public void insert( int aIndex, Gwid aElem ) {
    list.insert( aIndex, aElem );
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<Gwid> aElemList ) {
    list.insertAll( aIndex, aElemList );
  }

  @Override
  public void insertAll( int aIndex, Collection<Gwid> aElemColl ) {
    list.insertAll( aIndex, aElemColl );
  }

  @Override
  public void insertAll( int aIndex, Gwid... aArray ) {
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
  // IGwidList
  //

  @Override
  public IStringList listClassIds() {
    IStringListBasicEdit classIds = new SortedStringLinkedBundleList( DEFAULT_BUNDLE_CAPACITY, false );
    for( Gwid s : list ) {
      classIds.add( s.classId() );
    }
    return classIds;
  }

  @Override
  public ISkidList objIds() {
    SkidList ll = new SkidList();
    for( Gwid g : this ) {
      if( !g.isAbstract() ) {
        if( !ll.hasElem( g.skid() ) ) {
          ll.add( g.skid() );
        }
      }
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aDw ) {
    Gwid.KEEPER.writeColl( aDw, list, false );
  }

  @Override
  public void read( IStrioReader aDr ) {
    Gwid.KEEPER.readColl( aDr, list );
  }

}
