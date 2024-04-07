package org.toxsoft.core.tslib.math.combicond;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;

/**
 * Parameters for single condition (more precisely, condition checker) creation.
 *
 * @author hazard157
 */
public interface ISingleCondParams {

  /**
   * Type ID of the {@link #NEVER} constant.
   */
  String TYPE_ID_NEVER = "never"; //$NON-NLS-1$

  /**
   * Type ID of the {@link #ALWAYS} constant.
   */
  String TYPE_ID_ALWAYS = "always"; //$NON-NLS-1$

  /**
   * Parameters to create a condition that is always met.
   */
  // FIXME change to local class with readResolve()
  ISingleCondParams NEVER = new SingleCondParams( TYPE_ID_NEVER, IOptionSet.NULL );

  /**
   * Options for creating a condition that is never met.
   */
  // FIXME change to local class with readResolve()
  ISingleCondParams ALWAYS = new SingleCondParams( TYPE_ID_ALWAYS, IOptionSet.NULL );

  /**
   * Returns the identifier of the condition type.
   * <p>
   * This is the same identifier as {@link ISingleCondType#id()} of the corresponding type description.
   *
   * @return String - the identifier (IDpath) of the condition type
   */
  String typeId();

  /**
   * Returns the condition parameter values.
   *
   * @return {@link IOptionSet} - the condition parameter values
   */
  IOptionSet params();

}
