package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Параметры цилиндрического градиента.
 * <p>
 *
 * @author vs
 */
public class RadialGradientInfo
    extends AbstractGradientInfo {

  double centerX = 50;

  double centerY = 50;

  IListEdit<Pair<Double, RGBA>> fractions;

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  @SuppressWarnings( "unused" )
  private static final String INTERNAL_KEEPER_ID = "RadialGradientInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  private static final IEntityKeeper<RadialGradientInfo> INTERNAL_KEEPER =
      new AbstractEntityKeeper<>( RadialGradientInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, RadialGradientInfo aEntity ) {
          aSw.writeDouble( aEntity.centerX );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.centerY );
          aSw.writeSeparatorChar();
          int size = aEntity.fractions.size();
          aSw.writeInt( size );
          aSw.writeChar( '{' );
          for( int i = 0; i < size; i++ ) {
            Pair<Double, RGBA> p = aEntity.fractions.get( i );
            aSw.writeDouble( p.left().doubleValue() );
            aSw.writeSeparatorChar();
            RGBAKeeper.KEEPER.write( aSw, p.right() );
            if( i < size - 1 ) {
              aSw.writeSeparatorChar();
            }
          }
          aSw.writeChar( '}' );
        }

        @Override
        protected RadialGradientInfo doRead( IStrioReader aSr ) {
          double centerX = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double centerY = aSr.readDouble();
          aSr.ensureSeparatorChar();
          int size = aSr.readInt();
          aSr.ensureChar( '{' );
          IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
          for( int i = 0; i < size; i++ ) {
            double val = aSr.readDouble();
            aSr.ensureSeparatorChar();
            RGBA rgba = RGBAKeeper.KEEPER.read( aSr );
            if( i < size - 1 ) {
              aSr.ensureSeparatorChar();
            }
            Pair<Double, RGBA> pair = new Pair<>( Double.valueOf( val ), rgba );
            fractions.add( pair );
          }
          aSr.ensureChar( '}' );
          return new RadialGradientInfo( centerX, centerY, fractions );
        }
      };

  /**
   * Keeper singleton.
   *
   * @return IEntityKeeper - keeper singleton
   */
  public static final IEntityKeeper<RadialGradientInfo> keeper() {
    return INTERNAL_KEEPER;
  }

  /**
   * Конструктор.
   *
   * @param aCenterX double - X координата центра в процентах от ширины
   * @param aCenterY double - Y координата центра в процентах от высоты
   * @param aFractions IList&lt;Pair&lt;Double, RGBA>> aFractions - список фракций
   */
  public RadialGradientInfo( double aCenterX, double aCenterY, IList<Pair<Double, RGBA>> aFractions ) {
    centerX = aCenterX;
    centerY = aCenterY;
    fractions = new ElemArrayList<>( aFractions );
  }

  // ------------------------------------------------------------------------------------
  // IGradientInfo
  //

  @Override
  public EGradientType gradientType() {
    return EGradientType.RADIAL;
  }

  @Override
  public IGradient createGradient( ITsGuiContext aContext ) {
    return new RadialGradient( this, aContext );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает список фракций.
   *
   * @return IList&lt;Pair&lt;Double, RGBA>> aFractions - список фракций
   */
  public IList<Pair<Double, RGBA>> fractions() {
    return fractions;
  }

}
