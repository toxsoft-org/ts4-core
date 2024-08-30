package org.toxsoft.core.tsgui.graphics.vpcalc.impl1;

import org.toxsoft.core.tsgui.graphics.vpcalc.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

class Ctx {

  final CalculationStrategySettings settings;
  final ViewportOutput              output = new ViewportOutput();

  ID2SizeEdit     inSize2D = new D2SizeEdit( 16.0, 16.0 );
  TsRectangleEdit vpBounds = new TsRectangleEdit( 0, 0, 100, 100 );

  double       zoomFactor = 1.0;
  ID2PointEdit origin     = new D2PointEdit( 0.0, 0.0 );
  ID2AngleEdit rotation   = D2AngleEdit.ofRadians( 0.0 );

  public Ctx( CalculationStrategySettings aSettings ) {
    settings = aSettings;
  }

}
