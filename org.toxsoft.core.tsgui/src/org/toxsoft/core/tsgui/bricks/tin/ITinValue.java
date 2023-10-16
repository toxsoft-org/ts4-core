package org.toxsoft.core.tsgui.bricks.tin;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * TIN value is recursively defined tree node representing values for TIN inspector.
 *
 * @author hazard157
 */
public interface ITinValue {

  /**
   * "Unassigned value" singleton.
   */
  ITinValue NULL = new InternalNullTinValue();

  /**
   * Returns the kind of the value (kind of the node in value tree).
   *
   * @return {@link ETinTypeKind} - the kind of value
   */
  ETinTypeKind kind();

  /**
   * Returns the atomic representation of this TIN value if applicable.
   * <p>
   * Only {@link ETinTypeKind#FULL} and {@link ETinTypeKind#ATOMIC} have the atomic representation. Calling this method
   * for value of kind {@link ETinTypeKind#GROUP} will throw an exception.
   *
   * @return {@link IAtomicValue} - the atomic representation of this TIN value
   * @throws TsUnsupportedFeatureRtException the value is of kind {@link ETinTypeKind#GROUP}
   */
  IAtomicValue atomicValue();

  /**
   * Returns the values of the child fields.
   * <p>
   * For leaf nodes returns an empty map.
   *
   * @return {@link IStringMap}&lt;{@link ITinValue}&gt; - the map "child field ID" - "child field value"
   */
  IStringMap<ITinValue> childValues();

  // ------------------------------------------------------------------------------------
  // inline methods for convenience

  @SuppressWarnings( "javadoc" )
  default IAtomicValue findAtmicChild( String aFieldId ) {
    ITinValue childVal = childValues().findByKey( aFieldId );
    if( childVal != null ) {
      if( childVal.kind().hasAtomic() ) {
        return childVal.atomicValue();
      }
    }
    return null;
  }

  @SuppressWarnings( "javadoc" )
  default IAtomicValue getAtmicChild( String aFieldId ) {
    return childValues().getByKey( aFieldId ).atomicValue();
  }

}

class InternalNullTinValue
    implements ITinValue {

  @Override
  public ETinTypeKind kind() {
    return ETinTypeKind.FULL;
  }

  @Override
  public IAtomicValue atomicValue() {
    return IAtomicValue.NULL;
  }

  @Override
  public IStringMap<ITinValue> childValues() {
    return IStringMap.EMPTY;
  }

}
