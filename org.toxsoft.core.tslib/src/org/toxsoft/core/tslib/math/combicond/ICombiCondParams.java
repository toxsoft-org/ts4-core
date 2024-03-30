package org.toxsoft.core.tslib.math.combicond;

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
 * <li>???;</li>
 * <li>???.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface ICombiCondParams {

  /**
   * Returns optional name used to identify the condition.
   *
   * @return String - optional human-readable short name
   */
  String name();

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
