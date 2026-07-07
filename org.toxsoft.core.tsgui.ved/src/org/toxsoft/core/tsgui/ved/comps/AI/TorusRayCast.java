package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class TorusRayCast {

  static final double R       = 0.6;
  static final double r       = 0.25;
  static final double V_START = 0;
  static final double V_END   = 2 * Math.PI * 0.75;
  static final int    BASE_R  = 40, BASE_G = 140, BASE_B = 220;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Torus Ray Cast" );
    shell.setLayout( new FillLayout() );
    shell.setSize( 600, 600 );

    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED );
    canvas.addPaintListener( e -> render( e.gc, canvas.getClientArea() ) );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  static void render( GC gc, Rectangle area ) {
    int W = area.width, H = area.height;
    double scale = 1.1;

    // Windows SWT использует BGR порядок байт
    PaletteData palette = new PaletteData( 0x0000FF, 0x00FF00, 0xFF0000 );
    ImageData imageData = new ImageData( W, H, 24, palette );

    for( int py = 0; py < H; py++ ) {
      for( int px = 0; px < W; px++ ) {

        double sx = (px - W * 0.5) / (W * 0.5) * scale;
        double sy = -(py - H * 0.5) / (H * 0.5) * scale;

        double[] hit = intersectTorus( sx, sy, 3.0, -1.0 );

        int cr = 0, cg = 0, cb = 0;

        if( hit != null ) {
          double v = Math.atan2( sy, sx );
          if( v < 0 ) {
            v += 2 * Math.PI;
          }
          double vn = normalizeAngle( v, V_START );
          double ve = normalizeAngle( V_END, V_START );

          if( vn <= ve ) {
            double nx = hit[1], ny = hit[2], nz = hit[3];
            cr = Math.min( 255, Math.max( 0, (int)((nx * 0.5 + 0.5) * BASE_R * 2) ) );
            cg = Math.min( 255, Math.max( 0, (int)((ny * 0.5 + 0.5) * BASE_G * 2) ) );
            cb = Math.min( 255, Math.max( 0, (int)((nz * 0.5 + 0.5) * BASE_B * 2) ) );
          }
        }

        // setPixel принимает значение согласно маскам палитры
        int pixel = palette.getPixel( new RGB( cr, cg, cb ) );
        imageData.setPixel( px, py, pixel );
      }
    }

    Image image = new Image( gc.getDevice(), imageData );
    gc.drawImage( image, 0, 0 );
    image.dispose();
  }

  static double F( double x, double y, double z ) {
    double s = x * x + y * y + z * z + R * R - r * r;
    return s * s - 4 * R * R * (x * x + y * y);
  }

  static double[] intersectTorus( double ox, double oy, double oz, double dz ) {
    double tMin = (oz - (R + r + 0.01)) / (-dz);
    double tMax = (oz + (R + r + 0.01)) / (-dz);
    if( tMin > tMax ) {
      double tmp = tMin;
      tMin = tMax;
      tMax = tmp;
    }

    int STEPS = 200;
    double step = (tMax - tMin) / STEPS;

    double tPrev = tMin;
    double fPrev = F( ox, oy, oz + tMin * dz );
    double bestT = -1;

    for( int i = 1; i <= STEPS; i++ ) {
      double t = tMin + i * step;
      double fCur = F( ox, oy, oz + t * dz );

      if( fPrev * fCur <= 0 ) {
        double lo = tPrev, hi = t;
        for( int b = 0; b < 50; b++ ) {
          double mid = (lo + hi) * 0.5;
          double fMid = F( ox, oy, oz + mid * dz );
          if( fPrev * fMid <= 0 ) {
            hi = mid;
          }
          else {
            lo = mid;
          }
        }
        double tHit = (lo + hi) * 0.5;
        if( tHit > 0.001 ) {
          bestT = tHit;
          break;
        }
      }

      tPrev = t;
      fPrev = fCur;
    }

    if( bestT < 0 ) {
      return null;
    }

    double hx = ox;
    double hy = oy;
    double hz = oz + bestT * dz;

    double m = hx * hx + hy * hy + hz * hz + R * R - r * r;
    double gnx = 4 * m * hx - 8 * R * R * hx;
    double gny = 4 * m * hy - 8 * R * R * hy;
    double gnz = 4 * m * hz;

    double len = Math.sqrt( gnx * gnx + gny * gny + gnz * gnz );
    if( len < 1e-10 ) {
      return null;
    }

    return new double[] { bestT, gnx / len, gny / len, gnz / len };
  }

  static double normalizeAngle( double angle, double start ) {
    double a = angle - start;
    while( a < 0 ) {
      a += 2 * Math.PI;
    }
    while( a > 2 * Math.PI ) {
      a -= 2 * Math.PI;
    }
    return a;
  }
}
