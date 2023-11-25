package org.toxsoft.core.tsgui.utils.rectfit;

import static org.toxsoft.core.tsgui.utils.rectfit.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Modes to fit an object into the rectangle.
 *
 * @author hazard157
 */
public enum ERectFitMode
    implements IStridable {

  // TODO add the icon ID

  /**
   * Original size.
   */
  NONE( "none", STR_N_FM_NONE, STR_D_FM_NONE, false ), //$NON-NLS-1$

  /**
   * Fit either width or height.
   */
  FIT_BOTH( "both", STR_N_FM_FIT_BOTH, STR_D_FM_FIT_BOTH, true ), //$NON-NLS-1$

  /**
   * Fit width.
   */
  FIT_WIDTH( "width", STR_N_FM_FIT_WIDTH, STR_D_FM_FIT_WIDTH, true ), //$NON-NLS-1$

  /**
   * Fit height.
   */
  FIT_HEIGHT( "height", STR_N_FM_FIT_HEIGHT, STR_D_FM_FIT_HEIGHT, true ), //$NON-NLS-1$

  /**
   * Fit to fill.
   */
  FIT_FILL( "fill", STR_N_FM_FIT_FILL, STR_D_FM_FIT_FILL, true ), //$NON-NLS-1$

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
