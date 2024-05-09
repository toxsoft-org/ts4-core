package org.toxsoft.core.tslib.bricks;

/**
 * Mixin interface of an object capable to work in cooperative multitaskable environment.
 * <p>
 * This interface should be implemented by objects that work with similar ones. Cooperative multitasking is an
 * alternative to multiple threads of execution.
 * <p>
 * Be careful when implemention cooperative multitasking. Objects performing {@link #doJob()} task for a long time may
 * "hang" other objects.
 *
 * @author hazard157
 */
public interface ICooperativeMultiTaskable {

  /**
   * Implementation must perform next part of its job as fast as possible.
   * <p>
   * This method is called for all competitive objects sequentially.
   * <p>
   * Depending on implementation and state of object this method may throw runtime exceptions.
   */
  void doJob();

}
