package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Параметры линейного градиента.
 * <p>
 *
 * @author vs
 */
public class LinearGradientInfo
    extends AbstractSwtPatternInfo {

  private final D2Point startPoint;
  private final D2Point endPoint;
  private final RGBA    startRGBA;
  private final RGBA    endRGBA;

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  @SuppressWarnings( "unused" )
  private static final String INTERNAL_KEEPER_ID = "LinearGradientInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  private static final IEntityKeeper<LinearGradientInfo> INTERNAL_KEEPER =
      new AbstractEntityKeeper<>( LinearGradientInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, LinearGradientInfo aEntity ) {
          aSw.writeDouble( aEntity.startPoint.x() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.startPoint.y() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.endPoint.x() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.endPoint.y() );
          aSw.writeSeparatorChar();
          RGBAKeeper.KEEPER.write( aSw, aEntity.startRGBA() );
          aSw.writeSeparatorChar();
          RGBAKeeper.KEEPER.write( aSw, aEntity.endRGBA() );
        }

        @Override
        protected LinearGradientInfo doRead( IStrioReader aSr ) {
          double x1 = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double y1 = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double x2 = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double y2 = aSr.readDouble();
          aSr.ensureSeparatorChar();
          RGBA startRgba = RGBAKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          RGBA endRgba = RGBAKeeper.KEEPER.read( aSr );
          return new LinearGradientInfo( new D2Point( x1, y1 ), new D2Point( x2, y2 ), startRgba, endRgba );
        }
      };

  /**
   * Keeper singleton.
   *
   * @return IEntityKeeper - keeper singleton
   */
  public static final IEntityKeeper<LinearGradientInfo> keeper() {
    return INTERNAL_KEEPER;
  }

  /**
   * Конструктор со всеми инвариантами.<br>
   *
   * @param aStartPoint D2Point - координаты начальной точки
   * @param aEndPoint D2Point - координаты конечной точки
   * @param aStartRGBA RGBA - парметры цвета начальной точки
   * @param aEndRGBA RGBA - парметры цвета конечной точки
   */
  public LinearGradientInfo( D2Point aStartPoint, D2Point aEndPoint, RGBA aStartRGBA, RGBA aEndRGBA ) {
    startPoint = aStartPoint;
    endPoint = aEndPoint;
    startRGBA = aStartRGBA;
    endRGBA = aEndRGBA;
  }

  // ------------------------------------------------------------------------------------
  // AbstractSwtPatternInfo
  //

  @Override
  public ESwtPatternType type() {
    return ESwtPatternType.GRADIENT;
  }

  @Override
  public EGradientType gradientType() {
    return EGradientType.LINEAR;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает координаты начальной точки. <br>
   *
   * @return D2Point - координаты начальной точки
   */
  public final D2Point startPoint() {
    return startPoint;
  }

  /**
   * Возвращает координаты конечной точки. <br>
   *
   * @return D2Point - координаты нконечной точки
   */
  public final D2Point endPoint() {
    return endPoint;
  }

  /**
   * Возвращает параметры цвета начальной точки. <br>
   *
   * @return D2Point - координаты начальной точки
   */
  public final RGBA startRGBA() {
    return startRGBA;
  }

  /**
   * Возвращает параметры цвета конечной точки. <br>
   *
   * @return D2Point - координаты конечной точки
   */
  public final RGBA endRGBA() {
    return endRGBA;
  }

}
