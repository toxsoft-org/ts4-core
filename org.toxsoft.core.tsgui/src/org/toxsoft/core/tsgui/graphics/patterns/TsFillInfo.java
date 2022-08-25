package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Параметры заливки.
 * <p>
 *
 * @author vs
 */
public class TsFillInfo {

  /**
   * Отстутсвие заливки
   */
  public static final TsFillInfo NONE = new TsFillInfo( ETsFillKind.NONE );

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsFillInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsFillInfo> KEEPER =
      new AbstractEntityKeeper<>( TsFillInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsFillInfo aEntity ) {
          ETsFillKind kind = aEntity.kind();
          ETsFillKind.KEEPER.write( aSw, kind );
          aSw.writeSeparatorChar();
          switch( kind ) {
            case NONE: // ничего не записываем
              break;
            case SOLID:
              RGBAKeeper.KEEPER.write( aSw, aEntity.fillColor() );
              break;
            case IMAGE:
              TsImageFillInfo.KEEPER.write( aSw, aEntity.imageFillInfo() );
              break;
            case GRADIENT:
              TsGradientFillInfo.KEEPER.write( aSw, aEntity.gradientFillInfo() );
              break;
            default:
              break;
          }
        }

        @Override
        protected TsFillInfo doRead( IStrioReader aSr ) {
          ETsFillKind kind = ETsFillKind.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          switch( kind ) {
            case NONE:
              return new TsFillInfo( kind );
            case SOLID:
              RGBA rgba = RGBAKeeper.KEEPER.read( aSr );
              return new TsFillInfo( rgba );
            case IMAGE:
              TsImageFillInfo imgFillInfo = TsImageFillInfo.KEEPER.read( aSr );
              return new TsFillInfo( imgFillInfo );
            case GRADIENT:
              TsGradientFillInfo gradFillInfo = TsGradientFillInfo.KEEPER.read( aSr );
              return new TsFillInfo( gradFillInfo );
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
        }
      };

  /**
   * Тип заливки
   */
  private final ETsFillKind kind;

  /**
   * Цвет для сплошной заливки
   */
  private RGBA fillRgba = new RGBA( 0, 0, 0, 255 );

  private TsImageFillInfo imageFillInfo = TsImageFillInfo.DEFAULT;

  private TsGradientFillInfo gradientFillInfo = TsGradientFillInfo.DEFAULT;

  TsFillInfo( ETsFillKind aKind ) {
    kind = aKind;
  }

  public TsFillInfo( RGBA aRgba ) {
    kind = ETsFillKind.SOLID;
    fillRgba = aRgba;
  }

  public TsFillInfo( TsImageFillInfo aImageFillInfo ) {
    kind = ETsFillKind.IMAGE;
    imageFillInfo = aImageFillInfo;
  }

  public TsFillInfo( TsGradientFillInfo aGradientFillInfo ) {
    kind = ETsFillKind.GRADIENT;
    gradientFillInfo = aGradientFillInfo;
  }

  /**
   * Возвращает тип заливки.<br>
   *
   * @return ETsFillKind - тип заливки
   */
  public ETsFillKind kind() {
    return kind;
  }

  /**
   * Возвращает параметры цвета при сплошной заливке.
   *
   * @return RGBA - параметры цвета при сплошной заливке
   */
  public RGBA fillColor() {
    return fillRgba;
  }

  /**
   * Возвращает параметры при заливке изображением.
   *
   * @return TsImageFillInfo - параметры при заливке изображением
   */
  public TsImageFillInfo imageFillInfo() {
    return imageFillInfo;
  }

  /**
   * Возвращает параметры при градиентной заливки.
   *
   * @return TsGradientFillInfo - параметры градиентной заливки
   */
  public TsGradientFillInfo gradientFillInfo() {
    return gradientFillInfo;
  }

}
