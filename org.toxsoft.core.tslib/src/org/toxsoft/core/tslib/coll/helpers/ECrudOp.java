package org.toxsoft.core.tslib.coll.helpers;

import static org.toxsoft.core.tslib.coll.helpers.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Kind of operations over the collection of the elements.
 * <p>
 * Named is abbreviation CRUD (Create Read Update Delete).
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public enum ECrudOp
    implements IStridable {

  /**
   * New element added.
   */
  CREATE( "Create", STR_N_CREATE, STR_D_CREATE, false ),

  /**
   * An existing element changed.
   */
  EDIT( "Edit", STR_N_EDIT, STR_D_EDIT, false ),

  /**
   * Element removed.
   */
  REMOVE( "Remove", STR_N_REMOVE, STR_D_REMOVE, false ),

  /**
   * List items, batch changes of two or more elements.
   */
  LIST( "List", STR_N_LIST, STR_D_LIST, false ),

  ;

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "ECrudOp"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ECrudOp> KEEPER = new StridableEnumKeeper<>( ECrudOp.class );

  private static IStridablesList<ECrudOp> list = null;

  private final String  id;
  private final String  nmName;
  private final String  description;
  private final boolean batchOp;

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDPath)
   * @param aName String - short, human-readable name
   * @param aDescr String - description
   * @param aIsBatch boolean - the flag of the batch operatin
   */
  ECrudOp( String aId, String aName, String aDescr, boolean aIsBatch ) {
    id = aId;
    nmName = aName;
    description = aDescr;
    batchOp = aIsBatch;
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
  // static API
  //

  /**
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link ECrudOp} &gt; - list of all constants in order of declaration
   */
  public static IStridablesList<ECrudOp> asStridablesList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Determines if this is a batch operation that affects more than one element of the collection.
   *
   * @return boolean - a batch operation flag<br>
   *         <b>true</b> - operation affects to or more elements;<br>
   *         <b>false</b> - operation affects exactly one element of the collection.
   */
  public boolean isBatchOp() {
    return batchOp;
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
   * @return ECrudOp - found constant or <code>null</code> if no constant exists with specified identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ECrudOp findById( String aId ) {
    return asStridablesList().findByKey( aId );
  }

  /**
   * Returns constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return ECrudOp - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ECrudOp getById( String aId ) {
    return asStridablesList().getByKey( aId );
  }

  /**
   * Finds constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - identifier of constant to search for
   * @return ECrudOp - found constant or <code>null</code> if no constant exists with specified name
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ECrudOp findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ECrudOp item : asStridablesList() ) {
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
   * @return ECrudOp - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ECrudOp getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
