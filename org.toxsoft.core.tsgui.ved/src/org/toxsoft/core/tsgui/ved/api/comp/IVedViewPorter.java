package org.toxsoft.core.tsgui.ved.api.comp;

/**
 * The means to manipulate components geometrical visualization properties.
 *
 * @author hazard157
 */
public interface IVedViewPorter {

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

}
