package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Styles of buttons.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public enum EButtonViselState
    implements IStridable {

  NORMAL( "normal", STR_BTN_NORMAL, STR_BTN_NORMAL_D ), //$NON-NLS-1$

  PRESSED( "pressed", STR_BTN_PRESSED, STR_BTN_PRESSED_D ), //$NON-NLS-1$

  WORKING( "working", STR_BTN_WORKING, STR_BTN_WORKING_D ), //$NON-NLS-1$

  DISABLED( "disabled", STR_BTN_DISABLED, STR_BTN_DISABLED_D ), //$NON-NLS-1$

  SELECTED( "selected", STR_BTN_SELECTED, STR_BTN_SELECTED_D ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EButtonViselState"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EButtonViselState> KEEPER = new StridableEnumKeeper<>( EButtonViselState.class );

  private static IStridablesListEdit<EButtonViselState> list = null;

  private final String id;
  private final String name;
  private final String description;

  EButtonViselState( String aId, String aName, String aDescription ) {
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
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EButtonViselState} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EButtonViselState> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EButtonViselState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EButtonViselState getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EButtonViselState} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EButtonViselState findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EButtonViselState item : values() ) {
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
   * @return {@link EButtonViselState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EButtonViselState getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
