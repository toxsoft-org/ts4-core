package org.toxsoft.core.tsgui.chart.impl;

import static org.toxsoft.core.tsgui.chart.renderers.IStdG2CanvasRendererOptions.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Стандартный отрисовщик холста для отображения графиков.
 * <p>
 *
 * @author vs
 */
public class StdG2CanvasRenderer
    implements IG2CanvasRenderer {

  Margins margins;
  Color   marginColor;
  Color   bkColor;

  IG2GridRenderer gridRenderer;

  ITsColorManager colorManager;

  StdG2CanvasRenderer( IOptionSet aOptions, ITsGuiContext aContext ) {
    margins = MARGINS_INFO.getValue( aOptions ).asValobj();
    colorManager = aContext.get( ITsColorManager.class );

    RGBA rgba;
    rgba = MARGINS_COLOR.getValue( aOptions ).asValobj();
    marginColor = colorManager.getColor( rgba.rgb );
    rgba = BACKGROUND_COLOR.getValue( aOptions ).asValobj();
    bkColor = colorManager.getColor( rgba.rgb );

    // FIXME настройки сетки должны быть вынесены в реализацию IGrid

    // String grClassName = GRID_RENDERER_CLASS.getValue( aOptions ).asString();
    // IG2Params grParams = G2ChartUtils.createParams( grClassName, GRID_RENDERER_OPS.getValue( aOptions ) );
    String grClassName = StdG2GridRenderer.class.getName();
    IG2Params grParams = G2ChartUtils.createParams( grClassName, IOptionSet.NULL );
    gridRenderer = (IG2GridRenderer)G2ChartUtils.createObject( grParams );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStdG2CanvasRenderer
  //

  @Override
  public void drawBackground( GC aGc, ITsRectangle aBounds ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds );

    aGc.setBackground( bkColor );
    ITsRectangle cr = calcClientRect( aBounds );
    aGc.fillRectangle( cr.x1(), cr.x2(), cr.width(), cr.height() );

  }

  @Override
  public void drawCanvas( GC aGc, ITsRectangle aBounds, IList<IG2Graphic> aGraphics ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds );

    // Rectangle clipRect = aGc.getClipping();

    ITsRectangle clientRect = calcClientRect( aBounds );

    Rectangle cr = new Rectangle( clientRect.x1(), clientRect.y1(), clientRect.width(), clientRect.height() );
    aGc.setClipping( cr );

    aGc.setBackground( bkColor );
    aGc.fillRectangle( clientRect.x1(), clientRect.y1(), clientRect.width(), clientRect.height() );

    for( IG2Graphic graphic : aGraphics ) {
      if( graphic.isVisible() ) {
        graphic.draw( aGc, clientRect );
      }
    }

    aGc.setBackground( marginColor );
    aGc.fillRectangle( aBounds.x1(), aBounds.y1(), aBounds.width(), margins.top() + 1 );
    aGc.fillRectangle( aBounds.x1(), aBounds.y2() - margins().bottom(), aBounds.width(), margins.bottom() + 1 );
    aGc.fillRectangle( aBounds.x1(), aBounds.y1(), margins.left(), aBounds.height() );
    aGc.fillRectangle( aBounds.x2() - margins.right(), aBounds.y1(), margins.right() + 1, aBounds.height() );

    // aGc.setClipping( clipRect );
    aGc.setClipping( (Rectangle)null );
  }

  @Override
  public void drawGrid( GC aGc, ITsRectangle aBounds, IList<Pair<Double, ETickType>> aHorTicks,
      IList<Pair<Double, ETickType>> aVerTicks ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds, aHorTicks, aVerTicks );

    ITsRectangle clientRect = calcClientRect( aBounds );
    IIntListEdit horBigTicks = new IntArrayList();
    IIntListEdit horMidTicks = new IntArrayList();
    for( int i = 0; i < aHorTicks.size(); i++ ) {
      Pair<Double, ETickType> p = aHorTicks.get( i );
      if( p.right() == ETickType.BIG ) {
        horBigTicks.add( normToScreen( p.left(), clientRect.width() ).intValue() + clientRect.x1() );
      }
      if( p.right() == ETickType.MIDDLE ) {
        horMidTicks.add( normToScreen( p.left(), clientRect.width() ).intValue() );
      }
    }
    IIntListEdit verBigTicks = new IntArrayList();
    IIntListEdit verMidTicks = new IntArrayList();
    for( int i = 0; i < aVerTicks.size(); i++ ) {
      Pair<Double, ETickType> p = aVerTicks.get( i );
      if( p.right() == ETickType.BIG ) {
        verBigTicks.add( normToScreen( 100 - p.left(), clientRect.height() ).intValue() + clientRect.y1() );
      }
      if( p.right() == ETickType.MIDDLE ) {
        verMidTicks.add( normToScreen( 100 - p.left(), clientRect.height() ).intValue() + clientRect.y1() );
      }
    }
    gridRenderer.drawGrid( aGc, clientRect, horBigTicks, horMidTicks, verBigTicks, verMidTicks );
  }

  @Override
  public Margins margins() {
    return margins;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  protected Integer normToScreen( Double aCoord, int aLength ) {
    return Integer.valueOf( (int)Math.round( (aCoord.doubleValue() * aLength) / 100. ) );
  }

  ITsRectangle calcClientRect( ITsRectangle aBounds ) {
    try {
      return new TsRectangle( aBounds.x1() + margins.left(), aBounds.y1() + margins.top(), //
          aBounds.width() - margins.left() - margins.right(), //
          aBounds.height() - margins.top() - margins.bottom() );
    }
    catch( Throwable e ) {
      return new TsRectangle( 0, 0, 1, 1 );
    }
  }
}
