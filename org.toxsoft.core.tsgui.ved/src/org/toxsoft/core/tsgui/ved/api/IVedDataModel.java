package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.basis.*;

/**
 * Data model edited by the VED framework.
 *
 * @author hazard157
 */
public interface IVedDataModel
    extends ITsClearable, // to implement "New" command
    IGenericChangeEventCapable // inform about any user edits
{

  /**
   * Return canvas and background configuration options.
   *
   * @return {@link INotifierOptionSetEdit} - editable canvas config data model
   */
  INotifierOptionSetEdit canvasConfig();

  /**
   * Returns the components.
   *
   * @return {@link INotifierStridablesListEdit}&lt;{@link IVedComponent}&gt; - editable components data model
   */
  INotifierStridablesListEdit<IVedComponent> comps();

}
