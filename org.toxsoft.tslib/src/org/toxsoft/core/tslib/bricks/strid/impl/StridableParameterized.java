package org.toxsoft.core.tslib.bricks.strid.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.av.utils.IParameterizedEdit;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.IStridableParameterized;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IStridableParameterized} base implementation.
 * <p>
 * Unlike {@link Stridable}, this implementation of {@link IStridable} stores {@link #nmName()} and
 * {@link #description()} values in the {@link #params()} set in the {@link IAvMetaConstants#TSID_NAME} and
 * {@link IAvMetaConstants#TSID_DESCRIPTION} options respectively.
 * <p>
 * This class may be subclasses (including immatable classes) or used directly.
 * <p>
 * Subclass may be implemented as immutable or mutable classes. To make subclass mutable one should override
 * <code>setXxx()</code> mutator methods with <code>public</code> access modifier.
 *
 * @author hazard157
 */
public class StridableParameterized
    implements IStridableParameterized, IParameterizedEdit {

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<StridableParameterized> KEEPER =
      new AbstractStridableParameterizedKeeper<>( StridableParameterized.class, null ) {

        @Override
        protected StridableParameterized doCreate( String aId, IOptionSet aParams ) {
          return new StridableParameterized( aId, aParams );
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
  public StridableParameterized( String aId, IOptionSet aParams ) {
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
  public StridableParameterized( String aId ) {
    this( aId, IOptionSet.NULL );
  }

  /**
   * Constructor for special-case subclasses, where {@link #id()} is an empty string.
   */
  protected StridableParameterized() {
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
    if( aThat instanceof StridableParameterized that ) {
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
