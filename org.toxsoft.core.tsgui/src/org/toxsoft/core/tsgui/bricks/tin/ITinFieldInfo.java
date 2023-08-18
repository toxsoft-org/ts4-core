package org.toxsoft.core.tsgui.bricks.tin;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * The field of the object to be inspected.
 * <p>
 * Implements {@link IStridable}, where {@link #id()} is a field ID unique among other fields of the object,
 * {@link #nmName()} and {@link #description()} are human-readable representation of the field. Options in
 * {@link #params()} are additional constraints having higher priority than the options from
 * {@link ITinTypeInfo#dataType()} params.
 *
 * @author hazard157
 */
public interface ITinFieldInfo
    extends IStridableParameterized {

  /**
   * Returns the type of the field.
   *
   * @return {@link ITinTypeInfo} - the fied type information
   */
  ITinTypeInfo typeInfo();

}
