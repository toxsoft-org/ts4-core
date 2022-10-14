package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Стандартный отрисовщик графика.
 * <p>
 *
 * @author vs
 */
public class StdG2GraphicRenderer
    implements IG2GraphicRenderer {

  private final TsLineInfo            lineInfo;
  private final EGraphicRenderingKind renderingKind;
  private Color                       graphicColor;

  ITsColorManager colorManager;

  /**
   * Конструктор.<br>
   *
   * @param aRendererParams IG2Params - параметры отрисовки
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public StdG2GraphicRenderer( IG2Params aRendererParams, ITsGuiContext aContext ) {
    colorManager = aContext.get( ITsColorManager.class );
    RGBA rgba = IStdG2GraphicRendererOptions.GRAPHIC_RGBA.getValue( aRendererParams.params() ).asValobj();
    graphicColor = colorManager.getColor( rgba.rgb );
    lineInfo = IStdG2GraphicRendererOptions.GRAPHIC_LINE_INFO.getValue( aRendererParams.params() ).asValobj();
    renderingKind = IStdG2GraphicRendererOptions.RENDERING_KIND.getValue( aRendererParams.params() ).asValobj();
  }

  @Override
  public void drawGraphic( GC aGc, IList<IList<Pair<Integer, Integer>>> aPolyLines, ITsRectangle aBounds ) {

    aGc.setForeground( graphicColor );
    aGc.setLineWidth( lineInfo.width() );

    switch( renderingKind ) {
      case LINE: {
        for( int i = 0; i < aPolyLines.size(); i++ ) {
          IList<Pair<Integer, Integer>> polyline = aPolyLines.get( i );
          int[] points = new int[polyline.size() * 2];
          int idx = 0;
          for( int j = 0; j < polyline.size(); j++ ) {
            Pair<Integer, Integer> p = polyline.get( j );
            points[idx] = p.left().intValue();
            idx++;
            points[idx] = p.right().intValue();
            idx++;
          }
          aGc.drawPolyline( points );
        }
      }
        break;

      case LADDER: {
        for( int i = 0; i < aPolyLines.size(); i++ ) {
          IList<Pair<Integer, Integer>> oldpolyline = aPolyLines.get( i );
          IListEdit<Pair<Integer, Integer>> polyline = new ElemArrayList<>();
          Pair<Integer, Integer> p2 = null;
          for( int j = 0; j < oldpolyline.size() - 1; j++ ) {
            Pair<Integer, Integer> p1 = oldpolyline.get( j );
            p2 = oldpolyline.get( j + 1 );
            Pair<Integer, Integer> pm = new Pair<>( p2.left(), p1.right() );
            polyline.add( p1 );
            polyline.add( pm );
          }
          if( p2 != null ) {
            polyline.add( p2 );
          }

          int[] points = new int[polyline.size() * 2];
          int idx = 0;
          for( int j = 0; j < polyline.size(); j++ ) {
            Pair<Integer, Integer> p = polyline.get( j );
            points[idx] = p.left().intValue();
            idx++;
            points[idx] = p.right().intValue();
            idx++;
          }
          aGc.drawPolyline( points );
        }
        break;
      }

      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public void drawRepresentation( GC aGc, ITsRectangle aBounds ) {
    // int w = aBounds.width() - 2;
    // int h = lineInfo.thickness();
    // if( h > aBounds.height() - 2 ) {
    // h = aBounds.height() - 2;
    // }
    int w = aBounds.width() - 2;
    int h = aBounds.height();
    int x = aBounds.x1();
    int y = aBounds.y1() + (aBounds.height() - h) / 2;

    aGc.setForeground( graphicColor );
    aGc.setLineWidth( h );
    aGc.drawLine( x, y + h / 2, x + w, y + h / 2 );

    aGc.setForeground( colorManager.getColor( ETsColor.BLACK ) );
    aGc.setLineWidth( 1 );
    aGc.drawRectangle( x, y, w, h );
  }

}
