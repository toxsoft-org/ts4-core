package org.toxsoft.tsgui.panels.generic;

import org.toxsoft.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.utils.errors.TsRuntimeException;

/**
 * Generci panel to view some entity.
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
   * Detemines if panel content allows to return valid edited entity.
   * <p>
   * If method returns {@link EValidationResultType#ERROR} then {@link #getEntity()} will throw the exception. However
   * if this method succeeds {@link #getEntity()} may still throw an exception. This method just checks if each data in
   * widget and in whole set has valid values to edit the entity, but editing may fail even for valid values. For
   * example because of browken connection to the server.
   *
   * @return {@link ValidationResult} - the check result
   */
  ValidationResult canGetEntity();

}
