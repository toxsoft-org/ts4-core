package org.toxsoft.core.tslib.av.metainfo.constr;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A one-time class for constructing an instance of {@link IConstraintInfo}.
 *
 * @author hazard157
 */
public class ConstraintInfoBuilder {

  private final String constraintId;

  private String                       name            = DEFAULT_NAME;
  private String                       description     = EMPTY_STRING;
  private String                       categoryId      = ConstraintUtils.CATEGID_UNCATEGORIZED;
  private boolean                      isNullAllowed   = true;
  private boolean                      isSameType      = false;
  private EAtomicType                  constraintType  = EAtomicType.NONE;
  private boolean                      isOnlyLookup    = false;
  private IList<IAtomicValue>          lookupValues    = IList.EMPTY;
  private IStridablesList<EAtomicType> applicableTypes = EAtomicType.asList();

  /**
   * Constructor.
   * <p>
   * Initializes default settings:
   * <ul>
   * <li>{@link IConstraintInfo#categoryId()} = {@link ConstraintUtils#CATEGID_UNCATEGORIZED};</li>
   * <li>{@link IConstraintInfo#nmName()} = {@link IAvMetaConstants#DEFAULT_NAME};</li>
   * <li>{@link IConstraintInfo#description()} = empty string;</li>
   * <li>{@link IConstraintInfo#isNullAllowed()} = <code>true</code>;</li>
   * <li>{@link IConstraintInfo#isConstraintTypeSameAsDataType()} = <code>false</code> ;</li>
   * <li>{@link IConstraintInfo#constraintType()} = {@link EAtomicType#NONE};</li>
   * <li>{@link IConstraintInfo#isOnlyLookupValuesAllowed()} = <code>false</code>;</li>
   * <li>{@link IConstraintInfo#listLookupValues()} = empty list;</li>
   * <li>{@link IConstraintInfo#listApplicableDataTypes()} = all atomic types, {@link EAtomicType#asList()}.</li>
   * </ul>
   *
   * @param aConstraintId String - the ID of constraint to build the {@link IConstraintInfo} for it
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public ConstraintInfoBuilder( String aConstraintId ) {
    constraintId = StridUtils.checkValidIdPath( aConstraintId );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets value of {@link IConstraintInfo#categoryId()}.
   *
   * @param aCategoryId String - the category ID, an IDpath
   * @return {@link ConstraintInfoBuilder} - this builder to continue build process
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public ConstraintInfoBuilder setCategoryId( String aCategoryId ) {
    categoryId = StridUtils.checkValidIdPath( aCategoryId );
    return this;
  }

  /**
   * Sets {@link IConstraintInfo#nmName()} and {@link IConstraintInfo#description()}.
   *
   * @param aName String - short name
   * @param aDescription String - description
   * @return {@link ConstraintInfoBuilder} - this builder to continue build process
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ConstraintInfoBuilder setNameAndDescription( String aName, String aDescription ) {
    TsNullArgumentRtException.checkNull( aName, aDescription );
    name = aName;
    description = aDescription;
    return this;
  }

  /**
   * Sets value of {@link IConstraintInfo#isNullAllowed()}.
   *
   * @param aIsNullAllowed boolean - the sign that constraint may have value {@link IAtomicValue#NULL}
   * @return {@link ConstraintInfoBuilder} - this builder to continue build process
   */
  public ConstraintInfoBuilder setNullAllowed( boolean aIsNullAllowed ) {
    isNullAllowed = aIsNullAllowed;
    return this;
  }

  /**
   * Sets how constraint value type to be same as data type..
   * <p>
   * Is the same as call to {@link #setConstraintType(boolean, EAtomicType) setConstraintType( true, NONE )}.
   *
   * @return {@link ConstraintInfoBuilder} - this builder to continue build process
   */
  public ConstraintInfoBuilder setConstraintTypeToDataType() {
    setConstraintType( true, EAtomicType.NONE );
    return this;
  }

  /**
   * Sets how constraint value type will be processed.
   *
   * @param aIsSameType boolean - the sign that constraint must have the same type as {@link IDataType#atomicType()}
   * @param aConstraintType {@link EAtomicType} - atomic type of the constraint value
   * @return {@link ConstraintInfoBuilder} - this builder to continue build process
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ConstraintInfoBuilder setConstraintType( boolean aIsSameType, EAtomicType aConstraintType ) {
    isSameType = aIsSameType;
    constraintType = aConstraintType;
    return this;
  }

  /**
   * Sets lookup values settings.
   *
   * @param aIsOnlyLookup boolean - determines if only lookup values are allowed
   * @param aLookupValues {@link IList}&gt;{@link IAtomicValue}&gt; - lookup values to be set as a constraint value
   * @return {@link ConstraintInfoBuilder} - this builder to continue build process
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ConstraintInfoBuilder setLookupValues( boolean aIsOnlyLookup, IList<IAtomicValue> aLookupValues ) {
    TsNullArgumentRtException.checkNulls( aLookupValues );
    isOnlyLookup = aIsOnlyLookup;
    lookupValues = new ElemArrayList<>( aLookupValues );
    return this;
  }

  /**
   * Sets {@link IConstraintInfo#listApplicableDataTypes()}.
   *
   * @param aApplicableTypes {@link IStridablesList}&gt;{@link EAtomicType}&lt; - applicable types
   * @return {@link ConstraintInfoBuilder} - this builder to continue build process
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException applicable types list is empty
   */
  public ConstraintInfoBuilder setApplicableDataTypes( IStridablesList<EAtomicType> aApplicableTypes ) {
    TsNullArgumentRtException.checkNull( aApplicableTypes );
    TsIllegalArgumentRtException.checkTrue( aApplicableTypes.isEmpty() );
    applicableTypes = new StridablesList<>( aApplicableTypes );
    return this;
  }

  /**
   * Builds the instance of {@link IConstraintInfo},
   *
   * @return {@link IConstraintInfo} - the new instance of {@link IConstraintInfo}
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException applicable types list is empty
   * @throws TsIllegalArgumentRtException lookup values list is empty when not allowed so
   */
  public IConstraintInfo build() {
    return new ConstraintInfo( constraintId, name, description, categoryId, isNullAllowed, isSameType, constraintType,
        isOnlyLookup, lookupValues, applicableTypes );
  }

  // TODO ValidationResult canBuild();

}
