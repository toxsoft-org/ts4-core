package org.toxsoft.core.tsgui.graphics.vpcalc.impl1;

import org.toxsoft.core.tsgui.graphics.vpcalc.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

abstract class AbstractVpCalc {

  private final Ctx          ctx;
  private final ERectFitMode fitMode;
  private final boolean      isExpandToFit;

  private AbstractVpCalc( Ctx aCtx, ERectFitMode aMode, boolean aIsExpand ) {
    ctx = aCtx;
    fitMode = aMode;
    isExpandToFit = aIsExpand;
  }

  public static AbstractVpCalc create( Ctx aCtx, ERectFitMode aMode, boolean aIsExpand ) {
    switch( aMode ) {
      case NONE: {

        break;
      }
      case FIT_BOTH:
      case FIT_FILL:
      case FIT_HEIGHT:
      case FIT_WIDTH:
      case ZOOMED:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

    // TODO реализовать AbstractVpCalc.create()
    throw new TsUnderDevelopmentRtException( "AbstractVpCalc.create()" );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  protected boolean updateAfterContentSizeChange() {
    // TODO реализовать AbstractVpCalc.updateAfterContentSizeChange()
    throw new TsUnderDevelopmentRtException( "AbstractVpCalc.updateAfterContentSizeChange()" );
  }

  protected boolean updateAfterViewportBoundsChange() {
    // TODO реализовать AbstractVpCalc.updateAfterViewportBoundsChange()
    throw new TsUnderDevelopmentRtException( "AbstractVpCalc.updateAfterViewportBoundsChange()" );

  }

  /**
   * Retruns content drawing bounding rectangle size.
   * <p>
   * Content drawing bounding rectangle is a minimal rectangle bounding rotated and zoomed content.
   *
   * @return {@link ID2Size} - content drawing bounding rectangle size
   */
  ID2Size getContentBoundsSize() {
    return Utils.rotateAndZoomSize( ctx.inSize2D, ctx.rotation, ctx.zoomFactor );
  }

  /**
   * Retruns the virtual viewport.
   * <p>
   * "Virtual" vieport is a rectangle area where the content drawing rectangle may be placed anywhere. The virtual
   * viewport is determined by:
   * <ul>
   * <li>input viewport rectangle set by {@link IViewportCalculator#setViewportBounds(ITsRectangle)};</li>
   * <li>the bounding strategy {@link CalculationStrategySettings#boundingStrategy()};</li>
   * <li>the drawing margins {@link CalculationStrategySettings#boundingMargins()};</li>
   * <li>and last but not least by the size of drawing bounds {@link #getContentBoundsSize()}.</li>
   * </ul>
   *
   * @return {@link ID2Rectangle} - virtual viewport
   */
  ITsRectangle getVirtualVpRect() {
    ID2Size cbSize = getContentBoundsSize();
    EVpBoundingStrategy strategy = ctx.settings.boundingStrategy();
    ITsPoint margins = ctx.settings.boundingMargins();
    return strategy.calcVirtualViewport( ctx.vpBounds, cbSize.dims(), margins );
  }

  // ------------------------------------------------------------------------------------
  // API for IViewportCalculator
  //

  public ERectFitMode fitMode() {
    return fitMode;
  }

  public boolean isExpandToFit() {
    return isExpandToFit;
  }

  public abstract boolean queryConversionChange( ID2Conversion aConversion );

  public abstract boolean queryToChangeOrigin( int aX, int aY );

  public abstract boolean queryToChangeOriginByScrollBars( int aHorScrollBarPos, int aVerScrollBarPos );

  public abstract boolean queryToShift( int aDeltaX, int aDeltaY );

  public abstract boolean queryToMove( ETsCollMove aHorMove, ETsCollMove aVerMove );

  public abstract boolean queryToZoomFromPoint( int aX, int aY, double aZoomMultiplier );

}
