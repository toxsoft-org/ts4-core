package org.toxsoft.core.tsgui.ved.screen;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Convert coordinates between the different coordinate spaces used in the VED screen implementation.
 * <p>
 * There are three coordinates spaces:
 * <ul>
 * <li>SWT canvas - common coordinates in pixels used to draw on {@link GC} and handle user input (mouse cursor)
 * coordinates. Coordinates are <code>int</code> numbers and strictly corresponds to the {@link GC} coordinates;</li>
 * <li>VED screen - coordinates on the VED screen. These are the <code>double</code> numbers. VED screen view has it's
 * own transform {@link IVedScreenView#getConversion()} parameters used to draw screen transformed. When transformation
 * is not used (that is conversion is set to {@link ID2Conversion#NONE} VED screen coordinates are of the same values as
 * the SWT coordinates (just <code>double</code> and <code>int</code> types, respectively);</li>
 * <li>VISEL - VISEL adds it's own transformation {@link IVedVisel#getConversion()} to the VED screen conversion, so
 * VISEL is transformed drawn on the transformed screen. When both VISEL and screen transformation is not used, VISEL
 * coordinate space matched the VED screen and SWT coordinates space.</li>
 * </ul>
 * Conversion is often need, for example, to determine if mouse cursor is on the VISEL bounds rectangle. In this case
 * {@link #swt2Visel(ITsPoint, VedAbstractVisel)} method should be used.
 *
 * @author hazard157
 */
public interface IVedCoorsConverter {

  ID2Point swt2Screen( int aSwtX, int aSwtY );

  ID2Point swt2Visel( int aSwtX, int aSwtY, VedAbstractVisel aVisel );

  ITsPoint screen2Swt( double aScreenX, double aScreenY );

  ID2Point screen2Visel( double aScreenX, double aScreenY, VedAbstractVisel aVisel );

  ITsPoint visel2Swt( double aViselX, double aViselY, VedAbstractVisel aVisel );

  /**
   * Converts point location from VED screen to VISEL coordinate space.
   *
   * @param aViselX double - X coordinate in VISEL coordinates space
   * @param aViselY double - Y coordinate in VISEL coordinates space
   * @param aVisel {@link VedAbstractVisel} - the VISEL
   * @return {@link ID2Point} - the point in the VED screen coordinates space
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ID2Point visel2Screen( double aViselX, double aViselY, VedAbstractVisel aVisel );

  /**
   * Converts rectangle from SWT canvas to the VED screen coordinates.
   *
   * @param aSwtX int - X coordinate in the SWT canvas coordinates space
   * @param aSwtY int - Y coordinate in the SWT canvas coordinates space
   * @param aSwtWidth int - rectangle width in the SWT canvas coordinates space
   * @param aSwtHeight int - rectangle height in the SWT canvas coordinates space
   * @return {@link ID2Rectangle} - the rectangle in the the VED screen coordinates space
   * @throws TsIllegalArgumentRtException width or height is negative
   */
  ID2Rectangle swt2Screen( int aSwtX, int aSwtY, int aSwtWidth, int aSwtHeight );

  /**
   * Converts rectangle from SWT canvas to the VISEL coordinates.
   *
   * @param aSwtX int - X coordinate in the SWT canvas coordinates space
   * @param aSwtY int - Y coordinate in the SWT canvas coordinates space
   * @param aSwtWidth int - rectangle width in the SWT canvas coordinates space
   * @param aSwtHeight int - rectangle height in the SWT canvas coordinates space
   * @param aVisel {@link VedAbstractVisel} - the VISEL
   * @return {@link ID2Rectangle} - the rectangle in the the specified VISEL coordinates space
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException width or height is negative
   */
  ID2Rectangle swt2Visel( int aSwtX, int aSwtY, int aSwtWidth, int aSwtHeight, VedAbstractVisel aVisel );

  /**
   * Converts rectangle from VED screen to the VISEL coordinates.
   *
   * @param aScreenX double - X coordinate in the VED screen coordinates space
   * @param aScreenY double - Y coordinate in the VED screen coordinates space
   * @param aScreenWidth double - rectangle width in the VED screen coordinates space
   * @param aScreenHeight double - rectangle height in the VED screen coordinates space
   * @return {@link ITsRectangle} - the rectangle in the SWT canvas coordinates space
   * @throws TsIllegalArgumentRtException width or height is negative
   */
  ITsRectangle screen2Swt( double aScreenX, double aScreenY, double aScreenWidth, double aScreenHeight );

  /**
   * Converts rectangle from VED screen to the VISEL coordinates.
   *
   * @param aScreenX double - X coordinate in the VED screen coordinates space
   * @param aScreenY double - Y coordinate in the VED screen coordinates space
   * @param aScreenWidth double - rectangle width in the VED screen coordinates space
   * @param aScreenHeight double - rectangle height in the VED screen coordinates space
   * @param aVisel {@link VedAbstractVisel} - the VISEL
   * @return {@link ID2Rectangle} - the rectangle in the specified VISEL coordinates space
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException width or height is negative
   */
  ID2Rectangle screen2Visel( double aScreenX, double aScreenY, double aScreenWidth, double aScreenHeight,
      VedAbstractVisel aVisel );

  /**
   * Converts rectangle from VISEL to SWT coordinates.
   *
   * @param aViselX double - X coordinate in VISEL coordinates space
   * @param aViselY double - Y coordinate in VISEL coordinates space
   * @param aViselWidth double - rectangle width in VISEL coordinates space
   * @param aViselHeight double - rectangle height in VISEL coordinates space
   * @param aVisel {@link VedAbstractVisel} - the VISEL
   * @return {@link ITsRectangle} - the rectangle in the SWT canvas coordinates space
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException width or height is negative
   */
  ITsRectangle visel2Swt( double aViselX, double aViselY, double aViselWidth, double aViselHeight,
      VedAbstractVisel aVisel );

  /**
   * Converts rectangle from VISEL to VED screen coordinates.
   *
   * @param aViselX double - X coordinate in VISEL coordinates space
   * @param aViselY double - Y coordinate in VISEL coordinates space
   * @param aViselWidth double - rectangle width in VISEL coordinates space
   * @param aViselHeight double - rectangle height in VISEL coordinates space
   * @param aVisel {@link VedAbstractVisel} - the VISEL
   * @return {@link ID2Rectangle} - the rectangle in the VED screen coordinates space
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException width or height is negative
   */
  ID2Rectangle visel2Screen( double aViselX, double aViselY, double aViselWidth, double aViselHeight,
      VedAbstractVisel aVisel );

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience
  //

  @SuppressWarnings( "javadoc" )
  default ID2Point swt2Screen( ITsPoint aSwtCoors ) {
    return swt2Screen( aSwtCoors.x(), aSwtCoors.y() );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Point swt2Visel( ITsPoint aSwtCoors, VedAbstractVisel aVisel ) {
    return swt2Visel( aSwtCoors.x(), aSwtCoors.y(), aVisel );
  }

  @SuppressWarnings( "javadoc" )
  default ITsPoint screen2Swt( ID2Point aScreenCoors ) {
    return screen2Swt( aScreenCoors.x(), aScreenCoors.y() );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Point screen2Visel( ID2Point aScreenCoors, VedAbstractVisel aVisel ) {
    return screen2Visel( aScreenCoors.x(), aScreenCoors.y(), aVisel );
  }

  @SuppressWarnings( "javadoc" )
  default ITsPoint visel2Swt( ID2Point aViselCoors, VedAbstractVisel aVisel ) {
    return visel2Swt( aViselCoors.x(), aViselCoors.y(), aVisel );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Point visel2Screen( ID2Point aViselCoors, VedAbstractVisel aVisel ) {
    return visel2Screen( aViselCoors.x(), aViselCoors.y(), aVisel );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Rectangle swt2Screen( ITsRectangle aSwtRect ) {
    return swt2Screen( aSwtRect.x1(), aSwtRect.y1(), aSwtRect.width(), aSwtRect.height() );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Rectangle swt2Visel( ITsRectangle aSwtRect, VedAbstractVisel aVisel ) {
    return swt2Visel( aSwtRect.x1(), aSwtRect.y1(), aSwtRect.width(), aSwtRect.height(), aVisel );
  }

  @SuppressWarnings( "javadoc" )
  default ITsRectangle screen2Swt( ID2Rectangle aScreenRect ) {
    return screen2Swt( aScreenRect.x1(), aScreenRect.y1(), aScreenRect.width(), aScreenRect.height() );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Rectangle screen2Visel( ID2Rectangle aScreenRect, VedAbstractVisel aVisel ) {
    return screen2Visel( aScreenRect.x1(), aScreenRect.y1(), aScreenRect.width(), aScreenRect.height(), aVisel );
  }

  @SuppressWarnings( "javadoc" )
  default ITsRectangle visel2Swt( ID2Rectangle aViselRect, VedAbstractVisel aVisel ) {
    return visel2Swt( aViselRect.x1(), aViselRect.y1(), aViselRect.width(), aViselRect.height(), aVisel );
  }

  @SuppressWarnings( "javadoc" )
  default ID2Rectangle visel2Screen( ID2Rectangle aViselRect, VedAbstractVisel aVisel ) {
    return visel2Screen( aViselRect.x1(), aViselRect.y1(), aViselRect.width(), aViselRect.height(), aVisel );
  }

}
