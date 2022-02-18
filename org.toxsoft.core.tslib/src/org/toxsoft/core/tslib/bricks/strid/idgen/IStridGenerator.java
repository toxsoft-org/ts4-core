package org.toxsoft.core.tslib.bricks.strid.idgen;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Strid identifiers generator.
 * <p>
 * Implementation may generate IDpath, IDname or mixed unique STRIDs.
 * <p>
 * Subsequent calls no {@link #nextId()} generates unique STRIDs until the method {@link #setState(IOptionSet)} call.
 * Call to {@link #setState(IOptionSet)} may change generated STRIDs, for example, when reset to the initial state,
 * generator is allowed to generate the same STRIDs as before state reset. However each instance may add stringer rules
 * of STRID uniquiness, for example, UUID-based generator may have no state at all aand all instances may generate
 * globally unique STRIDs.
 * <p>
 * In general {@link IStridGenerator} implementations are not thread-safe. Use {@link SynchronizedStridGeneratorWrapper}
 * adapter to create thread-safe instances.
 *
 * @author hazard157
 */
public interface IStridGenerator {

  /**
   * Returns the next unique ID.
   *
   * @return String - generated STRID
   * @throws TsIllegalStateRtException all unique STRID were generated, can generate more
   */
  String nextId();

  /**
   * Returns initial state of the generator.
   * <p>
   * Initial state is the state of this instance as it was immediately after constructor.
   * <p>
   * May be used to store generator state to be restored by {@link #setState(IOptionSet)}.
   *
   * @return {@link IOptionSet} - initial state of the generator
   */
  IOptionSet getInitialState();

  /**
   * Saves current state of the generators. Сохраняет текущее состояние генератора для дальнейшего продолжения методом
   * {@link #setState(IOptionSet)}.
   * <p>
   * May be used to store generator state to be restored by {@link #setState(IOptionSet)}.
   *
   * @return {@link IOptionSet} - current state of the generator
   */
  IOptionSet getState();

  /**
   * Sets current state of the generator.
   * <p>
   * May be used to restore generator state saved by {@link #getState()}.
   *
   * @param aState {@link IOptionSet} - the generator state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #validateState(IOptionSet)}
   */
  void setState( IOptionSet aState );

  /**
   * Checks if state is valid and can be applied to the generator.
   *
   * @param aState {@link IOptionSet} - the state to validate
   * @return {@link ValidationResult} - vailidation result
   */
  ValidationResult validateState( IOptionSet aState );

}
