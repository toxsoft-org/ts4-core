package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.incub.props.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * The VED component.
 * <p>
 * Component is data model for the single component and also is the factory for the component viewes.
 *
 * @author hazard157
 */
public interface IVedComponent
    extends IStridable, IPropertable {

  /**
   * Returns library ID that provided component factory.
   *
   * @return String - the library ID (an IDpath)
   */
  String libraryId();

  /**
   * Returns component kind ID.
   * <p>
   * Kind ID identifies the component provider in the library.
   *
   * @return String - the factory ID (an IDpath)
   */
  String componentKindId();

  /**
   * Returns the capbilites constants.
   *
   * @return {@link IOptionSetEdit} - capabilities
   */
  IOptionSet capabilities();

  /**
   * Returns the values of the external data.
   *
   * @return {@link IOptionSetEdit} - the external data
   */
  IOptionSetEdit extdata();

  /**
   * Creates component viewer on the specified screen.
   *
   * @param aScreen {@link IVedScreen} - screen to create component onto
   * @return {@link IVedComponentView} - created component view
   */
  IVedComponentView createView( IVedScreen aScreen );

}
