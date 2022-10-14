package org.toxsoft.core.tsgui.chart.impl;

import static ru.toxsoft.tsgui.chart.renderers.IStdG2GridRendererOptions.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Отрисовщик сетки графика.
 * <p>
 *
 * @author vs
 */
public class StdG2GridRenderer
    implements IG2GridRenderer {

  TsLineInfo horBigTickLine;
  TsLineInfo horMidTickLine;
  TsLineInfo verBigTickLine;
  TsLineInfo verMidTickLine;

  Color horLineColor;
  Color verLineColor;

  /**
   * Конструктор.
   *
   * @param aOptions IOptionSet - параметры отрисовки
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public StdG2GridRenderer( IOptionSet aOptions, ITsGuiContext aContext ) {
    ITsColorManager colorManager = aContext.get( ITsColorManager.class );
    RGBA rgba = HOR_LINE_RGBA.getValue( aOptions ).asValobj();
    horLineColor = colorManager.getColor( rgba.rgb );
    rgba = VER_LINE_RGBA.getValue( aOptions ).asValobj();
    verLineColor = colorManager.getColor( rgba.rgb );

    horBigTickLine = HOR_BIG_TICK_LINE_INFO.getValue( aOptions ).asValobj();
    horMidTickLine = HOR_MID_TICK_LINE_INFO.getValue( aOptions ).asValobj();
    verBigTickLine = VER_BIG_TICK_LINE_INFO.getValue( aOptions ).asValobj();
    verMidTickLine = VER_MID_TICK_LINE_INFO.getValue( aOptions ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStdG2GridRenderer
  //

  @Override
  public void drawGrid( GC aGc, ITsRectangle aBounds, IIntList aHorBigTickPos, IIntList aHorMidTickPos,
      IIntList aVerBigTickPos, IIntList aVerMidTickPos ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds, aHorBigTickPos, aHorMidTickPos, aVerBigTickPos,
        aVerMidTickPos );

    aGc.setForeground( horLineColor );
    aGc.setLineWidth( horBigTickLine.width() );
    for( int i = 0; i < aHorBigTickPos.size(); i++ ) {
      aGc.drawLine( aHorBigTickPos.get( i ).intValue(), aBounds.y1(), aHorBigTickPos.get( i ).intValue(),
          aBounds.y2() );
    }

    aGc.setForeground( verLineColor );
    aGc.setLineWidth( verBigTickLine.width() );
    for( int i = 0; i < aVerBigTickPos.size(); i++ ) {
      aGc.drawLine( aBounds.x1(), aVerBigTickPos.get( i ).intValue(), aBounds.x2(),
          aVerBigTickPos.get( i ).intValue() );
    }
  }

}
