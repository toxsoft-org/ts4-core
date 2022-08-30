package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods to hangle 2D geometry.
 *
 * @author hazard157
 */
public class D2Utils {

  /**
   * Max allowed zoom factor is x1000 times.
   */
  public static final double MAX_ZOOM_FACTOR = 1_000.0;

  /**
   * Min allowed zoom factor is 1/1000 times.
   */
  public static final double MIN_ZOOM_FACTOR = 0.001;

  private static final double MAX_D2_VALUE         = (Long.MAX_VALUE) + 0.1;
  private static final double MIN_D2_VALUE         = (Long.MIN_VALUE) - 0.1;
  private static final double DUCK_DIFF_TRHRESHLOD = 0.000_000_1;
  private static final char   CHAR_ANGLE_DEGREES   = '∠';

  /**
   * Returns double exactly equal to the nearest long if aValue is near enough.
   * <p>
   * If something looks like duck, sounds like duck and walks like duck then it is the duck. If double value is near to
   * integer less then {@link #DUCK_DIFF_TRHRESHLOD} this method returns double exactly equal to integer value. This
   * method allows to avoid values like 0.0000000000123 instead of 0.0.
   *
   * @param aValue double - value very close to long value
   * @return double - exact long or unchanged aValue
   * @throws TsIllegalArgumentRtException argument is out of long range
   */
  public static double duck( double aValue ) {
    if( aValue < MIN_D2_VALUE || aValue > MAX_D2_VALUE ) {
      throw new TsIllegalArgumentRtException();
    }
    Double d = Double.valueOf( aValue );
    double diff = aValue - d.longValue();
    if( Math.abs( diff ) < DUCK_DIFF_TRHRESHLOD ) {
      return d.longValue();
    }
    return aValue;
  }

  /**
   * Fits zoom factor in range from {@link #MIN_ZOOM_FACTOR} to {@link #MAX_ZOOM_FACTOR}.
   *
   * @param aZoomFactor double - inital zoom factor
   * @return double zoom factor guaranteed to be in range
   */
  public static double zoomInRange( double aZoomFactor ) {
    if( aZoomFactor < MIN_ZOOM_FACTOR ) {
      return MIN_ZOOM_FACTOR;
    }
    if( aZoomFactor > MAX_ZOOM_FACTOR ) {
      return MAX_ZOOM_FACTOR;
    }
    return aZoomFactor;
  }

  /**
   * Checks zoom factor in range from {@link #MIN_ZOOM_FACTOR} to {@link #MAX_ZOOM_FACTOR}.
   *
   * @param aZoomFactor double - inital zoom factor
   * @return double - always returns argument value
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static double checkZoom( double aZoomFactor ) {
    if( !Double.isFinite( aZoomFactor ) || aZoomFactor < MIN_ZOOM_FACTOR || aZoomFactor > MAX_ZOOM_FACTOR ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_ZOOM_VALUE, //
          Double.valueOf( MIN_ZOOM_FACTOR ), Double.valueOf( MAX_ZOOM_FACTOR ), Double.valueOf( aZoomFactor ) );
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
   * Read instance may safdely be casted to {@link D2PointEdit}.
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
   * Read instance may safdely be casted to {@link D2VectorEdit}.
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
   * No subclassing.
   */
  private D2Utils() {
    // nop
  }

}
