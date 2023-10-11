package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * The actor - interactive item of the VED framework.
 *
 * @author hazard157
 */
public interface IVedActor
    extends IVedItem {

  /**
   * Returns list of the VISELs this actor is bound to.
   * <p>
   * If actor is not bound to any VISEL, returns an empty list. In most common case, when actor is bound to VISEL with
   * the property {@link IVedScreenConstants#PROP_VISEL_ID}, returned list contains the single element - value of the
   * mentioned property.
   * <p>
   * The returned list content may change during VED screen editing or at runtime.
   * <p>
   * Note: returned list may contain IDs of non-existing VISELs.
   *
   * @return {@link IStringList} - bound VISEL IDs
   */
  IStringList listBoundViselIds();

}
