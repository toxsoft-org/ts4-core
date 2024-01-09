package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Mix-in interface of entities capable to run several declared generic tasks.
 *
 * @author hazard157
 */
public interface IGenericTasksProvider {

  /**
   * Returns supported generic tasks.
   *
   * @return {@link IStringMap}&lt;{@link IGenericTask}&gt; - map "generic task ID" - "the task"
   */
  IStringMap<IGenericTask> listSupportedTasks();

}
