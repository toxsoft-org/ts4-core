package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The shapes filling parameters.
 *
 * @author vs
 */
public class TsFillInfo {

  /**
   * Singleton of the no fill.
   */
  public static final TsFillInfo NONE = new TsFillInfo();

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TsFillInfo"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<TsFillInfo> KEEPER =
      new AbstractEntityKeeper<>( TsFillInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsFillInfo aEntity ) {
          ETsFillKind kind = aEntity.kind();
          ETsFillKind.KEEPER.write( aSw, kind );
          aSw.writeSeparatorChar();
          switch( kind ) {
            case NONE:
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
              return NONE;
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

  private final ETsFillKind kind;

  private RGBA               fillRgba         = new RGBA( 0, 0, 0, 255 );
  private TsImageFillInfo    imageFillInfo    = TsImageFillInfo.DEFAULT;
  private TsGradientFillInfo gradientFillInfo = TsGradientFillInfo.DEFAULT;

  private TsFillInfo() {
    kind = ETsFillKind.NONE;
  }

  /**
   * Creates instance of kind {@link ETsFillKind#SOLID}.
   *
   * @param aRgba {@link RGBA} - the color
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsFillInfo( RGBA aRgba ) {
    fillRgba = TsNullArgumentRtException.checkNull( aRgba );
    kind = ETsFillKind.SOLID;
  }

  /**
   * Creates instance of kind {@link ETsFillKind#IMAGE}.
   *
   * @param aImageFillInfo {@link TsImageFillInfo} - the image parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsFillInfo( TsImageFillInfo aImageFillInfo ) {
    imageFillInfo = TsNullArgumentRtException.checkNull( aImageFillInfo );
    kind = ETsFillKind.IMAGE;
  }

  /**
   * Creates instance of kind {@link ETsFillKind#GRADIENT}.
   *
   * @param aGradientFillInfo {@link TsGradientFillInfo} - the gradient parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsFillInfo( TsGradientFillInfo aGradientFillInfo ) {
    gradientFillInfo = TsNullArgumentRtException.checkNull( aGradientFillInfo );
    kind = ETsFillKind.GRADIENT;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the filling kind.
   *
   * @return {@link ETsFillKind} - the filling kind
   */
  public ETsFillKind kind() {
    return kind;
  }

  /**
   * Returns the color of the solid fill (kind {@link ETsFillKind#SOLID}).
   *
   * @return {@link RGB} - solid fill color
   */
  public RGBA fillColor() {
    return fillRgba;
  }

  /**
   * Returns the image filling information (kind {@link ETsFillKind#IMAGE}).
   *
   * @return {@link TsImageFillInfo} - the image filling parameters
   */
  public TsImageFillInfo imageFillInfo() {
    return imageFillInfo;
  }

  /**
   * Returns the gradient filling information (kind {@link ETsFillKind#GRADIENT}).
   *
   * @return {@link TsGradientFillInfo} - the gradient filling parameters
   */
  public TsGradientFillInfo gradientFillInfo() {
    return gradientFillInfo;
  }

}
