package org.toxsoft.tsgui.utils.rectfit;

import static org.toxsoft.tsgui.utils.rectfit.ITsResources.*;

import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Modes to fit an object into the rectangle.
 *
 * @author goga
 */
public enum ERectFitMode
    implements IStridable {

  /**
   * Original size.
   */
  NONE( "none", STR_N_FM_NONE, STR_D_FM_NONE, false ), //$NON-NLS-1$

  /**
   * Fit both width and height.
   */
  FIT_BOTH( "both", STR_N_FM_FIT_BOTH, STR_D_FM_FIT_BOTH, false ), //$NON-NLS-1$

  /**
   * Fit width.
   */
  FIT_WIDTH( "width", STR_N_FM_FIT_WIDTH, STR_D_FM_FIT_WIDTH, false ), //$NON-NLS-1$

  /**
   * Fit height.
   */
  FIT_HEIGHT( "height", STR_N_FM_FIT_HEIGHT, STR_D_FM_FIT_HEIGHT, false ), //$NON-NLS-1$

  /**
   * Zoom - show with the specified scaling factor.
   */
  ZOOMED( "zoomed", STR_N_FM_ZOOMED, STR_D_FM_ZOOMED, false ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ERectFitMode"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ERectFitMode> KEEPER = new StridableEnumKeeper<>( ERectFitMode.class );

  private static IStridablesListEdit<ERectFitMode> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean vpSizeDependent;

  ERectFitMode( String aId, String aName, String aDescription, boolean aIsVpSizeDependent ) {
    id = aId;
    name = aName;
    description = aDescription;
    vpSizeDependent = aIsVpSizeDependent;
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
   * Determines if scaling (displayed size) depends on viewport size.
   *
   * @return boolean - <code>true</code> if object size adapts to viewport size
   */
  public boolean isAdaptiveScale() {
    return vpSizeDependent;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ERectFitMode} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ERectFitMode> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ERectFitMode} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ERectFitMode getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ERectFitMode} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ERectFitMode findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ERectFitMode item : values() ) {
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
   * @return {@link ERectFitMode} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ERectFitMode getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
