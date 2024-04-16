package org.toxsoft.core.tsgui.graphics.vpcalc;

import static org.toxsoft.core.tsgui.graphics.vpcalc.ITsResources.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * How the content placement will be limited by the viewport bounds.
 *
 * @author hazard157
 */
public enum EVpBoundingStrategy
    implements IStridable {

  /**
   * Content may be located anywhere even if it is not visible through the viewport.
   */
  NONE( "none", STR_BS_NONE, STR_BS_NONE_D, false ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
        ITsPoint aMargins ) {
      return aOrigin;
    }
  },

  /**
   * Content edge location is limited by the respective edge of the viewport.
   */
  VIEWPORT( "none", STR_BS_VIEWPORT, STR_BS_VIEWPORT_D, true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
        ITsPoint aMargins ) {
      // check X
      int x = (int)aOrigin.x();
      IntRange horRange = getHorRange( aVpRect, aMargins );
      if( horRange.isLeft( x ) ) {
        x = horRange.minValue();
      }
      else {
        if( horRange.isRight( x + aContentSize.x() ) ) {
          x = horRange.maxValue() - aContentSize.x();
        }
      }
      // check Y
      int y = (int)aOrigin.y();
      IntRange verRange = getVerRange( aVpRect, aMargins );
      if( verRange.isLeft( y ) ) {
        y = verRange.minValue();
      }
      else {
        if( verRange.isRight( y + aContentSize.y() ) ) {
          y = verRange.maxValue() - aContentSize.y();
        }
      }
      if( x == aOrigin.x() && y == aOrigin.y() ) {
        return aOrigin;
      }
      return new D2Point( x, y );
    }
  },

  /**
   * Content edge location is limited by the opposite edge of the viewport.
   */
  CONTENT( "none", STR_BS_CONTENT, STR_BS_CONTENT_D, true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
        ITsPoint aMargins ) {
      int x = (int)aOrigin.x();
      // IntRange horRange = getVerRange( aVpRect, aMargins );
      IntRange horRange = getHorRange( aVpRect, aMargins );
      if( horRange.isLeft( x + aContentSize.x() ) ) {
        x = horRange.minValue() - aContentSize.x();
      }
      else {
        if( horRange.isRight( x ) ) {
          x = horRange.maxValue();
        }
      }
      // check Y
      int y = (int)aOrigin.y();
      IntRange verRange = getVerRange( aVpRect, aMargins );
      if( verRange.isLeft( y + aContentSize.y() ) ) {
        y = verRange.minValue() - aContentSize.y();
      }
      else {
        if( verRange.isRight( y ) ) {
          y = verRange.maxValue();
        }
      }
      if( x == aOrigin.x() && y == aOrigin.y() ) {
        return aOrigin;
      }
      return new D2Point( x, y );
    }
  };

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EVpBoundingStrategy"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EVpBoundingStrategy> KEEPER =
      new StridableEnumKeeper<>( EVpBoundingStrategy.class );

  private static IStridablesListEdit<EVpBoundingStrategy> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean bounded;

  EVpBoundingStrategy( String aId, String aName, String aDescription, boolean aBounded ) {
    id = aId;
    name = aName;
    description = aDescription;
    bounded = aBounded;
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
  // implementation
  //

  private static IntRange getHorRange( ITsRectangle aVpRect, ITsPoint aMargins ) {
    if( aMargins.x() >= aVpRect.width() / 2 ) { // for small viewport do NOT apply margins
      return new IntRange( aVpRect.x1(), aVpRect.x2() );
    }
    return new IntRange( aVpRect.x1() + aMargins.x(), aVpRect.x2() - aMargins.y() );
  }

  private static IntRange getVerRange( ITsRectangle aVpRect, ITsPoint aMargins ) {
    if( aMargins.y() >= aVpRect.height() / 2 ) { // for small viewport do NOT apply margins
      return new IntRange( aVpRect.y1(), aVpRect.y2() );
    }
    return new IntRange( aVpRect.y1() + aMargins.y(), aVpRect.y2() - aMargins.y() );
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if any kind of bounding has to be applied.
   *
   * @return boolean - <code>true</code> content is bounded, <code>false</code> - no limits
   */
  public boolean isBounded() {
    return bounded;
  }

  /**
   * Calculates the bounded origin of the content for current strategy.
   *
   * @param aOrigin {@link ID2Point} - the requested origin
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentSize {@link ITsPoint} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated origin to be applied
   */
  public ID2Point calcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
    TsNullArgumentRtException.checkNulls( aOrigin, aVpRect, aContentSize, aMargins );
    return doCalcOrigin( aOrigin, aVpRect, aContentSize, aMargins );
  }

  protected abstract ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
      ITsPoint aMargins );

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EVpBoundingStrategy} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EVpBoundingStrategy> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EVpBoundingStrategy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EVpBoundingStrategy getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVpBoundingStrategy} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EVpBoundingStrategy findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVpBoundingStrategy item : values() ) {
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
   * @return {@link EVpBoundingStrategy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EVpBoundingStrategy getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
