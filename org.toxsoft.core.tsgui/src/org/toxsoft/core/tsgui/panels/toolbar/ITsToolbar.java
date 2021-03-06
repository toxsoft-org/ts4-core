package org.toxsoft.core.tsgui.panels.toolbar;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Tool bar containing buttons based on {@link ITsActionDef}.
 *
 * @author hazard157
 */
public interface ITsToolbar
    extends ILazyControl<Control>, IIconSizeable {

  // ------------------------------------------------------------------------------------
  // Configuration
  //

  /**
   * Sets the icon size of the buttons on toolbar.
   * <p>
   * This is the initial configuration method. It must be called before {@link #createControl(Composite)}, after widget
   * is created calling this method has no effect.
   *
   * @param aIconSize {@link EIconSize} - the icons size
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  @Override
  void setIconSize( EIconSize aIconSize );

  /**
   * Determines if toolbar is set configured as vertical one.
   * <p>
   * By default toolbar is horizontal.
   * <p>
   * Please note that this property only determines if {@link SWT#VERTICAL} bit will be set when creating
   * {@link ToolBarManager}. Clients must explicitly layout toolbar widget {@link #getControl()} for vertical layout
   * (for exmple as {@link BorderLayout#EAST} in {@link BorderLayout} parent composite.
   *
   * @return boolean - <code>true</code> for vertical toolbaer
   */
  boolean isVertical();

  /**
   * Configures toolbar as vertical or horizontal one (the value of {@link #isVertical()} property).
   * <p>
   * This is the initial configuration method. It must be called before {@link #createControl(Composite)}, after widget
   * is created calling this method has no effect.
   *
   * @param aVartical boolean - <code>true</code> for vertical toolbaer
   */
  void setVertical( boolean aVartical );

  /**
   * Returns the text displayed on name label.
   * <p>
   * Name label is the very first (leftmost) control on the toolbar.
   *
   * @return String - the name label text or an empty string if there is no name label
   */
  String getNameLabelText();

  /**
   * Sets the name lebel text {@link #getNameLabelText()}.
   * <p>
   * Name label text can be changed at any time.
   *
   * @param aText String - the name label text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setNameLabelText( String aText );

  /**
   * Returns the pop-up tooltip text.
   * <p>
   * Tooltip of toolbar is shown when mouse stops over empty places of toolbar or on name label.
   *
   * @return String - the tooltip text
   */
  String getTooltipText();

  /**
   * Sets the tooltip text @{@link #getTooltipText()}.
   * <p>
   * Tooltip text can be changed at any time.
   *
   * @param aTooltip String - the tooltip text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setTooltipText( String aTooltip );

  // ------------------------------------------------------------------------------------
  // Actions management
  //

  /**
   * Finds the action of specified ID.
   *
   * @param aActionId String - the action ID
   * @return {@link TsAction} - the action or <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsAction findAction( String aActionId );

  /**
   * Returns the action of specified ID.
   *
   * @param aActionId String - the action ID
   * @return {@link TsAction} - the action
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  TsAction getAction( String aActionId );

  /**
   * Returns list of button items - the items with corresponding buttons toolbar.
   * <p>
   * Method {@link #listButtonItems()} returns button items, that is items {@link #listAllItems()} without separators.
   *
   * @return {@link IList}&lt;{@link ITsActionDef}&gt; - list of button items in order of appearance
   */
  IList<ITsActionDef> listButtonItems();

  /**
   * Returns list of all items.
   * <p>
   * Method {@link #listButtonItems()} returns button items, that is items {@link #listAllItems()} without separators.
   *
   * @return {@link IList}&lt;{@link ITsActionDef}&gt; - list of all items
   */
  IList<ITsActionDef> listAllItems();

  /**
   * Determines if action button is anabled.
   *
   * @param aActionId String - the action ID
   * @return boolean - the enabled state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  boolean isActionEnabled( String aActionId );

  /**
   * Sets if the action button is enable.
   *
   * @param aActionId String - the action ID
   * @param aEnabled boolean - the enabled state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionEnabled( String aActionId, boolean aEnabled );

  /**
   * Returns the checked state of the action button.
   * <p>
   * If action is not of style {@link TsAction#AS_CHECK_BOX} or {@link TsAction#AS_RADIO_BUTTON} method returns
   * <code>false</code>.
   *
   * @param aActionId String - the action ID
   * @return boolean - the check state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isActionChecked( String aActionId );

  /**
   * Sets the action button checked state.
   * <p>
   * If action is not of style {@link TsAction#AS_CHECK_BOX} or {@link TsAction#AS_RADIO_BUTTON} method does nothing.
   *
   * @param aActionId String - the action ID
   * @param aChecked boolean - the check state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionChecked( String aActionId, boolean aChecked );

  /**
   * Sets the action button text.
   * <p>
   * Please note that for buttons with images no text is dispayed.
   *
   * @param aActionId String - the action ID
   * @param aText String - the text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionText( String aActionId, String aText );

  /**
   * Sets the action button image.
   *
   * @param aActionId String - the action ID
   * @param aImageDescriptior {@link ImageDescriptor} - image descriptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionImage( String aActionId, ImageDescriptor aImageDescriptior );

  /**
   * Sets the action button tooltip text.
   *
   * @param aActionId String - the action ID
   * @param aText String - the tooltip
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionTooltipText( String aActionId, String aText );

  /**
   * Sets the menu to the drop-down menu item.
   *
   * @param aActionId String - the action ID
   * @param aMenuCreator {@link IMenuCreator} - the menu to the action or <code>null</code> for no menu
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   * @throws TsIllegalStateRtException the item is not of {@link TsAction#AS_DROP_DOWN_MENU} style
   */
  void setActionMenu( String aActionId, IMenuCreator aMenuCreator );

  /**
   * Add an action to the end of the toolbar items.
   * <p>
   * Please note that for actions added with this method no {@link ITsToolbarListener#onToolButtonPressed(String)} is
   * called. Action must have implrmrnted method {@link Action#run()} or {@link Action#runWithEvent(Event)}.
   *
   * @param aAction {@link TsAction} - the action
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addAction( TsAction aAction );

  /**
   * Adds the action definition based button to the end of the toolbar items.
   *
   * @param aActionDef {@link ITsActionDef} - button action definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException action with same ID already exists
   */
  void addActionDef( ITsActionDef aActionDef );

  /**
   * Adds the action definition based buttons to the end of the toolbar items.
   *
   * @param aActionDefs {@link IList}&lt;{@link ITsActionDef}&gt; - button action definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException action with same ID already exists
   */
  void addActionDefs( IList<ITsActionDef> aActionDefs );

  /**
   * Adds the action definition based buttons to the end of the toolbar items.
   *
   * @param aActionDefs {@link ITsActionDef}[] - button action definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException action with same ID already exists
   */
  void addActionDefs( ITsActionDef... aActionDefs );

  /**
   * Adds the separator to the end of the toolbar items.
   */
  void addSeparator();

  /**
   * Adds user specified item to the end of the toolbar items.
   *
   * @param aItem {@link IContributionItem} - user-specified contribution item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   */
  void addContributionItem( IContributionItem aItem );

  // ------------------------------------------------------------------------------------
  // Action handling
  //

  /**
   * Add the listener.
   * <p>
   * Already added listeners are ignored.
   * <p>
   * Note: for actions added with {@link #addAction(TsAction)} no listener is called!
   *
   * @param aListener {@link ITsToolbarListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addListener( ITsToolbarListener aListener );

  /**
   * Removes the listener.
   * <p>
   * If listener was not added then method does nothing.
   *
   * @param aListener {@link ITsToolbarListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeListener( ITsToolbarListener aListener );

}
