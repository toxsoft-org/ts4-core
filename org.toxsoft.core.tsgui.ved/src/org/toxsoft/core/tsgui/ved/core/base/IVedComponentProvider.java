package org.toxsoft.core.tsgui.ved.core.base;

import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Component factory and properties definitions provider.
 *
 * @author hazard157
 */
public interface IVedComponentProvider
    extends IVedEntityProviderBase {

  /**
   * Returns the owner library ID.
   *
   * @return String - the owner library ID (an IDpath)
   */
  String libraryId();

  /**
   * Returns the information about properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  @Override
  IStridablesList<IDataDef> propDefs();

  /**
   * Creates the component.
   *
   * @param aCompId String - the ID of component to be created
   * @param aEnvironment {@link IVedEnvironment} the VED environment
   * @param aProps {@link IOptionSet} - propeties initial values
   * @param aExtdata {@link IOptionSet} - external data initial values
   * @return {@link IVedComponent} - created component
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  IVedComponent createComponent( String aCompId, IVedEnvironment aEnvironment, IOptionSet aProps, IOptionSet aExtdata );

}
