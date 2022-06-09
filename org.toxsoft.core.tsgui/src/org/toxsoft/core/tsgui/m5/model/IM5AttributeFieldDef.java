package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Attribute field definition.
 * <p>
 * Attribute is simply a field with value type {@link IAtomicValue}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5AttributeFieldDef<T>
    extends IM5FieldDef<T, IAtomicValue>, IDataType {

  /**
   * Returns the atomic type of the attribute.
   *
   * @return {@link EAtomicType} - atomic type
   */
  @Override
  EAtomicType atomicType();

  /**
   * For attribute fields never returns <code>null</code>, but {@link IAtomicValue#NULL} instead.
   */
  @Override
  IAtomicValue defaultValue();

}
