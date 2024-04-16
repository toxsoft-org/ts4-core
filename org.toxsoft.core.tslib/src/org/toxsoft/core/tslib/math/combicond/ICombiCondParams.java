package org.toxsoft.core.tslib.math.combicond;

import java.io.*;

import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parameters for combined condition (more precisely, condition checker) creation.
 * <p>
 * Combined condition is considered either as single condition or two combined conditions with logical operation.
 * <p>
 * Combined condition (when {@link #isSingle()}=<code>false</code>) consist of two conditions {@link #left()} and
 * {@link #right()} combined with logical {@link ELogicalOp} operation. To the condition logical NOT operation may be
 * applied (as defined by {@link #isInverted()}).
 * <p>
 * Single condition (when {@link #isSingle()}=<code>true</code>) consists of one {@link #single()} condition and
 * optional NOT operation {@link #isInverted()}.
 * <p>
 * TODO comment how to use
 * <ul>
 * <li>extends {@link SingleCondType} with <code>create()</code> method, use
 * {@link SingleCondType#checkCreationArg(ISingleCondParams)};</li>
 * <li>??? create own implementations of IMyCondition.ALWAYS, NEVET;</li>
 * <li>??? create own implementations of IMyCondType.ALWAYS, NEVET;</li>
 * <li>???.</li>
 * </ul>
 * <p>
 * In may comments this interface (and subclasses) are references by <b>CCP</b> abbreviation.
 *
 * @author hazard157
 */
public interface ICombiCondParams {

  /**
   * Parameters to create a combined condition that is always met.
   */
  ICombiCondParams ALWAYS = new InternalAlwaysCombiCondParams();

  /**
   * Options for creating a combined condition that is never met.
   */
  ICombiCondParams NEVER = new InternalNeverCombiCondParams();

  /**
   * Determines if this is the single condition.
   *
   * @return boolean - the single condition flag<br>
   *         <b>true</b> - this is single condition, {@link #single()} and {@link #isInverted()} are used;<br>
   *         <b>false</b> - this is combined condition, {@link #left()}, {@link #op()}, {@link #right()} and
   *         {@link #isInverted()} are used.
   */
  boolean isSingle();

  /**
   * Returns the single condition parameter values.
   *
   * @return {@link ISingleCondParams} - the single condition parameter values
   * @throws TsUnsupportedFeatureRtException this is combined condition (ie {@link #isSingle()} = <code>false</code>)
   */
  ISingleCondParams single();

  /**
   * Returns the left condition parameter values.
   *
   * @return {@link ICombiCondParams} - the left condition parameter values
   * @throws TsUnsupportedFeatureRtException this is single condition (ie {@link #isSingle()} = <code>true</code>)
   */
  ICombiCondParams left();

  /**
   * Returns the logical operation between left and right conditions.
   *
   * @return {@link ELogicalOp} - the logical operation between left and right condition
   * @throws TsUnsupportedFeatureRtException this is single condition (ie {@link #isSingle()} = <code>true</code>)
   */
  ELogicalOp op();

  /**
   * Returns the right condition parameter values.
   *
   * @return {@link ICombiCondParams} - the right condition parameter values
   * @throws TsUnsupportedFeatureRtException this is single condition (ie {@link #isSingle()} = <code>true</code>)
   */
  ICombiCondParams right();

  /**
   * Determines if condition operation must be inverted (NOT applied).
   *
   * @return boolean - the flag of the NOT unary operation
   */
  boolean isInverted();

}

/**
 * Internal class for {@link ICombiCondParams#ALWAYS} singleton implementation.
 *
 * @author hazard157
 */
class InternalAlwaysCombiCondParams
    implements ICombiCondParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ICombiCondParams#ALWAYS} will be read correctly.
   *
   * @return Object - always {@link ICombiCondParams#ALWAYS}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ICombiCondParams.ALWAYS;
  }

  @Override
  public boolean isSingle() {
    return true;
  }

  @Override
  public ISingleCondParams single() {
    return ISingleCondParams.ALWAYS;
  }

  @Override
  public ICombiCondParams left() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ELogicalOp op() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ICombiCondParams right() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public boolean isInverted() {
    return false;
  }

}

/**
 * Internal class for {@link ICombiCondParams#NEVER} singleton implementation.
 *
 * @author hazard157
 */
class InternalNeverCombiCondParams
    implements ICombiCondParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ICombiCondParams#NEVER} will be read correctly.
   *
   * @return Object - always {@link ICombiCondParams#NEVER}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ICombiCondParams.NEVER;
  }

  @Override
  public boolean isSingle() {
    return true;
  }

  @Override
  public ISingleCondParams single() {
    return ISingleCondParams.NEVER;
  }

  @Override
  public ICombiCondParams left() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ELogicalOp op() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ICombiCondParams right() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public boolean isInverted() {
    return false;
  }

}
