package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedCoorsConverter} implementation.
 *
 * @author hazard157
 */
class VedCoorsConverter
    implements IVedCoorsConverter {

  private final VedScreen vedScreen;

  private final D2Convertor convScreen2Swt   = new D2Convertor();
  private final D2Convertor convVisel2Screen = new D2Convertor();

  VedCoorsConverter( VedScreen aVedScreen ) {
    vedScreen = aVedScreen;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void refreshConversions( VedAbstractVisel aVisel ) {
    convScreen2Swt.setConversion( vedScreen.view().getConversion() );
    if( aVisel != null ) {
      convVisel2Screen.setConversion( aVisel.getConversion() );
    }
    else {
      convVisel2Screen.setConversion( ID2Conversion.NONE );
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedCoorsConvertor
  //

  @Override
  public ID2Point swt2Screen( int aX, int aY ) {
    refreshConversions( null );
    double x = convScreen2Swt.reverseX( aX, aY );
    double y = convScreen2Swt.reverseY( aX, aY );
    return new D2Point( x, y );
  }

  @Override
  public ID2Point swt2Visel( int aX, int aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    double x1 = convScreen2Swt.reverseX( aX, aY );
    double y1 = convScreen2Swt.reverseY( aX, aY );
    // ID2Rectangle d2r = aVisel.bounds();
    double x = convVisel2Screen.reverseX( x1, y1 ); // old
    double y = convVisel2Screen.reverseY( x1, y1 ); // old

    // double x = convVisel2Screen.reverseItemX( x1, y1, d2r.x1() + d2r.width() / 2., d2r.y1() + d2r.height() / 2. );
    // double y = convVisel2Screen.reverseItemY( x1, y1, d2r.x1() + d2r.width() / 2., d2r.y1() + d2r.height() / 2. );
    // double x = convVisel2Screen.reverseItemX( x1, y1, d2r.x1(), d2r.y1() );
    // double y = convVisel2Screen.reverseItemY( x1, y1, d2r.x1(), d2r.y1() );
    return new D2Point( x, y );
  }

  @Override
  public ITsPoint screen2Swt( double aX, double aY ) {
    refreshConversions( null );
    double x = convScreen2Swt.convertX( aX, aY );
    double y = convScreen2Swt.convertY( aX, aY );
    return new TsPoint( (int)x, (int)y );
  }

  @Override
  public ID2Point screen2Visel( double aX, double aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    double x = convVisel2Screen.reverseX( aY, aY );
    double y = convVisel2Screen.reverseY( aY, aY );
    return new D2Point( x, y );
  }

  @Override
  public ITsPoint visel2Swt( double aX, double aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    double x1 = convVisel2Screen.convertX( aX, aY );
    double y1 = convVisel2Screen.convertY( aX, aY );
    double x = convScreen2Swt.convertX( x1, y1 );
    double y = convScreen2Swt.convertY( x1, y1 );
    return new TsPoint( (int)x, (int)y );
  }

  @Override
  public ID2Point visel2Screen( double aX, double aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    double x = convVisel2Screen.convertX( aX, aY );
    double y = convVisel2Screen.convertY( aX, aY );
    return new D2Point( x, y );
  }

  @Override
  public ID2Rectangle swt2Screen( int aX, int aY, int aWidth, int aHeight ) {
    refreshConversions( null );
    TsIllegalArgumentRtException.checkTrue( aWidth < 0 || aHeight < 0 );
    int xb = aX + aWidth;
    int yb = aY + aHeight;
    double x1 = convScreen2Swt.reverseX( aX, aY );
    double y1 = convScreen2Swt.reverseY( aX, aY );
    double x2 = convScreen2Swt.reverseX( xb, yb );
    double y2 = convScreen2Swt.reverseY( xb, yb );
    return new D2Rectangle( new D2Point( x1, y1 ), new D2Point( x2, y2 ) );
  }

  @Override
  public ID2Rectangle swt2Visel( int aX, int aY, int aWidth, int aHeight, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    TsIllegalArgumentRtException.checkTrue( aWidth < 0 || aHeight < 0 );
    int xb = aX + aWidth;
    int yb = aY + aHeight;
    double x11 = convScreen2Swt.reverseX( aX, aY );
    double y11 = convScreen2Swt.reverseY( aX, aY );
    double x12 = convScreen2Swt.reverseX( xb, yb );
    double y12 = convScreen2Swt.reverseY( xb, yb );
    double x1 = convVisel2Screen.reverseX( x11, y11 );
    double y1 = convVisel2Screen.reverseY( x11, y11 );
    double x2 = convVisel2Screen.reverseX( x12, y12 );
    double y2 = convVisel2Screen.reverseY( x12, y12 );
    return new D2Rectangle( new D2Point( x1, y1 ), new D2Point( x2, y2 ) );
  }

  @Override
  public ITsRectangle screen2Swt( double aX, double aY, double aWidth, double aHeight ) {
    refreshConversions( null );
    TsIllegalArgumentRtException.checkTrue( aWidth < 0.0 || aHeight < 0.0 );
    double xb = aX + aWidth;
    double yb = aY + aHeight;
    double x1 = convScreen2Swt.convertX( aX, aY );
    double y1 = convScreen2Swt.convertY( aX, aY );
    double x2 = convScreen2Swt.convertX( xb, yb );
    double y2 = convScreen2Swt.convertY( xb, yb );
    return new TsRectangle( new TsPoint( (int)x1, (int)y1 ), new TsPoint( (int)x2, (int)y2 ) );
  }

  @Override
  public ID2Rectangle screen2Visel( double aX, double aY, double aWidth, double aHeight, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    TsIllegalArgumentRtException.checkTrue( aWidth < 0.0 || aHeight < 0.0 );
    double xb = aX + aWidth;
    double yb = aY + aHeight;
    double x1 = convVisel2Screen.reverseX( aX, aY );
    double y1 = convVisel2Screen.reverseY( aX, aY );
    double x2 = convVisel2Screen.reverseX( xb, yb );
    double y2 = convVisel2Screen.reverseY( xb, yb );
    return new D2Rectangle( new D2Point( x1, y1 ), new D2Point( x2, y2 ) );
  }

  @Override
  public ITsRectangle visel2Swt( double aX, double aY, double aWidth, double aHeight, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    TsIllegalArgumentRtException.checkTrue( aWidth < 0.0 || aHeight < 0.0 );
    double xb = aX + aWidth;
    double yb = aY + aHeight;
    double x11 = convVisel2Screen.convertX( aX, aY );
    double y11 = convVisel2Screen.convertY( aX, aY );
    double x12 = convVisel2Screen.convertX( xb, yb );
    double y12 = convVisel2Screen.convertY( xb, yb );
    double x1 = convScreen2Swt.convertX( x11, y11 );
    double y1 = convScreen2Swt.convertY( x11, y11 );
    double x2 = convScreen2Swt.convertX( x12, y12 );
    double y2 = convScreen2Swt.convertY( x12, y12 );
    return new TsRectangle( new TsPoint( (int)x1, (int)y1 ), new TsPoint( (int)x2, (int)y2 ) );
  }

  @Override
  public ID2Rectangle visel2Screen( double aX, double aY, double aWidth, double aHeight, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    TsIllegalArgumentRtException.checkTrue( aWidth < 0.0 || aHeight < 0.0 );
    double xb = aX + aWidth;
    double yb = aY + aHeight;
    double x1 = convVisel2Screen.convertX( aX, aY );
    double y1 = convVisel2Screen.convertY( aX, aY );
    double x2 = convVisel2Screen.convertX( xb, yb );
    double y2 = convVisel2Screen.convertY( xb, yb );
    return new D2Rectangle( new D2Point( x1, y1 ), new D2Point( x2, y2 ) );
  }

}
