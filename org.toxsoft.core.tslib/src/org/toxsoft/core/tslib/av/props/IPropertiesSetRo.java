package org.toxsoft.core.tslib.av.props;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * The read-only set of identifiable properties.
 * <p>
 * Property is an atomic value identified by an IDpath. Properties set is much like an options set with one difference -
 * it is not allowed to add/remove an items to/from it. In fact the read-only properties set <b>is</b> an option set,
 * the difference lies in editing interface {@link IPropertiesSet}.
 *
 * @author hazard157
 */
public interface IPropertiesSetRo
    extends IOptionSet {

  /**
   * Returns list if property identifiers.
   *
   * @return {@link IStringList} - property IDs list
   */
  IStringList ids();

}
