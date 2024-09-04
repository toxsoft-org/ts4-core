package org.toxsoft.core.tsgui.utils.margins;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An ediable implementation of {@link ITsMargins}.
 *
 * @author hazard157
 */
public sealed class TsMargins
    implements ITsMargins
    permits TsGridMargins {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TsMargins"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ITsMargins> KEEPER =
      new AbstractEntityKeeper<>( ITsMargins.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ITsMargins aEntity ) {
          aSw.writeInt( aEntity.left() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.right() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.top() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.bottom() );
          aSw.writeSeparatorChar();
        }

        @Override
        protected ITsMargins doRead( IStrioReader aSr ) {
          int left = aSr.readInt();
          aSr.ensureSeparatorChar();
          int right = aSr.readInt();
          aSr.ensureSeparatorChar();
          int top = aSr.readInt();
          aSr.ensureSeparatorChar();
          int bottom = aSr.readInt();
          aSr.ensureSeparatorChar();
          return new TsMargins( left, top, right, bottom );
        }

      };

  private int left;
  private int right;
  private int top;
  private int bottom;

  /**
   * Constructor.
   * <p>
   * Sets every option to default value 0.
   */
  public TsMargins() {
    this( 0 );
  }

  /**
   * Constructor.
   *
   * @param aInitVal int - initial value of all options
   */
  public TsMargins( int aInitVal ) {
    int initVal = ITsMargins.VALUES_RANGE.inRange( aInitVal );
    left = initVal;
    right = initVal;
    top = initVal;
    bottom = initVal;
  }

  /**
   * Constructor.
   *
   * @param aLeft int - distance between the internals and the left edge of the panel in pixels
   * @param aRight int - distance between the internals and the right edge of the panel in pixels
   * @param aTop int - distance between the internals and the top edge of the panel in pixels
   * @param aBottom int - distance between the internals and the bottom edge of the panel in pixels
   */
  public TsMargins( int aLeft, int aRight, int aTop, int aBottom ) {
    left = ITsMargins.VALUES_RANGE.inRange( aLeft );
    right = ITsMargins.VALUES_RANGE.inRange( aRight );
    top = ITsMargins.VALUES_RANGE.inRange( aTop );
    bottom = ITsMargins.VALUES_RANGE.inRange( aBottom );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsMargins} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsMargins( ITsMargins aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    left = ITsMargins.VALUES_RANGE.inRange( aSource.left() );
    right = ITsMargins.VALUES_RANGE.inRange( aSource.right() );
    top = ITsMargins.VALUES_RANGE.inRange( aSource.top() );
    bottom = ITsMargins.VALUES_RANGE.inRange( aSource.bottom() );
  }

  // ------------------------------------------------------------------------------------
  // ITsMargins
  //

  @Override
  public int left() {
    return left;
  }

  @Override
  public int right() {
    return right;
  }

  @Override
  public int top() {
    return top;
  }

  @Override
  public int bottom() {
    return bottom;
  }

  // ------------------------------------------------------------------------------------
  // class API
  //

  /**
   * Copies the values from the source to this instance.
   *
   * @param aSource {@link ITsMargins} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void copyFrom( ITsMargins aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    left = ITsMargins.VALUES_RANGE.inRange( aSource.left() );
    right = ITsMargins.VALUES_RANGE.inRange( aSource.right() );
    top = ITsMargins.VALUES_RANGE.inRange( aSource.top() );
    bottom = ITsMargins.VALUES_RANGE.inRange( aSource.bottom() );
  }

  /**
   * Sets {@link #left()}.
   *
   * @param aLeft int - the value
   */
  public void setLeftMargin( int aLeft ) {
    left = ITsMargins.VALUES_RANGE.inRange( aLeft );
  }

  /**
   * Sets {@link #right()}.
   *
   * @param aRight int - the value
   */
  public void setRightMargin( int aRight ) {
    right = ITsMargins.VALUES_RANGE.inRange( aRight );
  }

  /**
   * Sets {@link #top()}.
   *
   * @param aTop int - the value
   */
  public void setTopMargin( int aTop ) {
    top = ITsMargins.VALUES_RANGE.inRange( aTop );
  }

  /**
   * Sets {@link #bottom()}.
   *
   * @param aBottom int - the value
   */
  public void setBottomMargin( int aBottom ) {
    bottom = ITsMargins.VALUES_RANGE.inRange( aBottom );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "Margins: Top=%d, Bottom=%d, Left=%d, Right=%d", //$NON-NLS-1$
        top, bottom, left, right );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof ITsMargins that ) {
      return this.top == that.top() && this.bottom == that.bottom() && this.left == that.left()
          && this.right == that.right();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + top;
    result = TsLibUtils.PRIME * result + bottom;
    result = TsLibUtils.PRIME * result + left;
    result = TsLibUtils.PRIME * result + right;
    return result;
  }

}
