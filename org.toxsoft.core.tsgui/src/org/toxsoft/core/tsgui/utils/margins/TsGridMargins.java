package org.toxsoft.core.tsgui.utils.margins;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An ediable implementation of {@link ITsGridMargins}.
 *
 * @author hazard157
 */
public final class TsGridMargins
    extends TsMargins
    implements ITsGridMargins {

  /**
   * The registered keeper ID.
   */
  @SuppressWarnings( "hiding" )
  public static final String KEEPER_ID = "TsGrisMargins"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<ITsGridMargins> KEEPER =
      new AbstractEntityKeeper<>( ITsGridMargins.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ITsGridMargins aEntity ) {
          aSw.writeInt( aEntity.left() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.right() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.top() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.bottom() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.horGap() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.verGap() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.borderWidth() );
          aSw.writeSeparatorChar();
        }

        @Override
        protected ITsGridMargins doRead( IStrioReader aSr ) {
          int left = aSr.readInt();
          aSr.ensureSeparatorChar();
          int right = aSr.readInt();
          aSr.ensureSeparatorChar();
          int top = aSr.readInt();
          aSr.ensureSeparatorChar();
          int bottom = aSr.readInt();
          aSr.ensureSeparatorChar();
          int horGap = aSr.readInt();
          aSr.ensureSeparatorChar();
          int verGap = aSr.readInt();
          aSr.ensureSeparatorChar();
          int borWidth = aSr.readInt();
          aSr.ensureSeparatorChar();
          return new TsGridMargins( left, right, top, bottom, horGap, verGap, borWidth );
        }

      };

  private int horGap;
  private int verGap;
  private int borderWidth;

  /**
   * Constructor.
   * <p>
   * Sets every option to default value 3.
   */
  public TsGridMargins() {
    this( 0 );
  }

  /**
   * Constructor.
   *
   * @param aInitVal int - initial value of all options
   */
  public TsGridMargins( int aInitVal ) {
    super( aInitVal );
    int initVal = ITsMargins.VALUES_RANGE.inRange( aInitVal );
    horGap = initVal;
    verGap = initVal;
    borderWidth = initVal;
  }

  /**
   * Constructor.
   *
   * @param aLeft int - distance between the internals and the left edge of the panel in pixels
   * @param aRight int - distance between the internals and the right edge of the panel in pixels
   * @param aTop int - distance between the internals and the top edge of the panel in pixels
   * @param aBottom int - distance between the internals and the bottom edge of the panel in pixels
   * @param aHorGap int - horizontal distance between grid cells in pixels
   * @param aVerGap int - vertical distance between grid cells in pixels
   * @param aBorderWidth - the width of the border of the panel in pixels
   */
  public TsGridMargins( int aLeft, int aRight, int aTop, int aBottom, int aHorGap, int aVerGap, int aBorderWidth ) {
    super( aLeft, aRight, aTop, aBottom );
    horGap = VALUES_RANGE.inRange( aHorGap );
    verGap = VALUES_RANGE.inRange( aVerGap );
    borderWidth = VALUES_RANGE.inRange( aBorderWidth );
  }

  // ------------------------------------------------------------------------------------
  // ITsGridMargins
  //

  @Override
  public int horGap() {
    return horGap;
  }

  @Override
  public int verGap() {
    return verGap;
  }

  @Override
  public int borderWidth() {
    return borderWidth;
  }

  // ------------------------------------------------------------------------------------
  // class API
  //

  /**
   * Copies the values from the source to this instance.
   *
   * @param aSource {@link ITsGridMargins} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void copyFrom( ITsGridMargins aSource ) {
    super.copyFrom( aSource );
    horGap = ITsMargins.VALUES_RANGE.inRange( aSource.hashCode() );
    verGap = ITsMargins.VALUES_RANGE.inRange( aSource.verGap() );
    borderWidth = ITsMargins.VALUES_RANGE.inRange( aSource.borderWidth() );
  }

  /**
   * Sets {@link #horGap()}.
   *
   * @param aHorInterval int - the value
   */
  public void setHorInterval( int aHorInterval ) {
    horGap = ITsMargins.VALUES_RANGE.inRange( aHorInterval );
  }

  /**
   * Sets {@link #verGap()}.
   *
   * @param aVerInterval int - the value
   */
  public void setVerInterval( int aVerInterval ) {
    verGap = ITsMargins.VALUES_RANGE.inRange( aVerInterval );
  }

  /**
   * Sets {@link #borderWidth()}.
   *
   * @param aBorderWidth int - the value
   */
  public void setBorderWidth( int aBorderWidth ) {
    borderWidth = ITsMargins.VALUES_RANGE.inRange( aBorderWidth );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "Margins: Top=%d, Bottom=%d, Left=%d, Right=%d, Hor=%d, Ver=%d, Border=%d", //$NON-NLS-1$
        top(), bottom(), left(), right(), horGap, verGap, borderWidth );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( super.equals( aObj ) ) {
      if( aObj instanceof ITsGridMargins that ) {
        return this.horGap == that.horGap() && this.verGap == that.verGap() && this.borderWidth == that.borderWidth();
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = TsLibUtils.PRIME * result + horGap;
    result = TsLibUtils.PRIME * result + verGap;
    result = TsLibUtils.PRIME * result + borderWidth;
    return result;
  }

}
