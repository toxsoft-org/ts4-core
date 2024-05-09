package org.toxsoft.core.tsgui.bricks.cond;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GUI panel to edit {@link ITsSingleCondInfo}.
 * <p>
 * Contains:
 * <ul>
 * <li>{@link ITsSingleCondInfo#typeId()} selector drop-down combo;</li>
 * <li>main {@link IOptionSetPanel} to edit {@link ITsSingleCondInfo#params()}.</li>
 * </ul>
 * Condition parameters editor {@link IOptionSetPanel} content depends on option definitions for the selected condition
 * type ID. To find the {@link ITsSingleCondType#paramDefs()} the {@link #topicManager()} with the types descriptions
 * list {@link ITsConditionsTopicManager#listSingleCondTypes()} is used.
 *
 * @author hazard157
 */
public interface IPanelSingleCondInfo
    extends IGenericEntityEditPanel<ITsSingleCondInfo> {

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
