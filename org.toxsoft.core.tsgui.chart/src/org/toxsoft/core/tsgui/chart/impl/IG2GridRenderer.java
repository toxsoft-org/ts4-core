package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

interface IG2GridRenderer {

  void drawGrid( GC aGc, ITsRectangle aBounds, IIntList aHorBigTickPos, IIntList aHorMidTickPos,
      IIntList aVerBigTickPos, IIntList aVerMidTickPos );
}
