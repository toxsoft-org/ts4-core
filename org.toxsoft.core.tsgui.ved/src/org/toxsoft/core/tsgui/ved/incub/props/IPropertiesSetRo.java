package org.toxsoft.core.tsgui.ved.incub.props;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;

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

  /***
   * Returns the properties values change eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IPropertyChangeListener}&gt; - the eventer
   */
  ITsEventer<IPropertyChangeListener> propsEventer();

}
