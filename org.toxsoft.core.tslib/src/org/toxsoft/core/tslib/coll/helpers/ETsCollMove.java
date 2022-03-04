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
 * The main method for navigating over collection is {@link #navigateTo(int, int, int, boolean, boolean)}, other methods
 * <code>moveToXxx()</code>, <code>wrapToXxx()</code>, <code>findElemAtXxx()</code> call it.
 * <p>
 * Navigation means that there is some starting element (index) and after movement there will be ending element (index).
 * Navigation has different modes determoned byt the flags:
 * <ul>
 * <li><b>wrapping</b> - in wrapping mode navigation continues when reaching collection boudaries like list is the
 * circular buffer (navigation <i>wraps</i> over collection). In non-wrapping mode navigation stops at collection
 * boundaries;</li>
 * <li><b>NoneItem</b> - NoneItem is "virtual" item located before first element (at index -1) and is closly related to
 * the consept of "selected item" in GUI lists. "Selecting" NoneItem means to select no elemat. Navigating with NoneItem
 * (methods with siffix <b>Wni</b>) may end at NoneItem (return index -1). Note, that in wrapping mode NoneItem is
 * located before first and after last element of the collection.</li>
 * </ul>
 *
 * @author hazard157
 */
public enum ETsCollMove
    implements IStridable {

  @SuppressWarnings( "javadoc" )
  NONE("None", STR_N_TCM_NONE, STR_D_TCM_NONE ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aStartIndex;
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aStartIndex;
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aStartIndex;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aStartIndex;
    }

  },

  @SuppressWarnings( "javadoc" )
  FIRST("First", STR_N_TCM_FIRST, STR_D_TCM_FIRST ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return 0;
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return 0;
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return 0;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return 0;
    }

  },

  @SuppressWarnings( "javadoc" )
  MIDDLE("Middle", STR_N_TCM_MIDDLE, STR_D_TCM_MIDDLE ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize / 2;
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize / 2;
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize / 2;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize / 2;
    }

  },

  @SuppressWarnings( "javadoc" )
  LAST("Last", STR_N_TCM_LAST, STR_D_TCM_LAST ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize - 1;
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize - 1;
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize - 1;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      return aCollSize - 1;
    }
  },

  @SuppressWarnings( "javadoc" )
  PREV("Prev", STR_N_TCM_PREV, STR_D_TCM_PREV ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex - 1;
      if( newIndex < 0 ) {
        newIndex = -1;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      if( aStartIndex == -1 ) {
        return aCollSize - 1;
      }
      return aStartIndex - 1;
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex - 1;
      if( newIndex < 0 ) {
        newIndex = 0;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex - 1;
      if( newIndex < 0 ) {
        return aCollSize - 1;
      }
      return newIndex;
    }

  },

  @SuppressWarnings( "javadoc" )
  NEXT("Next", STR_N_TCM_NEXT, STR_D_TCM_NEXT ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex + 1;
      if( newIndex >= aCollSize ) {
        newIndex = aCollSize - 1;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      if( aStartIndex >= aCollSize - 1 ) {
        return -1;
      }
      return aStartIndex + 1;
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int newIndex = aStartIndex + 1;
      if( newIndex >= aCollSize ) {
        newIndex = aCollSize - 1;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      if( aStartIndex >= aCollSize - 1 ) {
        return 0;
      }
      return aStartIndex + 1;
    }

  },

  @SuppressWarnings( "javadoc" )
  JUMP_PREV("JumpPrev", STR_N_TCM_JUMP_PREV, STR_D_TCM_JUMP_PREV ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % (aCollSize + 1);
      // create "virtual" collection of size (aCollSize+1) with NoneItem at index 0
      int startIndex = aStartIndex + 1;
      int collSize = aCollSize + 1;
      int newIndex = startIndex + jumpDist;
      if( newIndex >= collSize ) {
        newIndex = collSize - 1;
      }
      return newIndex - 1; // indexation back to the original collection
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % (aCollSize + 1);
      // create "virtual" collection of size (aCollSize+1) with NoneItem at index 0
      int startIndex = aStartIndex + 1;
      int collSize = aCollSize + 1;
      int newIndex = startIndex + jumpDist;
      if( newIndex >= collSize ) {
        newIndex -= collSize;
      }
      return newIndex - 1; // indexation back to the original collection
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % aCollSize;
      int newIndex = aStartIndex + jumpDist;
      if( newIndex >= aCollSize ) {
        newIndex -= aCollSize;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % aCollSize;
      int newIndex = aStartIndex + jumpDist;
      if( newIndex >= aCollSize ) {
        newIndex -= aCollSize;
      }
      return newIndex;
    }

  },

  @SuppressWarnings( "javadoc" )
  JUMP_NEXT("JumpNext", STR_N_TCM_JUMP_NEXT, STR_D_TCM_JUMP_NEXT ) { //$NON-NLS-1$

    @Override
    protected int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      // create "virtual" collection of size (aCollSize+1) with NoneItem at index 0
      int collSize = aCollSize + 1;
      int startIndex = aStartIndex + 1;
      int jumpDist = aJumpDistance % collSize;
      int newIndex = startIndex - jumpDist;
      if( newIndex < 0 ) {
        newIndex = 0;
      }
      return newIndex - 1; // indexation back to the original collection
    }

    @Override
    protected int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance ) {
      // create "virtual" collection of size (aCollSize+1) with NoneItem at index 0
      int collSize = aCollSize + 1;
      int startIndex = aStartIndex + 1;
      int jumpDist = aJumpDistance % collSize;
      int newIndex = startIndex - jumpDist;
      if( newIndex < 0 ) {
        newIndex += collSize;
      }
      return newIndex - 1; // indexation back to the original collection
    }

    @Override
    protected int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % aCollSize;
      int newIndex = aStartIndex - jumpDist;
      if( newIndex < 0 ) {
        newIndex = 0;
      }
      return newIndex;
    }

    @Override
    protected int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance ) {
      int jumpDist = aJumpDistance % aCollSize;
      int newIndex = aStartIndex - jumpDist;
      if( newIndex < 0 ) {
        newIndex += aCollSize;
      }
      return newIndex;
    }

  };

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "ETsCollMove"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsCollMove> KEEPER = new StridableEnumKeeper<>( ETsCollMove.class );

  private static IStridablesListEdit<ETsCollMove> list = null;

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
  ETsCollMove( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * The difference between the two methods is that the {@link #doMoveToIndexWithNoneItem(int, int, int)} stops at
   * collection boundaries, while {@link #doWrapToIndexOnlyColl(int, int, int)} wraps over collection. Note that index
   * -1 is considered as part of the collection.
   *
   * @param aStartIndex int - index to start moving (always in range 0 .. aCollSize-1)
   * @param aCollSize int - size of the collection (always >= 0)
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range 0 .. aCollSize-1
   */
  protected abstract int doMoveToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance );

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * The difference between the two methods is that the {@link #doMoveToIndexWithNoneItem(int, int, int)} stops at
   * collection boundaries, while {@link #doWrapToIndexOnlyColl(int, int, int)} wraps over collection. Note that index
   * -1 is considered as part of the collection.
   *
   * @param aStartIndex int - index to start moving (always in range 0 .. aCollSize-1)
   * @param aCollSize int - size of the collection (always >= 0)
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range 0 .. aCollSize-1
   */
  protected abstract int doMoveToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance );

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * The difference between the two methods is that the {@link #doMoveToIndexWithNoneItem(int, int, int)} stops at
   * collection boundaries, while {@link #doWrapToIndexOnlyColl(int, int, int)} wraps over collection. Note that index
   * -1 is considered as part of the collection.
   *
   * @param aStartIndex int - index to start moving (always in range 0 .. aCollSize-1)
   * @param aCollSize int - size of the collection (always >= 0)
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range 0 .. aCollSize-1
   */
  protected abstract int doWrapToIndexWithNoneItem( int aStartIndex, int aCollSize, int aJumpDistance );

  /**
   * Determines the index after moving by the value specified by this constant.
   * <p>
   * The difference between the two methods is that the {@link #doMoveToIndexWithNoneItem(int, int, int)} stops at
   * collection boundaries, while {@link #doWrapToIndexOnlyColl(int, int, int)} wraps over collection. Note that index
   * -1 is considered as part of the collection.
   *
   * @param aStartIndex int - index to start moving (always in range 0 .. aCollSize-1)
   * @param aCollSize int - size of the collection (always >= 0)
   * @param aJumpDistance - number of elements to bypass for JUMP_XXX (always >= 1)
   * @return int - index of new position, must be in range 0 .. aCollSize-1
   */
  protected abstract int doWrapToIndexOnlyColl( int aStartIndex, int aCollSize, int aJumpDistance );

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
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link ETsCollMove} &gt; - list of all constants in order of declaration
   */
  public static IStridablesList<ETsCollMove> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns index of item after navigatind at moveing at specified amount.
   *
   * @param aStartIndex int - starting index of the movement (in range -1 .. aColSize-1)
   * @param aCollSize int - number of elements in collection (must be >= 0)
   * @param aJumpDistance int - number of bypassed elements for jummp movements (must be >=1)
   * @param aWrap boolean - the flag to wrap around collection
   * @param aNoneItem boolean - the flag for usage of the "NoneItem" at index of -1
   * @return int - index of element after movement in range -1 .. aCollSize-1
   * @throws TsIllegalArgumentRtException any argument is out of range
   */
  public int navigateTo( int aStartIndex, int aCollSize, int aJumpDistance, boolean aWrap, boolean aNoneItem ) {
    TsIllegalArgumentRtException.checkTrue( aCollSize < 0 );
    if( aCollSize == 0 ) {
      return -1;
    }
    TsIllegalArgumentRtException.checkTrue( aStartIndex < -1 || aStartIndex >= aCollSize );
    TsIllegalArgumentRtException.checkTrue( aJumpDistance < 1 );
    if( aNoneItem ) {
      if( aWrap ) {
        return doWrapToIndexWithNoneItem( aStartIndex, aCollSize, aJumpDistance );
      }
      return doMoveToIndexWithNoneItem( aStartIndex, aCollSize, aJumpDistance );
    }
    if( aWrap ) {
      return doWrapToIndexOnlyColl( aStartIndex, aCollSize, aJumpDistance );
    }
    return doMoveToIndexOnlyColl( aStartIndex, aCollSize, aJumpDistance );
  }

  @SuppressWarnings( "javadoc" )
  public int moveTo( int aStartIndex, int aCollSize, int aJumpDistance ) {
    return navigateTo( aStartIndex, aCollSize, aJumpDistance, false, false );
  }

  @SuppressWarnings( "javadoc" )
  public int wrapTo( int aStartIndex, int aCollSize, int aJumpDistance ) {
    return navigateTo( aStartIndex, aCollSize, aJumpDistance, true, false );
  }

  @SuppressWarnings( "javadoc" )
  public int moveToWni( int aStartIndex, int aCollSize, int aJumpDistance ) {
    return navigateTo( aStartIndex, aCollSize, aJumpDistance, false, true );
  }

  @SuppressWarnings( "javadoc" )
  public int wrapToWni( int aStartIndex, int aCollSize, int aJumpDistance ) {
    return navigateTo( aStartIndex, aCollSize, aJumpDistance, true, true );
  }

  @SuppressWarnings( "javadoc" )
  public <E> E findElemAt( E aCurrElem, IList<E> aColl, int aJumpDistance, boolean aWrap ) {
    TsNullArgumentRtException.checkNull( aColl );
    int startIndex = aCurrElem != null ? aColl.indexOf( aCurrElem ) : -1;
    int index = navigateTo( startIndex, aColl.size(), aJumpDistance, aWrap, false );
    return index >= 0 ? aColl.get( index ) : null;
  }

  @SuppressWarnings( "javadoc" )
  public <E> E findElemAtWni( E aCurrElem, IList<E> aColl, int aJumpDistance, boolean aWrap ) {
    TsNullArgumentRtException.checkNull( aColl );
    int startIndex = aCurrElem != null ? aColl.indexOf( aCurrElem ) : -1;
    int index = navigateTo( startIndex, aColl.size(), aJumpDistance, aWrap, true );
    return index >= 0 ? aColl.get( index ) : null;
  }

  // ----------------------------------------------------------------------------------
  // Find & get
  //

  /**
   * Finds constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return {@link ETsCollMove} - found constant or <code>null</code> if no constant exists with specified identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ETsCollMove findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Returns constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return {@link ETsCollMove} - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ETsCollMove getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - identifier of constant to search for
   * @return {@link ETsCollMove} - found constant or <code>null</code> if no constant exists with specified name
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ETsCollMove findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsCollMove item : asList() ) {
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
   * @return {@link ETsCollMove} - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ETsCollMove getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
