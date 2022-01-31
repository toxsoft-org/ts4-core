package org.toxsoft.core.tslib.av.metainfo;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.utils.IParameterized;

/**
 * Atimic data type - defines atomic type and type parameters.
 *
 * @author hazard157
 */
public interface IDataType
    extends IParameterized {

  /**
   * Returns the atomic type of the data.
   *
   * @return {@link EAtomicType} - atomic type
   */
  EAtomicType atomicType();

  /**
   * Returns type parameters.
   *
   * @return ITypeConstraints - atomic data type parameters
   */
  @Override
  IOptionSet params();

}
