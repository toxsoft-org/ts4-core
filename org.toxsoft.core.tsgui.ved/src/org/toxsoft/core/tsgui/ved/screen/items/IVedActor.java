package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The actor - interactive item of the VED framework.
 *
 * @author hazard157
 */
public interface IVedActor
    extends IVedItem {

  /**
   * Определяет может ли быть привязан хотя-бы к одному {@link IVedVisel}.
   *
   * @return <b>true</b> - актор модет быть привязан<br>
   *         <b>false</b> - у актора не предполагается привязки а визелю
   */
  boolean isBoudable();

  /**
   * Returns list of the VISELs this actor is bound to.
   * <p>
   * If actor is not bound to any VISEL, returns an empty list. In most common case, when actor is bound to VISEL with
   * the property {@link IVedScreenConstants#PROP_VISEL_ID}, returned list contains the single element - value of the
   * mentioned property.
   * <p>
   * The returned list content may change during VED screen editing or at runtime.
   * <p>
   * Notes:
   * <ul>
   * <li>generally actor may be bound to several VISELs, this method returns only specified IDs of VISELs. That is, if
   * VISEL ID is not specified (an empty string) or is invalid (non IDpath) such items must not be included in the
   * returned list;</li>
   * <li>returned list may contain IDs of non-existing VISELs.</li>
   * </ul>
   *
   * @return {@link IStringList} - bound VISEL IDs
   */
  IStringList listBoundViselIds();

  /**
   * Replaces an existing binding of the actor to the VISEL.
   * <p>
   * Notes:
   * <ul>
   * <li>the new VISEL ID may be an empty string, meaning that binding must be cancelled;</li>
   * <li>both old or new VISELs may not exist;</li>
   * <li>does nothing if old and new VISEL IDs are the same;</li>
   * <li>if actor has no binding to the <code>aOldViselId</code> then method does nothing.</li>
   * </ul>
   *
   * @param aOldViselId String - the ID of VISEL this actor currently is bind to (must be an IDpath)
   * @param aNewViselId String - the ID of the replacement VISEL or an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>aOldViselId</code> is not an IDpath
   * @throws TsIllegalArgumentRtException <code>aNewViselId</code> is not an empty string or an IDpath
   */
  void replaceBoundVisel( String aOldViselId, String aNewViselId );

}
