package org.toxsoft.core.tsgui.ved.api.items;

import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedVisel} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractActor
    extends VedAbstractItem
    implements IVedActor {

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedAbstractActor( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs ) {
    super( aConfig, aPropDefs );
  }

  // ------------------------------------------------------------------------------------
  // IVedActor
  //

}
