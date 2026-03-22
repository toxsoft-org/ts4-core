package org.toxsoft.core.tslib.utils.rwkind;

import static org.toxsoft.core.tslib.utils.rwkind.ISkResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The physical ability or permission to read and/or write values.
 *
 * @author AUTHOR_NAME
 */
@SuppressWarnings( "javadoc" )
public enum ERwKind
    implements IStridable {

  R( "rw", STR_ERWK_RW, STR_ERWK_RW_D, true, false ), //$NON-NLS-1$

  W( "rw", STR_ERWK_RW, STR_ERWK_RW_D, false, true ), //$NON-NLS-1$

  RW( "rw", STR_ERWK_RW, STR_ERWK_RW_D, true, false ), //$NON-NLS-1$

  ;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ERwKind"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ERwKind> KEEPER = new StridableEnumKeeper<>( ERwKind.class );

  private static IStridablesListEdit<ERwKind> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean canRead;
  private final boolean canWrite;

  ERwKind( String aId, String aName, String aDescription, boolean aRead, boolean aWrite ) {
    id = aId;
    name = aName;
    description = aDescription;
    canRead = aRead;
    canWrite = aWrite;
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

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if data can be read.
   *
   * @return boolean - <code>true</code> can read, <code>false</code> - data reading is not supported
   */
  public boolean canRead() {
    return canRead;
  }

  /**
   * Determines if data can be written.
   *
   * @return boolean - <code>true</code> can write, <code>false</code> - data writing is not supported
   */
  public boolean canWrite() {
    return canWrite;
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ERwKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ERwKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ERwKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ERwKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ERwKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ERwKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ERwKind item : values() ) {
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
   * @return {@link ERwKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ERwKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
