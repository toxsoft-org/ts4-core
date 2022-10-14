package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Отрисовщик графической компоненты.
 * <p>
 * Существует единственная реализация данного отрисовщика. Вся вариативность достигается использованием различных
 * отрисовщиков областей компоненты.
 *
 * @author vs
 */
final class G2ChartRenderer {

  /**
   * Менеджер размещений, который хранит набор областей и знает общие границы компоненты.
   */
  private final AbstractG2Layout layout;

  ITsColorManager colorManager;

  G2ChartRenderer( AbstractG2Layout aLayout, ITsGuiContext aContext ) {
    layout = aLayout;
    colorManager = aContext.get( ITsColorManager.class );
  }

  /**
   * Отрисовывает графическую компоненту, сначала вызывая отрисовщики заднего плана для всех областей, а затем
   * отрисовщики содержательной части областей.
   *
   * @param aGc
   */
  void draw( GC aGc ) {
    aGc.setBackground( colorManager.getColor( new RGB( 220, 220, 220 ) ) );
    aGc.fillRectangle( layout.bounds() );

    // Отрисуем все холсты с графиками
    IList<G2Canvas> canvases = layout.listG2Canvases();
    for( int i = 0; i < canvases.size(); i++ ) {
      IG2Canvas canvas = canvases.get( i );
      canvas.drawBackground( aGc );
      canvas.draw( aGc );
    }

    // Отрисуем все элементов кроме холстов, чтобы закрасить вылезающие графики
    IList<IG2ChartArea> areas = layout.listAreas();
    for( int i = 0; i < areas.size(); i++ ) {
      IG2ChartArea area = areas.get( i );
      if( area.elementKind() != EG2ChartElementKind.CANVAS ) {
        area.drawBackground( aGc );
        area.draw( aGc );
      }
    }
  }

}
