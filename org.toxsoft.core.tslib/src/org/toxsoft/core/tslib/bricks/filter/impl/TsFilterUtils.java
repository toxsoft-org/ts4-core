package org.toxsoft.core.tslib.bricks.filter.impl;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.bricks.filter.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.filter.*;

/**
 * Filters support.
 *
 * @author hazard157
 */
public class TsFilterUtils {

  /**
   * {@link ITsSingleFilterParams#NONE} filter type identifier.
   */
  public static final String NONE_FILTER_ID = TS_ID + "filter.NONE"; //$NON-NLS-1$

  /**
   * {@link ITsSingleFilterParams#ALL} filter type identifier.
   */
  public static final String ALL_FILTER_ID = TS_ID + "filter.ALL"; //$NON-NLS-1$

  /**
   * Factory creating {@link ITsFilter#NONE} filter.
   */
  public static final ITsSingleFilterFactory<Object> NONE_FILTER_FACTORY =
      new AbstractTsSingleFilterFactory<>( NONE_FILTER_ID, STR_N_SFF_NONE, STR_D_SFF_NONE, Object.class ) {

        @Override
        protected ITsFilter<Object> doCreateFilter( IOptionSet aParams ) {
          return ITsFilter.NONE;
        }
      };

  /**
   * Factory creating {@link ITsFilter#ALL} filter.
   */
  public static final ITsSingleFilterFactory<Object> ALL_FILTER_FACTORY =
      new AbstractTsSingleFilterFactory<>( ALL_FILTER_ID, STR_N_SFF_ALL, STR_D_SFF_ALL, Object.class ) {

        @Override
        protected ITsFilter<Object> doCreateFilter( IOptionSet aParams ) {
          return ITsFilter.ALL;
        }
      };

  /**
   * Prohibition of descendants creation.
   */
  private TsFilterUtils() {
    // nop
  }

}
