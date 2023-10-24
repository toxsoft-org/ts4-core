package org.toxsoft.core.tslib.bricks.d2;

/**
 * Geometry utility methods for 2D geometry with <code>double</code> coordinates.
 *
 * @author hazard157
 */
public class D2GeometryUtils {

  /**
   * Returns the union of the rectangles.
   * <p>
   * The union is the smallest rectangle containing both rectangles.
   *
   * @param aRect1 {@link ID2Rectangle} - first rectangle
   * @param aRect2 {@link ID2Rectangle} - second rectangle
   * @return {@link ID2Rectangle} - the union of the rectangles.
   */
  public static ID2Rectangle union( ID2Rectangle aRect1, ID2Rectangle aRect2 ) {
    double minX = Math.min( aRect1.a().x(), aRect2.a().x() );
    double minY = Math.min( aRect1.a().y(), aRect2.a().y() );
    double maxX = Math.max( aRect1.x1() + aRect1.width(), aRect2.x1() + aRect2.width() );
    double maxY = Math.max( aRect1.y1() + aRect1.height(), aRect2.y1() + aRect2.height() );
    return new D2Rectangle( minX, minY, maxX - minX, maxY - minY );
  }

  /**
   * No subclasses.
   */
  private D2GeometryUtils() {
    // nop
  }

}
