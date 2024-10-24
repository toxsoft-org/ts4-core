package org.toxsoft.core.tsgui.graphics.vpcalc2;

import static org.toxsoft.core.tsgui.graphics.vpcalc2.l10n.IVpCalcSharedResources.*;

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
 * How the content placement will be limited by the viewport bounds.
 *
 * @author hazard157, vs
 */
public enum EVpBoundingStrategy
    implements IStridable {

  /**
   * Content may be located anywhere even if it is not visible through the viewport.
   */
  NONE( "none", STR_BS_NONE, STR_BS_NONE_D, false ) { //$NON-NLS-1$

    private static final int          MAX_VIRT_VP_DIM = 1024 * 1024 * 1024;
    private static final ITsRectangle VIRT_VP_UNBOUND =
        new TsRectangle( -MAX_VIRT_VP_DIM / 2, -MAX_VIRT_VP_DIM / 2, MAX_VIRT_VP_DIM, MAX_VIRT_VP_DIM );

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
        ITsPoint aMargins ) {
      return aOrigin;
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      TsRectangle r =
          new TsRectangle( -aContentRect.x1(), -aContentRect.y1(), aContentRect.width(), aContentRect.height() );
      return TsGeometryUtils.union( aVpRect, r ).size();
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      ITsRectangle r = TsGeometryUtils.union( aVpRect, aContentRect );
      return new TsPoint( aContentRect.x1() - r.x1(), aContentRect.y1() - r.y1() );
    }

    @Override
    protected ITsRectangle doCalcVirtualViewport( ITsRectangle aVpRect, ITsDims aContentSize ) {
      return VIRT_VP_UNBOUND;
    }

  },

  /**
   * Content edge location is limited by the respective edge of the viewport.
   */
  VIEWPORT( "none", STR_BS_VIEWPORT, STR_BS_VIEWPORT_D, true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
        ITsPoint aMargins ) {
      // adjust X
      ITsPoint us = calcUnderLayingSize( aVpRect, aContentRect, aMargins );
      ITsPoint shift = calcContentShift( aVpRect, aContentRect, aMargins );

      int x = (int)aOrigin.x();
      int y = (int)aOrigin.y();
      int maxX = us.x() - aVpRect.width();
      int maxY = us.y() - aVpRect.height();

      if( x + shift.x() < 0 ) {
        x = -shift.x();
      }
      if( x + shift.x() > maxX ) {
        x = maxX - shift.x();
      }

      if( y + shift.y() < 0 ) {
        y = -shift.y();
      }
      if( y + shift.y() > maxY ) {
        y = maxY - shift.y();
      }
      return new D2Point( x, y );
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int width = aContentRect.width();
      // содержимое меньше окна просмотра по ширине
      if( aContentRect.width() < aVpRect.width() ) {
        int dx = aVpRect.width() - aContentRect.width();
        width = aVpRect.width() + dx;
      }
      int height = aContentRect.height();
      // содержимое меньше окна просмотра по высоте
      if( aContentRect.height() < aVpRect.height() ) {
        int dy = aVpRect.height() - aContentRect.height();
        height = aVpRect.height() + dy;
      }
      return new TsPoint( width, height );
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int dx = 0;
      int dy = 0;
      // содержимое меньше окна просмотра по ширине
      if( aContentRect.width() < aVpRect.width() ) {
        dx = aVpRect.width() - aContentRect.width() - aMargins.x();
      }
      // содержимое меньше окна просмотра по высоте
      if( aContentRect.height() < aVpRect.height() ) {
        dy = aVpRect.height() - aContentRect.height() - aMargins.y();
      }
      return new TsPoint( dx, dy );
    }

    @Override
    protected ITsRectangle doCalcVirtualViewport( ITsRectangle aVpRect, ITsDims aContentSize ) {
      int deltaW = aContentSize.width() - aVpRect.width();
      int deltaH = aContentSize.height() - aVpRect.height();
      // if content size is smaller than viewport then return viewport as a virtual one
      if( deltaW <= 0 && deltaH <= 0 ) {
        return aVpRect;
      }
      // prepare virtual viewport coordinates to enlarge in respective direction
      int x = aVpRect.x1();
      int y = aVpRect.y1();
      int w = aVpRect.width();
      int h = aVpRect.height();
      if( deltaW > 0 || deltaH > 0 ) {
        if( deltaW > 0 ) { // content is wider than viewport, adjust x and w
          w += 2 * deltaW;
          x -= deltaW;
        }
        if( aContentSize.height() > aVpRect.height() ) { // content is higher than viewport, adjust y and h
          h += 2 * deltaH;
          y -= deltaH;
        }
      }
      return new TsRectangle( x, y, w, h );
    }

  },

  /**
   * Content edge location is limited by the opposite edge of the viewport.
   */
  CONTENT( "none", STR_BS_CONTENT, STR_BS_CONTENT_D, true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
        ITsPoint aMargins ) {
      ITsPoint us = calcUnderLayingSize( aVpRect, aContentRect, aMargins );
      ITsPoint shift = calcContentShift( aVpRect, aContentRect, aMargins );

      int x = (int)aOrigin.x();
      int y = (int)aOrigin.y();
      int maxX = us.x() - aVpRect.width();
      int maxY = us.y() - aVpRect.height();

      if( x + shift.x() < 0 ) {
        x = -shift.x();
      }
      if( x + shift.x() > maxX ) {
        x = maxX - shift.x();
      }

      if( y + shift.y() < 0 ) {
        y = -shift.y();
      }
      if( y + shift.y() > maxY ) {
        y = maxY - shift.y();
      }
      return new D2Point( x, y );
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int width = aContentRect.width();
      // содержимое меньше окна просмотра по ширине
      if( aContentRect.width() < aVpRect.width() ) {
        width = 2 * aVpRect.width() + aContentRect.width() - 32;
      }
      int height = aContentRect.height();
      // содержимое меньше окна просмотра по высоте
      if( aContentRect.height() < aVpRect.height() ) {
        height = 2 * aVpRect.height() + aContentRect.height() - 32;
      }
      return new TsPoint( width, height );
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int dx = 0;
      // содержимое меньше окна просмотра по ширине
      if( aContentRect.width() < aVpRect.width() ) {
        dx = aVpRect.width() - aMargins.x() - 16;
      }
      int dy = 0;
      // содержимое меньше окна просмотра по высоте
      if( aContentRect.height() < aVpRect.height() ) {
        dy = aVpRect.height() - aMargins.y() - 16;
      }
      return new TsPoint( dx, dy );
    }

    @Override
    protected ITsRectangle doCalcVirtualViewport( ITsRectangle aVpRect, ITsDims aContentSize ) {
      int cW = aContentSize.width();
      int cH = aContentSize.height();
      int vpW = aVpRect.width();
      int vpH = aVpRect.height();
      int x, y, w, h;
      if( cW > vpW ) {
        x = aVpRect.x2() - cW;
        w = 2 * cW - aVpRect.width();
      }
      else {
        x = aVpRect.x1();
        w = vpW;
      }
      if( cH > vpH ) {
        y = aVpRect.y2() - cH;
        h = 2 * cH - aVpRect.height();
      }
      else {
        y = aVpRect.y1();
        h = vpH;
      }
      return new TsRectangle( x, y, w, h );
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
   * @param aContentRect {@link ITsRectangle} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated origin to be applied
   */
  public ID2Point calcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
    TsNullArgumentRtException.checkNulls( aOrigin, aVpRect, aContentRect, aMargins );
    return doCalcOrigin( aOrigin, aVpRect, aContentRect, aMargins );
  }

  /**
   * Вычисляет размеры прокручиваемой подложки.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentRect {@link ITsRectangle} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated size
   */
  public ITsPoint calcUnderLayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
    return doCalcUnderlayingSize( aVpRect, aContentRect, aMargins );
  }

  /**
   * Вычисляет размеры прокручиваемой подложки.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentRect {@link ITsRectangle} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated size
   */
  public ITsPoint calcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
    return doCalcContentShift( aVpRect, aContentRect, aMargins );
  }

  /**
   * Returns the "virtual" viewport rectangle.
   * <p>
   * Virtual viewport is a rectangle limiting (bounding) content placement. Content meets this bounding strategy
   * requirements if and only if it is contained in the virtual viewport rectangle.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentSize {@link ITsDims} - size of the content rectangle
   * @return {@link ITsRectangle} - the virtual viewport
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ITsRectangle calcVirtualViewport( ITsRectangle aVpRect, ITsDims aContentSize ) {
    TsNullArgumentRtException.checkNulls( aVpRect, aContentSize );
    return doCalcVirtualViewport( aVpRect, aContentSize );
  }

  protected abstract ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
      ITsPoint aMargins );

  protected abstract ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentSize,
      ITsPoint aMargins );

  protected abstract ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentSize, ITsPoint aMargins );

  protected abstract ITsRectangle doCalcVirtualViewport( ITsRectangle aVpRect, ITsDims aContentSize );

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
