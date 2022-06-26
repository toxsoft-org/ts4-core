package org.toxsoft.core.tsgui.ved.olds.incub;

import java.awt.*;
import java.awt.geom.*;

/**
 * Четырехугольник.
 * <p>
 * <b>Мотивация</b>:<br>
 * К сожалению, в реализации графических библиотек прямоугольником {@link Rectangle} считается прямоугольник, у которого
 * стороны параллельны осям X и Y. Поэтому даже для четырехугольника с прямыми углами, если он повернут, приходится
 * вводить понятие четырехугольника.
 *
 * @author vs
 */
public class Tetragon {

  Point2D[] points     = new Point2D[4];
  double[]  polyPoints = null;

  Polygon polygon = null;

  /**
   * Конструктор из "фигуры" в понятиях AWT.
   *
   * @param aShape Shape - "фигура" в понятиях AWT
   */
  public Tetragon( Shape aShape ) {

    PathIterator pi = aShape.getPathIterator( null );
    double[] coords = new double[6];
    int idx = 0;
    while( !pi.isDone() ) {
      int segType = pi.currentSegment( coords );
      if( segType == PathIterator.SEG_MOVETO ) {
        points[idx] = new Point2D.Double( coords[0], coords[1] );
      }
      if( segType == PathIterator.SEG_LINETO ) {
        points[idx] = new Point2D.Double( coords[0], coords[1] );
      }
      if( segType == PathIterator.SEG_QUADTO ) {
        points[idx] = new Point2D.Double( coords[0], coords[1] );
      }
      if( idx >= 3 ) {
        break;
      }
      pi.next();
      idx++;
    }
  }

  // public Tetragon( Shape aShape ) {
  //
  // PathIterator pi = aShape.getPathIterator( null );
  // Point2D[] pp = new Point2D[4];
  // double[] coords = new double[6];
  // int idx = 0;
  // while( pi.isDone() == false ) {
  // int segType = pi.currentSegment( coords );
  // if( segType == PathIterator.SEG_MOVETO ) {
  // pp[idx] = new Point2D.Double( coords[0], coords[1] );
  // }
  // if( segType == PathIterator.SEG_LINETO ) {
  // pp[idx] = new Point2D.Double( coords[0], coords[1] );
  // }
  // // if( segType == PathIterator.SEG_CLOSE ) {
  // // System.out.println( "SEG_CLOSE" );
  // // }
  // if( idx >= 3 ) {
  // break;
  // }
  // pi.next();
  // idx++;
  // }
  //
  // int minIdx = 0; // индекс левой нижней точки
  // double minX = pp[0].getX();
  // double maxY = pp[0].getY();
  // for( int i = 1; i < pp.length; i++ ) {
  // if( pp[i].getX() < minX ) {
  // minX = pp[i].getX();
  // minIdx = i;
  // }
  // else {
  // if( pp[i].getX() == minX ) {
  // if( pp[i].getX() > maxY ) {
  // maxY = pp[i].getX();
  // minIdx = i;
  // }
  // }
  // }
  // }
  //
  // for( int i = 0; i < pp.length; i++ ) {
  // points[i] = new Point2D.Double( pp[(i + minIdx) % 4].getX(), pp[(i + minIdx) % 4].getY() );
  // }
  //
  // }

  public Tetragon( Point2D[] aPoints ) {
    int minIdx = 0; // индекс левой нижней точки
    double minX = aPoints[0].getX();
    double maxY = aPoints[0].getY();
    for( int i = 1; i < aPoints.length; i++ ) {
      if( aPoints[i].getX() < minX ) {
        minX = aPoints[i].getX();
        minIdx = i;
      }
      else {
        if( aPoints[i].getX() == minX ) {
          if( aPoints[i].getX() > maxY ) {
            maxY = aPoints[i].getX();
            minIdx = i;
          }
        }
      }
    }

    for( int i = 0; i < points.length; i++ ) {
      points[i] = new Point2D.Double( aPoints[(i + minIdx) % 4].getX(), aPoints[(i + minIdx) % 4].getY() );
    }
  }

  public static Tetragon createFromPolyPoints( int[] aCoords ) {
    Point2D[] dblPoints = new Point2D[4];
    for( int i = 0; i < 4; i++ ) {
      dblPoints[i] = new Point2D.Double( aCoords[2 * i], aCoords[2 * i + 1] );
    }
    return new Tetragon( dblPoints );
  }

  public double[] polyPoints() {
    if( polyPoints == null ) {
      polyPoints = new double[points.length * 2];
      for( int i = 0; i < points.length; i++ ) {
        polyPoints[2 * i] = points[i].getX();
        polyPoints[2 * i + 1] = points[i].getY();
      }
    }
    return polyPoints;
  }

  public int[] screenPolyPoints( double aDx, double aDy ) {
    double[] dblPoints = polyPoints();
    int[] scrPoints = new int[dblPoints.length];
    for( int i = 0; i < dblPoints.length / 2; i++ ) {
      scrPoints[2 * i] = (int)Math.round( aDx + dblPoints[2 * i] );
      scrPoints[2 * i + 1] = (int)Math.round( aDy + dblPoints[2 * i + 1] );
    }
    return scrPoints;
  }

  public Polygon polygon() {
    if( polygon == null ) {
      int[] xCoords = new int[points.length];
      int[] yCoords = new int[points.length];
      for( int i = 0; i < points.length; i++ ) {
        xCoords[i] = (int)Math.round( points[i].getX() );
        yCoords[i] = (int)Math.round( points[i].getY() );
      }
      polygon = new Polygon( xCoords, yCoords, points.length );
    }
    return polygon;
  }

  public Rectangle bounds() {
    return polygon().getBounds();
  }

  public LineSegment segment( int aIdx ) {
    return new LineSegment( points[aIdx].getX(), points[aIdx].getY(), points[(aIdx + 1) % 4].getX(),
        points[(aIdx + 1) % 4].getY() );
  }

  public Tetragon move( double aDx, double aDy ) {
    Point2D[] pp = new Point2D[points.length];
    for( int i = 0; i < points.length; i++ ) {
      pp[i] = new Point2D.Double( points[i].getX() + aDx, points[i].getY() + aDy );
    }
    return new Tetragon( pp );
  }

  public Tetragon zoom( double aZoomFactor ) {
    Point2D[] pp = new Point2D[points.length];
    for( int i = 0; i < points.length; i++ ) {
      pp[i] = new Point2D.Double( points[i].getX() * aZoomFactor, points[i].getY() * aZoomFactor );
    }
    return new Tetragon( pp );
  }

  public Point2D[] points() {
    return points;
  }

}
