package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Стандартный отрисовщик разметки.
 *
 * @author vs
 */
public class StdG2AxisMarkingRenderer
    extends AbstractAxisPartRenderer
    implements IAxisMarkingRenderer {

  private final int        indent;
  private final TsLineInfo btLineInfo;
  private final TsLineInfo mtLineInfo;
  private final TsLineInfo ltLineInfo;

  private final Color btColor;
  // dima 11.11.22 экспериментируем с эффектом "вырезанная" шкала
  private final Color shadowColor = new Color( 250, 250, 250 );
  private final Color mtColor;
  private final Color ltColor;

  StdG2AxisMarkingRenderer( IOptionSet aOps, ITsGuiContext aContext ) {
    super( aContext );
    indent = IStdG2AxisMarkingRendererOptions.INDENT_FROM_EDGE.getValue( aOps ).asInt();
    btLineInfo = IStdG2AxisMarkingRendererOptions.BIG_TICK_LINE_INFO.getValue( aOps ).asValobj();
    mtLineInfo = IStdG2AxisMarkingRendererOptions.MID_TICK_LINE_INFO.getValue( aOps ).asValobj();
    ltLineInfo = IStdG2AxisMarkingRendererOptions.LIT_TICK_LINE_INFO.getValue( aOps ).asValobj();

    RGBA rgba;
    rgba = IStdG2AxisMarkingRendererOptions.BIG_TICK_RGBA.getValue( aOps ).asValobj();
    btColor = colorManager().getColor( rgba.rgb );
    rgba = IStdG2AxisMarkingRendererOptions.MID_TICK_RGBA.getValue( aOps ).asValobj();
    mtColor = colorManager().getColor( rgba.rgb );
    rgba = IStdG2AxisMarkingRendererOptions.LIT_TICK_RGBA.getValue( aOps ).asValobj();
    ltColor = colorManager().getColor( rgba.rgb );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStdG2AxisMarkingRenderer
  //

  @Override
  public ITsPoint requiredSize( int aHorHint, int aVerHint ) {
    int length = indent + axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG );
    switch( axisView.cardinalPoint() ) {
      case EAST:
      case WEST:
        return new TsPoint( length, SWT.DEFAULT );
      case NORTH:
      case SOUTH:
        return new TsPoint( SWT.DEFAULT, length );
      case CENTER:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public void drawMarking( GC aGc, ITsRectangle aBounds, AxisMarkingDef aMarking, IList<Pair<Double, ETickType>> aTicks,
      EBorderLayoutPlacement aPlace ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds, aMarking, aTicks, aPlace );
    switch( aPlace ) {
      case EAST:
      case WEST:
        drawVertical( aGc, aBounds, aMarking, aTicks, aPlace );
        break;
      case NORTH:
      case SOUTH:
        drawHorizontal( aGc, aBounds, aMarking, aTicks, aPlace );
        break;
      case CENTER:
        throw new TsIllegalArgumentRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //
  void drawHorizontal( GC aGc, ITsRectangle aBounds, AxisMarkingDef aMarking, IList<Pair<Double, ETickType>> aTicks,
      EBorderLayoutPlacement aPlace ) {
    int y = aBounds.y1() + indent;
    for( Pair<Double, ETickType> tick : aTicks ) {
      setLineType( aGc, tick.right() );
      int tickLength = aMarking.tickSize( tick.right() );
      if( aPlace == EBorderLayoutPlacement.NORTH ) {
        y = aBounds.y2() - indent - tickLength;
      }
      int x = G2ChartUtils.normToScreen( tick.left().doubleValue(), aBounds.width() );
      aGc.drawLine( aBounds.x1() + x, y, aBounds.x1() + x, y + tickLength );
      // dima 11.11.22 экспериментируем с эффектом "вырезанная" шкала
      aGc.setForeground( shadowColor );
      aGc.drawLine( aBounds.x1() + x + aGc.getLineWidth(), y + 1, aBounds.x1() + x + aGc.getLineWidth(),
          y + tickLength + 1 );
    }
  }

  void drawVertical( GC aGc, ITsRectangle aBounds, AxisMarkingDef aMarking, IList<Pair<Double, ETickType>> aTicks,
      EBorderLayoutPlacement aPlace ) {
    int x = aBounds.x1() + indent;
    for( Pair<Double, ETickType> tick : aTicks ) {
      setLineType( aGc, tick.right() );
      int tickLength = aMarking.tickSize( tick.right() );
      if( aPlace == EBorderLayoutPlacement.WEST ) {
        x = aBounds.x2() - indent - tickLength;
      }
      int y = G2ChartUtils.normToScreen( tick.left().doubleValue(), aBounds.height() );
      aGc.drawLine( x, aBounds.y2() - y, x + tickLength, aBounds.y2() - y );
    }
  }

  void setLineType( GC aGc, ETickType aType ) {
    switch( aType ) {
      case BIG:
        aGc.setForeground( btColor );
        aGc.setLineWidth( btLineInfo.width() );
        // FIXME установит стиль линии aGc.setLineStyle( btLineInfo.type(). );
        break;
      case LITTLE:
        aGc.setForeground( ltColor );
        aGc.setLineWidth( ltLineInfo.width() );
        // FIXME установит стиль линии aGc.setLineStyle( btLineInfo.type(). );
        break;
      case MIDDLE:
        aGc.setForeground( mtColor );
        aGc.setLineWidth( mtLineInfo.width() );
        // FIXME установит стиль линии aGc.setLineStyle( btLineInfo.type(). );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }
}
