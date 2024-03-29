package org.toxsoft.core.tslib.bricks.keeper.std;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Keeper of the {@link ILongList} .
 * <p>
 * Read references may be safely casted to {@link ILongListEdit}.
 *
 * @author hazard157
 */
public class LongListKeeper
    extends AbstractEntityKeeper<ILongList> {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "LongList"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ILongList> KEEPER = new LongListKeeper();

  /**
   * An empty list KTOR representation.
   */
  public static final String EMPTY_LIST = KEEPER.ent2str( ILongList.EMPTY );

  private LongListKeeper() {
    super( ILongList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, ILongList aEntity ) {
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    for( int i = 0, n = aEntity.size(); i < n; i++ ) {
      aSw.writeLong( aEntity.getValue( i ) );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_ARRAY_END );
  }

  @Override
  protected ILongList doRead( IStrioReader aSr ) {
    ILongListEdit result = new LongLinkedBundleList();
    if( aSr.readArrayBegin() ) {
      do {
        result.add( aSr.readLong() );
      } while( aSr.readArrayNext() );
    }
    return result;
  }

}
