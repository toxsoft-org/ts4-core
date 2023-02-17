package org.toxsoft.core.tslib.bricks;

import org.toxsoft.core.tslib.utils.*;

/**
 * Mix-in interface of an object that needs additional initialization after constructor.
 * <p>
 * This interface means that it is necessary the method {@link #initialize()} to be called immediately after
 * constructor. There may be several reasons for this kind of two-step initialization:
 * <ul>
 * <li>the object is pair of base class and subclass and base implementation needs to call overridden subclass method
 * for initialization to be completed. However it is impossible to call subclass method from base class
 * constructor;</li>
 * <li>the object is one of the services in service based application. Generally, service needs other service instances
 * to be created in order to finish own initialization. In such case services manager creates all instances and than
 * calls {@link #initialize()} of created instances;</li>
 * <li>the object is created by some container that supports dependency injection. To complete initialization injected
 * field values are needed. Usually @PostConstruct annotation is used but sometimes {@link #initialize()} may be
 * preferable.</li>
 * </ul>
 * Often {@link ICloseable} interface is used together with this interface.
 *
 * @author hazard157
 */
public interface IInitializable {

  /**
   * Implementation may finish initialization process started in constructor.
   * <p>
   * Called once after constructor but before any other method of the implementing class.
   */
  void initialize();

}
