package org.toxsoft.core.tsgui.chart.impl;

import static org.toxsoft.core.tsgui.chart.renderers.IStdG2VisirRendererOptions.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Стандартный отрисовщик "визира".
 * <p>
 * Рисует вертикальную линию на всю высоту, переданного прямоугольника, проходящую через координату х.
 *
 * @author vs
 */
public class StdG2VisirRenderer
    implements IG2VisirRenderer {

  private final static String dtFormat       = "%1$tF %1$tT"; //$NON-NLS-1$
  private final static String setPointFormat = "[%s]";        //$NON-NLS-1$

  private final TsLineInfo verticalLineInfo;
  private final Color      verticalLineColor;
  private final Color      colorBlack;
  private final Margins    margins     = new Margins( 8, 8, 8, 8 );
  private final int        vertIndent  = 2;
  private final int        shadowDepth = 8;
  private final int        thumbW      = 24;
  private final int        horIndent   = 4;
  private final boolean    showNames;

  private int itemH = 0;

  private Color bkColor;

  /**
   * Конструктор.
   *
   * @param aOptions IOptionSet - параметры отрисовки
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public StdG2VisirRenderer( IOptionSet aOptions, ITsGuiContext aContext ) {
    ITsColorManager colorManager = aContext.get( ITsColorManager.class );
    verticalLineInfo = VISIR_LINE_INFO.getValue( aOptions ).asValobj();
    RGBA rgba = VISIR_LINE_RGBA.getValue( aOptions ).asValobj();
    verticalLineColor = colorManager.getColor( rgba.rgb );
    colorBlack = colorManager.getColor( ETsColor.BLACK );
    bkColor = colorManager.getColor( new RGB( 255, 255, 200 ) );
    showNames = SHOW_NAMES.getValue( aOptions ).asBool();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2VisirRenderer
  //

  @Override
  public void drawVisir( GC aGc, ITsRectangle aBounds, int aX, int aY, long aVisirTime, IList<IG2Graphic> aGraphics ) {
    TsNullArgumentRtException.checkNulls( aGc, aGraphics );

    aGc.setForeground( verticalLineColor );
    aGc.setLineWidth( verticalLineInfo.width() );
    aGc.drawLine( aBounds.x1() + aX, aBounds.y1(), aBounds.x1() + aX, aBounds.y2() );
    // dima 06.02.23 перемещаем подсказку в левую часть
    TsPoint size = calcSize( aGc, aVisirTime, aGraphics );
    int tipX = aBounds.x1() + aX - size.x() - 2;
    // int tipX = aBounds.x1() + aX + 2; // вариант когда подсказка справа
    // drawBackground( aGc, aBounds.x1() + aX + 2, aBounds.y1() + aY, aVisirTime, aGraphics );
    drawBackground( aGc, tipX, aBounds.y1() + aY, aVisirTime, aGraphics );

    String timeStr = timeToString( aVisirTime );
    aGc.setForeground( colorBlack );
    // aGc.drawText( timeStr, aBounds.x1() + aX + 2 + margins.left(), aBounds.y1() + aY + margins.top(), true );
    aGc.drawText( timeStr, tipX + margins.left(), aBounds.y1() + aY + margins.top(), true );

    int y = aBounds.y1() + aY + 10 + aGc.textExtent( timeStr ).y + vertIndent;
    // int x = aBounds.x1() + aX + margins.left() + 2;
    int x = tipX + margins.left();

    for( IG2Graphic graph : aGraphics ) {
      if( graph.isVisible() ) {
        TsRectangle bounds = new TsRectangle( x, y, thumbW, itemH );
        graph.drawRepresentation( aGc, bounds );

        ITemporalAtomicValue val = graph.valueAt( aVisirTime );
        String valStr = graph.valueToString( val );
        if( graph instanceof StdG2Graphic stdGraph ) {
          IStringList setPoints = stdGraph.setPoints();
          for( String spVal : setPoints ) {
            valStr += String.format( setPointFormat, spVal );
          }
        }
        Point p = aGc.textExtent( valStr );
        aGc.drawText( valStr, x + thumbW + horIndent, y + (itemH - p.y) / 2, true );
        if( showNames ) {
          aGc.drawText( graph.plotDef().nmName(), x + thumbW + horIndent + p.x + horIndent, y + (itemH - p.y) / 2,
              true );
        }
        y += itemH + vertIndent;
      }
    }

  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  String timeToString( long aTime ) {
    return String.format( dtFormat, Long.valueOf( aTime ) );
  }

  TsPoint calcSize( GC aGc, long aTime, IList<IG2Graphic> aGraphics ) {
    int w = margins.left() + margins.right();
    int h = margins.top() + margins.bottom();
    itemH = aGc.textExtent( "Pg" ).y; //$NON-NLS-1$
    if( itemH < 16 ) {
      itemH = 16;
    }
    h += aGraphics.size() * vertIndent + (aGraphics.size() + 1) * itemH;

    String timeStr = timeToString( aTime );

    int maxW = aGc.textExtent( timeStr ).x;
    int curW;

    for( IG2Graphic graph : aGraphics ) {
      curW = aGc.textExtent( graph.valueToString( graph.valueAt( aTime ) ) ).x + thumbW + horIndent;
      if( showNames ) {
        curW += aGc.textExtent( graph.plotDef().nmName() ).x + horIndent;
      }
      // dima 24.01.23 добавляем место для уставок
      if( graph instanceof StdG2Graphic stdGraph ) {
        IStringList setPoints = stdGraph.setPoints();
        for( String spVal : setPoints ) {
          curW += aGc.textExtent( String.format( setPointFormat, spVal ) ).x;
        }
      }
      if( curW > maxW ) {
        maxW = curW;
      }
    }

    w += maxW;

    return new TsPoint( w, h );
  }

  void drawBackground( GC aGc, int aX, int aY, long aTime, IList<IG2Graphic> aGraphics ) {
    TsPoint size = calcSize( aGc, aTime, aGraphics );

    aGc.setForeground( colorBlack );
    aGc.setBackground( bkColor );
    aGc.setLineWidth( 1 );

    aGc.setAlpha( 200 );
    aGc.fillRectangle( aX, aY, size.x(), size.y() );
    aGc.setAlpha( 255 );
    aGc.drawRectangle( aX, aY, size.x(), size.y() );

    aGc.setAlpha( 100 );
    aGc.setBackground( colorBlack );
    aGc.fillRectangle( aX + shadowDepth, aY + size.y(), size.x(), shadowDepth );
    aGc.fillRectangle( aX + size.x(), aY + shadowDepth, shadowDepth, size.y() - shadowDepth );

    aGc.setAlpha( 255 );
  }

}
