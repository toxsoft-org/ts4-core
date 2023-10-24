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
   * @return {@link ITsRectangle} - the union of the rectangles.
   */
  public static ITsRectangle union( ITsRectangle aRect1, ITsRectangle aRect2 ) {
    int minX = Math.min( aRect1.a().x(), aRect2.a().x() );
    int minY = Math.min( aRect1.a().y(), aRect2.a().y() );
    int maxX = Math.max( aRect1.b().x(), aRect2.b().x() );
    int maxY = Math.max( aRect1.b().y(), aRect2.b().y() );
    return new TsRectangle( minX, minY, maxX - minX + 1, maxY - minY + 1 );
  }

  // TODO other methods like union(), contains(), intersects(), etc.

  /**
   * No subclasses. Constructor.
   */
  public TsGeometryUtils() {
    // nop
  }
}
