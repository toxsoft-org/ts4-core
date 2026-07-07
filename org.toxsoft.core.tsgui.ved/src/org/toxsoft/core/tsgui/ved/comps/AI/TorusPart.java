package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class TorusPart {

  // Параметры тора
  static final double R       = 100; // большой радиус
  static final double r       = 35;  // малый радиус (трубка)
  static final int    STEPS_U = 40;  // детализация трубки
  static final int    STEPS_V = 60;  // детализация вращения

  // Диапазон угла v — часть тора (здесь: половина)
  static final double V_START = 0;
  static final double V_END   = Math.PI; // от 0 до π = половина тора

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Часть тора — SWT" );
    shell.setSize( 600, 500 );

    Canvas canvas = new Canvas( shell, SWT.NONE );
    canvas.setBounds( 0, 0, 600, 500 );

    canvas.addPaintListener( e -> drawPartialTorus( e.gc, 300, 250 ) );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  static void drawPartialTorus( GC gc, int cx, int cy ) {
    gc.setAntialias( SWT.ON );

    // Собираем точки в 2D-проекции (изометрия или простая проекция)
    double angleV = V_START;
    double stepV = (V_END - V_START) / STEPS_V;

    for( int j = 0; j <= STEPS_V; j++, angleV += stepV ) {
      double angleU = 0;
      double stepU = 2 * Math.PI / STEPS_U;

      int[] prevX = new int[STEPS_U + 1];
      int[] prevY = new int[STEPS_U + 1];

      for( int i = 0; i <= STEPS_U; i++, angleU += stepU ) {
        // Параметрические уравнения тора
        double x3 = (R + r * Math.cos( angleU )) * Math.cos( angleV );
        double y3 = (R + r * Math.cos( angleU )) * Math.sin( angleV );
        double z3 = r * Math.sin( angleU );

        // Простая изометрическая проекция
        int px = (int)(cx + x3 - z3 * 0.5);
        int py = (int)(cy - y3 * 0.5 - z3 * 0.4);

        prevX[i] = px;
        prevY[i] = py;
      }

      // Рисуем кольцо трубки для текущего v
      gc.setForeground( gc.getDevice().getSystemColor( SWT.COLOR_DARK_CYAN ) );
      for( int i = 0; i < STEPS_U; i++ ) {
        gc.drawLine( prevX[i], prevY[i], prevX[i + 1], prevY[i + 1] );
      }
    }

    // Рисуем продольные линии (вдоль v) для каждого u
    for( int i = 0; i <= STEPS_U; i++ ) {
      double angleU = i * 2 * Math.PI / STEPS_U;
      int[] px = new int[STEPS_V + 1];
      int[] py = new int[STEPS_V + 1];

      for( int j = 0; j <= STEPS_V; j++ ) {
        double angleV2 = V_START + j * (V_END - V_START) / STEPS_V;
        double x3 = (R + r * Math.cos( angleU )) * Math.cos( angleV2 );
        double y3 = (R + r * Math.cos( angleU )) * Math.sin( angleV2 );
        double z3 = r * Math.sin( angleU );

        px[j] = (int)(cx + x3 - z3 * 0.5);
        py[j] = (int)(cy - y3 * 0.5 - z3 * 0.4);
      }

      gc.setForeground( gc.getDevice().getSystemColor( SWT.COLOR_BLUE ) );
      for( int j = 0; j < STEPS_V; j++ ) {
        gc.drawLine( px[j], py[j], px[j + 1], py[j + 1] );
      }
    }
  }
}
