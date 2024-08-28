package org.toxsoft.core.tslib.bricks.geometry.impl;

import static org.toxsoft.core.tslib.bricks.geometry.l10n.ITsGeometruSharedResources.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsRectangle} immutable implementation.
 *
 * @author hazard157
 */
public final class TsRectangle
    implements ITsRectangle {

  private final ITsPoint a;
  private final ITsPoint b;
  private final ITsPoint size;
  private final ITsDims  dims;

  /**
   * Constructor from the any of two opposite corner points.
   *
   * @param aP1 {@link ITsPoint} - the first point
   * @param aP2 {@link ITsPoint} - the second point
   * @author goga
   */
  public TsRectangle( ITsPoint aP1, ITsPoint aP2 ) {
    TsNullArgumentRtException.checkNulls( aP1, aP2 );
    int x1 = Math.min( aP1.x(), aP2.x() );
    int y1 = Math.min( aP1.y(), aP2.y() );
    int x2 = Math.max( aP1.x(), aP2.x() );
    int y2 = Math.max( aP1.y(), aP2.y() );
    a = new TsPoint( x1, y1 );
    b = new TsPoint( x2, y2 );
    size = new TsPoint( x2 - x1 + 1, y2 - y1 + 1 );
    dims = new TsDims( size.x(), size.y() );
  }

  /**
   * Constructor.
   *
   * @param aX int - X coordinate of the rectangle top right corner
   * @param aY int - W coordinate of the rectangle top right corner
   * @param aWidth int - the width of the rectangle
   * @param aHeight int - the height of the rectangle
   * @throws TsValidationFailedRtException failed {@link #validateArgs(int, int, int, int)}
   */
  public TsRectangle( int aX, int aY, int aWidth, int aHeight ) {
    checkArgs( aX, aY, aWidth, aHeight );
    a = new TsPoint( aX, aY );
    b = new TsPoint( aX + aWidth - 1, aY + aHeight - 1 );
    size = new TsPoint( aWidth, aHeight );
    dims = new TsDims( size.x(), size.y() );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsRectangle} - the source
   */
  public TsRectangle( ITsRectangle aSource ) {
    this( aSource.a(), aSource.b() );
  }

  // ------------------------------------------------------------------------------------
  // ITsRectangle
  //

  @Override
  public ITsPoint a() {
    return a;
  }

  @Override
  public ITsPoint b() {
    return b;
  }

  @Override
  public int x1() {
    return a.x();
  }

  @Override
  public int y1() {
    return a.y();
  }

  @Override
  public int x2() {
    return b.x();
  }

  @Override
  public int y2() {
    return b.y();
  }

  @Override
  public int width() {
    return size.x();
  }

  @Override
  public int height() {
    return size.y();
  }

  @Override
  public ITsPoint size() {
    return size;
  }

  @Override
  public ITsDims dims() {
    return dims;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Checks the rectangle creation arguments for validity.
   * <p>
   * Checks that dimensions (width, height) are >= 1, and checks that rectangle botoom-right corner coordinates fits
   * inside {@link Integer} range.
   *
   * @param aX int - X coordinate of the rectangle top right corner
   * @param aY int - W coordinate of the rectangle top right corner
   * @param aWidth int - the width of the rectangle
   * @param aHeight int - the height of the rectangle
   * @return {@link ValidationResult} - the validation result
   */
  public static ValidationResult validateArgs( int aX, int aY, int aWidth, int aHeight ) {
    if( aWidth < 1 ) {
      return ValidationResult.error( MSG_ERR_INVALID_WIDTH );
    }
    if( aHeight < 1 ) {
      return ValidationResult.error( MSG_ERR_INVALID_HEIGHT );
    }
    long b_x = (long)aX + (long)aWidth;
    if( b_x > Integer.MAX_VALUE ) {
      return ValidationResult.error( MSG_ERR_RECT_B_X_TOO_BIG );
    }
    long b_y = (long)aY + (long)aHeight;
    if( b_y > Integer.MAX_VALUE ) {
      return ValidationResult.error( MSG_ERR_RECT_B_Y_TOO_BIG );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Checks and the rectangle creation arguments are not valid throws an exception.
   *
   * @param aX int - X coordinate of the rectangle top right corner
   * @param aY int - W coordinate of the rectangle top right corner
   * @param aWidth int - the width of the rectangle
   * @param aHeight int - the height of the rectangle
   * @throws TsValidationFailedRtException method {@link #validateArgs(int, int, int, int)} returned an error
   */
  public static void checkArgs( int aX, int aY, int aWidth, int aHeight ) {
    TsValidationFailedRtException.checkError( validateArgs( aX, aY, aWidth, aHeight ) );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return "[" + a().toString() + "," + size.x() + "," + size.y() + "]";
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof ITsRectangle that ) {
      return this.x1() == that.x1() && this.x2() == that.x2() && this.y1() == that.y1() && this.y2() == that.y2();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    // Note: an editable rectangle must have the same algorithm
    result = TsLibUtils.PRIME * result + x1();
    result = TsLibUtils.PRIME * result + x2();
    result = TsLibUtils.PRIME * result + y1();
    result = TsLibUtils.PRIME * result + y2();
    return result;
  }

}
