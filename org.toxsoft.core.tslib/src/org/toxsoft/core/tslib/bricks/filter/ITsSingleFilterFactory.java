package org.toxsoft.core.tslib.bricks.filter;

import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Factory creates {@link ITsFilter} from signle filter definition {@link ITsSingleFilterParams}.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public interface ITsSingleFilterFactory<T>
    extends IStridable {

  /**
   * Returns decription of the filter parameters.
   * <p>
   * This is optional method and it may return empty set event if filter has parameters.
   * <p>
   * Parameters description is needed for filter creation GUI. Also mandatory parameters exsitance is checked before
   * filter creation in {@link ITsSingleFilterFactory#create(ITsSingleFilterParams)}.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - decription of the filter parameters
   */
  IStridablesList<IDataDef> paramDefs();

  /**
   * Returns class of the objects accepted by the filter.
   * <p>
   * Filters must allow objects of returned class or its descendants.
   *
   * @return {@link Class}&lt;T&gt; - accpeted objects class
   */
  Class<T> objectClass();

  /**
   * Creates filter instance baseod on filter parameters.
   *
   * @param aFilterParams {@link ITsSingleFilterParams} - filter parameters
   * @return {@link ITsFilter} - created filter
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException parameters are for other filter (identifiers does not match)
   * @throws TsItemNotFoundRtException mandatory parameter value is missed in {@link ITsSingleFilterParams#params()}
   */
  ITsFilter<T> create( ITsSingleFilterParams aFilterParams );

}
