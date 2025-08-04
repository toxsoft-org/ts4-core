package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods to handle 2D geometry.
 *
 * @author hazard157
 */
public class D2Utils {

  /**
   * Allowed range of the zoom factor.
   * <p>
   * Note: minValue must be much more than {@link #DUCK_DIFF_THRESHLOD}.
   */
  public static final DoubleRange ZOOM_RANGE = new DoubleRange( 0.001, 1_000.0 );

  private static final double MAX_D2_VALUE        = (Long.MAX_VALUE) + 0.1;
  private static final double MIN_D2_VALUE        = (Long.MIN_VALUE) - 0.1;
  private static final double DUCK_DIFF_THRESHLOD = 0.000_000_1;           // must be 1000+ times less that range min
  private static final char   CHAR_ANGLE_DEGREES  = '∠';

  // ------------------------------------------------------------------------------------
  // Duck methods

  /**
   * Returns double exactly equal to the nearest long if aValue is near enough.
   * <p>
   * If something looks like duck, sounds like duck and walks like duck then it is the duck. If double value is near to
   * integer less then {@link #DUCK_DIFF_THRESHLOD} this method returns double exactly equal to integer value. This
   * method allows to avoid values like 0.0000000000123 instead of 0.0.
   *
   * @param aValue double - value very close to long value
   * @return double - exact long or unchanged aValue
   * @throws TsIllegalArgumentRtException argument is out of <code>long</code> range
   */
  public static double duck( double aValue ) {
    // following code is the same as @isDuck() but is NOT replaced by isDuck() for optimization
    if( aValue < MIN_D2_VALUE || aValue > MAX_D2_VALUE ) {
      throw new TsIllegalArgumentRtException();
    }
    long closestLong = Math.round( aValue );
    double diff = aValue - closestLong;
    if( Math.abs( diff ) < DUCK_DIFF_THRESHLOD ) {
      return closestLong;
    }
    return aValue;
  }

  /**
   * Determines if this is the duck.
   * <p>
   * See comments to {@link #duck(double)}.
   *
   * @param aValue double - the value to be tested
   * @return boolean - <code>true</code> if value is close enoght to integer value
   * @throws TsIllegalArgumentRtException argument is out of <code>long</code> range
   */
  public static boolean isDuck( double aValue ) {
    if( aValue < MIN_D2_VALUE || aValue > MAX_D2_VALUE ) {
      throw new TsIllegalArgumentRtException();
    }
    long closestLong = Math.round( aValue );
    double diff = aValue - closestLong;
    return Math.abs( diff ) < DUCK_DIFF_THRESHLOD;
  }

  /**
   * Determines if two values are near enough to interpret as the same values.
   * <p>
   * "Near enough" means that difference between values are less than {@link #DUCK_DIFF_THRESHLOD}.
   * <p>
   * If any value is infinite then method returns result of {@link Double#compare(double, double)}.
   *
   * @param aValue1 double - first value
   * @param aValue2 double - second value
   * @return boolean - values are same or near enough
   */
  public static boolean isDuckEQ( double aValue1, double aValue2 ) {
    if( Double.isFinite( aValue1 ) && Double.isFinite( aValue2 ) ) {
      double diff = Math.abs( aValue2 - aValue1 );
      return diff < DUCK_DIFF_THRESHLOD;
    }
    return Double.compare( aValue1, aValue2 ) == 0;
  }

  /**
   * Determines if the value is greater or approximately equal to the limit.
   * <p>
   * "Approximately equal" means that difference between values are less than {@link #DUCK_DIFF_THRESHLOD}.
   * <p>
   * If any value is infinite then method returns result of operator <b>>=</b>.
   *
   * @param aValue double - the value
   * @param aLimit double - the limit
   * @return boolean - <code>true</code> if value is greater than aLimit - {@link #DUCK_DIFF_THRESHLOD},
   */
  public static boolean isDuckGE( double aValue, double aLimit ) {
    if( Double.isFinite( aValue ) && Double.isFinite( aLimit ) ) {
      return aValue > aLimit - DUCK_DIFF_THRESHLOD;
    }
    return aValue >= aLimit;
  }

  /**
   * Determines if the value is less or approximately equal to the limit.
   * <p>
   * "Approximately equal" means that difference between values are less than {@link #DUCK_DIFF_THRESHLOD}.
   * <p>
   * If any value is infinite then method returns result of operator <b><=</b>.
   *
   * @param aValue double - the value
   * @param aLimit double - the limit
   * @return boolean - <code>true</code> if value is less than aLimit + {@link #DUCK_DIFF_THRESHLOD},
   */
  public static boolean isDuckLE( double aValue, double aLimit ) {
    if( Double.isFinite( aValue ) && Double.isFinite( aLimit ) ) {
      return aValue < aLimit + DUCK_DIFF_THRESHLOD;
    }
    return aValue <= aLimit;
  }

