package org.toxsoft.core.tsgui.bricks.tin;

import static org.toxsoft.core.tsgui.bricks.tin.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of XXX.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ETinTypeKind
    implements IStridable {

  ATOMIC( "identifier", STR_TNK_ATOMIC, STR_TNK_ATOMIC_D, true, false ), //$NON-NLS-1$

  GROUP( "group", STR_TNK_GROUP, STR_TNK_GROUP_D, false, true ), //$NON-NLS-1$

  FULL( "full", STR_TNK_FULL, STR_TNK_FULL_D, true, true ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETinNodeKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETinTypeKind> KEEPER = new StridableEnumKeeper<>( ETinTypeKind.class );

  private static IStridablesListEdit<ETinTypeKind> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean hasAtomic;
  private final boolean hasChildren;

  ETinTypeKind( String aId, String aName, String aDescription, boolean aAtimic, boolean aChildren ) {
    id = aId;
    name = aName;
    description = aDescription;
    hasAtomic = aAtimic;
    hasChildren = aChildren;
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
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  public boolean hasAtomic() {
    return hasAtomic;
  }

  public boolean hasChildren() {
    return hasChildren;
  }

  public boolean isLeaf() {
    return hasAtomic && !hasChildren;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETinTypeKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETinTypeKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETinTypeKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETinTypeKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETinTypeKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETinTypeKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETinTypeKind item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETinTypeKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETinTypeKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
