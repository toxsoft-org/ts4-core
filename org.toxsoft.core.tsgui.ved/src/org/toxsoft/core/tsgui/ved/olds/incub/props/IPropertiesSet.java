package org.toxsoft.core.tsgui.ved.olds.incub.props;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * The read-only set of identifiable properties.
 * <p>
 * Property is an atomic value identified by an IDpath. Properties set is much like an options set with one difference -
 * it is not allowed to add/remove an items to/from it. In fact the read-only properties set <b>is</b> an option set,
 * the difference lies in editing interface {@link IPropertiesSetEdit}.
 *
 * @author hazard157
 */
public interface IPropertiesSet
    extends IOptionSet {

  // nop

}