  /**
   * Compares two double value with the equality threshold as {@link #DUCK_DIFF_THRESHLOD}.
   * <p>
   * Warning: do not use this method for sorting, just for human eye looking equality!
   *
   * @param aVal1 double - first value
   * @param aVal2 double - second value
   * @return int - -1, zero, or a +1 as the first argument is less than, equal to, or greater than the second.
   */
  public static int compareDoubles( double aVal1, double aVal2 ) {
    if( Math.abs( aVal2 - aVal1 ) < DUCK_DIFF_THRESHLOD ) {
      return 0;
    }
    if( aVal1 < aVal2 ) {
      return -1;
    }
    return +1;
  }

  // ------------------------------------------------------------------------------------
  // Constructors from other types

  /**
   * Creates {@link ID2Size} from {@link ITsDims}.
   *
   * @param aDims {@link ITsDims} - the source
   * @return {@link ID2Size} created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ID2Size createSize( ITsDims aDims ) {
    TsNullArgumentRtException.checkNull( aDims );
    return new D2Size( aDims.width(), aDims.height() );
  }

  // ------------------------------------------------------------------------------------
  // Misc helpers

  /**
   * Fits zoom factor in range {@link #ZOOM_RANGE}.
   *
   * @param aZoomFactor double - initial zoom factor
   * @return double zoom factor guaranteed to be in range
   */
  public static double zoomInRange( double aZoomFactor ) {
    return ZOOM_RANGE.inRange( aZoomFactor );
  }

  /**
   * Changes the <code>aZoomFactor</code> by the <code>aZoomMultiplier</code>.
   * <p>
   * Guarantees new zoom factor is in range. For non-finit arguments returns 1.0.
   *
   * @param aOriginalFactor double - the initial zoom factor
   * @param aZoomMultiplier double - zoom multiplication
   * @return double - new zoom factor
   */
  public static double zoom( double aOriginalFactor, double aZoomMultiplier ) {
    if( !Double.isFinite( aOriginalFactor ) || !Double.isFinite( aZoomMultiplier ) ) {
      return 1.0;
    }
    return ZOOM_RANGE.inRange( aOriginalFactor * aZoomMultiplier );
  }

  // ------------------------------------------------------------------------------------
  // Value checks

  /**
   * Checks zoom factor in allowed range.
   *
   * @param aZoomFactor double - initial zoom factor
   * @return double - always returns argument value
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double checkZoom( double aZoomFactor ) {
    if( !ZOOM_RANGE.isInRange( aZoomFactor ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_ZOOM_VALUE, //
          ZOOM_RANGE.toString(), Double.valueOf( aZoomFactor ) );
    }
    return aZoomFactor;
  }

  /**
   * Checks if coordinate value is valid.
   * <p>
   * Any finite <code>double</code> is considered as valid.
   *
   * @param aCoor double - coordinate value
   * @return double - always returns argument value
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double checkCoor( double aCoor ) {
    if( !Double.isFinite( aCoor ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_COOR_VALUE, Double.toString( aCoor ) );
    }
    return aCoor;
  }

  /**
   * Checks if length value is valid.
   * <p>
   * Any non-negative finite <code>double</code> is considered as valid.
   *
   * @param aLength double - length value
   * @return double - always returns argument value
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double checkLength( double aLength ) {
    if( !Double.isFinite( aLength ) || aLength < 0.0 ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_LENGTH_VALUE, Double.toString( aLength ) );
    }
    return aLength;
  }

  /**
   * Checks if coordinate value is valid.
   * <p>
   * Any finite <code>double</code> is considered as valid.
   *
   * @param aRadians double - angle value in radians
   * @return double returns the argument
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double checkAngleRadians( double aRadians ) {
    if( !Double.isFinite( aRadians ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_RADIANS_VALUE, Double.toString( aRadians ) );
    }
    return aRadians;
  }

  /**
   * Checks if coordinate value is valid.
   * <p>
   * Any finite <code>double</code> is considered as valid.
   *
   * @param aDegrees double - angle value in degrees
   * @return double returns the argument
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double checkAngleDegrees( double aDegrees ) {
    if( !Double.isFinite( aDegrees ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_DEGREES_VALUE, Double.toString( aDegrees ) );
    }
    return aDegrees;
  }

  /**
   * Converts angle radians to degrees.
   *
   * @param aRadians double - angle value in radians
   * @return double - angle value in degrees
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double rad2deg( double aRadians ) {
    checkAngleRadians( aRadians );
    return Math.toDegrees( aRadians );
  }

  /**
   * Converts angle degrees to radians.
   *
   * @param aDegrees double - angle value in degrees
   * @return double - angle value in radians
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double deg2rad( double aDegrees ) {
    checkAngleDegrees( aDegrees );
    return Math.toRadians( aDegrees );
  }

  // ------------------------------------------------------------------------------------
  // STRIO operations
  //

  /**
   * Writes {@link ID2Point} to the STRIO stream.
   *
   * @param aSw {@link IStrioWriter} - text writer
   * @param aPoint {@link ID2Point} - point to write
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error writing to the stream
   */
  public static void writeD2Point( IStrioWriter aSw, ID2Point aPoint ) {
    TsNullArgumentRtException.checkNulls( aSw, aPoint );
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.writeDouble( aPoint.x() );
    aSw.writeSeparatorChar();
    aSw.writeDouble( aPoint.y() );
    aSw.writeChar( CHAR_SET_END );
  }

