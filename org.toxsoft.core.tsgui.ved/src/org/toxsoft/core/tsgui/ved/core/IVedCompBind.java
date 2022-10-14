package org.toxsoft.core.tsgui.ved.core;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The link to the component.
 *
 * @author hazard157
 */
public interface IVedCompBind
    extends IPropertable {

  /**
   * Returns the binding definition.
   *
   * @return {@link IVedCompBindDef} - the binding definition
   */
  IVedCompBindDef bindInfo();

  /**
   * Returns the bind component ID.
   *
   * @return String - the bind component ID
   */
  String componentId();

  String compPropId();

  /**
   * Returns the information about entity properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

}
