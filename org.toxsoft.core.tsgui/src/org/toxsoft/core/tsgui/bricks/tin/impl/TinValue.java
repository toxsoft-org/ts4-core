package org.toxsoft.core.tsgui.bricks.tin.impl;

import java.util.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinValue} implementation.
 * <p>
 * Instances of this class should be created using static constructors <code>TinValue.ofXxx()</code>.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class TinValue
    implements ITinValue {

  private final ETinTypeKind          kind;
  private final IAtomicValue          atomicValue;
  private final IStringMap<ITinValue> childValues;

  private TinValue( IAtomicValue aAtomicValue, IStringMap<ITinValue> aChildValues, ETinTypeKind aKind ) {
    atomicValue = aAtomicValue;
    childValues = aChildValues;
    kind = aKind;
  }

  /**
   * Creates the value of kind {@link ETinTypeKind#ATOMIC}.
   *
   * @param aValue {@link IAtomicValue} - the atomic value
   * @return {@link ITinValue} - created instance
   */
  public static ITinValue ofAtomic( IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    return new TinValue( aValue, null, ETinTypeKind.ATOMIC );
  }

  /**
   * Creates the value of kind {@link ETinTypeKind#GROUP}.
   *
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - the child values
   * @return {@link ITinValue} - created instance
   */
  public static ITinValue ofGroup( IStringMap<ITinValue> aChildValues ) {
    TsNullArgumentRtException.checkNull( aChildValues );
    IStringMapEdit<ITinValue> valsMap = new StringMap<>();
    for( String key : aChildValues.keys() ) {
      StridUtils.checkValidIdPath( key );
      valsMap.put( key, aChildValues.getByKey( key ) );
    }
    return new TinValue( null, valsMap, ETinTypeKind.GROUP );
  }

  /**
   * Creates the value of kind {@link ETinTypeKind#FULL}.
   *
   * @param aValue {@link IAtomicValue} - the atomic value
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - the child values
   * @return {@link ITinValue} - created instance
   */
  public static ITinValue ofFull( IAtomicValue aValue, IStringMap<ITinValue> aChildValues ) {
    TsNullArgumentRtException.checkNulls( aValue, aChildValues );
    IStringMapEdit<ITinValue> valsMap = new StringMap<>();
    for( String key : aChildValues.keys() ) {
      StridUtils.checkValidIdPath( key );
      valsMap.put( key, aChildValues.getByKey( key ) );
    }
    return new TinValue( aValue, valsMap, ETinTypeKind.FULL );
  }

  // ------------------------------------------------------------------------------------
  // ITinValue
  //

  @Override
  public ETinTypeKind kind() {
    TsUnsupportedFeatureRtException.checkNull( kind ); // there will be the instance of NULL
    return kind;
  }

  @Override
  public IAtomicValue atomicValue() {
    TsUnsupportedFeatureRtException.checkNull( atomicValue );
    return atomicValue;
  }

  @Override
  public IStringMap<ITinValue> childValues() {
    TsUnsupportedFeatureRtException.checkNull( childValues );
    return childValues;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    if( atomicValue != null ) {
      return atomicValue.asString();
    }
    return String.format( "childValues[%d]", Integer.valueOf( childValues.size() ) ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TinValue that ) {
      return this.kind == that.kind && //
          Objects.equals( this.atomicValue, that.atomicValue ) && //
          Objects.equals( this.childValues, that.childValues );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + kind.hashCode();
    if( atomicValue != null ) {
      result = TsLibUtils.PRIME * result + atomicValue.hashCode();
    }
    if( childValues != null ) {
      result = TsLibUtils.PRIME * result + childValues.hashCode();
    }
    return result;
  }

}
