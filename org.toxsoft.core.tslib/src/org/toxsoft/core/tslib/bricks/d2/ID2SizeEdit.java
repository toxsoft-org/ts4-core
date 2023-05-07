package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The dimensions of a something.
 * <p>
 * Dimensions (a width and a height) are always >= 0.0.
 *
 * @author hazard157
 */
public sealed interface ID2SizeEdit
    extends ID2Size permits D2SizeEdit {

  /**
   * Sets the width.
   *
   * @param aWidth double - the width
   * @throws TsIllegalArgumentRtException argument < 0
   * @throws TsIllegalArgumentRtException invalid double number
   */
  void setWidth( double aWidth );

  /**
   * Sets the height.
   *
   * @param aHeight double - the height
   * @throws TsIllegalArgumentRtException argument < 0
   * @throws TsIllegalArgumentRtException invalid double number
   */
  void setHeight( double aHeight );

  /**
   * Sets new size (the width and the height at once).
   *
   * @param aWidth double - the width
   * @param aHeight double - the height
   * @throws TsIllegalArgumentRtException any argument < 0
   * @throws TsIllegalArgumentRtException invalid double number
   */
  void setSize( double aWidth, double aHeight );

  /**
   * Sets new size.
   *
   * @param aSize {@link ID2Size} - the new size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setSize( ID2Size aSize );

}
