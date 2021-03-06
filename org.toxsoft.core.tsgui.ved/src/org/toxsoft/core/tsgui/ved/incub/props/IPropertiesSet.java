package org.toxsoft.core.tsgui.ved.incub.props;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.opset.*;
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
   * Set several properies values at once.
   * <p>
   * Unknown property ID in argument are silently ignored.
   *
   * @param aNewValues {@link IStringMap}&lt;{@link IAtomicValue}&gt; - map "property ID" - "property value"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException incomatibe value type for any property
   */
  void setProps( IStringMap<IAtomicValue> aNewValues );

}
