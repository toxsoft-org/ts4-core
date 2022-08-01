package org.toxsoft.core.tslib.bricks.d2.helpers;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Coordinates conversion.
 *
 * @author hazard157
 */
public interface ID2Convertor {

  double convertX( double aX, double aY );

  double reverseX( double aX, double aY );

  double convertY( double aX, double aY );

  double reverseY( double aX, double aY );

  ID2Point convertPoint( double aX, double aY );

  ID2Point reversePoint( double aX, double aY );

  // considering conversion to display
  ITsRectangle rectBounds( ID2Rectangle aRect );

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
