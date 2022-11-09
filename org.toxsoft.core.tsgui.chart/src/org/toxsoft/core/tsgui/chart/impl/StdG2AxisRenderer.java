package org.toxsoft.core.tsgui.chart.impl;

import static org.toxsoft.core.tsgui.chart.renderers.IStdG2AxisRendererOptions.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * Финальная реализация стандартного отрисовщика шкалы.
 * <p>
 * Данная реализация предполагает наличие 3-х отдельных отрисовщиков:
 * <ul>
 * <li>Фона</li>
 * <li>Разметки</li>
 * <li>Подписей</li>
 * </ul>
 * В рамках такого подхода наличие другой реализации отрисовщика шкалы представляется нецелесообразным.<br>
 * Различные варианты отрисовки осуществляются путем выбора соотвествующих отрисовщиков фона, разметки или подписей, или
 * же выбором другой реализации {@link IG2AxisRenderer} (если подходящей нет - ее можно написать).
 *
 * @author vs
 */
public class StdG2AxisRenderer
    extends AbstractAxisPartRenderer
    implements IG2AxisRenderer {

  IBackgroundRenderer     bkRenderer;
  IAxisMarkingRenderer    markingRenderer;
  IAxisAnnotationRenderer annotationRenderer;

  StdG2AxisRenderer( IOptionSet aParams, ITsGuiContext aContext ) {
    super( aContext );

    IOptionSet opSet = BACKGROUND_RENDERER_OPS.getValue( aParams ).asValobj();
    IG2Params g2Params =
        G2ChartUtils.createParams( BACKGROUND_RENDERER_CLASS.getValue( aParams ).asString(), opSet, aContext );
    bkRenderer = (IBackgroundRenderer)G2ChartUtils.createObject( g2Params );

    opSet = MARKING_RENDERER_OPS.getValue( aParams ).asValobj();
    g2Params = G2ChartUtils.createParams( MARKING_RENDERER_CLASS.getValue( aParams ).asString(), opSet, aContext );
    markingRenderer = (IAxisMarkingRenderer)G2ChartUtils.createObject( g2Params );

    opSet = ANNOTATION_RENDERER_OPS.getValue( aParams ).asValobj();
    g2Params = G2ChartUtils.createParams( ANNOTATION_RENDERER_CLASS.getValue( aParams ).asString(), opSet, aContext );
    annotationRenderer = (IAxisAnnotationRenderer)G2ChartUtils.createObject( g2Params );
  }

  @Override
  void setAxisView( G2AxisViewBase aAxis ) {
    super.setAxisView( aAxis );
    ((AbstractAxisPartRenderer)markingRenderer).setAxisView( aAxis );
    ((AbstractAxisPartRenderer)annotationRenderer).setAxisView( aAxis );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2AxisRenderer
  //

  @Override
  public ITsPoint prefSize( GC aGc, int aHorHint, int aVertHint ) {
    ITsPoint mp = markingRenderer.requiredSize( aHorHint, aVertHint );
    ITsPoint ap = annotationRenderer.requiredSizeForAnnotation( aGc, aHorHint, aVertHint );
    return new TsPoint( mp.x() + ap.x(), mp.y() + ap.y() );
  }

  @Override
  public void drawBackground( GC aGc, ITsRectangle aBounds, EBorderLayoutPlacement aCardinalPoint ) {
    bkRenderer.drawBackground( aGc, aBounds, aCardinalPoint, false, false );
  }

  @Override
  public void drawAxis( GC aGc, ITsRectangle aBounds, EBorderLayoutPlacement aCardinalPoint ) {
    markingRenderer.drawMarking( aGc, axisView.markingBounds(), axisView.axisModel().axisMarkingDef(),
        axisView.calcTickPositions(), aCardinalPoint );
    annotationRenderer.drawAnnotation( aGc );
    // draw border
  }

  @Override
  public ITsPoint requiredTitleSize( GC aGc ) {
    return annotationRenderer.requiredSizeForTitle( aGc );
  }

  @Override
  public ETsOrientation titleOrientation() {
    return annotationRenderer.titleOrientation();
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  void setAnnotationRenderer( IAxisAnnotationRenderer aRenderer ) {
    annotationRenderer = aRenderer;
  }

}
