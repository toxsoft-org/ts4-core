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
public class VedCoorsConverterOld
    implements IVedCoorsConverter {

  private final VedScreen vedScreen;

  private final D2Convertor convScreen2Swt   = new D2Convertor();
  private final D2Convertor convVisel2Screen = new D2Convertor();

  VedCoorsConverterOld( VedScreen aVedScreen ) {
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

  // @Override
  // public ID2Point swt2Screen( int aX, int aY ) {
  // refreshConversions( null );
  // double x = convScreen2Swt.reverseX( aX, aY );
  // double y = convScreen2Swt.reverseY( aX, aY );
  // return new D2Point( x, y );
  // }

  // @Override
  // public ID2Point swt2Visel( int aX, int aY, VedAbstractVisel aVisel ) {
  // TsNullArgumentRtException.checkNull( aVisel );
  // refreshConversions( aVisel );
  // double x1 = convScreen2Swt.reverseX( aX, aY );
  // double y1 = convScreen2Swt.reverseY( aX, aY );
  // double x = convVisel2Screen.reverseX( x1, y1 ); // old
  // double y = convVisel2Screen.reverseY( x1, y1 ); // old
  // return new D2Point( x, y );
  // }

  @Override
  public ID2Point swt2Screen( int aX, int aY ) {
    refreshConversions( null );

    ID2Conversion conv = convScreen2Swt.getConversion();
    // AffTransform at = D2MatrixConverter.d2convToTransform( conv, 0, 0 );
    // at = at.inverse();
    // ID2Point d2p = new D2Point( at.calcX( aX, aY ), at.calcY( aX, aY ) );
    // return d2p;

    VedAffineTransform at = D2MatrixConverterNew.d2convToTransform( conv, 0, 0 );
    return at.inverseTransform( aX, aY );
  }

  @Override
  public ITsPoint screen2Swt( double aX, double aY ) {
    refreshConversions( null );

    ID2Conversion scrConv = convScreen2Swt.getConversion();
    // AffTransform at = D2MatrixConverter.d2convToTransform( scrConv, 0, 0 );
    // return new TsPoint( (int)at.calcX( aX, aY ), (int)at.calcY( aX, aY ) );

    VedAffineTransform at = D2MatrixConverterNew.d2convToTransform( scrConv, 0, 0 );
    ID2Point d2p = at.transform( aX, aY );
    return new TsPoint( (int)d2p.x(), (int)d2p.y() );
  }

  @Override
  public ID2Point swt2Visel( int aX, int aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    ID2Point d2p = swt2Screen( aX, aY );
    ID2Conversion d2conv = aVisel.getConversion();
    // AffTransform at = D2MatrixConverter.d2convToTransform( d2conv, aVisel.rotationX(), aVisel.rotationY() );
    // at = at.inverse();
    // return new D2Point( at.calcX( d2p.x(), d2p.y() ), at.calcY( d2p.x(), d2p.y() ) );

    VedAffineTransform at = D2MatrixConverterNew.d2convToTransform( d2conv, aVisel.rotationX(), aVisel.rotationY() );
    return at.inverseTransform( d2p );
  }

  @Override
  public ITsPoint visel2Swt( double aX, double aY, VedAbstractVisel aVisel ) {
    ID2Point d2p = visel2Screen( aX, aY, aVisel );
    return screen2Swt( d2p.x(), d2p.y() );
    // ID2Conversion scrConv = aVisel.vedScreen().view().getConversion();
    // AffTransform at = D2MatrixConverter.d2convToTransform( scrConv, 0, 0 );
    //
    // return new TsPoint( (int)at.calcX( d2p ), (int)at.calcY( d2p ) );
  }

  @Override
  public ID2Point screen2Visel( double aX, double aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    ID2Conversion d2conv = aVisel.getConversion();
    VedAffineTransform at = D2MatrixConverterNew.d2convToTransform( d2conv, aVisel.rotationX(), aVisel.rotationY() );
    return at.inverseTransform( aX, aY );
  }

  @Override
  public ID2Point visel2Screen( double aX, double aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    ID2Conversion d2conv = aVisel.getConversion();
    VedAffineTransform at = D2MatrixConverterNew.d2convToTransform( d2conv, aVisel.rotationX(), aVisel.rotationY() );
    return at.transform( aX, aY );
  }

  public ID2Point screen2ViselOld( double aX, double aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    ID2Conversion d2conv = aVisel.getConversion();
    AffTransform at = D2MatrixConverter.d2convToTransform( d2conv, aVisel.rotationX(), aVisel.rotationY() );
    at.inverse();
    return new D2Point( at.calcX( aX, aY ), at.calcY( aX, aY ) );
  }

  public ID2Point visel2ScreenOld( double aX, double aY, VedAbstractVisel aVisel ) {
    TsNullArgumentRtException.checkNull( aVisel );
    refreshConversions( aVisel );
    ID2Conversion d2conv = aVisel.getConversion();
    AffTransform at = D2MatrixConverter.d2convToTransform( d2conv, aVisel.rotationX(), aVisel.rotationY() );
    return new D2Point( at.calcX( aX, aY ), at.calcY( aX, aY ) );
  }

  // @Override
  // public ITsPoint visel2Swt( double aX, double aY, VedAbstractVisel aVisel ) {
  // TsNullArgumentRtException.checkNull( aVisel );
  // refreshConversions( aVisel );
  // double x1 = convVisel2Screen.convertX( aX, aY );
  // double y1 = convVisel2Screen.convertY( aX, aY );
  // double x = convScreen2Swt.convertX( x1, y1 );
  // double y = convScreen2Swt.convertY( x1, y1 );
  // return new TsPoint( (int)x, (int)y );
  // }

  // @Override
  // public ID2Point visel2Screen( double aX, double aY, VedAbstractVisel aVisel ) {
  // TsNullArgumentRtException.checkNull( aVisel );
  // refreshConversions( aVisel );
  // double x = convVisel2Screen.convertX( aX, aY );
  // double y = convVisel2Screen.convertY( aX, aY );
  // return new D2Point( x, y );
  // }

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
    return new TsRectangle( visel2Swt( aX, aY, aVisel ), visel2Swt( aX + aWidth, aY + aHeight, aVisel ) );
    //
    // double xb = aX + aWidth;
    // double yb = aY + aHeight;
    // double x11 = convVisel2Screen.convertX( aX, aY );
    // double y11 = convVisel2Screen.convertY( aX, aY );
    // double x12 = convVisel2Screen.convertX( xb, yb );
    // double y12 = convVisel2Screen.convertY( xb, yb );
    // double x1 = convScreen2Swt.convertX( x11, y11 );
    // double y1 = convScreen2Swt.convertY( x11, y11 );
    // double x2 = convScreen2Swt.convertX( x12, y12 );
    // double y2 = convScreen2Swt.convertY( x12, y12 );
    //
    // return new TsRectangle( new TsPoint( (int)x1, (int)y1 ), new TsPoint( (int)x2, (int)y2 ) );
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
