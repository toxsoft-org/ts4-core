package org.toxsoft.core.tsgui.bricks.cond;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GUI panel to edit {@link ITsCombiCondInfo}.
 * <p>
 * Contains:
 * <ul>
 * <li>formula string editor - SWT {@link Text} control to edit logical formula;</li>
 * <li>single conditions editor- with list of keywords mentioned in the formula at left and {@link IPanelSingleCondInfo}
 * of selected keyword at right;</li>
 * <li>optional validation pane - {@link ValidationResultPanel} displaying the formula correctness.</li>
 * </ul>
 * When editing single conditions, the {@link #topicManager()} with the types descriptions list
 * {@link ITsConditionsTopicManager#listSingleCondTypes()} is used.
 *
 * @author hazard157
 */
public interface IPanelCombiCondInfo
    extends IGenericEntityEditPanel<ITsCombiCondInfo> {

  /**
   * Determines if panel content editing is allowed right now.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> always returns <code>false</code>.
   *
   * @return boolean - edit mode flag
   */
  boolean isEditable();

  /**
   * Toggles panel content edit mode.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> this method does nothing.
   *
   * @param aEditable boolean - edit mode flag
   */
  void setEditable( boolean aEditable );

  /**
   * Returns the topic manager used for types information retrieval.
   * <p>
   * Topic manager must be set by {@link #setTopicManager(ITsConditionsTopicManager)}, otherwise this method will return
   * <code>null</code>.
   *
   * @return {@link ITsConditionsTopicManager} - the topic manager or <code>null</code>
   */
  ITsConditionsTopicManager topicManager();

  /**
   * Sets the topic manager used for condition editing.
   *
   * @param aTopicManager {@link ITsConditionsTopicManager} - the topic manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setTopicManager( ITsConditionsTopicManager aTopicManager );

}
