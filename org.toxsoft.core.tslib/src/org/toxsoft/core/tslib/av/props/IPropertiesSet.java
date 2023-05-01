package org.toxsoft.core.tslib.av.props;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of the properties set {@link IPropertiesSetRo}.
 *
 * @author hazard157
 */
public interface IPropertiesSet
    extends IPropertiesSetRo, IOpsSetter {

  /**
   * Set several properties values at once.
   * <p>
   * Unknown property ID in argument are silently ignored.
   *
   * @param aNewValues {@link IStringMap}&lt;{@link IAtomicValue}&gt; - map "property ID" - "property value"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException incompatible value type for any property
   */
  void setProps( IStringMap<IAtomicValue> aNewValues );

  /**
   * Sets all properties values to defaults.
   */
  void resetToDefaults();

  /**
   * Returns the properties values change eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IPropertyChangeListener}&gt; - the eventer
   */
  ITsEventer<IPropertyChangeListener> propsEventer();

}
