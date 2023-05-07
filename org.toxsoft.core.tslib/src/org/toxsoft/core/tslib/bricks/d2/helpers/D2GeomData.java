package org.toxsoft.core.tslib.bricks.d2.helpers;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2GeomData} immutable implementation.
 *
 * @author hazard157
 */
public final class D2GeomData
    implements ID2GeomData, Serializable {

  private static final long serialVersionUID = 7708854009779645308L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "D2GeomData"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   * Returned value may be safely casted to {@link ID2GeomDataEdit} (but not to {@link D2GeomData}).
   */
  public static final IEntityKeeper<ID2GeomData> KEEPER =
      new AbstractEntityKeeper<>( ID2GeomData.class, EEncloseMode.ENCLOSES_BASE_CLASS, ID2GeomData.ZERO ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2GeomData aEntity ) {
          D2Point.KEEPER.write( aSw, aEntity.location() );
          aSw.writeSeparatorChar();
          D2Conversion.KEEPER.write( aSw, aEntity.conversion() );
        }

        @Override
        protected ID2GeomData doRead( IStrioReader aSr ) {
          ID2Point p = D2Point.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ID2Conversion conv = D2Conversion.KEEPER.read( aSr );
          return new D2GeomDataEdit( p, conv );
        }
      };

  private final ID2Point      location;
  private final ID2Conversion conversion;

  /**
   * Constructor.
   * <p>
   * Creates the defensive copies of the arguments.
   *
   * @param aLocation {@link ID2Point} - the location
   * @param aConversion {@link ID2Conversion} - the conversion
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2GeomData( ID2Point aLocation, ID2Conversion aConversion ) {
    location = new D2Point( aLocation );
    conversion = new D2Conversion( aConversion );
  }

  // ------------------------------------------------------------------------------------
  // ID2GeomData
  //

  @Override
  public ID2Point location() {
    return location;
  }

  @Override
  public ID2Conversion conversion() {
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
