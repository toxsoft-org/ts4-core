package org.toxsoft.core.tsgui.bricks.tin;

import org.toxsoft.core.tsgui.utils.*;
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

  /**
   * Returns the visualizer of the field value.
   * <p>
   * The string {@link ITsVisualsProvider#getName(Object)} is used as text displayed in the "Value" cell of the
   * inspector row.
   * <p>
   * If not explicitly specified for field uses default implementation {@link ITinTypeInfo#valueVisualizer()}.
   *
   * @return {@link ITsVisualsProvider}&lt;{@link ITinValue}&gt; - field value visualizer, never is <code>null</code>
   */
  ITsVisualsProvider<ITinValue> valueVisualizer();

}
