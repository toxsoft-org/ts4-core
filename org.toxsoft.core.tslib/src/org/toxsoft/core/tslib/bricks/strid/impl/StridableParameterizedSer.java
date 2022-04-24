package org.toxsoft.core.tslib.bricks.strid.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The same as {@link StridableParameterized} but implements {@link Serializable}.
 *
 * @author hazard157
 */
public class StridableParameterizedSer
    implements IStridableParameterized, IParameterizedEdit, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<StridableParameterizedSer> KEEPER =
      new AbstractStridableParameterizedKeeper<>( StridableParameterizedSer.class, null ) {

        @Override
        protected StridableParameterizedSer doCreate( String aId, IOptionSet aParams ) {
          return new StridableParameterizedSer( aId, aParams );
        }
      };

  private final String         id;
  private final IOptionSetEdit params = new OptionSet();

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public StridableParameterizedSer( String aId, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    params.addAll( aParams );
  }

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public StridableParameterizedSer( String aId ) {
    this( aId, IOptionSet.NULL );
  }

  /**
   * Copy constructor.
   *
   * @param <E> - source object class
   * @param aSrc {@link IStridable} {@link IParameterized} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public <E extends IStridable & IParameterized> StridableParameterizedSer( E aSrc ) {
    this( TsNullArgumentRtException.checkNull( aSrc ).id(), aSrc.params() );
  }

  /**
   * Constructor for special-case subclasses, where {@link #id()} is an empty string.
   */
  protected StridableParameterizedSer() {
    id = TsLibUtils.EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return params.getStr( TSID_NAME, EMPTY_STRING );
  }

  @Override
  public String description() {
    return params.getStr( TSID_DESCRIPTION, EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // protected mutator methods
  //

  protected void setName( String aName ) {
    params.setStr( TSID_NAME, aName );
  }

  protected void setDescription( String aDescription ) {
    params.setStr( TSID_DESCRIPTION, aDescription );
  }

  /**
   * Sets name {@link #nmName()} and {@link #description()}.
   *
   * @param aName String - the name
   * @param aDescription String - the description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void setNameAndDescription( String aName, String aDescription ) {
    if( aName == null || aDescription == null ) {
      throw new TsNullArgumentRtException();
    }
    params.setStr( TSID_NAME, aName );
    params.setStr( TSID_DESCRIPTION, aDescription );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + id();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof StridableParameterizedSer that ) {
      return id.equals( that.id ) && params.equals( that.params );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + id.hashCode();
    result = PRIME * result + params.hashCode();
    return result;
  }

}
