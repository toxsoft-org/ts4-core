package org.toxsoft.core.tslib.bricks.d2.helpers;

import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * The base geometry data of any figure.
 * <p>
 * Contains information about figure's placement, dimensions, rotation and zoom.
 * <p>
 * There are some concepts related to the figure's geometry data:
 * <ul>
 * <li>the <b>global coordinates</b> in the 2D world - D2 package is dealing with <code>double</code> X and Y
 * coordinates on the imaginary plane (<i>global coordinates plane</i>). Coordinates (and sizes, etc) values
 * interpretation is application-specific. For example, when used for drawing on display <code>double</code> unit may be
 * considered as a pixel, while for the machinery calculations <code>double</code> unit may be 1 centimeter;</li>
 * <li>the <b>local coordinates</b> - it is assumed that every figure determines it's own <i>local coordinates
 * plane</i>. Local axis are parallel to the global axis, and have the same scale;</li>
 * <li>the <b>conversion</b> of a figure - а figure is defined as a geometric concept, such as an ellipse, a square, a
 * poly-line, besier curve, even a collection of other figures. Definition of a figure includes it's own sizes,
 * coordinates, angles, etc. After figure is defined and definition remain unchanged, a conversion may be applied to
 * rotate and or zoom (scale) the figure as a whole. Note that the coordinates of the combined point of rotation and
 * scaling of the figure are given in the local coordinate system.</li>
 * </ul>
 *
 * @author hazard157
 */
public sealed interface ID2GeomData
    permits ID2GeomDataEdit, D2GeomData {

  /**
   * Data at the the origin with no conversion.
   */
  ID2GeomData ZERO = new D2GeomData( ID2Point.ZERO, ID2Conversion.NONE );

  /**
   * Returns the location of the figure on the plane.
   *
   * @return {@link ID2Point} - the location of the local origin on the global plane
   */
  ID2Point location();

  /**
   * Returns the figure conversion information.
   * <p>
   * Coordinates of the {@link ID2Conversion#origin()} are given in the figures local coordinates.
   *
   * @return {@link ID2Conversion} - the figure conversion information
   */
  ID2Conversion conversion();

}
