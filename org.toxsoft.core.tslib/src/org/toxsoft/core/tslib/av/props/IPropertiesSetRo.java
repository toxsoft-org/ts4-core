package org.toxsoft.core.tslib.av.props;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The read-only set of identifiable properties.
 * <p>
 * Property is an atomic value identified by an IDpath. Properties set is much like an options set with one difference -
 * it is not allowed to add/remove an items to/from it. In fact the read-only properties set <b>is</b> an option set,
 * the difference lies in editing interface {@link IPropertiesSet}.
 *
 * @author hazard157
 * @param <S> - event source type, the entity characterized by the properties
 */
public interface IPropertiesSetRo<S>
    extends IOptionSet {

  /**
   * Returns the information about properties defined in this set.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Returns the properties values change eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IPropertyChangeListener}&gt; - the eventer
   */
  ITsEventer<IPropertyChangeListener<S>> propsEventer();

}
