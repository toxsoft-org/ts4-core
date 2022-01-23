package org.toxsoft.tslib.bricks;

/**
 * An object is capable to work in cooperative multitaskable environment.
 * <p>
 * This interface should be implemented by objects that work with similar ones. Cooperative multitasking is an
 * alternative to multiple threads of execution.
 * <p>
 * <p>
 * Be carefull when implemention cooperative multitasking. Objects performing {@link #doJob()} task for a long time may
 * "hang" other objects.
 *
 * @author hazard157
 */
public interface ICooperativeMultiTaskable {

  /**
   * Implementation must perform next part of its job as fast as possible.
   * <p>
   * This method is calld for all cooparative objects sequently.
   * <p>
   * Depending on implementation and state of object this method may throw runtime exceptions.
   */
  void doJob();

}
