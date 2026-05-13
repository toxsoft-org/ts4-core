package org.toxsoft.core.tslib.utils.checks;

import static org.toxsoft.core.tslib.utils.checks.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of XXX.
 *
 * @author AUTHOR_NAME
 */
public enum ECheckState
    implements IStridable {

  /**
   * Element is not checked.
   */
  UNCHECKED( "Unchecked", STR_UNCHECKED, STR_UNCHECKED_D, false, false ), //$NON-NLS-1$

  /**
   * Element is checked.
   */
  CHECKED( "Checked", STR_CHECKED, STR_CHECKED_D, true, false ), //$NON-NLS-1$

  /**
   * Child elements has the the different states.
   */
  GRAYED( "Grayed", STR_GRAYED, STR_GRAYED_D, true, true ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ECheckState"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ECheckState> KEEPER = new StridableEnumKeeper<>( ECheckState.class );

  private static IStridablesListEdit<ECheckState> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean isAnyCheck;
  private final boolean isGrayCheck;

  ECheckState( String aId, String aName, String aDescription, boolean aIsAnyCheck, boolean aIsGrayCheck ) {
    id = aId;
    name = aName;
    description = aDescription;
    isAnyCheck = aIsAnyCheck;
    isGrayCheck = aIsGrayCheck;
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
  // Additional API
  //

  /**
   * Determines whether any checked state corresponds to the given constant.
   *
   * @return boolean - a flag of a check mark {@link #CHECKED} or {@link #GRAYED}
   */
  public boolean isAnyCheck() {
    return isAnyCheck;
  }

  /**
   * Specifies that there is a grey mark.
   *
   * @return boolean - a flag of a check mark {@link #GRAYED}
   */
  public boolean isGrayCheck() {
    return isGrayCheck;
  }

  /**
   * Determines that there is exact check mark.
   *
   * @return boolean - a flag of a check mark {@link #CHECKED}
   */
  public boolean isCheck() {
    return isAnyCheck && !isGrayCheck;
  }

  /**
   * Returns the enum constant corresponding to the requested check state.
   * <p>
   * Two boolean arguments determines 4 different cases, while {@link ECheckState} has 3 constants. When
   * <code>aIsChecked</code> = <code>false</code> method returns {@link #UNCHECKED} regardless of <code>aIsGrayed</code>
   * value.
   *
   * @param aIsChecked boolean - a checked element flag
   * @param aIsGrayed boolean - a flag that element's children have different states
   * @return {@link ECheckState} - corresponding constant
   */
  public static ECheckState checkState( boolean aIsChecked, boolean aIsGrayed ) {
    if( aIsChecked ) {
      if( aIsGrayed ) {
        return GRAYED;
      }
      return CHECKED;
    }
    return UNCHECKED;
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ECheckState} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ECheckState> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ECheckState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ECheckState getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ECheckState} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ECheckState findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ECheckState item : values() ) {
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
   * @return {@link ECheckState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ECheckState getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
