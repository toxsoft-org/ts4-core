package org.toxsoft.core.tslib.bricks;

import org.toxsoft.core.tslib.bricks.coopcomp.*;
import org.toxsoft.core.tslib.gw.time.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mix-in interface of a component that performs its work under the control of a container.
 * <p>
 * This interface declares component must be started (method {@link #start()}) to work and stopped after work. Stopping
 * the component may take a long time. To avoid wasting time of other parts of the program, container requests stop
 * {@link #queryStop()} and periodically asks component if it is stopped via {@link #isStopped()}. When component stops
 * (or stopping takes too much time) the container calls {@link #destroy()}. Destroyed component is not usable any more.
 * <p>
 * Several aspects of the component's operation is out of the scope of this interface:
 * <ul>
 * <li>The component may support <b>reuse</b> after stopping. This interface does not specifies if stop may be called
 * after {@link #isStopped()} returns <code>true</code>. However it is specified that after {@link #destroy()} the
 * component is unusable. Implementation must cleary define if component supports reuse after stopping;</li>
 * <li>The component <b>initialization</b> is out of scope of this interface. COmmon constructor or
 * {@link ITsInitializable} pattern may be used to make component ready to start;</li>
 * <li>How the component implements business logic depends on implementation. Component may have main API for users
 * (note that {@link IWorkerComponent} is a mix-in interface). Other way is to start internal execution thread for
 * business logic purposes. Another option is to implement pattern {@link ICooperativeMultiTaskable} for cooperative
 * multi-tasking or {@link IRealTimeSensitive} / {@link IGwTimeFleetable} for time-sensitive applications;</li>
 * <li>Multiple instances of the component may be allowed or denies;</li>
 * <li>There are different ways of component identification. Simples one is to hold a reference to a created instance
 * and call it's methods directly. Other way is to use an identifier and find instance by ID in the container. The ID
 * may be assigned or be built in the component.</li>
 * <li></li>
 * </ul>
 * <p>
 * Anyway, how the component realizes listed aspects must be declared in concrete implementation.
 *
 * @author hazard157
 */
public interface IWorkerComponent {

  /**
   * The component start - after this method component is in working state.
   * <p>
   * Only after executing this method can you call the component's business logic methods, as well as
   * {@link ICooperativeMultiTaskable#doJob()}, if the class also implements {@link ICooperativeMultiTaskable}.
   * <p>
   * Calling this method on started component does nothing.
   *
   * @throws TsIllegalStateRtException component is in illegal state (eg. already destroyed)
   * @throws TsRuntimeException - the exception based on {@link TsRemoteIoRtException} may be thrown for many reasons
   */
  void start();

  /**
   * Initializes component stopping process.
   * <p>
   * The method returns immediately, even if the unit is not stopped. In this case, the stopping state should be queried
   * using the {@link #isStopped()} method.
   * <p>
   * Calling this method on stopped or stopping component does nothing.
   * <p>
   * Implementation must <b>not</b> throw any exception except listed below..
   *
   * @return <b>true</b> - the component has stopped working, {@link #isStopped()} = <code>true</code>;<br>
   *         <b>false</b> - component is still working, continue with {@link #isStopped()} calls until stop.
   * @throws TsIllegalStateRtException component is in illegal state (eg. already destroyed)
   */
  boolean queryStop();

  /**
   * Determines if component is stopped.
   * <p>
   * The container guarantees that this method is called only after {@link #queryStop()}, and only if the component is
   * not stopped. Once the component is stopped (that is, {@link #isStopped()} = <code>true</code>), this method is no
   * longer called.
   * <p>
   * If a component takes too long to stop voluntarily, the container should forcefully call {@link #destroy()} to
   * terminate the work (maybe not very correctly) and free up resources.
   * <p>
   * Implementation must <b>not</b> throw any exception except listed below..
   *
   * @return <b>true</b> - the component has stopped working, {@link #isStopped()} = <code>true</code>;<br>
   *         <b>false</b> - component is still working, continue with {@link #isStopped()} calls until stop.
   * @throws TsIllegalStateRtException component is in illegal state (eg. already destroyed)
   */
  boolean isStopped();

  /**
   * Unconditionally terminates the job and releases all resources.
   * <p>
   * Must be called after the component has finished working, or if the component has been waiting too long for
   * completion, after a timeout. This method must be called before the component reference is given to the garbage
   * collector.
   * <p>
   * Once this method is called, no other method can or should be called and the reference to the component should be
   * garbage collected.
   * <p>
   * Implementation must <b>not</b> throw any exception.
   */
  void destroy();

}
