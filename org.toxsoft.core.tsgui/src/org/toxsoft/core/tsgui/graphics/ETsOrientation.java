package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tsgui.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Orientation of visual components - horizontal or vertical.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ETsOrientation
    implements IStridable {

  HORIZONTAL( "Horizontal", STR_TSO_HORIZONTAL, STR_TSO_HORIZONTAL_D, SWT.HORIZONTAL ), //$NON-NLS-1$

  VERTICAL( "Vertical", STR_TSO_VERTICAL, STR_TSO_VERTICAL_D, SWT.VERTICAL ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TsOrientation"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<ETsOrientation> KEEPER = new StridableEnumKeeper<>( ETsOrientation.class );

  private static IStridablesListEdit<ETsOrientation> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  ETsOrientation( String aId, String aName, String aDescr, int aSwtStyle ) {
    id = aId;
    name = aName;
    description = aDescr;
    swtStyle = aSwtStyle;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns other orientation.
   *
   * @return {@link ETsOrientation} - other orientation
   */
  public ETsOrientation otherOrientation() {
    if( this == HORIZONTAL ) {
      return VERTICAL;
    }
    return HORIZONTAL;
  }

  /**
   * Determines if this is a horizontal orientation.
   *
   * @return boolean - this is the constant {@link #HORIZONTAL}
   */
  public boolean isHorisontal() {
    return this == HORIZONTAL;
  }

  /**
   * Determines if this is a vertical orientation.
   *
   * @return boolean - this is the constant {@link #VERTICAL}
   */
  public boolean isVertical() {
    return this == VERTICAL;
  }

  /**
   * Returns the {@link SWT} style corresponding to this constant.
   * <p>
   * Returns one of the values {@link SWT#HORIZONTAL} or {@link SWT#VERTICAL}.
   *
   * @return int - the {@link SWT} orientation style
   */
  public int swtStyle() {
    return swtStyle;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsOrientation} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsOrientation> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsOrientation} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsOrientation getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsOrientation} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsOrientation findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsOrientation item : values() ) {
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
   * @return {@link ETsOrientation} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsOrientation getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
