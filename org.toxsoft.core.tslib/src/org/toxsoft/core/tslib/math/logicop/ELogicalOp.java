package org.toxsoft.core.tslib.math.logicop;

import static org.toxsoft.core.tslib.math.logicop.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Binary logical operations (between two logical arguments).
 *
 * @author hazard157
 */
public enum ELogicalOp
    implements IStridable {

  /**
   * Logical AND.
   */
  AND( "AND", STR_N_OP_AND, STR_D_OP_AND, '&' ) { //$NON-NLS-1$

    @Override
    public boolean op( boolean aLeft, boolean aRight ) {
      return aLeft && aRight;
    }
  },

  /**
   * Logical OR.
   */
  OR( "OR", STR_N_OP_OR, STR_D_OP_OR, '|' ) { //$NON-NLS-1$

    @Override
    public boolean op( boolean aLeft, boolean aRight ) {
      return aLeft || aRight;
    }
  },

  /**
   * Logical XOR.
   */
  XOR( "XOR", STR_N_OP_XOR, STR_D_OP_XOR, '^' ) { //$NON-NLS-1$

    @Override
    public boolean op( boolean aLeft, boolean aRight ) {
      return aLeft ^ aRight;
    }
  };

  private static IStridablesList<ELogicalOp> list = null;

  /**
   * Keeper identifier for {@link TsValobjUtils#registerKeeper(String, IEntityKeeper)} registration.
   */
  public static final String KEEPER_ID = "ELogicalOp"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<ELogicalOp> KEEPER = new StridableEnumKeeper<>( ELogicalOp.class );

  private final String id;
  private final String nmName;
  private final String description;
  private final char   opChar;

  ELogicalOp( String aId, String aName, String aDescr, char aOpChar ) {
    id = aId;
    nmName = aName;
    description = aDescr;
    opChar = aOpChar;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return nmName;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Calculates the operation result.
   *
   * @param aLeft boolean - left (first) operand
   * @param aRight boolean - right (second) operand
   * @return boolean - operation result
   */
  public abstract boolean op( boolean aLeft, boolean aRight );

  /**
   * Returns the character representation of the operation.
   *
   * @return char - the character representation
   */
  public char opChar() {
    return opChar;
  }

  /**
   * Determines if argument is an operation char.
   *
   * @param aChar char - the symbol to check
   * @return {@link ELogicalOp} - the operation or <code>null</code>
   */
  public static ELogicalOp findByChar( char aChar ) {
    for( ELogicalOp op : asStridablesList() ) {
      if( op.opChar() == aChar ) {
        return op;
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  /**
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link ELogicalOp} &gt; - list of all constants in order of declaration
   */
  public static IStridablesList<ELogicalOp> asStridablesList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link ELogicalOp} &gt; - list of all constants in order of declaration
   */
  public static IStridablesList<ELogicalOp> asList() {
    return asStridablesList();
  }

  // ------------------------------------------------------------------------------------
  // Search and check methods
  //

  /**
   * Determines if constant with specified identifier ({@link #id()}) exists.
   *
   * @param aId String - identifier of constant to search for
   * @return boolean - <code>true</code> if constant exists
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Determines if constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - name of constant to search for
   * @return boolean - <code>true</code> if constant exists
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Find & get
  //

  /**
   * Finds constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return ETsFilterOp - found constant or <code>null</code> if no constant exists with specified identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ELogicalOp findById( String aId ) {
    return asStridablesList().findByKey( aId );
  }

  /**
   * Returns constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return ETsFilterOp - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ELogicalOp getById( String aId ) {
    return asStridablesList().getByKey( aId );
  }

  /**
   * Finds constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - identifier of constant to search for
   * @return ETsFilterOp - found constant or <code>null</code> if no constant exists with specified name
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ELogicalOp findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ELogicalOp item : asStridablesList() ) {
      if( item.nmName.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns constant with specified name ({@link #nmName()}).
   *
   * @param aName String - identifier of constant to search for
   * @return ETsFilterOp - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ELogicalOp getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
