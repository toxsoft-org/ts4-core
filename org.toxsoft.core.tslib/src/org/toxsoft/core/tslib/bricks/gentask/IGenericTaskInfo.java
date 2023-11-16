package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * The generic task meta-information.
 * <p>
 * Contains self-documentary description of the input (argument) and output (result) parameters - options and
 * references.
 * <p>
 * Generic task ID is recommended to be globally unique. Options of {@link IParameterized} are used as usual,
 * {@link IAvMetaConstants#TSID_NAME} option as a human-readable task name, an so on.
 * <p>
 * Note: {@link IGenericTaskConstants} defines commonly used and recommended inout and output options and references.
 *
 * @author hazard157
 */
public interface IGenericTaskInfo
    extends IStridableParameterized {

  /**
   * Return input options descriptions.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - map "input option ID" - "option description"
   */
  IStridablesList<IDataDef> inOps();

  /**
   * Returns the input references descriptions.
   *
   * @return {@link IStringMap}&lt;{@link ITsContextRefDef}&gt; - map "input references ID" - "reference description"
   */
  IStringMap<ITsContextRefDef<?>> inRefs();

  /**
   * Return output options descriptions.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - map "output option ID" - "option description"
   */
  IStridablesList<IDataDef> outOps();

  /**
   * Returns the output references descriptions.
   *
   * @return {@link IStringMap}&lt;{@link ITsContextRefDef}&gt; - map "output references ID" - "reference description"
   */
  IStringMap<ITsContextRefDef<?>> outRefs();

}
