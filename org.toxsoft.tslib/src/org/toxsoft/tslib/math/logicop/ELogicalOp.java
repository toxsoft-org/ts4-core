package org.toxsoft.tslib.math.logicop;

import static org.toxsoft.tslib.math.logicop.ITsResources.*;

import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

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
  AND( "AND", STR_N_OP_AND, STR_D_OP_AND ) { //$NON-NLS-1$

    @Override
    public boolean op( boolean aLeft, boolean aRight ) {
      return aLeft && aRight;
    }
  },

  /**
   * Logical OR.
   */
  OR( "OR", STR_N_OP_OR, STR_D_OP_OR ) { //$NON-NLS-1$

    @Override
    public boolean op( boolean aLeft, boolean aRight ) {
      return aLeft || aRight;
    }
  },

  /**
   * Logical XOR.
   */
  XOR( "XOR", STR_N_OP_XOR, STR_D_OP_XOR ) { //$NON-NLS-1$

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

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDPath)
   * @param aName String - short, human-readable name
   * @param aDescr String - description
   */
  ELogicalOp( String aId, String aName, String aDescr ) {
    id = aId;
    nmName = aName;
    description = aDescr;
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
   * Calculates the logical operation result.
   *
   * @param aLeft boolean - left (first) operand
   * @param aRight boolean - right (second) operand
   * @return boolean - opeartion result
   */
  public abstract boolean op( boolean aLeft, boolean aRight );

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
