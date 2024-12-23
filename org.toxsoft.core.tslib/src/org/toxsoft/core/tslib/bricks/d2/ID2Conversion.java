package org.toxsoft.core.tslib.bricks.d2;

/**
 * Coordinates conversion rules between two coordinates system.
 * <p>
 * Conversion from source coordinate system N (normalized coordinates system) to target S (screen coordinates system) is
 * performed as:
 * <ol>
 * <li>N will be rotated around coordinates space origin (0.0,0.0) point by {@link #rotation()} angle.</li>
 * <li>rotated N will be zoomed (from CS origin) by {@link #zoomFactor()}, where zoom of 1.0 means no zoom;</li>
 * <li>origin, the point (0.0,0.0) of N will be placed at {@link #origin()} coordinates on S;</li>
 * </ol>
 * Note that both normal and screen coordinate systems has Y axis directed from top to bottom (as it is usual for
 * computer display coordinates).
 *
 * @author hazard157
 */
public sealed interface ID2Conversion
    permits ID2ConversionEdit, D2Conversion {

  /**
   * No conversion parameters singleton.
   */
  ID2Conversion NONE = new D2Conversion( ID2Angle.ZERO, 1.0, ID2Point.ZERO );

  /**
   * Returns the rotation angle.
   *
   * @return {@link ID2Angle} - the rotation angle
   */
  ID2Angle rotation();

  /**
   * Returns the zoom factor where 1.0 means no zoom.
   *
   * @return double - the zoom factor
   */
  double zoomFactor();

  /**
   * Returns the coordinates of origin after conversion.
   *
   * @return {@link ID2Point} - source N origin (0.0,0.0) coordinates on target in S coordinates space
   */
  ID2Point origin();

  /**
   * Determines if this instance specifies real conversion.
   * <p>
   * This method simply analyzes parameters of the conversion and returns true if all of the following conditions are
   * met:
   * <ul>
   * <li>{@link #rotation()} equals to {@link ID2Angle#ZERO};</li>
   * <li>{@link #zoomFactor()} == 1.0;</li>
   * <li>{@link #origin()} equals to {@link ID2Point#ZERO}.</li>
   * </ul>
   *
   * @return boolean - <code>true</code> if conversion parameters makes any changes
   */
  boolean isConversion();

}
