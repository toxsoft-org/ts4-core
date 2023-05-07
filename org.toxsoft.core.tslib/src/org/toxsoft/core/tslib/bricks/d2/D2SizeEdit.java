package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2SizeEdit} implementation.
 *
 * @author hazard157
 */
public final class D2SizeEdit
    implements ID2SizeEdit, Serializable {

  private static final long serialVersionUID = 8377194535843766206L;

  private double width  = 0.0;
  private double height = 0.0;

  /**
   * Constructor.
   *
   * @param aWidth double - the width
   * @param aHeight double - the height
   * @throws TsIllegalArgumentRtException any argument < 0
   * @throws TsIllegalArgumentRtException invalid double number
   */
  public D2SizeEdit( double aWidth, double aHeight ) {
    setSize( aWidth, aHeight );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Size} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2SizeEdit( ID2Size aSource ) {
    setSize( aSource );
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
  // ID2SizeEdit
  //

  @Override
  public void setWidth( double aWidth ) {
    double w = duck( checkCoor( aWidth ) );
    TsIllegalArgumentRtException.checkTrue( w < 0.0 );
    width = w;
  }

  @Override
  public void setHeight( double aHeight ) {
    double h = duck( checkCoor( aHeight ) );
    TsIllegalArgumentRtException.checkTrue( h < 0.0 );
    height = h;
  }

  @Override
  public void setSize( double aWidth, double aHeight ) {
    double w = duck( checkCoor( aWidth ) );
    double h = duck( checkCoor( aHeight ) );
    TsIllegalArgumentRtException.checkTrue( w < 0.0 || h < 0.0 );
    width = w;
    height = h;
  }

  @Override
  public void setSize( ID2Size aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    width = duck( aSource.width() );
    height = duck( aSource.height() );
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
    if( !(object instanceof D2SizeEdit p) ) {
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
