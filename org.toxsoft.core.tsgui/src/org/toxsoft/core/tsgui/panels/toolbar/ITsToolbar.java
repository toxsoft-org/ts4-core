package org.toxsoft.core.tsgui.panels.toolbar;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Tool bar containing buttons based on {@link ITsActionDef}.
 *
 * @author hazard157
 */
public interface ITsToolbar
    extends ITsBasicToolbar {

  // ------------------------------------------------------------------------------------
  // Configuration
  //

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

  /**
   * Add an action to the end of the toolbar items.
   * <p>
   * Please note that for actions added with this method no {@link ITsActionHandler#handleAction(String)} is
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

}
