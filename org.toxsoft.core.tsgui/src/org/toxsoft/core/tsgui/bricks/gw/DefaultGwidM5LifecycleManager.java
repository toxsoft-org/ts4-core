package org.toxsoft.core.tsgui.bricks.gw;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The default lifecycle manager for {@link GwidM5Model}, considering GWID as simple value-object.
 * <p>
 * No master-object is needed for this lifecycle manager. Supports only creation and editing of entities. Removal and
 * listing is not supported.
 * <p>
 * Created and edits entity based on FIXME ???
 *
 * @author hazard157
 */
public class DefaultGwidM5LifecycleManager
    extends M5LifecycleManager<Gwid, Object> {

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;{@link Skid}&gt; - the model
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public DefaultGwidM5LifecycleManager( IM5Model<Gwid> aModel ) {
    super( aModel, true, true, false, false, null );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  // FIXME ???

}
