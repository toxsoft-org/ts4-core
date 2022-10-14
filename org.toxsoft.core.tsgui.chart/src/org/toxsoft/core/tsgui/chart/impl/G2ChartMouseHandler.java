package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.toxsoft.core.singlesrc.rcp.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Реализация обработчика событий мыши для графической копоненты.
 *
 * @author vs
 */
public class G2ChartMouseHandler {

  G2Chart    chart   = null;
  IG2Console console = null;

  /**
   * Публичный конструктор без аргументов.
   */
  public G2ChartMouseHandler() {

  }

  void init( G2Chart aChart ) {

    chart = aChart;
    console = chart.console();

    ISingleSourcing_MouseWheelListener mwl = new ISingleSourcing_MouseWheelListener() {

      @Override
      public void mouseScrolled( MouseEvent e ) {
        IG2ChartArea area = chart.chartLayout().areaUnderCursor( e.x, e.y );
        if( area != null ) {
          switch( area.elementKind() ) {
            case CANVAS:
              canvasMouseScrolled( e );
              break;
            case LABEL:
              break;
            case LEGEND:
              break;
            case TITLE:
              break;
            case X_AXIS:
              if( e.stateMask == SWT.CTRL ) {
                xAxisMouseScaled( e );
              }
              else {
                xAxisMouseScrolled( e );
              }
              break;
            case Y_AXIS:
              if( e.stateMask == SWT.CTRL ) {
                yAxisMouseScaled( area, e );
              }
              else {
                yAxisMouseScrolled( area, e );
              }
              break;
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
        }
      }
    };
    TsSinglesourcingUtils.Control_addMouseWheelListener( aChart.getControl(), mwl );
  }

  void dispose() {

  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //

  protected void xAxisMouseScrolled( MouseEvent aEvent ) {
    XAxisView axisView = chart.xAxisView();
    if( aEvent.count < 0 ) {
      console.locateX( console.getX1() + axisView.timeUnit.timeInMills() );
    }
    else {
      console.locateX( console.getX1() - axisView.timeUnit.timeInMills() );
    }
    chart.refresh();
  }

  protected void yAxisMouseScrolled( IG2ChartArea aArea, MouseEvent aEvent ) {
    console.shiftYAxis( aArea.elementId(), -aEvent.count );
    chart.refresh();
  }

  protected void canvasMouseScrolled( MouseEvent aEvent ) {
    for( IYAxisDef def : chart.yAxisDefs() ) {
      console.shiftYAxis( def.id(), -aEvent.count );
    }
    chart.refresh();
  }

  protected void yAxisMouseScaled( IG2ChartArea aArea, MouseEvent aEvent ) {
    if( aEvent.count < 0 ) {
      console.scaleYAxis( aArea.elementId(), 2 );
    }
    else {
      // double newScale = 0.5 * aEvent.count;
      console.scaleYAxis( aArea.elementId(), 0.5 );
    }
    chart.refresh();
  }

  protected void xAxisMouseScaled( MouseEvent aEvent ) {
    if( aEvent.count < 0 ) {
      console.changeXScale( 0, 2.0 );
    }
    else {
      console.changeXScale( 0, 0.5 );
    }
    chart.refresh();
  }

}
