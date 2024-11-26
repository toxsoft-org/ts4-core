package org.toxsoft.core.tsgui.graphics.patterns;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
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
            case SOLID: // для совместимости со старыми версиями
              if( aEntity.colorDescr == null ) {
                RGBAKeeper.KEEPER.write( aSw, aEntity.fillColor() );
              }
              else {
                aSw.writeSeparatorChar(); // если это color descriptor, то добавим паразитную запятую
                TsColorDescriptor.KEEPER.write( aSw, aEntity.colorDescriptor() );
              }
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
          return switch( kind ) {
            case NONE -> NONE;
            case SOLID -> {
              if( aSr.peekChar() == CHAR_ITEM_SEPARATOR ) { // это значит что сохранен ColorDescriptor
                aSr.nextChar();
                TsColorDescriptor cd = TsColorDescriptor.KEEPER.read( aSr );
                yield new TsFillInfo( cd );
              }
              RGBA rgba = RGBAKeeper.KEEPER.read( aSr );
              yield new TsFillInfo( rgba );
            }
            case IMAGE -> {
              TsImageFillInfo imgFillInfo = TsImageFillInfo.KEEPER.read( aSr );
              yield new TsFillInfo( imgFillInfo );
            }
            case GRADIENT -> {
              TsGradientFillInfo gradFillInfo = TsGradientFillInfo.KEEPER.read( aSr );
              yield new TsFillInfo( gradFillInfo );
            }
            default -> throw new TsNotAllEnumsUsedRtException();
          };
        }
      };

  private final ETsFillKind kind;

  private RGBA               fillRgba         = new RGBA( 0, 0, 0, 255 );
  private TsColorDescriptor  colorDescr       = null;
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
    IOptionSet opSet = OptionSetUtils.createOpSet( TsColorSourceKindRgba.OPDEF_RGBA, avValobj( aRgba ) );
    colorDescr = new TsColorDescriptor( TsColorSourceKindRgba.KIND_ID, opSet );
    kind = ETsFillKind.SOLID;
  }

  /**
   * Creates instance of kind {@link ETsFillKind#SOLID}.
   *
   * @param aColorDescr {@link TsColorDescriptor} - the color descriptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsFillInfo( TsColorDescriptor aColorDescr ) {
    colorDescr = TsNullArgumentRtException.checkNull( aColorDescr );
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
    if( colorDescr == null ) {
      return fillRgba;
    }
    return colorDescr.rgba();
  }

  /**
   * Returns color descriptor.
   *
   * @return {@link TsColorDescriptor} - color descriptor
   */
  public TsColorDescriptor colorDescriptor() {
    return colorDescr;
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
