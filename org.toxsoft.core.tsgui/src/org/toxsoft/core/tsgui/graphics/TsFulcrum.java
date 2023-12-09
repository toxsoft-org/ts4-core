package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The fulcrum of any shape is defined as the point, relative to the shape's bounding rectangle.
 * <p>
 * Relative coordinates of the point are defined in percents, where point (0.0,0.0) is top-left corner and (100.0,100.0)
 * is the bottom-right corner of the rectangle. Coordinates may be both negative and exceed 100.0. The enum
 * {@link ETsFulcrum} lists the 9 most commonly used fulcrums.
 * <p>
 * The implementation may limit the precision of coordinate storage to three decimal places.
 * {@link D2Utils#duck(double)} is used for coordinates processing.
 *
 * @author hazard157
 */
public final class TsFulcrum {

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "TsFulcrum"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsFulcrum> KEEPER =
      new AbstractEntityKeeper<>( TsFulcrum.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsFulcrum aEntity ) {
          if( aEntity.fulcrum != null ) {
            aSw.writeAsIs( aEntity.fulcrum.id() );
          }
          else {
            aSw.writeDouble( aEntity.xPerc, 3 );
            aSw.writeSeparatorChar();
            aSw.writeDouble( aEntity.yPerc, 3 );
          }
        }

        @Override
        protected TsFulcrum doRead( IStrioReader aSr ) {
          if( StridUtils.isIdStart( aSr.peekChar() ) ) {
            ETsFulcrum ef = ETsFulcrum.getById( aSr.readIdPath() );
            return predefinedFulcrums.getByKey( ef );
          }
          double x = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double y = aSr.readDouble();
          return TsFulcrum.of( x, y );
        }
      };

  private static final IMap<ETsFulcrum, TsFulcrum> predefinedFulcrums;

  private final double     xPerc;
  private final double     yPerc;
  private final ETsFulcrum fulcrum;

  static {
    IMapEdit<ETsFulcrum, TsFulcrum> map = new ElemMap<>();
    for( ETsFulcrum ef : ETsFulcrum.asList() ) {
      TsFulcrum f = new TsFulcrum( ef.getHorPercentage(), ef.getVerPercentage(), ef );
      map.put( ef, f );
    }
    predefinedFulcrums = map;
  }

  private TsFulcrum( double aXPerc, double aYPerc, ETsFulcrum aFulcrum ) {
    xPerc = aXPerc;
    yPerc = aYPerc;
    fulcrum = aFulcrum;
  }

  // ------------------------------------------------------------------------------------
  // Static constructors
  //

  /**
   * Returns the {@link TsFulcrum} of the specified coordinates.
   * <p>
   * If coordinates corresponds to one of the predefined 9 {@link ETsFulcrum} points, returns the same instance as
   * {@link #of(ETsFulcrum)}.
   *
   * @param aXPerc double - X coordinate in percents (0.0 = left edge, 100.0 - right edge of the rectangle)
   * @param aYPerc double - Y coordinate in percents (0.0 = topedge, 100.0 - bottom edge of the rectangle)
   * @return {@link TsFulcrum} - created instance
   * @throws TsIllegalArgumentRtException argument is out of <code>long</code> range
   */
  public static TsFulcrum of( double aXPerc, double aYPerc ) {
    double x = D2Utils.duck( aXPerc );
    double y = D2Utils.duck( aYPerc );
    for( ETsFulcrum ef : ETsFulcrum.asList() ) {
      if( ef.getHorPercentage() == x && ef.getVerPercentage() == y ) {
        return predefinedFulcrums.getByKey( ef );
      }
    }
    return new TsFulcrum( x, y, null );
  }

  /**
   * Returns the {@link TsFulcrum} corresponding to the specified enum constant.
   *
   * @param aFulcrumEnum {@link ETsFulcrum} - the predefined fulcrum point
   * @return {@link TsFulcrum} - corresponding instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsFulcrum of( ETsFulcrum aFulcrumEnum ) {
    return predefinedFulcrums.getByKey( aFulcrumEnum );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns fulcrum point X coordinate relative to the top-left corner of the rectangle.
   *
   * @return double - X coordinate in percents (0.0 = left edge, 100.0 - right edge of the rectangle)
   */
  public double xPerc() {
    return xPerc;
  }

  /**
   * Returns fulcrum point Y coordinate relative to the top-left corner of the rectangle.
   *
   * @return double - Y coordinate in percents (0.0 = topedge, 100.0 - bottom edge of the rectangle)
   */
  public double yPerc() {
    return yPerc;
  }

  /**
   * Returns the {@link ETsFulcrum} representation of this point, if applicable.
   *
   * @return {@link ETsFulcrum} - one of the9 predefined fulcrums or <code>null</code> if not applicable
   */
  public ETsFulcrum fulcrum() {
    return fulcrum;
  }

  /**
   * Calculates the coordinates of a rectangle of the specified width and height at the specified fulcrum point.
   *
   * @param aFulcrumX int - X coordinate of the fulcrum point
   * @param aFulcrumY int - Y coordinate of the fulcrum point
   * @param aWidth int - the rectangle width
   * @param aHeight int - the rectangle height
   * @return {@link ITsRectangle} - calculated rectangle
   * @throws TsIllegalArgumentRtException width or height < 0
   */
  public ITsRectangle calcRect( int aFulcrumX, int aFulcrumY, int aWidth, int aHeight ) {
    return new TsRectangle( calcSegmentX( aFulcrumX, aWidth ), calcSegmentY( aFulcrumY, aHeight ), aWidth, aHeight );
  }

  /**
   * Calculates the x-coordinate of the left end of a horizontal segment at the specified fulcrum point.
   *
   * @param aFulcrumX int - x coordinate of the fulcrum point
   * @param aSegmentLength int - the segment length
   * @return int - X coordinate of the left end
   * @throws TsIllegalArgumentRtException length < 0
   */
  public int calcSegmentX( int aFulcrumX, int aSegmentLength ) {
    TsIllegalArgumentRtException.checkTrue( aSegmentLength < 0 );
    return (int)((aFulcrumX - aSegmentLength) * xPerc);
  }

  /**
   * Calculates the x-coordinate of the top end of a vertical segment at the specified fulcrum point.
   *
   * @param aFulcrumY int - Y coordinate of the fulcrum point
   * @param aSegmentLength int - the segment length
   * @return int - Y coordinate of the top end
   * @throws TsIllegalArgumentRtException length < 0
   */
  public int calcSegmentY( int aFulcrumY, int aSegmentLength ) {
    TsIllegalArgumentRtException.checkTrue( aSegmentLength < 0 );
    return (int)((aFulcrumY - aSegmentLength) * yPerc);
  }

  /**
   * Calculates the X coordinate of a rectangle in the display area.
   *
   * @param aCanvasWidth int - the display area width
   * @param aRectWidth int - the rectangle width
   * @return int - X coordinate of the top-left corner
   */
  public int calcTopleftX( int aCanvasWidth, int aRectWidth ) {
    return (int)((aCanvasWidth - aRectWidth) * xPerc);
  }

  /**
   * Calculates the Y coordinate of a rectangle in the display area.
   *
   * @param aCanvasHeight int - the display area height
   * @param aRectHeight int - the rectangle height
   * @return int - Y coordinate of the top-left corner
   */
  public int calcTopleftY( int aCanvasHeight, int aRectHeight ) {
    return (int)((aCanvasHeight - aRectHeight) * yPerc);
  }

  /**
   * Determines whether the fulcrum point is located on the left edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the left, <code>false</code> - somewhere else
   */
  public boolean isLeft() {
    return fulcrum != null && fulcrum.isLeft();
  }

  /**
   * Determines whether the fulcrum point is located on the right edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the right, <code>false</code> - somewhere else
   */
  public boolean isRight() {
    return fulcrum != null && fulcrum.isRight();
  }

  /**
   * Determines whether the fulcrum point is located on the top edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the top, <code>false</code> - somewhere else
   */
  public boolean isTop() {
    return fulcrum != null && fulcrum.isTop();
  }

  /**
   * Determines whether the fulcrum point is located on the bottom edge of the rectangle.
   *
   * @return boolean - <code>true</code> fulcrum is on the bottom, <code>false</code> - somewhere else
   */
  public boolean isBottom() {
    return fulcrum != null && fulcrum.isBottom();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "(%.2f%%,%.2f%%)", Double.valueOf( xPerc ), Double.valueOf( yPerc ) ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TsFulcrum that ) {
      return Double.compare( this.xPerc, that.xPerc ) == 0 && Double.compare( this.yPerc, that.yPerc ) == 0;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    long dblval = Double.doubleToRawLongBits( xPerc );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    dblval = Double.doubleToRawLongBits( yPerc );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    return result;
  }

}
