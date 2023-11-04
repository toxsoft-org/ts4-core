package org.toxsoft.core.tsgui.panels.toolbar;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Tool bar containing buttons based on {@link ITsActionDef}.
 *
 * @author hazard157
 */
public interface ITsBasicToolbar
    extends ILazyControl<Control>, IIconSizeable {

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
   * Returns the enabled state of the action button if any.
   *
   * @param aActionId String - the action ID
   * @return boolean - the enabled state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  boolean isActionEnabled( String aActionId );

  /**
   * Sets the enabled state of the action button if any.
   *
   * @param aActionId String - the action ID
   * @param aEnabled boolean - the enabled state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setActionEnabled( String aActionId, boolean aEnabled );

  /**
   * Returns the checked state of the action button if any.
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
   * Sets the checked state of the action button if any.
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
   * Sets the text of the action button if any.
   * <p>
   * Please note that for buttons with images no text is displayed.
   *
   * @param aActionId String - the action ID
   * @param aText String - the text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionText( String aActionId, String aText );

  /**
   * Sets the image of the action button if any.
   *
   * @param aActionId String - the action ID
   * @param aImageDescriptior {@link ImageDescriptor} - image descriptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionImage( String aActionId, ImageDescriptor aImageDescriptior );

  /**
   * Sets the tooltip text of the action button if any.
   *
   * @param aActionId String - the action ID
   * @param aText String - the tooltip
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsItemNotFoundRtException no item of specified ID found
   */
  void setActionTooltipText( String aActionId, String aText );

  /**
   * Sets the menu to the drop-down menu item if any.
   *
   * @param aActionId String - the action ID
   * @param aMenuCreator {@link IMenuCreator} - the menu to the action or <code>null</code> for no menu
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException toolbar widget is not created yet
   * @throws TsIllegalStateRtException the item is not of {@link TsAction#AS_DROP_DOWN_MENU} style
   */
  void setActionMenu( String aActionId, IMenuCreator aMenuCreator );

  /**
   * Adds the action definition based buttons to the end of the toolbar items.
   *
   * @param aActionDefs {@link IList}&lt;{@link ITsActionDef}&gt; - button action definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException action with same ID already exists
   */
  void setActionDefs( IList<ITsActionDef> aActionDefs );

  /**
   * Adds the action definition based buttons to the end of the toolbar items.
   *
   * @param aActionDefs {@link ITsActionDef}[] - button action definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException action with same ID already exists
   */
  void setActionDefs( ITsActionDef... aActionDefs );

  // ------------------------------------------------------------------------------------
  // Inline methods for convinience

  @SuppressWarnings( "javadoc" )
  default boolean hasAction( String aActionId ) {
    return findAction( aActionId ) != null;
  }

  // ------------------------------------------------------------------------------------
  // Action run listener

  /**
   * Add the listener.
   * <p>
   * Already added listeners are ignored.
   *
   * @param aListener {@link ITsActionHandler} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addListener( ITsActionHandler aListener );

  /**
   * Removes the listener.
   * <p>
   * If listener was not added then method does nothing.
   *
   * @param aListener {@link ITsActionHandler} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeListener( ITsActionHandler aListener );

}
