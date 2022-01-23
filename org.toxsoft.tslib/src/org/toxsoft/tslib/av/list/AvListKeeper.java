package org.toxsoft.tslib.av.list;

import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.tslib.coll.impl.TsCollectionsUtils.*;

import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.AtomicValueKeeper;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;

/**
 * {@link IAvList} keeper.
 * <p>
 * Returned value may be safely casted to the {@link IAvListEdit}.
 * <p>
 * Implementation stores number of elements before elements itself. This approach allows to create size-optimized list
 * {@link ElemLinkedBundleList} when reading keeped value.
 *
 * @author hazard157
 */
class AvListKeeper
    extends AbstractEntityKeeper<IAvList> {

  private final boolean indent;

  AvListKeeper( boolean aIndent ) {
    super( IAvList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
    indent = aIndent;
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IAvList aEntity ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.writeInt( aEntity.size() );
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    for( int i = 0, n = aEntity.size(); i < n; i++ ) {
      IAtomicValue v = aEntity.get( i );
      AtomicValueKeeper.KEEPER.write( aSw, v );
      if( i < n - 1 ) {
        aSw.writeChar( CHAR_ITEM_SEPARATOR );
      }
      if( indent ) {
        aSw.writeEol();
      }
    }
    aSw.writeChar( CHAR_ARRAY_END );
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected IAvList doRead( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    int size = aSr.readInt();
    int bundleCapacity = getListInitialCapacity( estimateOrder( 2 * size ) );
    IAvListEdit result = new AvList( new ElemLinkedBundleList<>( bundleCapacity, false ) );
    if( aSr.readArrayBegin() ) {
      do {
        result.add( AtomicValueKeeper.KEEPER.read( aSr ) );
      } while( aSr.readArrayNext() );
    }
    aSr.ensureChar( CHAR_SET_END );
    return result;
  }

}
