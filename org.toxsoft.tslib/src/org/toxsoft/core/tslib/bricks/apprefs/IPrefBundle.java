package org.toxsoft.core.tslib.bricks.apprefs;

import org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.INotifierOptionSetEdit;
import org.toxsoft.core.tslib.av.utils.IParameterizedEdit;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * The bundle of related parameters used as application preferences.
 * <p>
 * Реализует интерфейс {@link IStridable}, {@link #nmName()} и {@link #description()} возвращают значения параметров
 * {@link #params()}, {@link IAvMetaConstants#TSID_NAME} и {@link IAvMetaConstants#TSID_DESCRIPTION} соответственно. При
 * этом, если {@link IAvMetaConstants#TSID_NAME} не задан, или она пустая строка, {@link #nmName()} возвращает
 * {@link #id()}.
 *
 * @author hazard157
 */
public interface IPrefBundle
    extends IStridable, IParameterizedEdit {

  @Override
  INotifierOptionSetEdit params();

  /**
   * Return all known definition of the parameters.
   * <p>
   * Note that parameter definitions is <b>not</b> stored by the storage backend.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of known definitions
   */
  IStridablesList<IDataDef> knownParams();

  /**
   * Defines parameter.
   *
   * @param aParamInfo {@link IDataDef} - parameter definition
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException definition with the same {@link IDataDef#id()} already exists
   */
  void defineParam( IDataDef aParamInfo );

  /**
   * Removes parameter definition.
   * <p>
   * If there is no parameter with specified ID, then method does nothing.
   *
   * @param aParamInfoId String - parameter ID
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void undefineParam( String aParamInfoId );

}
