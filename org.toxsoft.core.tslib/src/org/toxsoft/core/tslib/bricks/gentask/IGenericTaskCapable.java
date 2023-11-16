package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Mix-in interface of entities capable to run several declared generic tasks.
 *
 * @author hazard157
 */
public interface IGenericTaskCapable {

  /**
   * Returns supported generic tasks runners.
   *
   * @return {@link IStringMap}&lt;{@link IGenericTaskRunner}&gt; - map "generaic task ID" - "task runner"
   */
  IStringMap<IGenericTaskRunner> getGenericTaskRunners();

}
