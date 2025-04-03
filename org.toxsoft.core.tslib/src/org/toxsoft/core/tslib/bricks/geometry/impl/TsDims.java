package org.toxsoft.core.tslib.bricks.geometry.impl;

import static org.toxsoft.core.tslib.bricks.geometry.l10n.ITsGeometruSharedResources.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;

/**
 * {@link ITsDims} immutable implementation.
 *
 * @author hazard157
 */
public final class TsDims
    implements ITsDims {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TsDims"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   * Returned value may safely be casted to {@link TsDimsEdit}.
   */
  public static final IEntityKeeper<ITsDims> KEEPER =
      new AbstractEntityKeeper<>( ITsDims.class, EEncloseMode.ENCLOSES_BASE_CLASS, ITsDims.ONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ITsDims aEntity ) {
          aSw.writeInt( aEntity.width() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.height() );
        }

        @Override
        protected ITsDims doRead( IStrioReader aSr ) {
          int w = aSr.readInt();
          aSr.ensureSeparatorChar();
          int h = aSr.readInt();
          return new TsDimsEdit( w, h );
        }
      };

  private final int width;
  private final int height;

  /**
   * Constructor.
   *
   * @param aWidth int - the width (X dimension)
   * @param aHeight int - the width (Y dimension)
   * @throws TsValidationFailedRtException method {@link #validateArgs(int, int)} returned an error
   */
  public TsDims( int aWidth, int aHeight ) {
    checkArgs( aWidth, aHeight );
    width = aWidth;
    height = aHeight;
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
   * Checks the dimensions creation arguments for validity.
   * <p>
   * Checks that dimensions (width, height) are >= 1.
   *
   * @param aWidth int - the width (X dimension)
   * @param aHeight int - the width (Y dimension)
   * @return {@link ValidationResult} - the validation result
   */
  public static ValidationResult validateArgs( int aWidth, int aHeight ) {
    if( aWidth < 1 ) {
      return ValidationResult.error( MSG_ERR_INVALID_WIDTH );
    }
    if( aHeight < 1 ) {
      return ValidationResult.error( MSG_ERR_INVALID_HEIGHT );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Checks and the dimensions creation arguments are not valid throws an exception.
   *
   * @param aWidth int - the width (X dimension)
   * @param aHeight int - the width (Y dimension)
   * @throws TsValidationFailedRtException method {@link #validateArgs(int, int)} returned an error
   */
  public static void checkArgs( int aWidth, int aHeight ) {
    TsValidationFailedRtException.checkError( validateArgs( aWidth, aHeight ) );
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
