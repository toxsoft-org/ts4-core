package org.toxsoft.core.tsgui.panels.opsedit;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to edit kit of option sets at once.
 * <p>
 * Kit items are option sets. Each option set to be edited shall be accompanied with {@link IStridable} description.
 * <p>
 * Panel consists of:
 * <ul>
 * <li>left list of {@link IOpsetsKitItemDef} descriptions;</li>
 * <li>right {@link IOptionSetPanel} with currently selected item options editors.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IOpsetsKitPanel
    extends ILazyControl<Control> {

  /**
   * Sets the kit item definitions specified by {@link #setKitItemDefs(IStridablesList)}.
   * <p>
   * Order of items in argument list specifies order of appearance in left pane of this panel.
   * <p>
   * Changing item definitions also resets individual option values to {@link IDataDef#defaultValue()}.
   *
   * @param aItemDefs {@link IStridablesList}&lt;{@link IOpsetsKitItemDef}&gt; - list of item definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setKitItemDefs( IStridablesList<IOpsetsKitItemDef> aItemDefs );

  /**
   * Returns kit item definitions specified by {@link #setKitItemDefs(IStridablesList)}.
   * <p>
   * Initially list of item definitions is empty.
   *
   * @return {@link IStridablesListEdit}&lt;{@link IOpsetsKitItemDef}&gt; - list of item definitions
   */
  IStridablesListEdit<IOpsetsKitItemDef> listKitItemDefs();

  /**
   * Returns the ID of kite item currently selected in the left pane.
   *
   * @return String - selected kit item ID or <code>null</code> if no kit item is selected
   */
  String currentSelectedKitItemId();

  /**
   * Sets selected kit item in the left panel and hence opens it's option set for editing in right pane.
   * <p>
   * On selection change fire the {@link IOpsetsKitChangeListener#onKitItemsSelected(IOpsetsKitPanel, String)} event.
   *
   * @param aKitItemId String - selected kit item ID or <code>null</code> to deselect any kit items
   */
  void setCurrentSelectedKitItemId( String aKitItemId );

  /**
   * Sets the option values of specified kit item.
   * <p>
   * If no such kit item is listed in {@link IOpsetsKitPanel#listKitItemDefs()} than method does nothing. Option may may
   * contain only part of the options listed in kit item definition {@link IOpsetsKitItemDef#optionDefs()}. Values of
   * missing options will be initialized by default values from the corresponding {@link IDataDef}.
   * <p>
   * Extra option values not listed in kit item definition will remain unchanged and will be returned by
   * {@link #getKitOptionValues(String)}.
   * <p>
   * Does not fires any event.
   *
   * @param aKitItemId String - kit item ID
   * @param aValues {@link IOptionSet} - the options values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setKitOptionValues( String aKitItemId, IOptionSet aValues );

  /**
   * Returns the option values of specified kit item.
   * <p>
   * Returned set contains at least values for each option lited in kit definition
   * {@link IOpsetsKitItemDef#optionDefs()}. Besides, if {@link #setKitOptionValues(String, IOptionSet)} specified
   * additional values they will be retained intact.
   *
   * @param aKitItemId String - kit item ID
   * @return {@link IOptionSet} - the options values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such kit exists in {@link #listKitItemDefs()}
   */
  IOptionSet getKitOptionValues( String aKitItemId );

  /**
   * Sets values of option sets for kit items.
   * <p>
   * Repeatedly calls {@link #setKitOptionValues(String, IOptionSet)} for each key in argument map.
   * <p>
   * Does not fires any event.
   *
   * @param aAllValues {@link IStringMap}&lt;{@link IOptionSet}&gt; - map "kit itemID" - "kit item values"
   */
  void setAllKitOptionValues( IStringMap<IOptionSet> aAllValues );

  /**
   * Returns values of all option sets of all kit items.
   * <p>
   * Returns map collected by call to {@link #getKitOptionValues(String)} for each item.
   *
   * @return {@link IStringMap}&lt;{@link IOptionSet}&gt; - map "kit itemID" - "kit item values"
   */
  IStringMap<IOptionSet> getAllKitOptionValues();

  /**
   * Returns the eventer of change events in this panel. Returns
   *
   * @return {@link ITsEventer}&lt;{@link IOpsetsKitChangeListener}&gt; - the eventer
   */
  ITsEventer<IOpsetsKitChangeListener> eventer();

}
