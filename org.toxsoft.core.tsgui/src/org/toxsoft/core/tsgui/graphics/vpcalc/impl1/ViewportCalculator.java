package org.toxsoft.core.tsgui.graphics.vpcalc.impl1;

import org.toxsoft.core.tsgui.graphics.vpcalc.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IViewportCalculator} implementation.
 *
 * @author hazard157
 */
public class ViewportCalculator
    implements IViewportCalculator {

  /**
   * FIXME hot to change strategy when query needs to unlock adaptive fit mode
   */

  private final ViewportOutput output = new ViewportOutput();
  private final Ctx            ctx;

  private AbstractVpCalc vpCalc;

  /**
   * Constructor.
   *
   * @param aSettings {@link CalculationStrategySettings} - the calculation strategy settings
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ViewportCalculator( CalculationStrategySettings aSettings ) {
    TsNullArgumentRtException.checkNull( aSettings );
    ctx = new Ctx( aSettings );
    vpCalc = AbstractVpCalc.create( ctx, ERectFitMode.NONE, false );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private boolean applyCtxToOutput() {
    ID2Conversion c = new D2Conversion( ctx.rotation, ctx.zoomFactor, ctx.origin );
    ID2Size cbSize = vpCalc.getContentBoundsSize();
    ITsRectangle vvRect = vpCalc.getVirtualVpRect();
    ScrollBarSettings hBar = ScrollBarSettings.ofTuned( vvRect.x1(), vvRect.x2(), cbSize.intW(), ctx.origin.intX() );
    ScrollBarSettings vBar = ScrollBarSettings.ofTuned( vvRect.y1(), vvRect.y2(), cbSize.intH(), ctx.origin.intY() );
    return output.setParams( c, hBar, vBar );
  }

  // ------------------------------------------------------------------------------------
  // IViewportCalculator
  //

  @Override
  public CalculationStrategySettings settings() {
    return ctx.settings;
  }

  @Override
  public ERectFitMode fitMode() {
    return vpCalc.fitMode();
  }

  @Override
  public boolean isExpandToFit() {
    return vpCalc.isExpandToFit();
  }

  @Override
  public boolean setFitParams( ERectFitMode aFitMode, boolean aExpandToFit ) {
    TsNullArgumentRtException.checkNull( aFitMode );
    if( vpCalc.isExpandToFit() == aExpandToFit && vpCalc.fitMode() == aFitMode ) {
      return false;
    }
    vpCalc = AbstractVpCalc.create( ctx, aFitMode, aExpandToFit );
    return applyCtxToOutput();
  }

  @Override
  public boolean setContentSize( ID2Size aContentSize ) {
    TsNullArgumentRtException.checkNull( aContentSize );
    if( ctx.inSize2D.equals( aContentSize ) ) {
      return false;
    }
    ctx.inSize2D.setSize( aContentSize );
    if( vpCalc.updateAfterContentSizeChange() ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public boolean setViewportBounds( ITsRectangle aViewportBounds ) {
    TsNullArgumentRtException.checkNull( aViewportBounds );
    if( ctx.vpBounds.equals( aViewportBounds ) ) {
      return false;
    }
    ctx.vpBounds.setRect( aViewportBounds );
    if( vpCalc.updateAfterViewportBoundsChange() ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public boolean queryConversionChange( ID2Conversion aConversion ) {
    TsNullArgumentRtException.checkNull( aConversion );
    if( vpCalc.queryConversionChange( aConversion ) ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public boolean queryToChangeOrigin( int aX, int aY ) {
    if( vpCalc.queryToChangeOrigin( aX, aY ) ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public boolean queryToChangeOriginByScrollBars( int aHorScrollBarPos, int aVerScrollBarPos ) {
    if( vpCalc.queryToChangeOriginByScrollBars( aHorScrollBarPos, aVerScrollBarPos ) ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public boolean queryToShift( int aDeltaX, int aDeltaY ) {
    if( vpCalc.queryToShift( aDeltaX, aDeltaY ) ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public boolean queryToMove( ETsCollMove aHorMove, ETsCollMove aVerMove ) {
    TsNullArgumentRtException.checkNulls( aHorMove, aVerMove );
    if( vpCalc.queryToMove( aHorMove, aVerMove ) ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public boolean queryToZoomFromPoint( int aX, int aY, double aZoomMultiplier ) {
    if( vpCalc.queryToZoomFromPoint( aX, aY, aZoomMultiplier ) ) {
      return applyCtxToOutput();
    }
    return false;
  }

  @Override
  public IViewportOutput output() {
    return output;
  }

}
