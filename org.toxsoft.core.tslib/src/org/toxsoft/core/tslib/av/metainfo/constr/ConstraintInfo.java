package org.toxsoft.core.tslib.av.metainfo.constr;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IConstraintInfo} immutable implementation.
 *
 * @author hazard157
 */
public final class ConstraintInfo
    extends Stridable
    implements IConstraintInfo {

  private final String                        categoryId;
  private final boolean                       isNullAllowed;
  private final EAtomicType                   constraintType;
  private final boolean                       isSameType;
  private final boolean                       isOnlyLookup;
  private final IList<IAtomicValue>           lookupValues;
  private final ITsNameProvider<IAtomicValue> lookupValuesNameProvider;
  private final String                        valobjValueKeeperId;
  private final IStridablesList<EAtomicType>  applicableTypes;

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
   * @param aLookupNameProvider {@link ITsNameProvider}&lt;{@link IAtomicValue}&gt; - lookup values name provider
   * @param aValobjValueKeeperId String - constraint value keeper ID or an empty string if not applicable
   * @param aApplicableTypes {@link IStridablesList}&gt;{@link EAtomicType}&lt; - applicable types
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException applicable types list is empty
   * @throws TsIllegalArgumentRtException lookup values list is empty when not allowed so
   */
  public ConstraintInfo( String aId, String aName, String aDescription, String aCategoryId, boolean aIsNullAllowed,
      boolean aIsSameType, EAtomicType aConstraintType, boolean aIsOnlyLookup, IList<IAtomicValue> aLookupValues,
      ITsNameProvider<IAtomicValue> aLookupNameProvider, String aValobjValueKeeperId,
      IStridablesList<EAtomicType> aApplicableTypes ) {
    super( aId, aName, aDescription );
    // check preconditions
    TsNullArgumentRtException.checkNulls( aCategoryId, aConstraintType, aLookupValues, aLookupNameProvider,
        aValobjValueKeeperId, aApplicableTypes );
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
    lookupValuesNameProvider = aLookupNameProvider;
    valobjValueKeeperId = aValobjValueKeeperId;
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
  public ITsNameProvider<IAtomicValue> lookupValuesNameProvider() {
    return lookupValuesNameProvider;
  }

  @Override
  public String valobjValueKeeperId() {
    return valobjValueKeeperId;
  }

  @Override
  public IStridablesList<EAtomicType> listApplicableDataTypes() {
    return applicableTypes;
  }

  @Override
  public ValidationResult validateConstraint( EAtomicType aDataAtomicType, IAtomicValue aConstraintValue ) {
    TsNullArgumentRtException.checkNulls( aDataAtomicType, aConstraintValue );
    // check atomic type is applicable
    if( !applicableTypes.hasElem( aDataAtomicType ) ) {
      return ValidationResult.error( "Constraint '%s' is not applicable to the data type '%d'", id(),
          aDataAtomicType.id() );
    }
    // check if NULL it is allowed
    if( aConstraintValue == IAtomicValue.NULL && !isNullAllowed ) {
      return ValidationResult.error( "Constraint '%s' does not allows NULL value", id() );
    }
    // check value has same type as data if requested so
    if( isConstraintTypeSameAsDataType() ) {
      if( !AvTypeCastRtException.canAssign( aDataAtomicType, aConstraintValue.atomicType() ) ) {
        return ValidationResult.error( "Constraint '%s': value type '%s' must be the same as data type '%s'", id(),
            aConstraintValue.atomicType().id(), aDataAtomicType.id() );
      }
    }
    // check value has type assignable to constraintType()
    if( !AvTypeCastRtException.canAssign( constraintType, aConstraintValue.atomicType() ) ) {
      return ValidationResult.error( "Constraint '%s': value type '%s' must be of requested type '%s'", id(),
          aConstraintValue.atomicType().id(), constraintType().id() );
    }
    // check value is from lookup list if requested so
    if( isOnlyLookupValuesAllowed() ) {
      if( !lookupValues.hasElem( aConstraintValue ) ) {
        return ValidationResult.error( "Constraint '%s': value '%s' is not in allowed lookup values list", id(),
            aConstraintValue.toString() );
      }
    }
    return ValidationResult.SUCCESS;
  }

}
