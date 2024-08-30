package org.toxsoft.core.tsgui.graphics.vpcalc2.impl;

import org.toxsoft.core.tsgui.graphics.vpcalc2.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
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
  private final IVpCalcCfg      inCfg;
  private final TsRectangleEdit inVpRect   = new TsRectangleEdit( 0, 0, 1, 1 );
  private final ID2SizeEdit     inContSize = new D2SizeEdit( 16.0, 16.0 );
  private final ID2AngleEdit    inAngle    = new D2AngleEdit();
  private ERectFitMode          inFitMode  = ERectFitMode.NONE;
  private boolean               inExpand   = false;

  // ------------------------------------------------------------------------------------
  // Some setpoint used in non-adaptive modes
  private double      setpZoom   = 1.0;
  private TsPointEdit setpOrigin = new TsPointEdit( 0, 0 );

  // ------------------------------------------------------------------------------------
  // real values used for drawing
  private final VpOutput vpOut = new VpOutput();

  /**
   * Constructor.
   *
   * @param aCfg {@link IVpCalcCfg} - the calculation strategy settings
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VpCalc( IVpCalcCfg aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    inCfg = aCfg;
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

  private static ScrollBarCfg calcHorScrollBarCfg( int aOrigingX, ID2Size aRealSize, ITsRectangle aVirtVp ) {
    int range = aVirtVp.width() - aRealSize.intW() + 1;
    int min = 0;
    int max = range;
    int thumb = (int)(range * (aRealSize.width() / aVirtVp.width()));
    int inc = range / 100 > 1 ? range / 100 : 1;
    int pageInc = range / 10 > inc ? range / 10 : 2 * inc;
    int sel = aOrigingX;
    return new ScrollBarCfg( sel, min, max, thumb, inc, pageInc );
  }

  private static ScrollBarCfg calcVerScrollBarCfg( int aOrigingY, ID2Size aRealSize, ITsRectangle aVirtVp ) {
    int range = aVirtVp.height() - aRealSize.intH() + 1;
    int min = 0;
    int max = range;
    int thumb = (int)(range * (aRealSize.height() / aVirtVp.height()));
    int inc = range / 100 > 1 ? range / 100 : 1;
    int pageInc = range / 10 > inc ? range / 10 : 2 * inc;
    int sel = aOrigingY;
    return new ScrollBarCfg( sel, min, max, thumb, inc, pageInc );
  }

  private boolean internalRecalcAdaptive() {
    // content drawing size
    ID2Size realSize = VpCalcUtils.rotateAndZoomSize( inContSize, inAngle, setpZoom );

    // TODO реализовать VpCalc.internalRecalc()
    throw new TsUnderDevelopmentRtException( "VpCalc.internalRecalc()" );

  }

  private boolean internalRecalcNonAdaptive() {
    // content drawing bounding rectangle size
    ID2Size realSize = VpCalcUtils.rotateAndZoomSize( inContSize, inAngle, setpZoom );
    // virtual viewport
    ITsRectangle virtVp = inCfg.boundsStrategy().calcVirtualViewport( inVpRect, realSize.dims(), inCfg.margins() );

    // FIXME

    /**
     * The anchor point is fixed in Ncs (Normalized coordinates space. and after conversion it must be placed in Scs
     * (Screen coodinates space) at the fixed position. Accordin to this input the Ncs origin (th point (O,0))
     * coordinates must be specified as #vpOut.origin().
     * <p>
     * Anchor point is determined relatively to the content rectangle. In each axis value achRel = 0.0 means left/top
     * edge, 1.0 -> bottom/right edge, negative values lefter/upper and values >1.0 righter/lower of the content
     * rectangle.
     */
    // double anchRelX = 0.0;
    // double anchRelY = 0.0;
    // ITsPoint anchN = new TsPoint( 0, 0 );
    // ITsPoint anchS = new TsPoint( 0, 0 );

    // either apply fulcrum or #setpOrigin
    int x, y;
    if( mustApplyFulcrum( realSize ) ) {
      x = inCfg.fulcrum().calcTopleftX( virtVp.width(), realSize.intW() );
      y = inCfg.fulcrum().calcTopleftY( virtVp.height(), realSize.intH() );
    }
    else { // fit content in virtual viewport
      x = setpOrigin.x(); // real origin X
      y = setpOrigin.y(); // real origin Y
      if( !virtVp.contains( x, y, realSize.intW(), realSize.intH() ) ) {
        x = x < virtVp.x1() ? virtVp.x1() : virtVp.x2() - realSize.intW();
        x = y < virtVp.y1() ? virtVp.y1() : virtVp.y2() - realSize.intH();
      }
    }
    // now (x,y) is ccordinates of realRect top-left corner, but we need to find drawn content top-left point coors
    // FIXME recalc x,y with rotation and zoom

    // prepare and apply values to the output
    ID2Conversion c = new D2Conversion( inAngle, setpZoom, new D2Point( x, y ) );
    ScrollBarCfg hs = calcHorScrollBarCfg( x, realSize, virtVp );
    ScrollBarCfg vs = calcVerScrollBarCfg( y, realSize, virtVp );

    // FIXME set scroll bars visibility

    return vpOut.setParams( c, hs, vs );
  }

  private boolean internalRecalc() {
    if( inFitMode.isAdaptiveScale() ) {
      return internalRecalcAdaptive();
    }
    return internalRecalcNonAdaptive();
  }

  // ------------------------------------------------------------------------------------
  // IVpCalc
  //

  @Override
  public IVpCalcCfg getCfg() {
    return inCfg;
  }

  @Override
  public ERectFitMode getFitMode() {
    return inFitMode;
  }

  @Override
  public boolean isExpandToFit() {
    return inExpand;
  }

  @Override
  public boolean setFitParams( ERectFitMode aFitMode, boolean aExpandToFit ) {
    TsNullArgumentRtException.checkNull( aFitMode );
    if( inFitMode == aFitMode && inExpand == aExpandToFit ) {
      return false;
    }
    inFitMode = aFitMode;
    inExpand = aExpandToFit;
    return internalRecalc();
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
    return internalRecalc();
  }

  @Override
  public boolean setContentSize( ID2Size aContentSize ) {
    TsNullArgumentRtException.checkNull( aContentSize );
    if( inContSize.equals( aContentSize ) ) {
      return false;
    }
    inContSize.setSize( aContentSize );
    return internalRecalc();
  }

  @Override
  public boolean setViewportBounds( ITsRectangle aViewportBounds ) {
    TsNullArgumentRtException.checkNull( aViewportBounds );
    if( inVpRect.equals( aViewportBounds ) ) {
      return false;
    }
    inVpRect.setRect( aViewportBounds );
    return internalRecalc();
  }

  @Override
  public double getParamZoom() {
    return setpZoom;
  }

  @Override
  public boolean setParamZoom( double aZoom ) {
    D2Utils.checkZoom( aZoom );
    if( D2Utils.isDuckEQ( setpZoom, aZoom ) ) {
      return false;
    }
    setpZoom = aZoom;
    return internalRecalc();
  }

  @Override
  public ITsPoint getParamOrigin() {
    return setpOrigin;
  }

  @Override
  public boolean setParamOrigin( ITsPoint aOrigin ) {
    TsNullArgumentRtException.checkNull( aOrigin );
    if( setpOrigin.equals( aOrigin ) ) {
      return false;
    }
    setpOrigin.setPoint( aOrigin );
    return internalRecalc();
  }

  @Override
  public IVpOutput output() {
    return vpOut;
  }

}
