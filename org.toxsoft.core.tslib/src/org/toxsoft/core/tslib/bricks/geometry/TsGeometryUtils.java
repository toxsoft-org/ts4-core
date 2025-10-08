package org.toxsoft.core.tslib.bricks.geometry;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Geometry utility methods for 2D geometry with <code>int</code> coordinates.
 *
 * @author hazard157
 */
public class TsGeometryUtils {

  /**
   * Determines if <code>aDims1</code> are equal to the <code>aDims2</code>.
   *
   * @param aDims1 {@link ITsDims} - the first dimensions
   * @param aDims2 {@link ITsDims} - the second dimensions
   * @return boolean - <code>true</code> if both width and height of the arguemnts are the same
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean isEQ( ITsDims aDims1, ITsDims aDims2 ) {
    TsNullArgumentRtException.checkNulls( aDims1, aDims2 );
    return aDims1.width() == aDims2.width() && aDims1.height() == aDims2.height();
  }

  /**
   * Determines if this dimensions are bigger than the argument.
   *
   * @param aDims1 {@link ITsDims} - the first dimensions
   * @param aDims2 {@link ITsDims} - the argument dimensions
   * @return boolean - <code>true</code> if both width and height of first argument are greater than second
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean isGT( ITsDims aDims1, ITsDims aDims2 ) {
    TsNullArgumentRtException.checkNulls( aDims1, aDims2 );
    return aDims1.width() > aDims2.width() && aDims1.height() > aDims2.height();
  }

  /**
   * Determines if this dimensions are bigger or equal than the argument.
   *
   * @param aDims1 {@link ITsDims} - the first dimensions
   * @param aDims2 {@link ITsDims} - the argument dimensions
   * @return boolean - <code>true</code> if both width and height of first argument are greater or equal than second
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean isGE( ITsDims aDims1, ITsDims aDims2 ) {
    TsNullArgumentRtException.checkNulls( aDims1, aDims2 );
    return aDims1.width() >= aDims2.width() && aDims1.height() >= aDims2.height();
  }

  /**
   * Creates {@link TsPointEdit} from {@link ID2Point}.
   *
   * @param aSource {@link ID2Point} - the source
   * @return {@link TsPointEdit} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsPointEdit create( ID2Point aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    int x = (int)aSource.x();
    int y = (int)aSource.y();
    return new TsPointEdit( x, y );
  }

  /**
   * Creates {@link TsRectangleEdit} from {@link ID2Rectangle}.
   *
   * @param aSource {@link ID2Rectangle} - the source
   * @return {@link TsRectangleEdit} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsRectangleEdit create( ID2Rectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    int x = (int)aSource.x1();
    int y = (int)aSource.y1();
    int width = (int)aSource.width();
    int height = (int)aSource.height();
    return new TsRectangleEdit( x, y, width, height );
  }

  /**
   * Returns the union of the rectangles.
   * <p>
   * The union is the smallest rectangle containing both rectangles.
   *
   * @param aRect1 ITsRectangle - first rectangle
   * @param aRect2 ITsRectangle - second rectangle
   * @return {@link ITsRectangle} - the union of the r ectangles.
   */
  public static ITsRectangle union( ITsRectangle aRect1, ITsRectangle aRect2 ) {
    int minX = Math.min( aRect1.a().x(), aRect2.a().x() );
    int minY = Math.min( aRect1.a().y(), aRect2.a().y() );
    int maxX = Math.max( aRect1.b().x(), aRect2.b().x() );
    int maxY = Math.max( aRect1.b().y(), aRect2.b().y() );
    return new TsRectangle( minX, minY, maxX - minX + 1, maxY - minY + 1 );
  }

