package org.toxsoft.core.tsgui.utils.rectfit;

import static org.toxsoft.core.tsgui.utils.rectfit.ITsResources.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Modes to fit an object into the rectangle.
 *
 * @author hazard157
 */
public enum ERectFitMode
    implements IStridable {

  // TODO add the icon ID

  /**
   * Original size.
   */
  NONE( "none", STR_N_FM_NONE, STR_D_FM_NONE, false ) { //$NON-NLS-1$

    @Override
    public ITsPoint doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return new TsPoint( aContentWidth, aContentHeight );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return 1.0;
    }

    @Override
    protected boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return false;
    }

  },

  /**
   * Fit either width or height.
   */
  FIT_BOTH( "both", STR_N_FM_FIT_BOTH, STR_D_FM_FIT_BOTH, true ) { //$NON-NLS-1$

    @Override
    public ITsPoint doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      double vpAspect = ((double)aVpWidth) / ((double)aVpHeight);
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      if( contentAspect > vpAspect ) { // fit width
        return new TsPoint( aVpWidth, (int)(aVpWidth / contentAspect) );
      }
      // fit height
      return new TsPoint( (int)(aVpHeight * contentAspect), aVpHeight );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
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
  FIT_WIDTH( "width", STR_N_FM_FIT_WIDTH, STR_D_FM_FIT_WIDTH, true ) { //$NON-NLS-1$

    @Override
    public ITsPoint doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      return new TsPoint( aVpWidth, (int)(aVpWidth / contentAspect) );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
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
  FIT_HEIGHT( "height", STR_N_FM_FIT_HEIGHT, STR_D_FM_FIT_HEIGHT, true ) { //$NON-NLS-1$

    @Override
    public ITsPoint doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      return new TsPoint( (int)(aVpHeight * contentAspect), aVpHeight );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
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
  FIT_FILL( "fill", STR_N_FM_FIT_FILL, STR_D_FM_FIT_FILL, true ) {//$NON-NLS-1$

    @Override
    public ITsPoint doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      double vpAspect = ((double)aVpWidth) / ((double)aVpHeight);
      double contentAspect = ((double)aContentWidth) / ((double)aContentHeight);
      if( contentAspect > vpAspect ) { // fit height
        return new TsPoint( (int)(aVpHeight * contentAspect), aVpHeight );
      }
      // fit width
      return new TsPoint( aVpWidth, (int)(aVpWidth / contentAspect) );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
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
  },

  /**
   * Zoom - show with the specified scaling factor.
   */
  ZOOMED( "zoomed", STR_N_FM_ZOOMED, STR_D_FM_ZOOMED, false ) { //$NON-NLS-1$

    @Override
    public ITsPoint doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return new TsPoint( aContentWidth, aContentHeight );
    }

    @Override
    protected double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return 1.0; // value not used for this fit mode
    }

    @Override
    protected boolean doIsScalingNeeded( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
      return false;
    }
  }

  ;

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
  private final boolean vpSizeDependent;

  ERectFitMode( String aId, String aName, String aDescription, boolean aIsVpSizeDependent ) {
    id = aId;
    name = aName;
    description = aDescription;
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
   * When scaling is needed fitted content size may be calculated by {@link #doCalcFitSize(int, int, int, int)}.
   * Otherwise content size remains unchanged.
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
   *
   * @param aVpWidth int - viewport width
   * @param aVpHeight int - viewport height
   * @param aContentWidth int - content width
   * @param aContentHeight int - content height
   * @return {@link ITsPoint} - fitted rectangle size
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public ITsPoint calcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight ) {
    TsIllegalArgumentRtException.checkTrue( aVpWidth < 1 || aVpHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( D2Utils.compareDoubles( aContentWidth, 0.0 ) <= 0 );
    TsIllegalArgumentRtException.checkTrue( D2Utils.compareDoubles( aContentHeight, 0.0 ) <= 0 );
    return doCalcFitSize( aVpWidth, aVpHeight, aContentWidth, aContentHeight );
  }

  /**
   * Calculates size of the content fitted into the viewport retaining the aspect ratio of the content.
   * <p>
   * Is the same as {@link #isScalingNeeded(int, int, int, int, boolean)} with other argument types.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport coordinates
   * @param aContentSize {@link ID2Size} - the content size
   * @return {@link ITsPoint} - fitted rectangle size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public ITsPoint calcFitSize( ITsRectangle aVpRect, ID2Size aContentSize ) {
    TsNullArgumentRtException.checkNulls( aVpRect, aContentSize );
    TsIllegalArgumentRtException.checkTrue( aVpRect.width() < 1 || aVpRect.height() < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intW() <= 0 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intH() <= 0 );
    return doCalcFitSize( aVpRect.width(), aVpRect.height(), aContentSize.intW(), aContentSize.intH() );
  }

  /**
   * Calculates zoom factor to fit the content into the viewport retaining the aspect ratio of the content.
   * <p>
   * Is the same as {@link #isScalingNeeded(int, int, int, int, boolean)} with other argument types.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport coordinates
   * @param aContentSize {@link ID2Size} - the content size
   * @return {@link ITsPoint} - fitted rectangle size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument < 1
   */
  public double calcFitZoom( ITsRectangle aVpRect, ID2Size aContentSize ) {
    TsNullArgumentRtException.checkNulls( aVpRect, aContentSize );
    TsIllegalArgumentRtException.checkTrue( aVpRect.width() < 1 || aVpRect.height() < 1 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intW() <= 0 );
    TsIllegalArgumentRtException.checkTrue( aContentSize.intH() <= 0 );
    return doCalcFitZoom( aVpRect.width(), aVpRect.height(), aContentSize.intW(), aContentSize.intH() );
  }

  protected abstract ITsPoint doCalcFitSize( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight );

  protected abstract double doCalcFitZoom( int aVpWidth, int aVpHeight, int aContentWidth, int aContentHeight );

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
