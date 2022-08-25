package org.toxsoft.core.tslib.av.opset;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link IOptionSet}.
 *
 * @author hazard157
 */
public interface IOptionSetEdit
    extends IOptionSet, IOpsSetter, IStringMapEdit<IAtomicValue>, IOpsBatchEdit {

  /**
   * Removes parameter from the set.
   *
   * @param aId String - identifier of the option to be removed
   * @return {@link IAtomicValue} - an removed value or <code>null</code> if there was no value
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  IAtomicValue remove( String aId );

  /**
   * Removes parameter from the set.
   *
   * @param aOpId {@link IDataDef} - identifier of the option to be removed
   * @return {@link IAtomicValue} - an removed value or <code>null</code> if there was no value
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  IAtomicValue remove( IDataDef aOpId );

}
