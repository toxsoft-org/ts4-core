package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tsgui.graphics.ITsResources.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Anchor point (snap point) of the rectangle.
 * <p>
 * Comes from the word Fulcrum - the fulcrum of the lever.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ETsFulcrum
    implements IStridable {

  CENTER( "Center", STR_ETF_CENTER, STR_ETF_CENTER_D, 50.0, 50.0 ), //$NON-NLS-1$

  LEFT_TOP( "LeftTop", STR_ETF_LEFT_TOP, STR_ETF_LEFT_TOP_D, 0.0, 0.0 ), //$NON-NLS-1$

  LEFT_BOTTOM( "LeftBottom", STR_ETF_LEFT_BOTTOM, STR_ETF_LEFT_BOTTOM_D, 0.0, 100.0 ), //$NON-NLS-1$

  LEFT_CENTER( "LeftCenter", STR_ETF_LEFT_CENTER, STR_ETF_LEFT_CENTER_D, 0.0, 50.0 ), //$NON-NLS-1$

  RIGHT_TOP( "RightTop", STR_ETF_RIGHT_TOP, STR_ETF_RIGHT_TOP_D, 100.0, 0.0 ), //$NON-NLS-1$

  RIGHT_BOTTOM( "RightBottom", STR_ETF_RIGHT_BOTTOM, STR_ETF_RIGHT_BOTTOM_D, 100.0, 100.0 ), //$NON-NLS-1$

  RIGHT_CENTER( "RightCenter", STR_ETF_RIGHT_CENTER, STR_ETF_RIGHT_CENTER_D, 100.0, 50.0 ), //$NON-NLS-1$

  TOP_CENTER( "TopCenter", STR_ETF_TOP_CENTER, STR_ETF_TOP_CENTER_D, 50.0, 0.0 ), //$NON-NLS-1$

  BOTTOM_CENTER( "BottomCenter", STR_ETF_BOTTOM_CENTER, STR_ETF_BOTTOM_CENTER_D, 50.0, 100.0 ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETsFulcrum"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsFulcrum> KEEPER = new StridableEnumKeeper<>( ETsFulcrum.class );

  private static IStridablesListEdit<ETsFulcrum> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final double xPerc;
  private final double yPerc;

  ETsFulcrum( String aId, String aName, String aDescription, double aXPerc, double aYPerc ) {
    id = aId;
    name = aName;
    description = aDescription;
    xPerc = aXPerc;
    yPerc = aYPerc;
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
   * Calculates the coordinates of a rectangle of the specified width and height at the specified fulcrum point.
   *
   * @param aFulcrumX int - X coordinate of the fulcrum point
   * @param aFulcrumY int - Y coordinate of the fulcrum point
   * @param aWidth int - the rectangle width
   * @param aHeight int - the rectangle height
   * @return {@link ITsRectangle} - calculated rectangle
   * @throws TsIllegalArgumentRtException width or height < 0
   */
  public ITsRectangle calcRect( int aFulcrumX, int aFulcrumY, int aWidth, int aHeight ) {
    return new TsRectangle( calcSegmentX( aFulcrumX, aWidth ), calcSegmentY( aFulcrumY, aHeight ), aWidth, aHeight );
  }

  /**
   * Calculates the x-coordinate of the left end of a horizontal segment at the specified fulcrum point.
   *
   * @param aFulcrumX int - x coordinate of the fulcrum point
   * @param aSegmentLength int - the segment length
   * @return int - X coordinate of the left end
   * @throws TsIllegalArgumentRtException length < 0
   */
  public int calcSegmentX( int aFulcrumX, int aSegmentLength ) {
    TsIllegalArgumentRtException.checkTrue( aSegmentLength < 0 );
    int x = switch( this ) {
      case CENTER, BOTTOM_CENTER, TOP_CENTER -> aFulcrumX - aSegmentLength / 2;
      case LEFT_TOP, LEFT_CENTER, LEFT_BOTTOM -> aFulcrumX;
      case RIGHT_TOP, RIGHT_CENTER, RIGHT_BOTTOM -> aFulcrumX - aSegmentLength;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    return x;
  }

  /**
   * Calculates the x-coordinate of the top end of a vertical segment at the specified fulcrum point.
   *
   * @param aFulcrumY int - Y coordinate of the fulcrum point
   * @param aSegmentLength int - the segment length
   * @return int - Y coordinate of the top end
   * @throws TsIllegalArgumentRtException length < 0
   */
  public int calcSegmentY( int aFulcrumY, int aSegmentLength ) {
    TsIllegalArgumentRtException.checkTrue( aSegmentLength < 0 );
    int y = switch( this ) {
      case CENTER, LEFT_CENTER, RIGHT_CENTER -> aFulcrumY - aSegmentLength / 2;
      case LEFT_TOP, RIGHT_TOP, TOP_CENTER -> aFulcrumY;
      case LEFT_BOTTOM, RIGHT_BOTTOM, BOTTOM_CENTER -> aFulcrumY - aSegmentLength;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    return y;
  }

  /**
   * Calculates the X coordinate of a rectangle in the display area.
   *
   * @param aCanvasWidth int - the display area width
   * @param aRectWidth int - the rectangle width
   * @return int - X coordinate of the top-left corner
   */
  public int calcTopleftX( int aCanvasWidth, int aRectWidth ) {
    return switch( this ) {
      case CENTER, TOP_CENTER, BOTTOM_CENTER -> (aCanvasWidth - aRectWidth) / 2;
      case LEFT_TOP, LEFT_CENTER, LEFT_BOTTOM -> 0;
      case RIGHT_TOP, RIGHT_CENTER, RIGHT_BOTTOM -> aCanvasWidth - aRectWidth;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Calculates the Y coordinate of a rectangle in the display area.
   *
   * @param aCanvasHeight int - the display area height
   * @param aRectHeight int - the rectangle height
   * @return int - Y coordinate of the top-left corner
   */
  public int calcTopleftY( int aCanvasHeight, int aRectHeight ) {
    return switch( this ) {
      case CENTER, LEFT_CENTER, RIGHT_CENTER -> (aCanvasHeight - aRectHeight) / 2;
      case LEFT_TOP, RIGHT_TOP, TOP_CENTER -> 0;
      case LEFT_BOTTOM, RIGHT_BOTTOM, BOTTOM_CENTER -> aCanvasHeight - aRectHeight;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Determines whether the fulcrum point is located on the left edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the left, <code>false</code> - somewhere else
   */
  public boolean isLeft() {
    return switch( this ) {
      case CENTER, RIGHT_CENTER, RIGHT_TOP, TOP_CENTER, RIGHT_BOTTOM, BOTTOM_CENTER -> false;
      case LEFT_CENTER, LEFT_TOP, LEFT_BOTTOM -> true;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Determines whether the fulcrum point is located on the right edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the right, <code>false</code> - somewhere else
   */
  public boolean isRight() {
    return switch( this ) {
      case CENTER, LEFT_CENTER, LEFT_TOP, TOP_CENTER, LEFT_BOTTOM, BOTTOM_CENTER -> false;
      case RIGHT_CENTER, RIGHT_TOP, RIGHT_BOTTOM -> true;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Determines whether the fulcrum point is located on the top edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the top, <code>false</code> - somewhere else
   */
  public boolean isTop() {
    return switch( this ) {
      case CENTER, LEFT_CENTER, RIGHT_CENTER, LEFT_BOTTOM, RIGHT_BOTTOM, BOTTOM_CENTER -> false;
      case LEFT_TOP, RIGHT_TOP, TOP_CENTER -> true;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Determines whether the fulcrum point is located on the vertical center of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the vertical center, <code>false</code> - somewhere else
   */
  public boolean isVerticalCenter() {
    return switch( this ) {
      case CENTER, LEFT_CENTER, RIGHT_CENTER -> true;
      case LEFT_TOP, RIGHT_TOP, TOP_CENTER, LEFT_BOTTOM, RIGHT_BOTTOM, BOTTOM_CENTER -> false;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Determines whether the fulcrum point is located on the horizontal center of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the horizontal center, <code>false</code> - somewhere else
   */
  public boolean isHorizontalCenter() {
    return switch( this ) {
      case CENTER, TOP_CENTER, BOTTOM_CENTER -> true;
      case LEFT_TOP, RIGHT_TOP, LEFT_CENTER, LEFT_BOTTOM, RIGHT_BOTTOM, RIGHT_CENTER -> false;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Determines whether the fulcrum point is located on the bottom edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the bottom, <code>false</code> - somewhere else
   */
  public boolean isBottom() {
    return switch( this ) {
      case CENTER, LEFT_CENTER, RIGHT_CENTER, LEFT_TOP, RIGHT_TOP, TOP_CENTER -> false;
      case LEFT_BOTTOM, RIGHT_BOTTOM, BOTTOM_CENTER -> true;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * Returns the placement percentage of the fulcrum on the horizontal edge.
   * <p>
   * One of the following value may be returned: 0.0 - left edge, 50.0 - center, 100.0 - right edge.
   *
   * @return double - placement percentage: 0.0, 50.0 or 100.0
   */
  public double getHorPercentage() {
    return xPerc;
  }

  /**
   * Returns the placement percentage of the fulcrum on the vertical edge.
   * <p>
   * One of the following value may be returned: 0.0 - top edge, 50.0 - center, 100.0 - bottom edge.
   *
   * @return double - placement percentage: 0.0, 50.0 or 100.0
   */
  public double getVerPercentage() {
    return yPerc;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsFulcrum} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsFulcrum> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsFulcrum} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsFulcrum getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsFulcrum} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsFulcrum findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsFulcrum item : values() ) {
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
   * @return {@link ETsFulcrum} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsFulcrum getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
