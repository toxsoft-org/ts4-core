package org.toxsoft.core.tsgui.utils.margins;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility and helper methods of margins usage.
 *
 * @author hazard157
 */
public class TsMarginUtils {

  /**
   * Creates the rectangle with the margins applied to the source rectangle.
   * <p>
   * Is margins are too big, minimal dimensions of the resulting rectangle is determined by <code>aMinDims</code>, If
   * any dimension in <code>aMinDims</code> is greater than source rectangle dimension, the source rectangle dimension
   * will be used.
   * <p>
   * The positive margins will shrink rectangle, negative margin will grow in the respective direction.
   *
   * @param aSource {@link ITsRectangle} - the source rectangle
   * @param aMargins {@link ITsMargins} - the margins to apply
   * @param aMinDims {@link ITsDims} - minimal dimensions of the resulting rectangle
   * @return {@link ITsRectangle} - the rectangle with the margins applied
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final ITsRectangle applyMargins( ITsRectangle aSource, ITsMargins aMargins, ITsDims aMinDims ) {
    TsNullArgumentRtException.checkNulls( aSource, aMargins, aMinDims );
    // prepare width/height for checking
    int left = aMargins.left();
    int right = aMargins.right();
    int top = aMargins.top();
    int bottom = aMargins.bottom();
    int newWidth = aSource.width() - left - right;
    int newHeight = aSource.height() - top - bottom;
    int minWidth = Math.min( aMinDims.width(), aSource.width() );
    int minHeight = Math.min( aMinDims.height(), aSource.height() );
    // check width/height and proportionally correct margins to apply
    if( newWidth < minWidth ) {
      int deltaW = minWidth - newWidth;
      if( left == 0 ) {
        right -= deltaW;
      }
      else {
        if( right == 0 ) {
          left -= deltaW;
        }
        else {
          double k = left / right;
          int deltaL = (int)(deltaW / (1.0 + k));
          left -= deltaL;
          right -= deltaW - deltaL;
        }
      }
    }
    if( newHeight < minHeight ) {
      int deltaH = minHeight - newHeight;
      if( top == 0 ) {
        bottom -= deltaH;
      }
      else {
        if( bottom == 0 ) {
          top -= deltaH;
        }
        else {
          double k = bottom / top;
          int deltaT = (int)(deltaH / (1.0 + k));
          top -= deltaT;
          bottom -= deltaH - deltaT;
        }
      }
    }
    int x1 = aSource.x1() + left;
    int y1 = aSource.y1() + top;
    int x2 = aSource.x2() - right;
    int y2 = aSource.y2() - bottom;
    return new TsRectangle( new TsPoint( x1, y1 ), new TsPoint( x2, y2 ) );
  }

  /**
   * Creates the rectangle with the margins applied to the source rectangle.
   * <p>
   * Is the same as calling {@link #applyMargins(ITsRectangle, ITsMargins)} with minimal dimensions assumed as 1/3 of
   * the source rectangle respective dimension.
   *
   * @param aSource {@link ITsRectangle} - the source rectangle
   * @param aMargins {@link ITsMargins} - the margins to apply
   * @return {@link ITsRectangle} - the rectangle with the margins applied
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final ITsRectangle applyMargins( ITsRectangle aSource, ITsMargins aMargins ) {
    TsNullArgumentRtException.checkNulls( aSource, aMargins );
    int minWidth = Math.max( 1, aSource.width() / 3 );
    int minHeight = Math.max( 1, aSource.height() / 3 );
    return applyMargins( aSource, aMargins, new TsDims( minWidth, minHeight ) );
  }

  /**
   * No subclasses.
   */
  private TsMarginUtils() {
    // nop
  }

}
