package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tsgui.graphics.colors.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The SWT system color corresponding to the <code>SWT.COLOR_XXX</code> constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ESwtSysColor
    implements IStridable {

  SYSCOL_LINK_FOREGROUND( "syscol_link_foreground", STR_SYSCOL_LINK_FOREGROUND, STR_SYSCOL_LINK_FOREGROUND_D ), //$NON-NLS-1$

  // FIXME add colors

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ESwtSysColor"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ESwtSysColor> KEEPER = new StridableEnumKeeper<>( ESwtSysColor.class );

  private static IStridablesListEdit<ESwtSysColor> list = null;

  private final String id;
  private final String name;
  private final String description;

  ESwtSysColor( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
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

  public static ESwtSysColor findBySwtColorId( int aSwtColorId ) {

  }

  public int getSwtColorId

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ESwtSysColor} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ESwtSysColor> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ESwtSysColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ESwtSysColor getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ESwtSysColor} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ESwtSysColor findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ESwtSysColor item : values() ) {
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
   * @return {@link ESwtSysColor} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ESwtSysColor getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
