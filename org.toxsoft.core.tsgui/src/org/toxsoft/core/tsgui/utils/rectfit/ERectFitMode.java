package org.toxsoft.core.tsgui.utils.rectfit;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.l10n.ITsGuiSharedResources.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * Modes to fit an object into the rectangle.
 *
 * @author hazard157
 */
public enum ERectFitMode
    implements IStridable, IIconIdable {

  /**
   * No fitting (no adaptive size), the content size is defined by zoom factor.
   */
  FIT_NONE( "none", STR_ZOOM_FIT_NONE, STR_ZOOM_FIT_NONE, ICONID_ZOOM_FIT_NONE, false ) { //$NON-NLS-1$

    @Override
    public ITsDims doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {
      return new TsDims( aContentWidth, aContentHeight );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {
      return 1.0;
    }

    @Override
    protected boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return false;
    }

  },

  /**
   * Best fit - fit either width or height.
   */
  FIT_BEST( "best", STR_ZOOM_FIT_BEST, STR_ZOOM_FIT_BEST_D, ICONID_ZOOM_FIT_BEST, true ) { //$NON-NLS-1$

    @Override
    public ITsDims doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_BEST.{...}.doCalcFitSize()

      double vpAspect = ((double)aVpWidth) / ((double)aVpHeight);
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      if( contentAspect > vpAspect ) { // fit width
        return new TsDims( aVpWidth, (int)(aVpWidth / contentAspect) );
      }
      // fit height
      return new TsDims( (int)(aVpHeight * contentAspect), aVpHeight );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_BEST.{...}.doCalcFitZoom()

      double vpAspect = ((double)aVpWidth) / ((double)aVpHeight);
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      if( contentAspect > vpAspect ) { // fit width
        return ((double)aVpWidth) / ((double)aContentWidth);
      }
      // fit height
      return ((double)aVpHeight) / ((double)aContentHeight);
    }

    @Override
    protected boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return aVpWidth < aContentWidth || aVpHeight < aContentHeight;
    }
  },

  /**
   * Fit width.
   */
  FIT_WIDTH( "width", STR_ZOOM_FIT_WIDTH, STR_ZOOM_FIT_WIDTH_D, ICONID_ZOOM_FIT_WIDTH, true ) { //$NON-NLS-1$

    @Override
    public ITsDims doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_WIDTH.{...}.doCalcFitSize()

      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      return new TsDims( aVpWidth, (int)(aVpWidth / contentAspect) );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_WIDTH.{...}.doCalcFitZoom()

      return ((double)aVpWidth) / ((double)aContentWidth);
    }

    @Override
    protected boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return aVpWidth < aContentWidth;
    }
  },

  /**
   * Fit height.
   */
  FIT_HEIGHT( "height", STR_ZOOM_FIT_HEIGHT, STR_ZOOM_FIT_HEIGHT_D, ICONID_ZOOM_FIT_HEIGHT, true ) { //$NON-NLS-1$

    @Override
    public ITsDims doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_HEIGHT.{...}.doCalcFitSize()

      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      return new TsDims( (int)(aVpHeight * contentAspect), aVpHeight );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_HEIGHT.{...}.doCalcFitZoom()

      return ((double)aVpHeight) / ((double)aContentHeight);
    }

    @Override
    protected boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return aVpHeight < aContentHeight;
    }
  },

  /**
   * Fit to fill.
   */
  FIT_FILL( "fill", STR_ZOOM_FIT_FILL, STR_ZOOM_FIT_FILL_D, ICONID_ZOOM_FIT_FILL, true ) {//$NON-NLS-1$

    @Override
    public ITsDims doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_FILL.{...}.doCalcFitSize()

      double vpAspect = ((double)aVpWidth) / ((double)aVpHeight);
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      if( contentAspect > vpAspect ) { // fit height
        return new TsDims( (int)(aVpHeight * contentAspect), aVpHeight );
      }
      // fit width
      return new TsDims( aVpWidth, (int)(aVpWidth / contentAspect) );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
        boolean aExpandToFit ) {

      // TODO ERectFitMode.FIT_FILL.{...}.doCalcFitZoom()

      double vpAspect = ((double)aVpWidth) / ((double)aVpHeight);
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      if( contentAspect > vpAspect ) { // fit width
        return ((double)aVpHeight) / ((double)aContentHeight);
      }
      // fit height
      return ((double)aVpWidth) / ((double)aContentWidth);
    }

    @Override
    protected boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return aVpWidth < aContentWidth || aVpHeight < aContentHeight;
    }
  };

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ERectFitMode"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ERectFitMode> KEEPER = new StridableEnumKeeper<>( ERectFitMode.class );

  private static IStridablesListEdit<ERectFitMode> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final String  iconId;
  private final boolean vpSizeDependent;

  ERectFitMode( String aId, String aName, String aDescription, String aIconId, boolean aIsVpSizeDependent ) {
    id = aId;
    name = aName;
    description = aDescription;
    iconId = aIconId;
    vpSizeDependent = aIsVpSizeDependent;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if scaling (displayed size) depends on viewport size.
   *
   * @return boolean - <code>true</code> if object size adapts to viewport size
   */
  public boolean isAdaptiveScale() {
    return vpSizeDependent;
  }

  /**
   * Determines if scaling is needed.
   * <p>
   * When scaling is needed fitted content size may be calculated by
   * {@link #doCalcFitSize(int, int, int, int, boolean)}. Otherwise content size remains unchanged.
   *
   * @param aVpWidth int - viewport width
   * @param aVpHeight int - viewport height
   * @param aContentWidth int - content width
   * @param aContentHeight int - content height
   * @param aExpandToFit boolean - expand small images in {@link #isAdaptiveScale()} modes
   * @return boolean - <code>true</code> content size changes to fit in viewport
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public boolean isScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
      boolean aExpandToFit ) {
    TsIllegalArgumentRtException.checkTrue( aVpWidth < 1 || aVpHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentWidth < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentHeight < 1 );
    if( aExpandToFit ) {
      return true;
    }
    return doIsScalingNeeded( aVpWidth, aVpHeight, aContentWidth, aContentHeight );
  }

  /**
   * Determines if scaling is needed.
   * <p>
   * Is the same as {@link #isScalingNeeded(int, int, int, int, boolean)} with other argument types.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport coordinates
   * @param aContentSize {@link ID2Size} - the content size
   * @param aExpandToFit boolean - expand small images in {@link #isAdaptiveScale()} modes
   * @return boolean - <code>true</code> content size changes to fit in viewport
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public boolean isScalingNeeded( ITsRectangle aVpRect, ID2Size aContentSize, boolean aExpandToFit ) {
    TsNullArgumentRtException.checkNulls( aVpRect, aContentSize );
    TsIllegalArgumentRtException.checkTrue( aVpRect.width() < 1 || aVpRect.height() < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intW() <= 0 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intH() <= 0 );
    if( aExpandToFit ) {
      return true;
    }
    return doIsScalingNeeded( aVpRect.width(), aVpRect.height(), aContentSize.intW(), aContentSize.intH() );
  }

  /**
   * Calculates size of the content fitted into the viewport retaining the aspect ratio of the content.
   * <p>
   * The same as calling {@link #calcFitSize(int, int, int, int, boolean)} with <code>aExpandToFit</code> =
   * <code>false</code>.
   *
   * @param aVpWidth int - viewport width
   * @param aVpHeight int - viewport height
   * @param aContentWidth int - content width
   * @param aContentHeight int - content height
   * @return {@link ITsDims} - fitted rectangle size
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public ITsDims calcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
    return calcFitSize( aVpWidth, aVpHeight, aContentWidth, aContentHeight, false );
  }

  /**
   * Calculates size of the content fitted into the viewport retaining the aspect ratio of the content.
   *
   * @param aVpWidth int - viewport width
   * @param aVpHeight int - viewport height
   * @param aContentWidth int - content width
   * @param aContentHeight int - content height
   * @param aExpandToFit boolean - expand small images in {@link #isAdaptiveScale()} modes
   * @return {@link ITsDims} - fitted rectangle size
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public ITsDims calcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
      boolean aExpandToFit ) {
    TsIllegalArgumentRtException.checkTrue( aVpWidth < 1 || aVpHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( D2Utils.compareDoubles( aContentWidth, 0.0 ) <= 0 );
    TsIllegalArgumentRtException.checkTrue( D2Utils.compareDoubles( aContentHeight, 0.0 ) <= 0 );
    return doCalcFitSize( aVpWidth, aVpHeight, aContentWidth, aContentHeight, aExpandToFit );
  }

  /**
   * Calculates size of the content fitted into the viewport retaining the aspect ratio of the content.
   * <p>
   * The same as calling {@link #calcFitSize(ITsRectangle, ID2Size, boolean)} with <code>aExpandToFit</code> =
   * <code>false</code>.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport coordinates
   * @param aContentSize {@link ID2Size} - the content size
   * @return {@link ITsDims} - fitted rectangle size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public ITsDims calcFitSize( ITsRectangle aVpRect, ID2Size aContentSize ) {
    return calcFitSize( aVpRect, aContentSize, false );
  }

  /**
   * Calculates size of the content fitted into the viewport retaining the aspect ratio of the content.
   * <p>
   * Is the same as {@link #calcFitSize(int, int, int, int)} with other argument types.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport coordinates
   * @param aContentSize {@link ID2Size} - the content size
   * @param aExpandToFit boolean - expand small images in {@link #isAdaptiveScale()} modes
   * @return {@link ITsDims} - fitted rectangle size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public ITsDims calcFitSize( ITsRectangle aVpRect, ID2Size aContentSize, boolean aExpandToFit ) {
    TsNullArgumentRtException.checkNulls( aVpRect, aContentSize );
    TsIllegalArgumentRtException.checkTrue( aVpRect.width() < 1 || aVpRect.height() < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intW() <= 0 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intH() <= 0 );
    return doCalcFitSize( aVpRect.width(), aVpRect.height(), aContentSize.intW(), aContentSize.intH(), aExpandToFit );
  }

  /**
   * Calculates zoom factor to fit the content into the viewport retaining the aspect ratio of the content.
   * <p>
   * The same as calling {@link #calcFitZoom(ITsRectangle, ID2Size, boolean)} with <code>aExpandToFit</code> =
   * <code>false</code>.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport coordinates
   * @param aContentSize {@link ID2Size} - the content size
   * @return {@link ITsPoint} - fitted rectangle size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public double calcFitZoom( ITsRectangle aVpRect, ID2Size aContentSize ) {
    return calcFitZoom( aVpRect, aContentSize, false );
  }

  /**
   * Calculates zoom factor to fit the content into the viewport retaining the aspect ratio of the content.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport coordinates
   * @param aContentSize {@link ID2Size} - the content size
   * @param aExpandToFit boolean - expand small images in {@link #isAdaptiveScale()} modes
   * @return {@link ITsPoint} - fitted rectangle size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public double calcFitZoom( ITsRectangle aVpRect, ID2Size aContentSize, boolean aExpandToFit ) {
    TsNullArgumentRtException.checkNulls( aVpRect, aContentSize );
    TsIllegalArgumentRtException.checkTrue( aVpRect.width() < 1 || aVpRect.height() < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intW() <= 0 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intH() <= 0 );
    return doCalcFitZoom( aVpRect.width(), aVpRect.height(), aContentSize.intW(), aContentSize.intH(), aExpandToFit );
  }

  /**
   * Calculates zoom factor to fit the content into the viewport retaining the aspect ratio of the content.
   *
   * @param aVpWidth int - viewport width
   * @param aVpHeight int - viewport height
   * @param aContentWidth double - content width
   * @param aContentHeight double - content height
   * @param aExpandToFit boolean - expand small images in {@link #isAdaptiveScale()} modes
   * @return {@link ITsPoint} - fitted rectangle size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public double calcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
      boolean aExpandToFit ) {
    TsIllegalArgumentRtException.checkTrue( aVpWidth < 1 || aVpHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentWidth < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentHeight < 1 );
    return doCalcFitZoom( aVpWidth, aVpHeight, aContentWidth, aContentHeight, aExpandToFit );
  }

  protected abstract ITsDims doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
      boolean aExpandToFit );

  protected abstract double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight,
      boolean aExpandToFit );

  protected abstract boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight );

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ERectFitMode} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ERectFitMode> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ERectFitMode} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ERectFitMode getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ERectFitMode} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ERectFitMode findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ERectFitMode item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ERectFitMode} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ERectFitMode getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
