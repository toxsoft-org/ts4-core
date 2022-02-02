package org.toxsoft.core.tslib.av.list;

import java.util.Iterator;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * An {@link IAvList} implementation as wrapper over any {@link IListEdit}.
 *
 * @author hazard157
 */
public class AvList
    implements IAvListEdit {

  private final IListEdit<IAtomicValue> source;

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "AvList"; //$NON-NLS-1$

  /**
   * Keeper singleton (does not indents text representation).
   */
  public static final IEntityKeeper<IAvList> KEEPER = new AvListKeeper( false );

  /**
   * Keeper singleton (does indents text representation).
   */
  public static final IEntityKeeper<IAvList> KEEPER_INDENTED = new AvListKeeper( true );

  /**
   * Constructor.
   *
   * @param aSource {@link IListEdit}&lt;{@link IAtomicValue}&gt; - wrapped list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AvList( IListEdit<IAtomicValue> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    source = aSource;
  }

  // ------------------------------------------------------------------------------------
  // IAvList
  //

  @Override
  public int indexOf( IAtomicValue aElem ) {
    return source.indexOf( aElem );
  }

  @Override
  public IAtomicValue get( int aIndex ) {
    return source.get( aIndex );
  }

  @Override
  public boolean hasElem( IAtomicValue aElem ) {
    return source.hasElem( aElem );
  }

  @Override
  public IAtomicValue[] toArray( IAtomicValue[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  @Override
  public Iterator<IAtomicValue> iterator() {
    return source.iterator();
  }

  @Override
  public int size() {
    return source.size();
  }

  @Override
  public IAtomicValue[] toArray() {
    return source.toArray( TsLibUtils.EMPTY_AV_ARRAY );
  }

  // ------------------------------------------------------------------------------------
  // IAvListEdit
  //

  @Override
  public IAtomicValue set( int aIndex, IAtomicValue aElem ) {
    return source.set( aIndex, aElem );
  }

  @Override
  public void insert( int aIndex, IAtomicValue aElem ) {
    source.insert( aIndex, aElem );
  }

  @Override
  public int add( IAtomicValue aElem ) {
    return source.add( aElem );
  }

  @Override
  public int remove( IAtomicValue aElem ) {
    return source.remove( aElem );
  }

  @Override
  public IAtomicValue removeByIndex( int aIndex ) {
    // TODO Auto-generated method stub
    return source.removeByIndex( aIndex );
  }

  @Override
  public void clear() {
    source.clear();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return source.toString();
  }

  @Override
  public int hashCode() {
    return source.hashCode();
  }

  @Override
  public boolean equals( Object aObj ) {
    return source.equals( aObj );
  }

}
