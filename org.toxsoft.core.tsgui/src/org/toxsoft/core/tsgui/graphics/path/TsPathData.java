package org.toxsoft.core.tsgui.graphics.path;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Данные, необходимые для создания SWT {@link Path} - аналог {@link PathData}.
 * <p>
 *
 * @author vs
 */
public class TsPathData {

  /**
   * Default path.
   */
  public static final TsPathData NONE = new TsPathData( new byte[0], new float[0] );

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsPathData"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsPathData> KEEPER =
      new AbstractEntityKeeper<>( TsPathData.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsPathData aEntity ) {
          // write types
          byte[] t = aEntity.types;
          aSw.writeInt( t.length );
          aSw.writeChar( CHAR_ARRAY_BEGIN );
          for( int i = 0; i < t.length; i++ ) {
            aSw.writeInt( t[i] );
            if( i < t.length - 1 ) {
              aSw.writeSeparatorChar();
            }
          }
          aSw.writeChar( CHAR_ARRAY_END );
          // write points
          float[] p = aEntity.points;
          aSw.writeInt( p.length );
          aSw.writeChar( CHAR_ARRAY_BEGIN );
          for( int i = 0; i < p.length; i++ ) {
            aSw.writeFloat( p[i] );
            if( i < p.length - 1 ) {
              aSw.writeSeparatorChar();
            }
          }
          aSw.writeChar( CHAR_ARRAY_END );
        }

        @Override
        protected TsPathData doRead( IStrioReader aSr ) {
          // read types
          int count = aSr.readInt();
          aSr.ensureChar( CHAR_ARRAY_BEGIN );
          byte[] t = new byte[count];
          for( int i = 0; i < count; i++ ) {
            t[i] = (byte)aSr.readInt();
            if( i < t.length - 1 ) {
              aSr.ensureSeparatorChar();
            }
          }
          aSr.ensureChar( CHAR_ARRAY_END );
          // read points
          count = aSr.readInt();
          aSr.ensureChar( CHAR_ARRAY_BEGIN );
          float[] p = new float[count];
          for( int i = 0; i < count; i++ ) {
            p[i] = aSr.readFloat();
            if( i < p.length - 1 ) {
              aSr.ensureSeparatorChar();
            }
          }
          aSr.ensureChar( CHAR_ARRAY_END );
          return new TsPathData( t, p );
        }

      };

  private final byte[]  types;
  private final float[] points;

  /**
   * Constructor.
   *
   * @param aTypes byte[] - типы точек пути
   * @param aPoints float[] - координаты точек пути
   */
  public TsPathData( byte[] aTypes, float[] aPoints ) {
    types = new byte[aTypes.length];
    points = new float[aPoints.length];
    System.arraycopy( aTypes, 0, types, 0, aTypes.length );
    System.arraycopy( aPoints, 0, points, 0, aPoints.length );
    if( points.length > 0 && points[0] == Float.NaN ) {
      System.out.println( "NaN" );
    }
  }

  /**
   * Возвращает данные контура в понятиях SWT.
   *
   * @return {@link PathData} - данные контура в понятиях SWT
   */
  public PathData PathData() {
    PathData pd = new PathData();
    pd.types = new byte[types.length];
    pd.points = new float[points.length];
    System.arraycopy( types, 0, pd.types, 0, types.length );
    System.arraycopy( points, 0, pd.points, 0, points.length );
    return pd;
  }

  /**
   * Возвращает типы вершин пути.
   *
   * @return byte[] - типы вершин пути
   */
  public byte[] types() {
    return types;
  }

  /**
   * Возвращает набор координат точек пути.
   *
   * @return float[] - набор координат точек пути
   */
  public float[] points() {
    return points;
  }

  /**
   * Возвращает новый экземпляр {@link TsPathData} повернутый вокруг указанной точки на заданный угол.
   *
   * @param aCx - x координата точки вокруг которой осуществляется поворот
   * @param aCy - y координата точки вокруг которой осуществляется поворот
   * @param aAngleDeg - угол поорота в градусах (+ поврот против часовой стрелки)
   * @return {@link TsPathData} - новый экземпляр {@link TsPathData} повернутый вокруг указанной точки на заданный угол
   */
  public TsPathData rotate( float aCx, float aCy, float aAngleDeg ) {
    double rad = Math.toRadians( aAngleDeg );
    double cosA = Math.cos( rad );
    double sinA = Math.sin( rad );

    float[] newPoints = new float[points.length];
    for( int i = 0; i < points.length; i += 2 ) {
      double dx = points[i] - aCx;
      double dy = points[i + 1] - aCy;
      newPoints[i] = (float)(aCx + dx * cosA - dy * sinA);
      newPoints[i + 1] = (float)(aCy + dx * sinA + dy * cosA);
    }
    return new TsPathData( types, newPoints );
  }

  /**
   * Возвращает новый экземпляр {@link TsPathData} с измененным масштабом относительно указанной точки.
   *
   * @param aAnchorX float - x координата точки, относительно которой осуществляется масштабирование
   * @param aAnchorY float - y координата точки, относительно которой осуществляется масштабирование
   * @param aScaleX float - коэффициент масштабирования по оси x
   * @param aScaleY float - коэффициент масштабирования по оси y
   * @return {@link TsPathData} - новый экземпляр {@link TsPathData} с измененным масштабом относительно указанной точки
   */
  public TsPathData scale( float aAnchorX, float aAnchorY, float aScaleX, float aScaleY ) {
    float[] newPoints = new float[points.length];
    for( int i = 0; i < points.length; i += 2 ) {
      newPoints[i] = aAnchorX + (points[i] - aAnchorX) * aScaleX;
      newPoints[i + 1] = aAnchorY + (points[i + 1] - aAnchorY) * aScaleY;
    }
    return new TsPathData( types, newPoints );
  }

}
