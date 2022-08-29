package org.toxsoft.core.tsgui.graphics.patterns;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Параметры градиентной заливки.
 * <p>
 *
 * @author vs
 */
public class TsGradientFillInfo
    implements IGradientInfo {

  /**
   * Параметры заливки изображением по-умолчанию.
   */
  public static final TsGradientFillInfo DEFAULT = new TsGradientFillInfo( EGradientType.LINEAR );

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsGradientFillInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsGradientFillInfo> KEEPER =
      new AbstractEntityKeeper<>( TsGradientFillInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsGradientFillInfo aEntity ) {
          EGradientType gType = aEntity.gradientType();
          EGradientType.KEEPER.write( aSw, gType );
          aSw.writeSeparatorChar();
          switch( gType ) {
            case NONE:
              break;
            case CYLINDER:
              CylinderGradientInfo.keeper().write( aSw, aEntity.cylinderGradientInfo() );
              break;
            case LINEAR:
              LinearGradientInfo.keeper().write( aSw, aEntity.linearGradientInfo() );
              break;
            case RADIAL:
              RadialGradientInfo.keeper().write( aSw, aEntity.radialGradientInfo() );
              break;
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
        }

        @Override
        protected TsGradientFillInfo doRead( IStrioReader aSr ) {
          EGradientType gType = EGradientType.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          switch( gType ) {
            case NONE:
              break;
            case CYLINDER:
              return new TsGradientFillInfo( CylinderGradientInfo.keeper().read( aSr ) );
            case LINEAR:
              return new TsGradientFillInfo( LinearGradientInfo.keeper().read( aSr ) );
            case RADIAL:
              return new TsGradientFillInfo( RadialGradientInfo.keeper().read( aSr ) );
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
          return null;
        }
      };

  private final EGradientType type;

  private LinearGradientInfo   linearGradientInfo;
  private CylinderGradientInfo cylinderGradientInfo;
  private RadialGradientInfo   radialGradientInfo;

  TsGradientFillInfo( EGradientType aType ) {
    type = aType;
  }

  /**
   * Конструктор.<br>
   *
   * @param aInfo IGradientInfo - параметры градиента
   */
  public TsGradientFillInfo( IGradientInfo aInfo ) {
    type = aInfo.gradientType();
    switch( type ) {
      case NONE:
        break;
      case CYLINDER:
        cylinderGradientInfo = (CylinderGradientInfo)aInfo;
        break;
      case LINEAR:
        linearGradientInfo = (LinearGradientInfo)aInfo;
        break;
      case RADIAL:
        radialGradientInfo = (RadialGradientInfo)aInfo;
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // {@link IGradientInfo}
  //

  @Override
  public EGradientType gradientType() {
    return type;
  }

  @Override
  public IGradient createGradient( ITsGuiContext aContext ) {
    switch( type ) {
      case NONE:
        break;
      case CYLINDER:
        return cylinderGradientInfo.createGradient( aContext );
      case LINEAR:
        return linearGradientInfo.createGradient( aContext );
      case RADIAL:
        return radialGradientInfo.createGradient( aContext );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает параметры линейного градиента.
   *
   * @return LinearGradientInfo - параметры линейного градиента
   */
  public LinearGradientInfo linearGradientInfo() {
    return linearGradientInfo;
  }

  /**
   * Возвращает параметры радиального градиента.
   *
   * @return RadialGradientInfo - параметры радиального градиента
   */
  public RadialGradientInfo radialGradientInfo() {
    return radialGradientInfo;
  }

  /**
   * Возвращает параметры цилиндрического градиента.
   *
   * @return CylinderGradientInfo - параметры цилиндрического градиента
   */
  public CylinderGradientInfo cylinderGradientInfo() {
    return cylinderGradientInfo;
  }

}
