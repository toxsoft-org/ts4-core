package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

interface IG2CanvasRenderer {

  void drawBackground( GC aGc, ITsRectangle aBounds );

  void drawGrid( GC aGc, ITsRectangle aBounds, IList<Pair<Double, ETickType>> aHorTicks,
      IList<Pair<Double, ETickType>> aVerTicks );

  void drawCanvas( GC aGc, ITsRectangle aBounds, IList<IG2Graphic> aGraphics );

  Margins margins();
}
