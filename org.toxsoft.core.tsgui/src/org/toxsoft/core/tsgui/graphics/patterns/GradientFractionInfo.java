package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Информация описывающая фракцию градиента.
 * <p>
 *
 * @author vs
 */
public class GradientFractionInfo {

  private final double startValue;

  private final double endValue;

  private final RGBA startRGBA;

  private final RGBA endRGBA;

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "GradientFractionInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<GradientFractionInfo> KEEPER =
      new AbstractEntityKeeper<>( GradientFractionInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, GradientFractionInfo aEntity ) {
          aSw.writeDouble( aEntity.startValue() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.endValue() );
          aSw.writeSeparatorChar();
          RGBAKeeper.KEEPER.write( aSw, aEntity.startRGBA() );
          aSw.writeSeparatorChar();
          RGBAKeeper.KEEPER.write( aSw, aEntity.endRGBA() );
        }

        @Override
        protected GradientFractionInfo doRead( IStrioReader aSr ) {
          double startValue = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double endValue = aSr.readDouble();
          aSr.ensureSeparatorChar();
          RGBA startRgba = RGBAKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          RGBA endRgba = RGBAKeeper.KEEPER.read( aSr );
          return new GradientFractionInfo( startValue, endValue, startRgba, endRgba );
        }
      };

  /**
   * Конструктор со всеми инвариантами.<br>
   *
   * @param aStartVaue double - левая граница диапазона значений фракции
   * @param aEndValue double - правая граница диапазона значений фракции
   * @param aStartRGBA RGBA - левая граница дапазона значений цвета
   * @param aEndRGBA RGBA - правая граница дапазона значений цвета
   */
  GradientFractionInfo( double aStartVaue, double aEndValue, RGBA aStartRGBA, RGBA aEndRGBA ) {
    startValue = aStartVaue;
    endValue = aEndValue;
    startRGBA = aStartRGBA;
    endRGBA = aEndRGBA;
  }

  final double startValue() {
    return startValue;
  }

  final double endValue() {
    return endValue;
  }

  final RGBA startRGBA() {
    return startRGBA;
  }

  final RGBA endRGBA() {
    return endRGBA;
  }

}
