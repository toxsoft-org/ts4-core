package org.toxsoft.core.tsgui.ved.api.view;

import org.toxsoft.core.tsgui.ved.incub.geom.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * The means to manipulate components geometrical visualization properties.
 * <p>
 * Each method of this interface is just a shorthand to the appropriate properties direct change opeartions.
 * <p>
 * Note that all methods requre the component to have corresponding properties. At least X and Y coordinates are
 * expected. Even more, some component may not allow to change some of it's properties. For exmple, component with
 * raster image may fix it's width and height. This is not an error so appropriate methods does not throw an exception.
 *
 * @author hazard157
 */
public interface IVedPorter {

  /**
   * Sets the the components location.
   *
   * @param aX double - X virtual coordinate
   * @param aY double - X virtual coordinate
   */
  void locate( double aX, double aY );

  /**
   * Shifts comonient (changes X/Y coordinates) on specified value.
   *
   * @param aDx double - X virtual shift (>0 - to right, <0 - to left)
   * @param aDy double - Y virtual shift (>0 - to bottom, <0 - to top)
   */
  void shiftOn( double aDx, double aDy );

  /**
   * Sets size of the component.
   *
   * @param aWidth double - virtual width of the bounds rectangle
   * @param aHeight double - virtual height of the bounds rectangle
   */
  void setSize( double aWidth, double aHeight );

  /**
   * Set the bound.
   * <p>
   * Is similar to call {@link #locate(double, double)} and {@link #setSize(double, double)}.
   *
   * @param aX double - X virtual coordinate
   * @param aY double - X virtual coordinate
   * @param aWidth double - virtual width of the bounds rectangle
   * @param aHeight double - virtual height of the bounds rectangle
   */
  void setBounds( double aX, double aY, double aWidth, double aHeight );

  /**
   * Rotates the component.
   * <p>
   * This method requres the component to support rotation amount propperty and optionally rotaion center property.
   * <p>
   * Rotation center is measured statring from shapes X,Y coordinates or <code>null</code> value is used to specify
   * rotation around bounding rectanges center point.
   *
   * @param aRotationCenter {@link ID2Point} - rotation center point coordinates or <code>null</code>
   * @param aDegrees double - angle to rotate in degrees (<0 - clockwise, >0 - counterclockwise)
   */
  void rotate( ID2Point aRotationCenter, double aDegrees );

  // TODO API

  void flipHor( boolean aFlip );

  void flipVer( boolean aFlip );

  void zoom( double aZoomFactorX, double aZoomFactorY );

}
