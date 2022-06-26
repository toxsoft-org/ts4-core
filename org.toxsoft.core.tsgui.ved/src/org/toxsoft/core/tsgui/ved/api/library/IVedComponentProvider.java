package org.toxsoft.core.tsgui.ved.api.library;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Component factory and properties definitions provider.
 *
 * @author hazard157
 */
public interface IVedComponentProvider
    extends IStridableParameterized {

  /**
   * Returns the information about properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Creates the component.
   *
   * @param aEnvironment {@link IVedEnvironment} the VED environment
   * @param aProps {@link IOptionSet} - propeties initial values
   * @param aExtdata {@link IOptionSet} - external data initial values
   * @return {@link IVedComponent} - created component
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IVedComponent createComponent( IVedEnvironment aEnvironment, IOptionSet aProps, IOptionSet aExtdata );

}
