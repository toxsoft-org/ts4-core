package org.toxsoft.core.tsgui.ved.api.items;

import static org.toxsoft.core.tsgui.ved.api.IVedFrameworkConstants.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Base interface of VED items.
 *
 * @author hazard157
 */
public interface IVedItem
    extends IStridableParameterized, IPropertable {

  /**
   * Determines if item operation is enabled.
   * <p>
   * Disabled visual elements became invisible, disabled actors stop responding to any events or time flow.
   * <p>
   * This method simply returns the value of the {@link IVedFrameworkConstants#PROP_IS_ACTIVE} property.
   *
   * @return boolean - the sign the item is enabled, is active
   */
  boolean isActive();

  /**
   * Sets the value of the {@link IVedFrameworkConstants#PROP_IS_ACTIVE} property.
   *
   * @param aIsActive boolean - VED item activity state
   */
  default void setActive( boolean aIsActive ) {
    props().setBool( PROP_IS_ACTIVE, aIsActive );
  }

}
