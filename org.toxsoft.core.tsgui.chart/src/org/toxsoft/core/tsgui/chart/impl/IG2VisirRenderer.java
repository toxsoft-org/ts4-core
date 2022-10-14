package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;

interface IG2VisirRenderer {

  void drawVisir( GC aGc, ITsRectangle aBounds, int aX, int aY, long aVisirTime, IList<IG2Graphic> aGraphics );
}
