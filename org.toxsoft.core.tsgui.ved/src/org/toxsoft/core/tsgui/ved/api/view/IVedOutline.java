package org.toxsoft.core.tsgui.ved.api.view;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Full information about viewed component shape outline and neighbourhood.
 * <p>
 * Note: all the coordinates and sizes are in the virtual 2D plane units.
 * <p>
 * Note: when path is not supported the {@link #bounds()} rectangle is considered as outline in calculations.
 *
 * @author hazard157
 */
public interface IVedOutline {

  /**
   * returns the smalles rectangle that contains whole shape.
   *
   * @return {@link ID2Rectangle} - the bounds of the shape
   */
  ID2Rectangle bounds();

  /**
   * Returns the center point of the {@link #bounds()} rectangle.
   *
   * @return {@link ID2Point} - center of bounds rectangle
   */
  ID2Point boundsCenter();

  /**
   * Determines is the specified point is inside or on outline of this shape.
   *
   * @param aX double - X virtual coordinate of the point
   * @param aY double - Y virtual coordinate of the point
   * @return boolean - <code>true</code> if point is inside the shape
   */
  boolean contains( double aX, double aY );

  /**
   * Finds coordinates on the outline path which is nearest point to the specified point.
   *
   * @param aX double - X virtual coordinate of the point
   * @param aY double - Y virtual coordinate of the point
   * @return {@link ID2Point} - virtual coordinates of the point on the outline
   */
  ID2Point nearestPoint( double aX, double aY );

  /**
   * Determines distance from the specified point to the
   *
   * @param aX double - X virtual coordinate of the point
   * @param aY double - Y virtual coordinate of the point
   * @return double - distance from the specified point to the {@link #nearestPoint(double, double)}
   */
  double distanceTo( double aX, double aY );

  /**
   * Returns the point on shape outline.
   * <p>
   * Some point on outline is considered as starting point. Place of any outline point is counted counterclockwise on
   * the outline in percents. 0.00% is starting point and 100% is the starting point again.
   *
   * @param aPercent double - percents of perimeter from starting point (in range 0.0 - 100.0)
   * @return {@link ID2Point} - normal coordinatel of the point
   */
  ID2Point outlinePoint( double aPercent );

  /**
   * Determines if outline path information is supported by this component.
   *
   * @return boolean - flags that {@link #outlinePath()} is supported
   */
  boolean hasPath();

  /**
   * Retruns the outline path.
   *
   * @return {@link Path} - the outline
   * @throws TsUnderDevelopmentRtException path is not supported
   */
  Path outlinePath();

}
