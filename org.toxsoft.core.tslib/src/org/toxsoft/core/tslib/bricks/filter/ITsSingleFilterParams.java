package org.toxsoft.core.tslib.bricks.filter;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.filter.impl.TsFilterUtils;

/**
 * Parameters for single filter creation.
 *
 * @author hazard157
 */
public interface ITsSingleFilterParams {

  /**
   * Parameters for {@link ITsFilter} creating {@link ITsFilter#NONE NONE} filter.
   */
  ITsSingleFilterParams NONE = new InternalNoneSingleFilterParams();

  /**
   * Parameters for {@link ITsFilter} creating {@link ITsFilter#ALL ALL} filter.
   */
  ITsSingleFilterParams ALL = new InternalAllSingleFilterParams();

  /**
   * Returns the identifier of the filter.
   *
   * @return String - the identifier (IDpath) of the filter
   */
  String typeId();

  /**
   * Returns the filter parameters.
   *
   * @return {@link IOptionSet} - the filter parameters
   */
  IOptionSet params();

}

/**
 * Internal class for {@link ITsSingleFilterParams#NONE} singleton implementation.
 *
 * @author hazard157
 */
class InternalNoneSingleFilterParams
    implements ITsSingleFilterParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsSingleFilterParams#NONE} will be read correctly.
   *
   * @return Object - always {@link ITsSingleFilterParams#NONE}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsSingleFilterParams.NONE;
  }

  @Override
  public String typeId() {
    return TsFilterUtils.NONE_FILTER_ID;
  }

  @Override
  public IOptionSet params() {
    return IOptionSet.NULL;
  }

}

/**
 * Internal class for {@link ITsSingleFilterParams#ALL} singleton implementation.
 *
 * @author hazard157
 */
class InternalAllSingleFilterParams
    implements ITsSingleFilterParams, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsSingleFilterParams#ALL} will be read correctly.
   *
   * @return Object - always {@link ITsSingleFilterParams#ALL}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsSingleFilterParams.ALL;
  }

  @Override
  public String typeId() {
    return TsFilterUtils.ALL_FILTER_ID;
  }

  @Override
  public IOptionSet params() {
    return IOptionSet.NULL;
  }

}
