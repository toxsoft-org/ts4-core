package org.toxsoft.core.tslib.coll.primtypes.impl;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An {@link IStringMapEdit} allowing only IDpath/IDnames as the keys.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class StridMap<E>
    extends StringMap<E> {

  private static final long serialVersionUID = 157157L;

  private final boolean allowDupElems;
  private final boolean allowIdPath;

  /**
   * Constructor.
   *
   * @param aIsIdPathAllowed boolean - the flag to allow IDpaths, not only IDnames<br>
   *          <b>true</b> - keys may be IDpaths (and IDnames also, by definition);<br>
   *          <b>false</b> - keys may be only IDname, not IDpaths.
   * @param aIsDupElemsAllowed boolean - the flag to allow duplicate values in the map<br>
   *          <b>true</b> - equal values with different keys are allowed;<br>
   *          <b>false</b> - value equal to existing one with different key can not be added to the map.
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   */
  public StridMap( boolean aIsIdPathAllowed, boolean aIsDupElemsAllowed, int aBucketsCount ) {
    super( aBucketsCount );
    allowIdPath = aIsIdPathAllowed;
    allowDupElems = aIsDupElemsAllowed;
  }

  /**
   * Constructor with default buckets count.
   *
   * @param aIsIdPathAllowed boolean - the flag to allow IDpaths, not only IDnames<br>
   *          <b>true</b> - keys may be IDpaths (and IDnames also, by definition);<br>
   *          <b>false</b> - keys may be only IDname, not IDpaths.
   * @param aIsDupElemsAllowed boolean - the flag to allow duplicate values in the map<br>
   *          <b>true</b> - equal values with different keys are allowed;<br>
   *          <b>false</b> - value equal to existing one with different key can not be added to the map.
   */
  public StridMap( boolean aIsIdPathAllowed, boolean aIsDupElemsAllowed ) {
    allowIdPath = aIsIdPathAllowed;
    allowDupElems = aIsDupElemsAllowed;
  }

  /**
   * Constructor of the map allowing IDpaths as keys and duplicate values.
   */
  public StridMap() {
    this( true, true );
  }

  // --------------------------------------------------------------------------
  // implementation
  //

  @Override
  protected void checkArgsValidity( String aKey, E aElem ) {
    if( allowIdPath ) {
      StridUtils.checkValidIdPath( aKey );
    }
    else {
      StridUtils.checkValidIdName( aKey );
    }
    if( !allowDupElems ) {
      if( findByKey( aKey ) != aElem ) { // check if the same (by reference) item already exists in the map
        TsItemAlreadyExistsRtException.checkTrue( hasElem( aElem ) );
      }
    }
  }

  // --------------------------------------------------------------------------
  // Class API
  //

  /**
   * Determines if map allows only IDnames or IDpaths too.
   *
   * @return boolean - the flag to allow IDpaths, not only IDnames<br>
   *         <b>true</b> - keys may be IDpaths (and IDnames also, by definition);<br>
   *         <b>false</b> - keys may be only IDname, not IDpaths.
   */
  public boolean isIdPathAllowed() {
    return allowIdPath;
  }

  /**
   * Determines if duplicate values may be stored in the map.
   * <p>
   * Value duplicates are checked by the {@link #equals(Object)} method.
   *
   * @return boolean - the flag to allow duplicate values in the map<br>
   *         <b>true</b> - equal values with different keys are allowed;<br>
   *         <b>false</b> - value equal to existing one with different key can not be added to the map.
   */
  public boolean isDupElemsAllowed() {
    return allowDupElems;
  }

}
