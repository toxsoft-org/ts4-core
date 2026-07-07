package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

import java.io.*;
import java.util.List;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Пример использования SvgParser. Запуск: java svg.SvgParserExample path/to/file.svg
 */
public class SvgParserExample {

  public static void main( String[] args )
      throws Exception {

    String filePath = args.length > 0 ? args[0] : "C:/1/arrow.svg";

    SvgParser parser = new SvgParser();
    SvgDocument doc = parser.parse( new File( filePath ) );

    System.out.println( "=== SVG документ ===" );
    System.out.println( doc );
    System.out.println();

    System.out.println( "=== Все фигуры ===" );
    for( SvgShape shape : doc.getShapes() ) {
      System.out.println( shape );
    }
    System.out.println();

    // Пример: получить только пути (path) — сегменты кольца
    System.out.println( "=== Только <path> ===" );
    List<SvgPath> paths = doc.getShapesOfType( SvgPath.class );
    for( int i = 0; i < paths.size(); i++ ) {
      SvgPath p = paths.get( i );
      System.out.printf( "  Сегмент %d: fill=%s%n", i + 1, p.getFill() );
    }

    // Пример: получить только окружности
    System.out.println( "\n=== Только <circle> ===" );
    List<SvgCircle> circles = doc.getShapesOfType( SvgCircle.class );
    for( SvgCircle c : circles ) {
      System.out.printf( "  Окружность: cx=%.1f cy=%.1f r=%.1f fill=%s%n", c.getCx(), c.getCy(), c.getR(),
          c.getFill() );
    }

    Display display = new Display();
    org.eclipse.swt.widgets.Shell shell = new org.eclipse.swt.widgets.Shell( display );
    shell.setText( "SvgPathParser — Demo" );
    shell.setSize( 620, 480 );

    shell.addPaintListener( e -> {
      e.gc.setBackground( display.getSystemColor( org.eclipse.swt.SWT.COLOR_WHITE ) );
      e.gc.fillRectangle( shell.getClientArea() );
      e.gc.setAntialias( org.eclipse.swt.SWT.ON );
      e.gc.setLineWidth( 2 );

      e.gc.setBackground( display.getSystemColor( org.eclipse.swt.SWT.COLOR_BLACK ) );
      for( SvgPath svgPath : paths ) {
        // Path swtPath = SvgPathParser.parse( display, svgPath.getD() );
        Path swtPath = SvgArcOptimizer.parse( display, svgPath.getD() );
        e.gc.fillPath( swtPath );
        e.gc.drawPath( swtPath );
        swtPath.dispose();
      }

    } );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();

  }
}
