package org.toxsoft.core.tslib.gw.gwid;

import static org.toxsoft.core.tslib.gw.gwid.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GWID selection option - allows to select abstract, concrete or both type of GWIDs.
 *
 * @author hazard157
 */
public enum EGwidSelectionOption
    implements IStridable {

  /**
   * Allows to select concrete GWID, that is {@link Gwid#isAbstract()} = <code>false</code> for selected GWIDs.
   */
  CONCRETE( "concrete", STR_GST_CONCRETE, STR_GST_CONCRETE_D ), //$NON-NLS-1$

  /**
   * Allows to select abstract GWID, that is {@link Gwid#isAbstract()} = <code>true</code> for selected GWIDs.
   */
  ABSTRACT( "abstract", STR_GST_ABSTRACT, STR_GST_ABSTRACT_D ), //$NON-NLS-1$

  /**
   * Allows to select both abstract or concrete GWID.
   */
  BOTH( "both", STR_GST_BOTH, STR_GST_BOTH_D ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EGwidSelectorType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EGwidSelectionOption> KEEPER =
      new StridableEnumKeeper<>( EGwidSelectionOption.class );

  private static IStridablesListEdit<EGwidSelectionOption> list = null;

  private final String id;
  private final String name;
  private final String description;

  EGwidSelectionOption( String aId, String aName, String aDescription ) {
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

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EGwidSelectionOption} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EGwidSelectionOption> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EGwidSelectionOption} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EGwidSelectionOption getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EGwidSelectionOption} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EGwidSelectionOption findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EGwidSelectionOption item : values() ) {
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
   * @return {@link EGwidSelectionOption} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EGwidSelectionOption getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