  /**
   * Reads {@link D2PointEdit} from the STRIO stream.
   *
   * @param aSr {@link IStrioReader} - text reader
   * @param aPoint {@link D2PointEdit} - editable point to be updated from stream
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error accessing input stream
   * @throws StrioRtException invalid format
   */
  public static void readD2Point( IStrioReader aSr, D2PointEdit aPoint ) {
    TsNullArgumentRtException.checkNulls( aSr, aPoint );
    aSr.ensureChar( CHAR_SET_BEGIN );
    double x = aSr.readDouble();
    aSr.ensureSeparatorChar();
    double y = aSr.readDouble();
    aSr.ensureChar( CHAR_SET_END );
    aPoint.setPoint( x, y );
  }

  /**
   * Reads {@link ID2Point} from the STRIO stream.
   * <p>
   * Read instance may safely be casted to {@link D2PointEdit}.
   *
   * @param aSr {@link IStrioReader} - text reader
   * @return {@link ID2Point} - read point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error accessing input stream
   * @throws StrioRtException invalid format
   */
  public static ID2Point readD2Point( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    aSr.ensureChar( CHAR_SET_BEGIN );
    double x = aSr.readDouble();
    aSr.ensureSeparatorChar();
    double y = aSr.readDouble();
    aSr.ensureChar( CHAR_SET_END );
    return new D2PointEdit( x, y );
  }

  /**
   * Writes ID2Vector to the STRIO stream.
   * <p>
   * Vector is saved as to {@link ID2Point} coordinates.
   *
   * @param aSw {@link IStrioWriter} - text writer
   * @param aVector {@link ID2Vector} - vector to write
   */
  public static void writeD2Vector( IStrioWriter aSw, ID2Vector aVector ) {
    TsNullArgumentRtException.checkNulls( aSw, aVector );
    aSw.writeChar( CHAR_SET_BEGIN );
    writeD2Point( aSw, aVector.a() );
    aSw.writeSeparatorChar();
    aSw.writeDouble( aVector.length() );
    aSw.writeSeparatorChar();
    aSw.writeChar( CHAR_ANGLE_DEGREES );
    aSw.writeDouble( aVector.angle().degrees() );
    aSw.writeChar( CHAR_SET_END );
  }

  /**
   * Reads {@link D2VectorEdit} from the STRIO stream.
   *
   * @param aSr {@link IStrioReader} - text reader
   * @param aVector {@link ID2VectorEdit} - editable vector to be updated from stream
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error accessing input stream
   * @throws StrioRtException invalid format
   */
  public static void readD2Vector( IStrioReader aSr, ID2VectorEdit aVector ) {
    TsNullArgumentRtException.checkNulls( aSr, aVector );
    aSr.ensureChar( CHAR_SET_BEGIN );
    ID2Point a = readD2Point( aSr );
    aSr.ensureSeparatorChar();
    double length = aSr.readDouble();
    aSr.ensureSeparatorChar();
    aSr.ensureChar( CHAR_ANGLE_DEGREES );
    double degrees = aSr.readDouble();
    aSr.ensureChar( CHAR_SET_END );
    aVector.setVectorDeg( a, length, degrees );
  }

  /**
   * Reads {@link ID2Vector} from the STRIO stream.
   * <p>
   * Read instance may safely be casted to {@link D2VectorEdit}.
   *
   * @param aSr {@link IStrioReader} - text reader
   * @return {@link ID2Vector} - read vector
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error accessing input stream
   * @throws StrioRtException invalid format
   */
  public static ID2Vector readD2Vector( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    aSr.ensureChar( CHAR_SET_BEGIN );
    ID2Point a = readD2Point( aSr );
    aSr.ensureSeparatorChar();
    ID2Point b = readD2Point( aSr );
    aSr.ensureChar( CHAR_SET_END );
    return new D2VectorEdit( a, b );
  }

  /**
   * TODO add here fulcrum-related methods (as in {@link ETsFulcrum} API) with double arguments
   */

  /**
   * No subclasses.
   */
  private D2Utils() {
    // nop
  }

}
