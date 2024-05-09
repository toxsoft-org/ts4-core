package org.toxsoft.core.tslib.math.cond;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Parameters for single condition (more precisely, condition checker) creation.
 * <p>
 * In may comments this interface (and subclasses) are references by <b>SCP</b> abbreviation.
 *
 * @author hazard157
 */
public interface ITsSingleCondInfo {

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
  ITsSingleCondInfo NEVER = new InternalNeverSingleCondParams();

  /**
   * Options for creating a condition that is never met.
   */
  ITsSingleCondInfo ALWAYS = new InternalAlwaysSingleCondParams();

  /**
   * Returns the identifier of the condition type.
   * <p>
   * This is the same identifier as {@link ITsSingleCondType#id()} of the corresponding type description.
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

/**
 * Internal class for {@link ITsSingleCondInfo#NEVER} singleton implementation.
 *
 * @author hazard157
 */
class InternalNeverSingleCondParams
    implements ITsSingleCondInfo, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsCombiCondInfo#NEVER} will be read correctly.
   *
   * @return Object - always {@link ITsCombiCondInfo#NEVER}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsSingleCondInfo.NEVER;
  }

  @Override
  public String typeId() {
    return TYPE_ID_NEVER;
  }

  @Override
  public IOptionSet params() {
    return IOptionSet.NULL;
  }

  @Override
  public String toString() {
    return TYPE_ID_NEVER;
  }

}

/**
 * Internal class for {@link ITsSingleCondInfo#ALWAYS} singleton implementation.
 *
 * @author hazard157
 */
class InternalAlwaysSingleCondParams
    implements ITsSingleCondInfo, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsCombiCondInfo#ALWAYS} will be read correctly.
   *
   * @return Object - always {@link ITsCombiCondInfo#ALWAYS}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsSingleCondInfo.ALWAYS;
  }

  @Override
  public String typeId() {
    return TYPE_ID_ALWAYS;
  }

  @Override
  public IOptionSet params() {
    return IOptionSet.NULL;
  }

  @Override
  public String toString() {
    return TYPE_ID_ALWAYS;
  }

}
