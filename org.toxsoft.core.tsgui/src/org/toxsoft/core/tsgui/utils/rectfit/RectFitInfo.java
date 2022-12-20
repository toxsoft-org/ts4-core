package org.toxsoft.core.tsgui.utils.rectfit;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Settings how to fit an object in a rectangular area.
 *
 * @author hazard157
 */
public final class RectFitInfo {

  /**
   * Min value of {@link #zoomFactor()}.
   */
  public static final double MIN_ZOOM_FACTOR = 0.000_001;

  /**
   * Max value of {@link #zoomFactor()}.
   */
  public static final double MAX_ZOOM_FACTOR = 1_000_000.0;

  /**
   * Default zoom factor.
   */
  public static final double DEFAULT_ZOOM = 1.0;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "RectFitInfo"; //$NON-NLS-1$

  /**
   * Convinience constant of no special fitting - display original object clipped in vieweport.
   */
  public static final RectFitInfo NONE = new RectFitInfo( ERectFitMode.NONE, false, DEFAULT_ZOOM );

  /**
   * Convinience constant best fitting - fit original object in vieweport but not increase if it's small.
   */
  public static final RectFitInfo BEST = new RectFitInfo( ERectFitMode.FIT_BOTH, false, DEFAULT_ZOOM );

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<RectFitInfo> KEEPER =
      new AbstractEntityKeeper<>( RectFitInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, RectFitInfo aEntity ) {
          ERectFitMode.KEEPER.write( aSw, aEntity.fitMode );
          if( aEntity.expandToFit ) {
            aSw.writeSeparatorChar();
            aSw.writeBoolean( aEntity.expandToFit );
          }
          if( aEntity.zoomFactor != DEFAULT_ZOOM ) {
            aSw.writeSeparatorChar();
            aSw.writeDouble( aEntity.zoomFactor );
          }
        }

        @Override
        protected RectFitInfo doRead( IStrioReader aSr ) {
          ERectFitMode fitMode = ERectFitMode.KEEPER.read( aSr );
          boolean expandToFit = false;
          double zoomFactor = DEFAULT_ZOOM;
          if( aSr.peekChar() == CHAR_ITEM_SEPARATOR ) {
            aSr.ensureSeparatorChar();
            expandToFit = aSr.readBoolean();
            if( aSr.peekChar() == CHAR_ITEM_SEPARATOR ) {
              aSr.ensureSeparatorChar();
              zoomFactor = aSr.readDouble();
            }
          }
          return new RectFitInfo( fitMode, expandToFit, zoomFactor );
        }

      };

  private final ERectFitMode fitMode;
  private final double       zoomFactor;
  private final boolean      expandToFit;

  /**
   * Constrictor.
   *
   * @param aFitMode {@link ERectFitMode} - fit mode
   * @param aIsExpandToFit boolean - specifies to expand small images to fit in viewport
   * @param aZoomFactor double - zoom factor for {@link ERectFitMode#ZOOMED} mode
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException zoom factor is out of range
   */
  public RectFitInfo( ERectFitMode aFitMode, boolean aIsExpandToFit, double aZoomFactor ) {
    TsNullArgumentRtException.checkNull( aFitMode );
    TsIllegalArgumentRtException.checkTrue( aZoomFactor < MIN_ZOOM_FACTOR || aZoomFactor > MAX_ZOOM_FACTOR );
    fitMode = aFitMode;
    expandToFit = aIsExpandToFit;
    zoomFactor = aZoomFactor;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the fit mode.
   *
   * @return {@link ERectFitMode} - fit mode
   */
  public ERectFitMode fitMode() {
    return fitMode;
  }

  /**
   * Determines if small objects are expanded to fit viewport.
   *
   * @return double - expand small images in {@link ERectFitMode#isAdaptiveScale()} modes
   */
  public boolean isExpandToFit() {
    return expandToFit;
  }

  /**
   * Returns the zoom factor for {@link ERectFitMode#ZOOMED} mode.
   * <p>
   * Original size (100%) is represented by value 1.0.
   *
   * @return doublt - the zoom factor in range {@link #MIN_ZOOM_FACTOR} .. {@link #MAX_ZOOM_FACTOR}
   */
  public double zoomFactor() {
    return zoomFactor;
  }

  /**
   * Calculates image size for this fit settings.
   *
   * @param aImgW int - image width in pixels
   * @param aImgH int - image height in pixels
   * @param aViewW int - viewport width in pixels
   * @param aViewH int - viewport height in pixels
   * @return {@link ITsPoint} - fitted size in pixels
   * @throws TsIllegalArgumentRtException any argument < 0
   */
  public ITsPoint calcSize( int aImgW, int aImgH, int aViewW, int aViewH ) {
    if( aImgW < 0 || aImgH < 0 || aViewW < 0 || aViewH < 0 ) {
      throw new TsIllegalArgumentRtException();
    }
    if( aImgW == 0 || aImgH == 0 || aViewW == 0 || aViewH == 0 ) {
      return ITsPoint.ZERO;
    }
    double imgAspect = ((double)aImgW) / ((double)aImgH);
    double viewAspect = ((double)aViewW) / ((double)aViewH);
    int w, h;
    switch( fitMode ) {
      case FIT_BOTH:
        if( imgAspect > viewAspect ) { // вместим в ширину
          w = aViewW;
          h = (int)(w / imgAspect);
        }
        else { // вместим в высоту
          h = aViewH;
          w = (int)(h * imgAspect);
        }
        break;
      case FIT_HEIGHT:
        h = aViewH;
        w = (int)(h * imgAspect);
        break;
      case FIT_WIDTH:
        w = aViewW;
        h = (int)(w / imgAspect);
        break;
      case NONE:
        w = aImgW;
        h = aImgH;
        break;
      case ZOOMED:
        w = (int)(aImgW * zoomFactor);
        h = (int)(aImgH * zoomFactor);
        if( w == 0 ) {
          w = 1;
        }
        if( h == 0 ) {
          h = 1;
        }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException( fitMode.id() );
    }
    if( fitMode.isAdaptiveScale() && !expandToFit ) { // IS adaptive scaling and NOT expanding to fit
      if( w > aImgW || h > aImgH ) {
        w = aImgW;
        h = aImgH;
      }
    }
    return new TsPoint( w, h );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder( "RectFitInfo(" ); //$NON-NLS-1$
    sb.append( fitMode.id() );
    sb.append( ',' );
    sb.append( zoomFactor );
    sb.append( ',' );
    sb.append( expandToFit );
    return sb.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof RectFitInfo that ) {
      return this.fitMode == that.fitMode && //
          (Double.compare( this.zoomFactor, that.zoomFactor ) == 0) && //
          this.expandToFit == that.expandToFit;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + fitMode.hashCode();
    long dblval = Double.doubleToRawLongBits( zoomFactor );
    result = TsLibUtils.PRIME * result + (int)(dblval ^ (dblval >>> 32));
    result = TsLibUtils.PRIME * result + (expandToFit ? 1 : 0);
    return result;
  }

}
