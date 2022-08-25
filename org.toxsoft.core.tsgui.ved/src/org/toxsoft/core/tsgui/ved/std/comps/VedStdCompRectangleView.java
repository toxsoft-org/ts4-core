package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * {@link IVedComponentView} implementation for {@link VedStdCompRectangle}.
 *
 * @author hazard157
 */
class VedStdCompRectangleView
    extends VedAbstractComponentView {

  // --- CACHE cached properties with conversion applied
  private D2RectOutline outline;
  private Color         bgColor;
  private Color         fgColor;
  private int           fgAlpha;
  int                   lineWidth;
  double                x, y, w, h;
  TsFillInfo            fillInfo = null;
  // ---

  public VedStdCompRectangleView( VedAbstractComponent aComponent, IVedScreen aScreen ) {
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
    // TODO Auto-generated method stub
  }

  @Override
  protected void doUpdateOnPropertiesChange( String aPropId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    x = props().getDouble( PDEF_X );
    y = props().getDouble( PDEF_Y );
    w = props().getDouble( PDEF_WIDTH );
    h = props().getDouble( PDEF_HEIGHT );
    outline = new D2RectOutline( x, y, w, h );

    // TODO respect conversion!
    RGBA fgRgb = props().getValobj( PDEF_FG_COLOR );
    fgColor = colorManager().getColor( fgRgb.rgb );
    fgAlpha = fgRgb.alpha;
    RGBA bgRgb = props().getValobj( PDEF_BG_COLOR );
    bgColor = colorManager().getColor( bgRgb.rgb );
    int bgAlpha = bgRgb.alpha;
    lineWidth = 2;
    x = (int)props().getDouble( PDEF_X );
    y = (int)props().getDouble( PDEF_Y );
    w = (int)props().getDouble( PDEF_WIDTH );
    h = (int)props().getDouble( PDEF_HEIGHT );

    fillInfo = props().getValobj( PDEF_FILL_INFO );
  }

  @Override
  protected void doPaint( GC aGc, ITsRectangle aPaintBounds ) {
    // TODO respect conversion!
    // TODO filling properties
    // TODO line drawing properties
    aGc.setAdvanced( true );
    aGc.setAntialias( SWT.ON );
    Transform oldTransfrom = null;
    Transform t = null;
    // ID2Conversion d2conv = ownerScreen().getConversion();
    ID2Conversion d2conv = getConversion();
    if( d2conv.isConversion() ) {
      oldTransfrom = new Transform( aGc.getDevice() );
      aGc.getTransform( oldTransfrom );

      t = TsGraphicsUtils.conv2transform( d2conv, aGc );

      aGc.setTransform( t );
      t.dispose();
    }

    int xi = (int)x;
    int yi = (int)y;
    int wi = (int)w;
    int hi = (int)h;

    if( fillInfo != null && fillInfo != TsFillInfo.NONE ) { // если есть заливка
      switch( fillInfo.kind() ) {
        case GRADIENT:
          break;
        case IMAGE:
          break;
        case NONE:
          break;
        case SOLID:
          RGBA rgba = fillInfo.fillColor();
          aGc.setAlpha( rgba.alpha );
          aGc.setBackground( colorManager().getColor( rgba.rgb ) );
          aGc.fillRectangle( xi, yi, wi, hi );
          break;
        default:
          break;
      }
    }

    // if( fillInfo == null || fillInfo == TsFillInfo.NONE ) {
    // aGc.setAlpha( bgAlpha );
    // aGc.setBackground( bgColor );
    // aGc.fillRectangle( xi, yi, wi, hi );
    // }
    // else {
    // if( fillInfo.kind() == ETsFillKind.GRADIENT ) {
    // // Pattern p = fillInfo.createSwtPattern( fillInfo, tsContext() ).pattern( aGc, wi, hi );
    // // aGc.setBackgroundPattern( p );
    // // Transform pTransform = new Transform( aGc.getDevice() );
    // // aGc.getTransform( pTransform );
    // // pTransform.translate( xi, yi );
    // // aGc.setTransform( pTransform );
    // // aGc.fillRectangle( 0, 0, wi, hi );
    // // pTransform.translate( -xi, -yi );
    // // aGc.setTransform( pTransform );
    // // pTransform.dispose();
    // // p.dispose();
    // }
    // }
    aGc.setAlpha( fgAlpha );
    aGc.setForeground( fgColor );
    aGc.setLineWidth( lineWidth );
    aGc.drawRectangle( xi, yi, wi, hi );

    if( oldTransfrom != null ) { // восстановим старый transform
      aGc.setTransform( oldTransfrom );
      oldTransfrom.dispose();
    }
  }

}
