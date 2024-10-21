package org.toxsoft.core.tslib.coll.helpers;

import static org.toxsoft.core.tslib.coll.helpers.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Typical movement when navigating through a collection.
 * <p>
 * There is a consept of
 *
 * @author hazard157
 */
public enum ETsCollMove_old
    implements IStridable {

  @SuppressWarnings( "javadoc" )
  NONE("None", STR_TCM_NONE, STR_TCM_NONE_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aStartIndex;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aStartIndex;
    }
  },

  @SuppressWarnings( "javadoc" )
  FIRST("First", STR_TCM_FIRST, STR_TCM_FIRST_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return 0;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return doMoveToIndex( aStartIndex, aCollSize, aJumpDistance );
    }
  },

  @SuppressWarnings( "javadoc" )
  MIDDLE("Middle", STR_TCM_MIDDLE, STR_TCM_MIDDLE_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize / 2;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return doMoveToIndex( aStartIndex, aCollSize, aJumpDistance );
    }
  },

  @SuppressWarnings( "javadoc" )
  LAST("Last", STR_TCM_LAST, STR_TCM_LAST_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize - 1;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return doMoveToIndex( aStartIndex, aCollSize, aJumpDistance );
    }
  },

  @SuppressWarnings( "javadoc" )
  PREV("Prev", STR_TCM_PREV, STR_TCM_PREV_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex - 1;
      if( newIndex < 0 ) {
        newIndex = 0;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      if( aStartIndex == -1 ) {
        return aCollSize - 1;
      }
      return aStartIndex - 1;
    }
  },

  @SuppressWarnings( "javadoc" )
  NEXT("Next", STR_TCM_NEXT, STR_TCM_NEXT_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex + 1;
      if( newIndex >= aCollSize ) {
        newIndex = aCollSize - 1;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      if( aStartIndex >= aCollSize - 1 ) {
        return -1;
      }
      return aStartIndex + 1;
    }
  },

  @SuppressWarnings( "javadoc" )
  JUMP_PREV("JumpPrev", STR_TCM_JUMP_PREV, STR_TCM_JUMP_PREV_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex - aJumpDistance;
      if( newIndex < 0 ) {
        newIndex = 0;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % (aCollSize + 1);
      int newIndex = (aStartIndex + 1) - jumpDist + aCollSize + 1;
      newIndex = newIndex % (aCollSize + 1);
      return newIndex - 1;
    }
  },

  @SuppressWarnings( "javadoc" )
  JUMP_NEXT("JumpNext", STR_TCM_JUMP_NEXT, STR_TCM_JUMP_NEXT_D ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex + aJumpDistance;
      if( newIndex >= aCollSize ) {
        newIndex = aCollSize - 1;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % (aCollSize + 1);
      int newIndex = (aStartIndex + 1) + jumpDist;
      newIndex = newIndex % (aCollSize + 1);
      return newIndex - 1;
    }
  };

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "ETsCollMove"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsCollMove_old> KEEPER = new StridableEnumKeeper<>( ETsCollMove_old.class );

  private static IStridablesListEdit<ETsCollMove_old> list = null;

  private final String id;
  private final String name;
  private final String description;

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDPath)
   * @param aName String - short, human-readable name
   * @param aDescription String - description
   */
  ETsCollMove_old( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * The difference between the two methods is that the {@link #doMoveToIndex(int, int, int)} stops at collection
   * boundaries, while {@link #doWrapToIndex(int, int, int)} wraps over collection. Note that index -1 is considered as
   * part of the collection.
   *
   * @param aStartIndex int - index to start moving (always in range -1 .. aCollSize-1)
   * @param aCollSize int - size of the collection (always >= 0)
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range -1 .. aCollSize-1
   */
  protected abstract int doMoveToIndex( int aStartIndex, int aCollSize, int aJumpDistance );

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * The difference between the two methods is that the {@link #doMoveToIndex(int, int, int)} stops at collection
   * boundaries, while {@link #doWrapToIndex(int, int, int)} wraps over collection. Note that index -1 is considered as
   * part of the collection.
   *
   * @param aStartIndex int - index to start moving (always in range -1 .. aCollSize-1)
   * @param aCollSize int - size of the collection (always >= 0)
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range -1 .. aCollSize-1
   */
  protected abstract int doWrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance );

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Двигаться по коллекции можно в следующих режимах:
   * <ul>
   * <li>wrap - ходить по кругу;</li>
   * <li>no sel - есть "виртуальный" элемент с индексом -1;</li>
   * </ul>
   */
  // TODO сейчас нет варианта без выделения "виртуального" элемента

  /**
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link ETsCollMove_old} &gt; - list of all constants in order of declaration
   */
  public static IStridablesList<ETsCollMove_old> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Determines the index after moving (without wrapping) by the value specified by this constant.
   *
   * @param aStartIndex int - index to start moving (always in range -1 .. aCollSize-1)
   * @param aCollSize int - the size of the collection
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range -1 .. aCollSize-1
   * @throws TsIllegalArgumentRtException aCollSize < 0
   * @throws TsIllegalArgumentRtException aStartIndex is out of range
   * @throws TsIllegalArgumentRtException aJumpDistance < 1
   */
  public int moveToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
    TsIllegalArgumentRtException.checkTrue( aCollSize < 0 );
    if( aCollSize == 0 ) {
      return -1;
    }
    TsIllegalArgumentRtException.checkTrue( aStartIndex < -1 || aStartIndex >= aCollSize );
    TsIllegalArgumentRtException.checkTrue( aJumpDistance < 1 );
    return doMoveToIndex( aStartIndex, aCollSize, aJumpDistance );
  }

  /**
   * Determines the index after moving (with wrapping) by the value specified by this constant.
   *
   * @param aStartIndex int - index to start moving (always in range -1 .. aCollSize-1)
   * @param aCollSize int - the size of the collection
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range -1 .. aCollSize-1
   * @throws TsIllegalArgumentRtException aCollSize < 0
   * @throws TsIllegalArgumentRtException aStartIndex is out of range
   * @throws TsIllegalArgumentRtException aJumpDistance < 1
   */
  public int wrapToIndex( int aStartIndex, int aCollSize, int aJumpDistance ) {
    TsIllegalArgumentRtException.checkTrue( aCollSize < 0 );
    if( aCollSize == 0 ) {
      return -1;
    }
    TsIllegalArgumentRtException.checkTrue( aStartIndex < -1 || aStartIndex >= aCollSize );
    TsIllegalArgumentRtException.checkTrue( aJumpDistance < 1 );
    return doWrapToIndex( aStartIndex, aCollSize, aJumpDistance );
  }

  /**
   * Returns element the index after moving by the value specified by this constant.
   * <p>
   * This method returns element at index returned by the method {@link #findNewPos(Object, IList, int, boolean)}.
   * <p>
   * If <code>aStartingItem</code> is <code>null</code> or not in collection, it is considered as starting index of -1.
   *
   * @param <E> - collection elements type
   * @param aStartingItem &lt;E&gt; - element to start moving (always in range -1 .. aCollSize-1)
   * @param aItems {@link IList} - the collection
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @param aWrap boolean - a flag indicates that the move should wrap around the collection
   * @return int - index of new position, must be in range -1 .. aCollSize-1
   */
  public <E> E findItemAtNewPos( E aStartingItem, IList<E> aItems, int aJumpDistance, boolean aWrap ) {
    int newIndex = findNewPos( aStartingItem, aItems, aJumpDistance, aWrap );
    return newIndex >= 0 ? aItems.get( newIndex ) : null;
  }

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * This method returns the same value as the method {@link #findNewPos(Object, IList, int, boolean)}.
   * <p>
   * If <code>aStartingItem</code> is <code>null</code> or not in collection, it is considered as starting index of -1.
   *
   * @param <E> - collection elements type
   * @param aStartingItem &lt;E&gt; - element to start moving (always in range -1 .. aCollSize-1)
   * @param aItems {@link IList} - the collection
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @param aWrap boolean - a flag indicates that the move should wrap around the collection
   * @return int - index of new position, must be in range -1 .. aCollSize-1
   */
  public <E> int findNewPos( E aStartingItem, IList<E> aItems, int aJumpDistance, boolean aWrap ) {
    if( aItems == null ) {
      return -1;
    }
    int currIndex = (aStartingItem != null) ? aItems.indexOf( aStartingItem ) : -1;
    int newIndex;
    if( aWrap ) {
      newIndex = wrapToIndex( currIndex, aItems.size(), aJumpDistance );
    }
    else {
      newIndex = moveToIndex( currIndex, aItems.size(), aJumpDistance );
    }
    return newIndex;
  }

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * If argument <code>aItems</code> is null then method returns -1.
   * <p>
   * Depending on the value of the <code>aWrap</code> argument, moing either stops at collection boundaries
   * (<code>aWrap=false</code>), or wraps over the collection (<code>aWrap=true</code>). Note that index -1 is
   * considered as part of the collection.
   * <p>
   * Method does not throws an exception. Invalid argument values will be fitted to allowed ranges.
   *
   * @param <E> - collection elements type
   * @param aStartIndex int - index to start moving (always in range -1 .. aCollSize-1)
   * @param aItems {@link IList} - the collection
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @param aWrap boolean - a flag indicates that the move should wrap around the collection
   * @return int - index of new position, must be in range -1 .. aCollSize-1
   */
  public <E> int findNewPos( int aStartIndex, IList<E> aItems, int aJumpDistance, boolean aWrap ) {
    if( aItems == null ) {
      return -1;
    }
    int jumpDistance = aJumpDistance;
    if( jumpDistance < 1 ) {
      jumpDistance = 1;
    }
    int startIndex = aStartIndex;
    if( startIndex < -1 || startIndex >= aItems.size() ) {
      startIndex = -1;
    }
    int newIndex;
    if( aWrap ) {
      newIndex = wrapToIndex( startIndex, aItems.size(), jumpDistance );
    }
    else {
      newIndex = moveToIndex( startIndex, aItems.size(), jumpDistance );
    }
    return newIndex;
  }

  // ----------------------------------------------------------------------------------
  // Find & get
  //

  /**
   * Finds constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return {@link ETsCollMove_old} - found constant or <code>null</code> if no constant exists with specified identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ETsCollMove_old findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Returns constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return {@link ETsCollMove_old} - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ETsCollMove_old getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - identifier of constant to search for
   * @return {@link ETsCollMove_old} - found constant or <code>null</code> if no constant exists with specified name
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ETsCollMove_old findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsCollMove_old item : asList() ) {
      if( item.nmName().equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns constant with specified name ({@link #nmName()}).
   *
   * @param aName String - identifier of constant to search for
   * @return {@link ETsCollMove_old} - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ETsCollMove_old getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
