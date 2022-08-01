package org.toxsoft.core.tslib.bricks.d2.helpers;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Coordinates conversion.
 * <p>
 * Terminology:
 * <ul>
 * <li><b>N</b> - normalized coordinates system is the source ov conversion and target for reversion;</li>
 * <li><b>S</b> - screen coordinates system is the tagret for convertion and source for reversion.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface ID2Convertor {

  /**
   * Converts X coordinate from <b>N</b> to <b>S</b>.
   *
   * @param aX double - X coordinate in <b>N</b> space
   * @param aY double - Y coordinate in <b>N</b> space
   * @return double - X coordinate in <b>S</b> space
   */
  double convertX( double aX, double aY );

  /**
   * Converts Y coordinate from <b>N</b> to <b>S</b>.
   *
   * @param aX double - X coordinate in <b>N</b> space
   * @param aY double - Y coordinate in <b>N</b> space
   * @return double - Y coordinate in <b>S</b> space
   */
  double convertY( double aX, double aY );

  /**
   * Converts point coordinates from <b>N</b> to <b>S</b>.
   *
   * @param aX double - X coordinate in <b>N</b> space
   * @param aY double - Y coordinate in <b>N</b> space
   * @return {@link ID2Point} - point coordinates in <b>S</b> space
   */
  ID2Point convertPoint( double aX, double aY );

  /**
   * Reverses X coordinate from <b>S</b> to <b>N</b>.
   *
   * @param aX double - X coordinate in <b>S</b> space
   * @param aY double - Y coordinate in <b>S</b> space
   * @return double - X coordinate in <b>N</b> space
   */
  double reverseX( double aX, double aY );

  /**
   * Reverses Y coordinate from <b>S</b> to <b>N</b>.
   *
   * @param aX double - X coordinate in <b>S</b> space
   * @param aY double - Y coordinate in <b>S</b> space
   * @return double - Y coordinate in <b>N</b> space
   */
  double reverseY( double aX, double aY );

  /**
   * Reverses point coordinates from <b>S</b> to <b>N</b>.
   *
   * @param aX double - X coordinate in <b>S</b> space
   * @param aY double - Y coordinate in <b>S</b> space
   * @return {@link ID2Point} - point coordinates in <b>N</b> space
   */
  ID2Point reversePoint( double aX, double aY );

  // considering conversion to display

  /**
   * Finds surrounding rectangle in <b>S</b> around rectangle specified in <b>N</b> space.
   * <p>
   * The sides of the found rectangle are parallel to the S (screen) coordinate axes. This helper method assumes that
   * found rectangle is needed for drawing on screen so it returns rectanle with integer coordinates in pixels.
   *
   * @param aSourceRect {@link ID2Rectangle} - rectangle in <b>N</b> space
   * @return {@link ITsRectangle} - rectangle in <b>S</b> space with integer coordinates
   */
  ITsRectangle rectBounds( ID2Rectangle aSourceRect );

  // ------------------------------------------------------------------------------------
  // inline methods for convinience
  //

  @SuppressWarnings( "javadoc" )
  default double convertX( ID2Point aPoint ) {
    TsNullArgumentRtException.checkNull( aPoint );
    return convertX( aPoint.x(), aPoint.y() );
  }

  @SuppressWarnings( "javadoc" )
  default double convertY( ID2Point aPoint ) {
    TsNullArgumentRtException.checkNull( aPoint );
    return convertY( aPoint.x(), aPoint.y() );
  }

  @SuppressWarnings( "javadoc" )
  default double reverseX( ID2Point aPoint ) {
    TsNullArgumentRtException.checkNull( aPoint );
    return reverseX( aPoint.x(), aPoint.y() );
  }

  @SuppressWarnings( "javadoc" )
  default double reverseY( ID2Point aPoint ) {
    TsNullArgumentRtException.checkNull( aPoint );
    return reverseY( aPoint.x(), aPoint.y() );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Point convertPoint( ID2Point aPoint ) {
    TsNullArgumentRtException.checkNull( aPoint );
    return convertPoint( aPoint.x(), aPoint.y() );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Point reversePoint( ID2Point aPoint ) {
    TsNullArgumentRtException.checkNull( aPoint );
    return reversePoint( aPoint.x(), aPoint.y() );
  }

}
