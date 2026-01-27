package org.toxsoft.core.tslib.bricks;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A component in a container that realizes cooperative multitasking.
 * <p>
 * This interface does not introduce new methods, but only combines two parent interfaces -
 * {@link ICooperativeMultiTaskable} and {@link IWorkerComponent}.
 *
 * @author hazard157
 */
public interface ICooperativeWorkerComponent
    extends ICooperativeMultiTaskable, IWorkerComponent {

  /**
   * Method from the parent interface {@link ICooperativeMultiTaskable}.
   * <p>
   * It is assumed that the container has a main thread of execution. The main thread calls this method in a loop on all
   * container components. Having a mechanism such as {@link ICooperativeMultiTaskable#doJob()} allows you to avoid
   * creating separate threads in components that periodically do small work (for example, checking whether a timer has
   * expired and generating a message).
   *
   * @throws TsIllegalStateRtException component has already stopped working
   */
  @Override
  void doJob();

}
