package org.toxsoft.core.tsgui.graphics.vpcalc2.impl;

import static java.lang.Math.*;

import org.toxsoft.core.tslib.bricks.d2.*;

class VpCalcUtils {

  public static ID2Size rotateAndZoomSize( ID2Size aSize, ID2Angle aAngle, double aZoomFactor ) {
    double alpha = aAngle.radians();
    double w1 = aZoomFactor * aSize.width();
    double h1 = aZoomFactor * aSize.height();
    double w2 = abs( w1 * cos( alpha ) ) + abs( h1 * sin( alpha ) );
    double h2 = abs( w1 * sin( alpha ) ) + abs( h1 * cos( alpha ) );
    return new D2Size( w2, h2 );
  }

}
