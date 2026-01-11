package org.toxsoft.core.tslib.av.metainfo.constr;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IConstraintInfo} immutable implementation.
 *
 * @author hazard157
 */
public final class ConstraintInfo
    extends Stridable
    implements IConstraintInfo {

  private final String                       categoryId;
  private final boolean                      isNullAllowed;
  private final EAtomicType                  constraintType;
  private final boolean                      isSameType;
  private final boolean                      isOnlyLookup;
  private final IList<IAtomicValue>          lookupValues;
  private final IStridablesList<EAtomicType> applicableTypes;

  /**
   * Constructor.
   *
   * @param aId String - the constraint ID
   * @param aName String - short name
   * @param aDescription String - description
   * @param aCategoryId String - the category ID, an IDpath
   * @param aIsNullAllowed boolean - the sign that constraint may have value {@link IAtomicValue#NULL}
   * @param aIsSameType boolean - the sign that constraint must have the same type as {@link IDataType#atomicType()}
   * @param aConstraintType {@link EAtomicType} - atomic type of the constraint value
   * @param aIsOnlyLookup boolean - determines if only lookup values are allowed
   * @param aLookupValues {@link IList}&gt;{@link IAtomicValue}&gt; - lookup values to be set as a constraint value
   * @param aApplicableTypes {@link IStridablesList}&gt;{@link EAtomicType}&lt; - applicable types
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException applicable types list is empty
   * @throws TsIllegalArgumentRtException lookup values list is empty when not allowed so
   */
  public ConstraintInfo( String aId, String aName, String aDescription, String aCategoryId, boolean aIsNullAllowed,
      boolean aIsSameType, EAtomicType aConstraintType, boolean aIsOnlyLookup, IList<IAtomicValue> aLookupValues,
      IStridablesList<EAtomicType> aApplicableTypes ) {
    super( aId, aName, aDescription );
    // check preconditions
    TsNullArgumentRtException.checkNulls( aCategoryId, aConstraintType, aLookupValues, aApplicableTypes );
    StridUtils.checkValidIdPath( aCategoryId );
    TsIllegalArgumentRtException.checkTrue( aApplicableTypes.isEmpty() );
    if( aIsOnlyLookup && !aIsNullAllowed && aLookupValues.isEmpty() ) {
      throw new TsIllegalArgumentRtException();
    }
    //
    categoryId = aCategoryId;
    isNullAllowed = aIsNullAllowed;
    isSameType = aIsSameType;
    constraintType = aConstraintType;
    isOnlyLookup = aIsOnlyLookup;
    lookupValues = new ElemArrayList<>( aLookupValues );
    applicableTypes = new StridablesList<>( aApplicableTypes );
  }

  // ------------------------------------------------------------------------------------
  // IConstraintInfo
  //

  @Override
  public String categoryId() {
    return categoryId;
  }

  @Override
  public boolean isNullAllowed() {
    return isNullAllowed;
  }

  @Override
  public boolean isConstraintTypeSameAsDataType() {
    return isSameType;
  }

  @Override
  public EAtomicType constraintType() {
    return constraintType;
  }

  @Override
  public boolean isOnlyLookupValuesAllowed() {
    return isOnlyLookup;
  }

  @Override
  public IList<IAtomicValue> listLookupValues() {
    return lookupValues;
  }

  @Override
  public IStridablesList<EAtomicType> listApplicableDataTypes() {
    return applicableTypes;
  }

}
