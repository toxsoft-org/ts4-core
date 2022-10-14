package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Отрисовщик разметки шкалы.
 * <p>
 * <b>На заметку:</b><br>
 * Интерфейс предназначен для разработчиков графической компоненты, чтобы они могли разработать собственный отрисовщик
 * (В случае если не устраивает поведение стандартного отрисовщика {@link StdG2AxisMarkingRenderer} без переделки
 * остального кода.
 *
 * @author vs
 */
interface IAxisMarkingRenderer {

  ITsPoint requiredSize( int aHorHint, int aVerHint );

  void drawMarking( GC aGc, ITsRectangle aBounds, AxisMarkingDef aMarking, IList<Pair<Double, ETickType>> aTicks,
      EBorderLayoutPlacement aPlace );
}
