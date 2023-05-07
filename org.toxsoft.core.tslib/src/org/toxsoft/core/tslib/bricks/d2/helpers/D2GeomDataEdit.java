package org.toxsoft.core.tslib.bricks.d2.helpers;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2GeomData} immutable implementation.
 *
 * @author hazard157
 */
public final class D2GeomDataEdit
    implements ID2GeomDataEdit, Serializable {

  private static final long serialVersionUID = 7708854009779645308L;

  private final ID2PointEdit      location;
  private final ID2ConversionEdit conversion;

  /**
   * Constructor.
   *
   * @param aLocation {@link ID2Point} - the location
   * @param aConversion {@link ID2Conversion} - the conversion
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2GeomDataEdit( ID2Point aLocation, ID2Conversion aConversion ) {
    location = new D2PointEdit( aLocation );
    conversion = new D2ConversionEdit( aConversion );
  }

  // ------------------------------------------------------------------------------------
  // ID2GeomData
  //

  @Override
  public ID2PointEdit location() {
    return location;
  }

  @Override
  public ID2ConversionEdit conversion() {
    return conversion;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ": " + location.toString() + ',' + conversion.toString(); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( object instanceof ID2GeomData p ) {
      return this.location.equals( p.location() ) && this.conversion.equals( p.conversion() );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + location.hashCode();
    result = PRIME * result + conversion.hashCode();
    return result;
  }

}
