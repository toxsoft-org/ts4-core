package org.toxsoft.tslib.bricks.filter;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.tslib.math.logicop.ELogicalOp;
import org.toxsoft.tslib.utils.errors.TsUnsupportedFeatureRtException;

/**
 * Parameters for combined filter creation.
 * <p>
 * Combined filter is considered either as single filter or two combi filters with logical operation.
 * <p>
 * Combined filer (when {@link #isSingle()}=<code>false</code>) consist of two filters {@link #left()} and
 * {@link #right()} combined with logical {@link ELogicalOp} operation. To the filter logical NOT operation may be
 * applied (as defined by {@link #isInverted()}).
 * <p>
 * Single filer (when {@link #isSingle()}=<code>true</code>) consists of one {@link #single()} filter and optional NOT
 * operation {@link #isInverted()}.
 *
 * @author hazard157
 */
public interface ITsCombiFilterParams {

  /**
   * Parameters for creating {@link ITsFilter#NONE NONE} filter as combined filter.
   */
  ITsCombiFilterParams NONE = new InternalNoneFilterParams();

  /**
   * Parameters for creating {@link ITsFilter#ALL ALL} filter as combined filter.
   */
  ITsCombiFilterParams ALL = new InternalAllFilterParams();

  /**
   * Determines if this is the single filter.
   *
   * @return boolean - the single filter flag<br>
   *         <b>true</b> - this is single filter, {@link #single()} and {@link #isInverted()} are used;<br>
   *         <b>false</b> - this is combi filter, {@link #left()}, {@link #op()}, {@link #right()} and
   *         {@link #isInverted()} are used.
   */
  boolean isSingle();

  /**
   * Returns the single filter parameters.
   *
   * @return {@link ITsSingleFilterParams} - the single filter parameters
   * @throws TsUnsupportedFeatureRtException this is combi filter (ie {@link #isSingle()} = <code>false</code>)
   */
  ITsSingleFilterParams single();

  /**
   * Returns the left filter parameters.
   *
   * @return {@link ITsCombiFilterParams} - the left filter parameters
   * @throws TsUnsupportedFeatureRtException this is single filter (ie {@link #isSingle()} = <code>true</code>)
   */
  ITsCombiFilterParams left();

  /**
   * return the logical operation between left and right filter.
   *
   * @return {@link ELogicalOp} - the logical operation between left and right filter
   * @throws TsUnsupportedFeatureRtException this is single filter (ie {@link #isSingle()} = <code>true</code>)
   */
  ELogicalOp op();

  /**
   * Returns the right filter parameters.
   *
   * @return {@link ITsCombiFilterParams} - the right filter parameters
   * @throws TsUnsupportedFeatureRtException this is single filter (ie {@link #isSingle()} = <code>true</code>)
   */
  ITsCombiFilterParams right();

  /**
   * Determones if filter operation must be inverted (NOT applied).
   *
   * @return boolean - the flag of the NOT unary operation
   */
  boolean isInverted();

}

/**
 * Internal class for {@link ITsCombiFilterParams#NONE} singleton implementation.
 *
 * @author hazard157
 */
class InternalNoneFilterParams
    implements ITsCombiFilterParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsCombiFilterParams#NONE} will be read correctly.
   *
   * @return Object - always {@link ITsCombiFilterParams#NONE}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsCombiFilterParams.NONE;
  }

  @Override
  public boolean isSingle() {
    return true;
  }

  @Override
  public ITsSingleFilterParams single() {
    return ITsSingleFilterParams.NONE;
  }

  @Override
  public ITsCombiFilterParams left() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ELogicalOp op() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ITsCombiFilterParams right() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public boolean isInverted() {
    return false;
  }

}

/**
 * Internal class for {@link ITsCombiFilterParams#ALL} singleton implementation.
 *
 * @author hazard157
 */
class InternalAllFilterParams
    implements ITsCombiFilterParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsCombiFilterParams#ALL} will be read correctly.
   *
   * @return Object - always {@link ITsCombiFilterParams#ALL}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsCombiFilterParams.ALL;
  }

  @Override
  public boolean isSingle() {
    return true;
  }

  @Override
  public ITsSingleFilterParams single() {
    return ITsSingleFilterParams.ALL;
  }

  @Override
  public ITsCombiFilterParams left() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ELogicalOp op() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ITsCombiFilterParams right() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public boolean isInverted() {
    return false;
  }

}
