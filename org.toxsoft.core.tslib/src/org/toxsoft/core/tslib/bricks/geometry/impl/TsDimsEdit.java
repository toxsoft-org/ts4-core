package org.toxsoft.core.tslib.bricks.geometry.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsDims} editable implementation.
 *
 * @author hazard157
 */
public final class TsDimsEdit
    implements ITsDims {

  private int width;
  private int height;

  /**
   * Constructor with initial values (1,1).
   */
  public TsDimsEdit() {
    width = 1;
    height = 1;
  }

  /**
   * Constructor.
   *
   * @param aWidth int - the width (X dimension)
   * @param aHeight int - the width (Y dimension)
   * @throws TsValidationFailedRtException method {@link TsDims#validateArgs(int, int)} returned an error
   */
  public TsDimsEdit( int aWidth, int aHeight ) {
    TsDims.checkArgs( aWidth, aHeight );
    width = aWidth;
    height = aHeight;
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsDims} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsDimsEdit( ITsDims aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    TsDims.checkArgs( aSource.width(), aSource.height() );
    width = aSource.width();
    height = aSource.height();
  }

  // ------------------------------------------------------------------------------------
  // ITsDims
  //

  @Override
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the width.
   *
   * @param aWidth int - the width (X dimension)
   * @throws TsValidationFailedRtException method {@link TsDims#validateArgs(int, int)} returned an error
   */
  public void setWidth( int aWidth ) {
    TsDims.checkArgs( aWidth, height );
    width = aWidth;
  }

  /**
   * Sets the height.
   *
   * @param aHeight int - the width (Y dimension)
   * @throws TsValidationFailedRtException method {@link TsDims#validateArgs(int, int)} returned an error
   */
  public void setHeight( int aHeight ) {
    TsDims.checkArgs( width, aHeight );
    height = aHeight;
  }

  /**
   * Sets the both dimaensions.
   *
   * @param aWidth int - the width (X dimension)
   * @param aHeight int - the width (Y dimension)
   * @throws TsValidationFailedRtException method {@link TsDims#validateArgs(int, int)} returned an error
   */
  public void setDims( int aWidth, int aHeight ) {
    TsDims.checkArgs( aWidth, aHeight );
    width = aWidth;
    height = aHeight;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( object instanceof ITsDims d ) {
      return (d.width() == this.width) && (d.height() == this.height);
    }
    return false;
  }

  @Override
  public int hashCode() {
    // Note: an editable dimensions must have the same algorithm
    return width ^ height;
  }

  @Override
  public String toString() {
    return EMPTY_STRING + width + 'x' + height;
  }

}
