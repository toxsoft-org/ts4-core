package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.tsgui.chart.api.*;
import ru.toxsoft.tsgui.chart.legaсy.*;
import ru.toxsoft.tsgui.chart.renderers.*;

/**
 * Холст для отображения графиков.
 * <p>
 *
 * @author vs
 */
public class G2Canvas
    extends AbstractG2ChartArea
    implements IG2Canvas {

  boolean               drawGrid   = true;
  private final Margins margins;
  ITsRectangle          clientRect = null;

  IListEdit<IG2Graphic> graphics = new ElemArrayList<>();

  IG2CanvasRenderer renderer;

  XAxisView xAxisView;
  YAxisView yAxisView;

  IG2Visir visir = null;

  /**
   * Конструктор.
   *
   * @param aId String - ИД холста
   * @param aDescription String - описание холста
   * @param aRendererOptions IG2Params - параметры отрисовки
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public G2Canvas( String aId, String aDescription, IG2Params aRendererOptions, ITsGuiContext aContext ) {
    super( aId, TsLibUtils.EMPTY_STRING, aDescription );
    margins = IStdG2CanvasRendererOptions.MARGINS_INFO.getValue( aRendererOptions.params() ).asValobj();
    renderer = new StdG2CanvasRenderer( aRendererOptions.params(), aContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractG2ChartArea
  //

  @Override
  public String elementId() {
    return id();
  }

  @Override
  public EG2ChartElementKind elementKind() {
    return EG2ChartElementKind.CANVAS;
  }

  @Override
  public ITsPoint prefSize( GC aGc, int aHorHint, int aVertHint ) {
    return new TsPoint( -1, -1 );
  }

  @Override
  public void drawBackground( GC aGc ) {
    renderer.drawBackground( aGc, bounds );
  }

  @Override
  public void draw( GC aGc ) {
    renderer.drawCanvas( aGc, bounds, graphics );

    if( drawGrid ) {
      if( yAxisView != null ) {
        renderer.drawGrid( aGc, bounds, xAxisView.calcTickPositions(), yAxisView.calcTickPositions() );
      }
      else {
        renderer.drawGrid( aGc, bounds, xAxisView.calcTickPositions(), IList.EMPTY );
      }
    }

    if( visir != null && visir.isVisible() ) {
      int x = G2ChartUtils.normToScreen( visir.getPosition(), clientRect.width() );
      visir.renderer().drawVisir( aGc, clientRect, x, 10, visir.getVisirTime(), graphics );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов IG2Canvas
  //

  @Override
  public Margins margins() {
    return margins;
  }

  @Override
  protected void onBoundsChanged() {
    try {
      clientRect = new TsRectangle( bounds.x1() + margins.left(), bounds.y1() + margins.right(), //
          bounds.width() - margins.left() - margins.right(), //
          bounds.height() - margins.top() - margins.bottom() );
    }
    catch( Throwable e ) {
      e.printStackTrace();
      clientRect = new TsRectangle( 0, 0, 1, 1 );
    }
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  public void setXAxisView( XAxisView aAxis ) {
    xAxisView = aAxis;
  }

  public void setYAxis( YAxisView aAxis ) {
    yAxisView = aAxis;
  }

  public void addGraphic( IG2Graphic aGraphic ) {
    graphics.add( aGraphic );
  }

  void setVisir( IG2Visir aVisir ) {
    visir = aVisir;
  }

  public IList<IG2Graphic> listGraphics() {
    return graphics;
  }

}
