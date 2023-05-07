package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Size} immutable implementation.
 *
 * @author hazard157
 */
public final class D2Size
    implements ID2Size, Serializable {

  private static final long serialVersionUID = -2241593242416606705L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "D2Size"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   * Returned value may be safely casted to {@link D2SizeEdit} (but not to {@link D2Size}).
   */
  public static final IEntityKeeper<ID2Size> KEEPER =
      new AbstractEntityKeeper<>( ID2Size.class, EEncloseMode.ENCLOSES_BASE_CLASS, ID2Size.ZERO ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2Size aEntity ) {
          aSw.writeDouble( aEntity.width() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.height() );
        }

        @Override
        protected ID2Size doRead( IStrioReader aSr ) {
          double width = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double height = aSr.readDouble();
          return new D2SizeEdit( width, height );
        }
      };

  private final double width;
  private final double height;

  /**
   * Constructor.
   *
   * @param aWidth double - the width
   * @param aHeight double - the height
   * @throws TsIllegalArgumentRtException any argument < 0.0
   */
  public D2Size( double aWidth, double aHeight ) {
    width = duck( checkCoor( aWidth ) );
    height = duck( checkCoor( aHeight ) );
    if( width < 0.0 || height < 0.0 ) {
      throw new TsIllegalArgumentRtException();
    }
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Size} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Size( ID2Size aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    width = duck( aSource.width() );
    height = duck( aSource.height() );
  }

  // ------------------------------------------------------------------------------------
  // ID2Size
  //

  @Override
  public double width() {
    return width;
  }

  @Override
  public double height() {
    return height;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return "{" + width + ',' + height + '}'; //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2Size p) ) {
      return false;
    }
    return (p.width() == this.width) && (p.height() == this.height);
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    long dblval = Double.doubleToRawLongBits( width );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    dblval = Double.doubleToRawLongBits( height );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    return result;
  }

}
