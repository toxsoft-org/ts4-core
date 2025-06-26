package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.*;

interface IAxisAnnotationRenderer
    extends IDisposable {

  ITsPoint requiredSizeForAnnotation( GC aGc, int aHorHint, int aVertHint );

  ITsPoint requiredSizeForTitle( GC aGc );

  ETsOrientation titleOrientation();

  void drawAnnotation( GC aGc );
}
