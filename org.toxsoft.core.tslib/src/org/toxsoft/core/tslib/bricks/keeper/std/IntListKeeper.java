package org.toxsoft.core.tslib.bricks.keeper.std;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Keeper of the {@link IIntList} .
 * <p>
 * Read references may be safely casted to {@link IIntListEdit}.
 *
 * @author hazard157
 */
public class IntListKeeper
    extends AbstractEntityKeeper<IIntList> {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "IntList"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IIntList> KEEPER = new IntListKeeper();

  /**
   * An empty list KTOR representation.
   */
  public static final String EMPTY_LIST = KEEPER.ent2str( IIntList.EMPTY );

  private IntListKeeper() {
    super( IIntList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IIntList aEntity ) {
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    for( int i = 0, n = aEntity.size(); i < n; i++ ) {
      aSw.writeInt( aEntity.getValue( i ) );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_ARRAY_END );
  }

  @Override
  protected IIntList doRead( IStrioReader aSr ) {
    IIntListEdit result = new IntLinkedBundleList();
    if( aSr.readArrayBegin() ) {
      do {
        result.add( aSr.readInt() );
      } while( aSr.readArrayNext() );
    }
    return result;
  }

}
