package org.toxsoft.core.tslib.bricks.coopcomp;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The stridable container manages lifecycle and work of the components {@link ITsStridableComponent}.
 * <p>
 * Note: {@link ITsStridableContainer} itself is a {@link ITsStridableComponent}. As for common component, method
 * {@link #compInitArgs()} returns the container init parameters.
 *
 * @author mvk
 */
public interface ITsStridableContainer
    extends ITsStridableComponent {

  /**
   * Returns the list of components contained in this container.
   * <p>
   * Returned list may may contain not all units added with {@link #addComponent(AbstractTsStridableComponent)}. During
   * initialization the failed components are removed from the list.
   *
   * @return {@link IList}&lt;{@link ITsStridableComponent}&gt; - the list of components
   */
  IList<ITsStridableComponent> componentList();

  /**
   * Add component to the container, initializes and starts immediately.
   * <p>
   * The method brings the component into compliance with the state of the container:
   * <ul>
   * <li>{@link ETsCoopCompState#CREATED} - adds component to {@link #componentList()};</li>
   * <li>{@link ETsCoopCompState#INITIALIZED} - initializes and on success adds component to
   * {@link #componentList()};</li>
   * <li>{@link ETsCoopCompState#WORKING} - initializes and on success starts and adds component to
   * {@link #componentList()};</li>
   * <li>{@link ETsCoopCompState#STOPPING} - method does nothing;</li>
   * <li>{@link ETsCoopCompState#DESTROYED} - method does nothing.</li>
   * </ul>
   *
   * @param aComponent AbstractTsStridableComponent - the component
   * @return {@link ValidationResult} - result of initialization or {@link ValidationResult#SUCCESS}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument aComponent is not in state {@link ETsCoopCompState#CREATED}
   * @throws TsItemAlreadyExistsRtException component is already in the box
   */
  ValidationResult addComponent( AbstractTsStridableComponent aComponent );

  /**
   * Stops and removes component from the container.
   * <p>
   * Method request component stop and returns immediately. Actual stopping and component removal may take a long time.
   * <p>
   * Invoking method for already stopping components or components not contained in the container has no effect.
   *
   * @param aComponentId String - the component ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeComponent( String aComponentId );

}
