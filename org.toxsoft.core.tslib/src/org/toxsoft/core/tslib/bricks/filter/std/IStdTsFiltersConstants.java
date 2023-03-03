package org.toxsoft.core.tslib.bricks.filter.std;

import org.toxsoft.core.tslib.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.std.av.*;
import org.toxsoft.core.tslib.bricks.filter.std.paramed.*;
import org.toxsoft.core.tslib.bricks.filter.std.string.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Standard (built in tslib) filters constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public interface IStdTsFiltersConstants {

  /**
   * Standard filter type IDs prefix.
   */
  String STD_FILTERID_ID_PREFIX = ITsHardConstants.TS_ID + "filter.std";

  /**
   * List of {@link String} objects filter factories declared in this package.
   */
  IStridablesList<ITsSingleFilterFactory<String>> TS_STRING_FILTER_FACTORIES = new StridablesList<>( //
      StdFilterStringMatcher.FACTORY //
  );

  /**
   * List of {@link IAtomicValue} objects filter factories declared in this package.
   */
  IStridablesList<ITsSingleFilterFactory<IAtomicValue>> TS_AV_FILTER_FACTORIES = new StridablesList<>( //
      StdFilterAtimicValueVsConst.FACTORY //
  );

  /**
   * List of {@link IParameterized} objects filter factories declared in this package.
   */
  IStridablesList<ITsSingleFilterFactory<IParameterized>> TS_PARAMETERIZED_FILTER_FACTORIES = new StridablesList<>( //
      StdFilterOptionVsConst.FACTORY //
  );

}
