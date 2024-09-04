package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.ITsResources.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The <a href=https://en.wikipedia.org/wiki/Quadrant_(plane_geometry)>quadrant</a> enumeration.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ED2Quadrant
    implements IStridable {

  Q1( "q1", STR_EQ_Q1, STR_EQ_Q1_D, 0.0, 90.0, 0.0, Math.PI / 2 ) { //$NON-NLS-1$

    @Override
    protected double doCalcContentRectOriginX( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.x1();
    }

    @Override
    protected double doCalcContentRectOriginY( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.y1() + aInnerWidth * Math.sin( aRadsIn2Pi );
    }
  },

  Q2( "q2", STR_EQ_Q2, STR_EQ_Q2_D, 90.0, 180.0, Math.PI / 2, Math.PI ) { //$NON-NLS-1$

    @Override
    protected double doCalcContentRectOriginX( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.x1() + aInnerWidth * Math.sin( aRadsIn2Pi - Math.PI / 2 );
    }

    @Override
    protected double doCalcContentRectOriginY( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.y2();
    }
  },

  Q3( "q3", STR_EQ_Q3, STR_EQ_Q3_D, 180.0, 270.0, Math.PI, 3 * Math.PI / 2 ) { //$NON-NLS-1$

    @Override
    protected double doCalcContentRectOriginX( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.x2();
    }

    @Override
    protected double doCalcContentRectOriginY( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.y2() - aInnerWidth * Math.sin( aRadsIn2Pi - Math.PI );
    }
  },

  Q4( "q4", STR_EQ_Q4, STR_EQ_Q4_D, 270.0, 360.0, 3 * Math.PI / 2, 2 * Math.PI ) { //$NON-NLS-1$

    @Override
    protected double doCalcContentRectOriginX( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.x2() - aInnerWidth * Math.sin( aRadsIn2Pi - 3 * Math.PI / 2 );
    }

    @Override
    protected double doCalcContentRectOriginY( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi ) {
      return aBoundRect.y1();
    }
  },

  ;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ED2Quadrant"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ED2Quadrant> KEEPER = new StridableEnumKeeper<>( ED2Quadrant.class );

  private static IStridablesListEdit<ED2Quadrant> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final double deg1, deg2, rad1, rad2;

  ED2Quadrant( String aId, String aName, String aDescription, double aDeg1, double aDeg2, double aRad1, double aRad2 ) {
    id = aId;
    name = aName;
    description = aDescription;
    deg1 = aDeg1;
    deg2 = aDeg2;
    rad1 = aRad1;
    rad2 = aRad2;
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
  // ED2Quadrant
  //

  /**
   * Returns the quadrant of the specified angle.
   *
   * @param aDegrees double - the angle in degrees
   * @return {@link ED2Quadrant} - the quadrant
   */
  public static ED2Quadrant findByDegrees( double aDegrees ) {
    double d = degIn360( aDegrees );
    for( ED2Quadrant q : asList() ) {
      if( q.isInQuadrantDegrees( d ) ) {
        return q;
      }
    }
    throw new TsInternalErrorRtException();
  }

  /**
   * Returns the quadrant of the specified angle.
   *
   * @param aRadians double - the angle in radians
   * @return {@link ED2Quadrant} - the quadrant
   */
  public static ED2Quadrant findByRadians( double aRadians ) {
    double r = radIn2Pi( aRadians );
    for( ED2Quadrant q : asList() ) {
      if( q.isInQuadrantRadians( r ) ) {
        return q;
      }
    }
    throw new TsInternalErrorRtException();
  }

  /**
   * Determines if the angle is on his quadrant.
   *
   * @param aDegrees double - the angle in degrees
   * @return boolean - <code>true</code> angle is in this quadrant
   */
  public boolean isInQuadrantDegrees( double aDegrees ) {
    double d = degIn360( aDegrees );
    return deg1 <= d && d < deg2;
  }

  /**
   * Determines if the angle is on his quadrant.
   *
   * @param aRadians double - the angle in radians
   * @return boolean - <code>true</code> angle is in this quadrant
   */
  public boolean isInQuadrantRadians( double aRadians ) {
    double r = radIn2Pi( aRadians );
    return rad1 <= r && r < rad2;
  }

  /**
   * Returns an angle in range 0..360° removing full circles.
   *
   * @param aRadians double - angle in degrees
   * @return double - angle normalized in range 0..360°
   */
  public static double degIn360( double aDegrees ) {
    double d = aDegrees % 360.0;
    if( d < 0.0 ) {
      return d + 360.0;
    }
    return d;
  }

  /**
   * Returns an angle in range 0..2*π removing full circles.
   *
   * @param aRadians double - angle in radians
   * @return double - angle normalized in range 0..2*π
   */
  public static double radIn2Pi( double aRadians ) {
    double r = aRadians % (2 * Math.PI);
    if( r < 0.0 ) {
      return r + 2 * Math.PI;
    }
    return r;
  }

  /**
   * Returns rotated content rectangles origin (top-left) corner X coordinate.
   * <p>
   * Method assumes that some rectangular image (called content) has to be drawn rotated. Drawing always starts from
   * top-left corner (called origin). Method assumes that bounding rectangle coordinates is already calculate. By
   * definition, corners of the content rectangle are always placed on four different edges (or corners) of the bounding
   * rectangle. This method calculates X coordinate of the origin (top-left) corner of the content rectangle,
   *
   * @param aBoundRect {@link ITsRectangle} - bounding rectangle coordinates
   * @param aInnerRectWidth double - the width of the content (rotated) rectangle
   * @param aRadians double - the rotation angle (positive values are counter-clockwise)
   * @return double - the X coordinate of the content top-left corner
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public double contentRectOriginX( ITsRectangle aBoundRect, double aInnerRectWidth, double aRadians ) {
    TsNullArgumentRtException.checkNull( aBoundRect );
    double rads = radIn2Pi( aRadians );
    return doCalcContentRectOriginX( aBoundRect, aInnerRectWidth, rads );
  }

  /**
   * Returns rotated content rectangles origin (top-left) corner Y coordinate.
   * <p>
   * This is the same as {@link #contentRectOriginX(ITsRectangle, double, double)} but for Y coordinate.
   *
   * @param aBoundRect {@link ITsRectangle} - bounding rectangle coordinates
   * @param aInnerRectWidth double - the width of the content (rotated) rectangle
   * @param aRadians double - the rotation angle (positive values are counter-clockwise)
   * @return double - the Y coordinate of the content top-left corner
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public double contentRectOriginY( ITsRectangle aBoundRect, double aInnerRectWidth, double aRadians ) {
    TsNullArgumentRtException.checkNull( aBoundRect );
    double rads = radIn2Pi( aRadians );
    return doCalcContentRectOriginY( aBoundRect, aInnerRectWidth, rads );
  }

  protected abstract double doCalcContentRectOriginX( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi );

  protected abstract double doCalcContentRectOriginY( ITsRectangle aBoundRect, double aInnerWidth, double aRadsIn2Pi );

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ED2Quadrant} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ED2Quadrant> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ED2Quadrant} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ED2Quadrant getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ED2Quadrant} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ED2Quadrant findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ED2Quadrant item : values() ) {
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
   * @return {@link ED2Quadrant} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ED2Quadrant getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