  /**
   * Returns new instance of {@link TsPointEdit} with shifted coordinates.
   *
   * @param aPoint {@link ITsPoint} - original point
   * @param aDeltaX int - horizontal shift (0 means no shift)
   * @param aDeltaY int - vertical shift (0 means no shift)
   * @return {@link TsPointEdit} - new instance of shifted point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsPointEdit shiftPoint( ITsPoint aPoint, int aDeltaX, int aDeltaY ) {
    TsNullArgumentRtException.checkNull( aPoint );
    return new TsPointEdit( aPoint.x() + aDeltaX, aPoint.y() + aDeltaY );
  }

  /**
   * Returns new instance of {@link TsRectangleEdit} with shifted coordinates.
   *
   * @param aRectangle {@link ITsRectangle} - original rectangle
   * @param aDeltaX int - horizontal shift (0 means no shift)
   * @param aDeltaY int - vertical shift (0 means no shift)
   * @return {@link TsRectangleEdit} - new instance of shifted rectangle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsRectangleEdit shiftRect( ITsRectangle aRectangle, int aDeltaX, int aDeltaY ) {
    TsNullArgumentRtException.checkNull( aRectangle );
    return new TsRectangleEdit( aRectangle.a().x() + aDeltaX, aRectangle.a().y() + aDeltaY, //
        aRectangle.width(), aRectangle.height() );
  }

  // TODO other methods like union(), contains(), intersects(), etc.

  /**
   * Returns new point, rotated around the center the on angle in radians.
   *
   * @param aPoint {@link ID2Point} - point to be rotated
   * @param aCenter {@link ID2Point} - center of rotation
   * @param aRadians double - angle of rotation in radians
   * @return {@link ID2Point} - rotated point
   */
  public static ID2Point rotatePoint( ID2Point aPoint, ID2Point aCenter, double aRadians ) {
    double x0 = aCenter.x();
    double y0 = aCenter.y();

    double x = aPoint.x() - x0;
    double y = aPoint.y() - y0;

    double xNew = x * Math.cos( aRadians ) - y * Math.sin( aRadians ) + x0;
    double yNew = x * Math.sin( aRadians ) + y * Math.cos( aRadians ) + y0;

    return new D2Point( xNew, yNew );
  }

  /**
   * Return array of 4 rotated rectangle vertexes.
   *
   * @param aR {@link ID2Rectangle} - rectangle to be rotated
   * @param aCenter {@link ID2Point} - center of rotation
   * @param aRadians - double angle of rotation in radians
   * @return ID2Point[] - array of four rotated rectangle vertexes
   */
  public static ID2Point[] rotateRect( ID2Rectangle aR, ID2Point aCenter, double aRadians ) {
    ID2Point[] points = new D2Point[4];

    points[0] = rotatePoint( new D2Point( aR.x1(), aR.y1() ), aCenter, aRadians );
    points[1] = rotatePoint( new D2Point( aR.x1() + aR.width(), aR.y1() ), aCenter, aRadians );
    points[2] = rotatePoint( new D2Point( aR.x1() + aR.width(), aR.y1() + aR.height() ), aCenter, aRadians );
    points[3] = rotatePoint( new D2Point( aR.x1(), aR.y1() + aR.height() ), aCenter, aRadians );

    return points;
  }

  /**
   * Return array of 4 rotated rectangle vertexes around its cetnter.
   *
   * @param aR {@link ID2Rectangle} - rectangle to be rotated
   * @param aRadians - double angle of rotation in radians
   * @return ID2Point[] - array of four rotated rectangle vertexes
   */
  public static ID2Point[] rotateRect( ID2Rectangle aR, double aRadians ) {
    ID2Point center = new D2Point( aR.x1() + aR.width() / 2., aR.y1() + aR.height() / 2. );
    return rotateRect( aR, center, aRadians );
  }

  /**
   * Returns the minimal rectangle containing all the points.
   *
   * @param aPoints ID2Point[] - array of point to be bounded
   * @return {@link ID2Rectangle} - the minimal rectangle containing all the points
   */
  public static ID2Rectangle bounds( ID2Point[] aPoints ) {
    double minX = aPoints[0].x();
    double maxX = minX;
    double minY = aPoints[0].y();
    double maxY = minY;

    for( ID2Point p : aPoints ) {
      if( p.x() < minX ) {
        minX = p.x();
      }
      if( p.x() > maxX ) {
        maxX = p.x();
      }
      if( p.y() < minY ) {
        minY = p.y();
      }
      if( p.y() > maxY ) {
        maxY = p.y();
      }
    }

    double width = maxX - minX;
    if( width == 0 ) {
      width = 1;
    }
    double height = maxY - minY;
    if( height == 0 ) {
      height = 1;
    }

    return new D2Rectangle( minX, minY, width, height );
  }

  /**
   * No subclasses. Constructor.
   */
  public TsGeometryUtils() {
    // nop
  }
}
