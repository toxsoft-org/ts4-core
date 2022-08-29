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
public class CylinderGradientInfo
    extends AbstractGradientInfo {

  IListEdit<Pair<Double, RGBA>> fractions;

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  @SuppressWarnings( "unused" )
  private static final String INTERNAL_KEEPER_ID = "CylinderGradientInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  private static final IEntityKeeper<CylinderGradientInfo> INTERNAL_KEEPER =
      new AbstractEntityKeeper<>( CylinderGradientInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, CylinderGradientInfo aEntity ) {
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
        protected CylinderGradientInfo doRead( IStrioReader aSr ) {
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
          return new CylinderGradientInfo( fractions );
        }
      };

  /**
   * Keeper singleton.
   *
   * @return IEntityKeeper - keeper singleton
   */
  public static final IEntityKeeper<CylinderGradientInfo> keeper() {
    return INTERNAL_KEEPER;
  }

  /**
   * Конструктор.
   *
   * @param aFractions IList&lt;Pair&lt;Double, RGBA>> aFractions - список фракций
   */
  public CylinderGradientInfo( IList<Pair<Double, RGBA>> aFractions ) {
    super();
    fractions = new ElemArrayList<>( aFractions );
  }

  // ------------------------------------------------------------------------------------
  // IGradientInfo
  //

  @Override
  public EGradientType gradientType() {
    return EGradientType.CYLINDER;
  }

  @Override
  public IGradient createGradient( ITsGuiContext aContext ) {
    return new CylinderGradient( this, aContext );
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
