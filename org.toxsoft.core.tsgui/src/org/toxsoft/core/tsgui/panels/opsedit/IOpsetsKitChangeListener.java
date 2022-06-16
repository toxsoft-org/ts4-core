package org.toxsoft.core.tsgui.panels.opsedit;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;

/**
 * Listens changes to the {@link IOpsetsKitPanel}.
 *
 * @author hazard157
 */
public interface IOpsetsKitChangeListener {

  /**
   * Informs that user entered valid option value.
   * <p>
   * Note: eventer is paused {@link ITsEventer#isFiringPaused()}=<code>true</code> only last option change value will be
   * remembered and fired after.
   *
   * @param aSource {@link IOpsetsKitPanel} - the event source
   * @param aKitItemId String - kit item ID from list {@link IOpsetsKitPanel#listKitItemDefs()}
   * @param aOptionId String - ID of the option in {@link IOptionSet#keys()}
   * @param aNewValue {@link IAtomicValue} - the new value never is <code>null</code>
   */
  void onOptionValueChanged( IOpsetsKitPanel aSource, String aKitItemId, String aOptionId, IAtomicValue aNewValue );

  /**
   * Informs that user selected another kit in the left pane of {@link IOpsetsKitPanel}.
   * <p>
   * Note: when eventer is paused {@link ITsEventer#isFiringPaused()}=<code>true</code> then only last selection change
   * event will be remembered and fired.
   *
   * @param aSource {@link IOpsetsKitPanel} - the event source
   * @param aKitItemId String - kit item ID from list {@link IOpsetsKitPanel#listKitItemDefs()}
   */
  void onKitItemsSelected( IOpsetsKitPanel aSource, String aKitItemId );

}
