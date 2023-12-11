package org.toxsoft.core.tsgui.graphics.vpcalc;

import static org.toxsoft.core.tsgui.graphics.vpcalc.ScrollBarSettings.*;

import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IViewportCalculator} implementation.
 *
 * @author hazard157
 */
public class ViewportCalculator
    implements IViewportCalculator {

  private final CalculationStrategySettings settings;

  private final ViewportOutput output = new ViewportOutput();

  private ERectFitMode fitMode     = ERectFitMode.NONE;
  private boolean      expandToFit = false;
  private ID2Size      inSize2D    = new D2Size( 16.0, 16.0 );
  private ITsPoint     inSize      = new TsPoint( (int)inSize2D.width(), (int)inSize2D.height() );
  private ITsRectangle vpRect      = ITsRectangle.MINRECT;

  /**
   * Constructor.
   *
   * @param aSettings {@link CalculationStrategySettings} - the calculation strategy settings
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ViewportCalculator( CalculationStrategySettings aSettings ) {
    TsNullArgumentRtException.checkNull( aSettings );
    settings = aSettings;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Determines if current settings requires image size and position to be adapted to the viewport.
   *
   * @return boolean - output is determined by the viewport
   */
  private boolean isAdaptiveMode() {
    ID2Size boundsSize = new D2Convertor( output.conversion() ).convertSize( inSize2D );
    return fitMode.isScalingNeeded( //
        vpRect.width(), vpRect.height(), //
        (int)boundsSize.width(), (int)boundsSize.height(), //
        expandToFit );
  }

  private boolean internalRecalcAdaptive() {
    D2ConversionEdit conv = new D2ConversionEdit( output.conversion() );
    ITsPoint fitSize = fitMode.calcFitSize( vpRect.width(), vpRect.height(), inSize.x(), inSize.y() );
    // in adaptive modes fulcrum is used anyway, as a hint how to place content along the not-fit edge
    conv.origin().setX( vpRect.x1() + settings.fulcrum().calcTopleftX( vpRect.width(), fitSize.x() ) );
    conv.origin().setY( vpRect.y1() + settings.fulcrum().calcTopleftY( vpRect.height(), fitSize.y() ) );
    /**
     * Here we know the origin and #fitSize - the rectangle that must have the transformed content. As the rotation
     * angle remain unchanged by the adaptive scaling, we need to adjust the zoom factor to get the size #fitSize.
     */
    ID2Size boundsSize = new D2Convertor( output.conversion() ).convertSize( inSize2D );
    double deltaZoom = fitSize.x() / boundsSize.width();
    conv.setZoomFactor( deltaZoom * conv.zoomFactor() ); // adjust zoom factor
    ScrollBarSettings hBar = ofTuned( vpRect.x1(), vpRect.x2(), fitSize.x(), (int)conv.origin().x() );
    ScrollBarSettings vBar = ofTuned( vpRect.y1(), vpRect.y2(), fitSize.y(), (int)conv.origin().y() );
    return output.setParams( conv, vpRect, hBar, vBar );
  }

  private boolean internalRecalcSpecified( ID2ConversionEdit conv ) {
    ID2Size boundsSize = new D2Convertor( conv ).convertSize( inSize2D );
    // determine if fulcrum must be used
    boolean useFulcrum = switch( settings.fulcrumUsageStartegy() ) {
      case ALWAYS -> true;
      case INSIDE -> boundsSize.width() < vpRect.width() && boundsSize.height() < vpRect.height();
      case HINT -> false;
    };
    // use either fulcrum or EVpBoundingStrategy
    if( useFulcrum ) {
      conv.origin().setX( settings.fulcrum().calcTopleftX( vpRect.width(), (int)boundsSize.width() ) );
      conv.origin().setY( settings.fulcrum().calcTopleftY( vpRect.height(), (int)boundsSize.height() ) );
    }
    else {
      // TODO apply bounding strategy
    }
    ScrollBarSettings hBar = ofTuned( vpRect.x1(), vpRect.x2(), (int)boundsSize.width(), (int)conv.origin().x() );
    ScrollBarSettings vBar = ofTuned( vpRect.y1(), vpRect.y2(), (int)boundsSize.height(), (int)conv.origin().y() );
    return output.setParams( conv, vpRect, hBar, vBar );

  }

  /**
   * Recalculate output when any of {@link #fitMode}, {@link #expandToFit}, {@link #inSize2D} or {@link #vpRect}
   * changes.
   * <p>
   * Simply chooses between {@link #internalRecalcAdaptive()} and {@link #internalRecalcSpecified(ID2ConversionEdit)}
   * depending on {@link #isAdaptiveMode()} value.
   *
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  private boolean internalRecalc() {
    if( isAdaptiveMode() ) {
      return internalRecalcAdaptive();
    }
    return internalRecalcSpecified( new D2ConversionEdit( output.conversion() ) );
  }

  private static int newCoor( double aCoor, int aVp1, int aVp2, int aContentSize, ScrollBarSettings aSbs,
      ETsCollMove aMove ) {
    return (int)switch( aMove ) {
      case NONE -> aCoor;
      case FIRST -> aVp1;
      case JUMP_NEXT -> aCoor + aSbs.pageIncrement();
      case JUMP_PREV -> aCoor - aSbs.pageIncrement();
      case LAST -> Math.max( aVp2, aCoor + aContentSize ) - aContentSize;
      case MIDDLE -> (aVp2 - aVp1 - aContentSize) / 2;
      case NEXT -> aCoor + aSbs.increment();
      case PREV -> aCoor - aSbs.increment();
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  // ------------------------------------------------------------------------------------
  // IViewportCalculator
  //

  @Override
  public CalculationStrategySettings settings() {
    return settings;
  }

  @Override
  public ERectFitMode fitMode() {
    return fitMode;
  }

  @Override
  public boolean isExpandToFit() {
    return expandToFit;
  }

  @Override
  public boolean setFitParams( ERectFitMode aFitMode, boolean aExpandToFit ) {
    TsNullArgumentRtException.checkNull( aFitMode );
    if( fitMode == aFitMode && expandToFit == aExpandToFit ) {
      return false;
    }
    return internalRecalc();
  }

  @Override
  public boolean setContentSize( ID2Size aContentSize ) {
    TsNullArgumentRtException.checkNull( aContentSize );
    if( inSize2D.equals( aContentSize ) ) {
      return false;
    }
    inSize2D = aContentSize;
    inSize = new TsPoint( (int)inSize2D.width(), (int)inSize2D.height() );
    return internalRecalc();
  }

  @Override
  public boolean setViewportBounds( ITsRectangle aViewportBounds ) {
    TsNullArgumentRtException.checkNull( aViewportBounds );
    if( vpRect.equals( aViewportBounds ) ) {
      return false;
    }
    vpRect = aViewportBounds;
    return internalRecalc();
  }

  @Override
  public boolean queryConversionChange( ID2Conversion aConversion ) {
    TsNullArgumentRtException.checkNull( aConversion );
    // changing conversion is not allowed in unlockable adaptive mode
    if( isAdaptiveMode() && !settings.isFitModeUnlocked() ) {
      return false;
    }
    // reset fit mode if needed
    if( fitMode.isAdaptiveScale() ) {
      fitMode = ERectFitMode.ZOOMED;
    }
    return internalRecalcSpecified( new D2ConversionEdit( aConversion ) );
  }

  @Override
  public boolean queryToChangeOrigin( int aX, int aY ) {
    ID2ConversionEdit conv = new D2ConversionEdit( output.conversion() );
    conv.origin().setPoint( aX, aY );
    return queryConversionChange( conv );
  }

  @Override
  public boolean queryToShift( int aDeltaX, int aDeltaY ) {
    ID2ConversionEdit conv = new D2ConversionEdit( output.conversion() );
    conv.origin().shiftOn( aDeltaX, aDeltaY );
    return queryConversionChange( conv );
  }

  @Override
  public boolean queryToMove( ETsCollMove aHorMove, ETsCollMove aVerMove ) {
    TsNullArgumentRtException.checkNulls( aHorMove, aVerMove );
    if( aHorMove == ETsCollMove.NONE && aVerMove == ETsCollMove.NONE ) {
      return false;
    }
    ID2ConversionEdit conv = new D2ConversionEdit( output.conversion() );
    ID2Size outSize = new D2Convertor( conv ).convertSize( inSize2D );
    int nexX =
        newCoor( conv.origin().x(), vpRect.x1(), vpRect.x2(), outSize.intW(), output.horBarSettings(), aHorMove );
    int newY =
        newCoor( conv.origin().y(), vpRect.y1(), vpRect.y2(), outSize.intH(), output.verBarSettings(), aVerMove );
    return queryToChangeOrigin( nexX, newY );
  }

  @Override
  public boolean queryToZoomFromPoint( int aX, int aY, double aZoomMultiplier ) {
    // nothing to do
    if( !vpRect.contains( aX, aY ) || aZoomMultiplier == 1.0 ) {
      return false;
    }
    // changing conversion is not allowed in unlockable adaptive mode
    if( isAdaptiveMode() && !settings.isFitModeUnlocked() ) {
      return false;
    }

    // if point is outside content - do nothing
    ID2Convertor oldConverter = new D2Convertor( output.conversion() );
    ID2PointEdit origin = new D2PointEdit( output.conversion().origin() );
    ID2Size currentSize = oldConverter.convertSize( inSize2D );
    ITsRectangle currentBounds = oldConverter.rectBounds( new D2Rectangle( //
        origin.x(), origin.y(), inSize2D.width(), inSize2D.height() ) //
    );
    if( !currentBounds.contains( aX, aY ) ) {
      return false;
    }
    // now calculate new parameters
    ID2ConversionEdit newConverter = new D2ConversionEdit( output.conversion() );
    newConverter.setZoomFactor( newConverter.zoomFactor() * aZoomMultiplier );
    ID2Convertor d2conv = new D2Convertor( newConverter );
    ID2Size newSize = d2conv.convertSize( inSize2D );
    int deltaX = (int)((1.0 - newSize.width() / currentSize.width()) * (aX - origin.x()));
    int deltaY = (int)((1.0 - newSize.height() / currentSize.height()) * (aY - origin.y()));
    origin.shiftOn( deltaX, deltaY );
    newConverter.origin().setPoint( origin );
    ScrollBarSettings hBar = new ScrollBarSettings( output.horBarSettings() );
    ScrollBarSettings vBar = new ScrollBarSettings( output.verBarSettings() );
    hBar.tuneScrollBar( vpRect.x1(), vpRect.x2(), (int)newSize.width(), (int)origin.x() );
    vBar.tuneScrollBar( vpRect.y1(), vpRect.y2(), (int)newSize.height(), (int)origin.y() );
    return output.setParams( newConverter, vpRect, hBar, vBar );
  }

  @Override
  public IViewportOutput output() {
    return output;
  }

}
