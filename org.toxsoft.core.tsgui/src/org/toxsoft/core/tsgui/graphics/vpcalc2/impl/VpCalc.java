package org.toxsoft.core.tsgui.graphics.vpcalc2.impl;

import org.toxsoft.core.tsgui.graphics.vpcalc2.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVpCalc} implementation.
 *
 * @author hazard157
 */
public class VpCalc
    implements IVpCalc {

  // ------------------------------------------------------------------------------------
  // Input parameters are set from outside and never changed from inside calculator
  private final VpCalcCfg       inCfg      = new VpCalcCfg();
  private final TsRectangleEdit inVpBounds = new TsRectangleEdit( 0, 0, 1, 1 ); // the viewport
  private final TsRectangleEdit inVpRect   = new TsRectangleEdit( 0, 0, 1, 1 ); // viewport with margins applied
  private final ID2SizeEdit     inContSize = new D2SizeEdit( 16.0, 16.0 );
  private final ID2AngleEdit    inAngle    = new D2AngleEdit();

  // ------------------------------------------------------------------------------------
  // Some setpoint used in non-adaptive modes
  private double      setpZoom   = 1.0;
  private TsPointEdit setpOrigin = new TsPointEdit( 0, 0 );

  // ------------------------------------------------------------------------------------
  // real values used for drawing
  private final VpOutput vpOut = new VpOutput();

  /**
   * Constructor.
   */
  public VpCalc() {
    inCfg.genericChangeEventer().addListener( s -> internalRecalc( true ) );
  }

  /**
   * Constructor.
   *
   * @param aCfg {@link IVpCalcCfg} - the calculation strategy settings
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VpCalc( IVpCalcCfg aCfg ) {
    inCfg.copyFrom( aCfg );
    inCfg.genericChangeEventer().addListener( s -> internalRecalc( true ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private boolean mustApplyFulcrum( ID2Size aRealSize ) {
    return switch( inCfg.fulcrumStartegy() ) {
      case ALWAYS -> true;
      case INSIDE -> inVpRect.width() >= aRealSize.intW() && inVpRect.height() >= aRealSize.intH();
      case HINT -> false;
      default -> throw new IllegalArgumentException();
    };
  }

  private static ScrollBarCfg calcScrollBarCfg( int aBoundOrigin, int aBoundDim, int aVirtDim ) {
    int range = aVirtDim;
    int min = 0;
    int max = range;
    int thumb = aBoundDim;
    int sel = aBoundOrigin;
    int inc = range / 100 > 1 ? range / 100 : 1;
    int pageInc = range / 10 > inc ? range / 10 : 2 * inc;
    return new ScrollBarCfg( sel, min, max, thumb, inc, pageInc );
  }

  private ITsRectangle calcDrawingBounds( ID2Size aRealSize, ITsRectangle aVirtVp, boolean aForceFulcrum ) {
    int x, y;
    if( aForceFulcrum || mustApplyFulcrum( aRealSize ) ) {
      x = aVirtVp.x1() + inCfg.fulcrum().calcTopleftX( aVirtVp.width(), aRealSize.intW() );
      y = aVirtVp.y1() + inCfg.fulcrum().calcTopleftY( aVirtVp.height(), aRealSize.intH() );
    }
    else { // fit content in virtual viewport
      x = setpOrigin.x(); // real origin X
      y = setpOrigin.y(); // real origin Y
      if( !aVirtVp.contains( x, y, aRealSize.intW(), aRealSize.intH() ) ) {
        x = x < aVirtVp.x1() ? aVirtVp.x1() : aVirtVp.x2() - aRealSize.intW();
        x = y < aVirtVp.y1() ? aVirtVp.y1() : aVirtVp.y2() - aRealSize.intH();
      }
    }
    return new TsRectangle( x, y, aRealSize.intW(), aRealSize.intH() );
  }

  private ITsRectangle calcVirtualViewport( ID2Size aDrawSize ) {
    return switch( inCfg.boundsStrategy() ) {
      case NONE -> {
        /**
         * Instead of returning very big rectangle (as does EVpBoundingStrategy.NONE) will returns the rectangle few
         * times bigger than current placement of the viewport and content. This is done for the scroll bars to have
         * meaningful values.
         */
        int maxW = Math.max( aDrawSize.dims().width(), inVpRect.width() );
        int x1 = Math.min( inVpRect.x1(), inVpRect.x1() ) - maxW;
        int x2 = Math.max( inVpRect.x2(), inVpRect.x2() ) + maxW;
        int maxH = Math.max( aDrawSize.dims().height(), inVpRect.height() );
        int y1 = Math.min( inVpRect.y1(), inVpRect.y1() ) - maxH;
        int y2 = Math.max( inVpRect.y2(), inVpRect.y2() ) + maxH;
        yield new TsRectangle( new TsPoint( x1, y1 ), new TsPoint( x2, y2 ) );
      }
      case VIEWPORT -> inCfg.boundsStrategy().calcVirtualViewport( inVpRect, aDrawSize.dims() );
      case CONTENT -> inCfg.boundsStrategy().calcVirtualViewport( inVpRect, aDrawSize.dims() );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  private boolean prepareAndSetOutput( ID2Size aDrawSize, ITsRectangle aVirtVp, double aZoom, boolean aForceFulcrum ) {
    // drawing bounds with fulcrum and placement limits applied
    ITsRectangle drawBounds = calcDrawingBounds( aDrawSize, aVirtVp, aForceFulcrum );
    // set drawing origin (0,0) to be on one of the edges of the drawing bounds rectangle
    ED2Quadrant quadrant = ED2Quadrant.findByRadians( inAngle.radians() );
    double contW = inContSize.width() * aZoom;
    double x = quadrant.contentRectOriginX( drawBounds, contW, inAngle.radians() );
    double y = quadrant.contentRectOriginY( drawBounds, contW, inAngle.radians() );
    ID2Point contDrawOrigin = new D2Point( x, y );
    // set scroll bars settings and visibility
    ScrollBarCfg hs = calcScrollBarCfg( drawBounds.x1(), drawBounds.width(), aVirtVp.width() );
    ScrollBarCfg vs = calcScrollBarCfg( drawBounds.y1(), drawBounds.height(), aVirtVp.height() );

    // TODO scroll bars are visible when ???

    hs.setVisible( ((drawBounds.x1() < inVpRect.x1()) || (drawBounds.x2() > inVpRect.x2())) );
    vs.setVisible( ((drawBounds.y1() < inVpRect.y1()) || (drawBounds.y2() > inVpRect.y2())) );
    // apply conversion and scroll bars to the viewport output
    ID2Conversion c = new D2Conversion( inAngle, aZoom, contDrawOrigin );
    return vpOut.setParams( c, hs, vs, drawBounds );
  }

  private boolean internalRecalc( boolean aForceFulcrum ) {
    ID2Size drawSize;
    ITsRectangle virtVp;
    double zoomFactor = setpZoom;
    ID2Size rotatedSize = VpCalcUtils.rotateAndZoomSize( inContSize, inAngle, setpZoom );
    if( inCfg.fitMode().isAdaptiveScale() ) {
      // calculate adapted (fitted) size
      if( inCfg.fitMode().isScalingNeeded( inVpRect, rotatedSize, inCfg.isExpandToFit() ) ) {
        drawSize = D2Utils.createSize( inCfg.fitMode().calcFitSize( inVpRect, rotatedSize ) );
        zoomFactor = inCfg.fitMode().calcFitZoom( inVpRect, rotatedSize );
      }
      else {
        drawSize = rotatedSize;
      }
      // in adaptive mode VIWPORT bounding strategy is forced to calculate virtual viewport
      virtVp = EVpBoundingStrategy.VIEWPORT.calcVirtualViewport( inVpRect, drawSize.dims() );
    }
    else {
      drawSize = VpCalcUtils.rotateAndZoomSize( inContSize, inAngle, setpZoom );
      virtVp = calcVirtualViewport( drawSize );
    }
    //
    return prepareAndSetOutput( drawSize, virtVp, zoomFactor, aForceFulcrum );
  }

  // ------------------------------------------------------------------------------------
  // IVpCalc
  //

  @Override
  public IVpCalcCfg cfg() {
    return inCfg;
  }

  @Override
  public ID2Angle getAngle() {
    return new D2Angle( inAngle );
  }

  @Override
  public boolean setAngle( ID2Angle aAngle ) {
    TsNullArgumentRtException.checkNull( aAngle );
    if( inAngle.equals( aAngle ) ) {
      return false;
    }
    inAngle.setAngle( aAngle );
    return internalRecalc( false );
  }

  @Override
  public boolean setContentSize( ID2Size aContentSize ) {
    TsNullArgumentRtException.checkNull( aContentSize );
    if( inContSize.equals( aContentSize ) ) {
      return false;
    }
    inContSize.setSize( aContentSize );
    return internalRecalc( true );
  }

  @Override
  public boolean setViewportBounds( ITsRectangle aViewportBounds ) {
    TsNullArgumentRtException.checkNull( aViewportBounds );
    if( inVpBounds.equals( aViewportBounds ) ) {
      return false;
    }
    inVpBounds.setRect( aViewportBounds );
    inVpRect.setRect( TsMarginUtils.applyMargins( inVpBounds, inCfg.margins() ) );
    return internalRecalc( false );
  }

  @Override
  public double getDesiredZoom() {
    return setpZoom;
  }

  @Override
  public boolean setDesiredZoom( double aZoom ) {
    D2Utils.checkZoom( aZoom );
    if( D2Utils.isDuckEQ( setpZoom, aZoom ) ) {
      return false;
    }
    setpZoom = aZoom;
    return internalRecalc( false );
  }

  @Override
  public ITsPoint getDesiredOrigin() {
    return setpOrigin;
  }

  @Override
  public boolean setDesiredOrigin( ITsPoint aOrigin ) {
    TsNullArgumentRtException.checkNull( aOrigin );
    if( setpOrigin.equals( aOrigin ) ) {
      return false;
    }
    setpOrigin.setPoint( aOrigin );
    return internalRecalc( false );
  }

  @Override
  public IVpOutput output() {
    return vpOut;
  }

}
