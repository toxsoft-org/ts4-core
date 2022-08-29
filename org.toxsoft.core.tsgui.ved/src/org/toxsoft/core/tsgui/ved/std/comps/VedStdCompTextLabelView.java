package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedComponentView} implementation for {@link VedStdCompBorder}.
 *
 * @author vs
 */
class VedStdCompTextLabelView
    extends VedAbstractComponentView {

  // --- CACHE cached properties with conversion applied
  private D2RectOutline outline;
  private TsBorderInfo  borderInfo;
  private double        x, y, w, h;
  private String        text;
  private Font          font;
  private int           textAlpha;
  private Color         textColor;
  private int           bgAlpha;
  private Color         bgColor;
  private double        txtX;
  private double        txtY;
  private double        verMargin;

  public VedStdCompTextLabelView( VedAbstractComponent aComponent, IVedScreen aScreen ) {
    super( aComponent, aScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractComponentView
  //

  @Override
  public IVedOutline outline() {
    return outline;
  }

  @Override
  protected void doUpdateOnConversionChange() {
    // nop
  }

  @Override
  protected void doUpdateOnPropertiesChange( String aPropId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    x = props().getDouble( PDEF_X );
    y = props().getDouble( PDEF_Y );
    w = props().getDouble( PDEF_WIDTH );
    h = props().getDouble( PDEF_HEIGHT );
    double horMargin = props().getDouble( VedStdCompTextLabel.OPDEF_HOR_MARGIN );
    verMargin = props().getDouble( VedStdCompTextLabel.OPDEF_VER_MARGIN );
    text = props().getStr( VedStdCompTextLabel.OPDEF_TEXT );
    RGBA rgba = props().getValobj( PDEF_BG_COLOR );
    bgColor = colorManager().getColor( rgba.rgb );
    bgAlpha = rgba.alpha;

    rgba = props().getValobj( PDEF_FG_COLOR );
    textColor = colorManager().getColor( rgba.rgb );
    textAlpha = rgba.alpha;
    FontInfo fi = props().getValobj( VedStdCompTextLabel.OPDEF_FONT );
    font = fontManager().getFont( fi );
    borderInfo = props().getValobj( VedStdCompBorder.PDEF_BORDER_INFO );
    outline = new D2RectOutline( x, y, w, h );

    txtX = x;
    txtY = y;
    GC gc = null;
    try {
      gc = new GC( getDisplay() );
      gc.setFont( font );
      Point p = gc.textExtent( text, SWT.DRAW_TRANSPARENT );// | SWT.DRAW_DELIMITER );
      EHorAlignment ha = props().getValobj( VedStdCompTextLabel.OPDEF_HOR_ALIGN );
      EVerAlignment va = props().getValobj( VedStdCompTextLabel.OPDEF_VER_ALIGN );

      txtX = switch( ha ) {
        case FILL, CENTER -> x + (w - p.x) / 2.;
        case LEFT -> x + horMargin;
        case RIGHT -> x + w - p.x - horMargin;
        default -> throw new TsNotAllEnumsUsedRtException();
      };

      txtY = switch( va ) {
        case FILL, CENTER -> y + (h - p.y) / 2.;
        case TOP -> y + verMargin;
        case BOTTOM -> y + h - p.y - verMargin;
        default -> throw new TsNotAllEnumsUsedRtException();
      };
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }

  }

  @Override
  protected void doPaint( GC aGc, ITsRectangle aPaintBounds ) {
    aGc.setAdvanced( true );
    aGc.setAntialias( SWT.ON );
    Transform oldTransfrom = null;
    Transform t = null;
    ID2Conversion d2conv = getConversion();
    if( d2conv.isConversion() ) {
      oldTransfrom = new Transform( aGc.getDevice() );
      aGc.getTransform( oldTransfrom );

      t = TsGraphicsUtils.conv2transform( d2conv, aGc );

      aGc.setTransform( t );
      t.dispose();
    }

    aGc.setAlpha( bgAlpha );
    aGc.setBackground( bgColor );
    aGc.fillRectangle( (int)x, (int)y, (int)w, (int)h );

    TsFillInfo fillInfo = props().getValobj( VedStdCompTextLabel.PDEF_TEXT_FILL_INFO );
    if( fillInfo != null && fillInfo != TsFillInfo.NONE ) { // если есть заливка
      switch( fillInfo.kind() ) {
        case NONE:
          break;
        case GRADIENT: {
          IGradient gradient = fillInfo.gradientFillInfo().createGradient( tsContext() );
          Pattern p = gradient.pattern( aGc, (int)w, (int)h );
          aGc.setBackgroundPattern( p );
          Transform pTransform = new Transform( aGc.getDevice() );
          aGc.getTransform( pTransform );
          pTransform.translate( (int)txtX, (int)txtY );
          aGc.setTransform( pTransform );
          Path path = new Path( aGc.getDevice() );
          path.addString( text, 0, 0, font );
          aGc.fillPath( path );
          path.dispose();
          pTransform.translate( (int)-txtX, (int)-txtY );
          aGc.setTransform( pTransform );
          pTransform.dispose();
          p.dispose();
        }
          break;
        case IMAGE:
          break;
        case SOLID:
          aGc.setFont( font );
          aGc.setAlpha( textAlpha );
          aGc.setForeground( textColor );
          aGc.drawText( text, (int)txtX, (int)txtY, true );
          break;
        default:
          break;
      }
    }

    // aGc.setAlpha( bgAlpha );
    // aGc.setBackground( bgColor );
    // aGc.fillRectangle( (int)x, (int)y, (int)w, (int)h );

    TsGraphicsUtils.drawBorder( aGc, borderInfo, new TsRectangle( (int)x, (int)y, (int)w, (int)h ), colorManager() );

    // aGc.setFont( font );
    // aGc.setAlpha( textAlpha );
    // aGc.setForeground( textColor );
    // aGc.drawText( text, (int)txtX, (int)txtY, true );

    if( oldTransfrom != null ) { // восстановим старый transform
      aGc.setTransform( oldTransfrom );
      oldTransfrom.dispose();
    }
  }

}
