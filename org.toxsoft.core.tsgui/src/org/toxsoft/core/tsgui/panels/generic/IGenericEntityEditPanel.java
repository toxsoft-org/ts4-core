package org.toxsoft.core.tsgui.panels.generic;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Generic panel to edit some entity.
 * <p>
 * "Editing" may be either changing properties of the existing entity or selecting entity from some kind of collection.
 * <p>
 * Extends viewer panel {@link IGenericEntityPanel}.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericEntityEditPanel<T>
    extends IGenericEntityPanel<T> {

  /**
   * Returns an edited entity.
   * <p>
   * This method may return either the same entity as set by {@link #setEntity(Object)} or new instance if editing leads
   * to new instance creation (as it always happens for immutable classes).
   *
   * @return &lt;T&gt; - edited entity, never is <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canGetEntity()}
   * @throws TsRuntimeException other, implementation-specific errors
   */
  T getEntity();

  /**
   * Determines if panel content allows to return valid edited entity.
   * <p>
   * If method returns {@link EValidationResultType#ERROR} then {@link #getEntity()} will throw the exception. However
   * if this method succeeds {@link #getEntity()} may still throw an exception. This method just checks if each data in
   * widget and in whole set has valid values to edit the entity, but editing may fail even for valid values. For
   * example because of broken connection to the server.
   *
   * @return {@link ValidationResult} - the check result
   */
  ValidationResult canGetEntity();

}
