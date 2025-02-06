package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.singlemodown.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5SingleModownFieldDef} implementation.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - embeddable entity type
 */
public class M5SingleModownFieldDef<T, V>
    extends M5SingleLinkFieldDefBase<T, V>
    implements IM5SingleModownFieldDef<T, V> {

  /**
   * Constructor.
   * <p>
   * By default {@link #canUserSelectNull()} = <code>false</code>.
   *
   * @param aId String - the field ID (an IDpath)
   * @param aItemModelId String - the ID of the model {@link #itemModel()} (anIDpath)
   * @param aIdsAndValues Object[] - identifier / value pairs of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public M5SingleModownFieldDef( String aId, String aItemModelId, Object... aIdsAndValues ) {
    super( aId, aItemModelId );
    params().addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    setValedEditor( ValedSingleModownEditor.FACTORY_NAME );
  }

}
