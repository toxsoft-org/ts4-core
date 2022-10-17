package org.toxsoft.core.tsgui.ved.api.comp;

import org.toxsoft.core.tsgui.ved.api.entity.*;

/**
 * The VED component - single drawing unit on the VED screens.
 * <p>
 * Component is data model for the single component and also is the factory for the component viewes.
 *
 * @author hazard157
 */
public interface IVedComponent
    extends IVedEntity {

  /**
   * Creates component viewer on the specified screen.
   *
   * @param aScreen {@link IVedScreen} - screen to create component onto
   * @return {@link IVedComponentView} - created component view
   */
  IVedComponentView createView( IVedScreen aScreen );

}
