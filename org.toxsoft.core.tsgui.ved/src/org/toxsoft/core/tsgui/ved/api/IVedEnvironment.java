package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The environment shared between all components of the mnemo editor.
 *
 * @author hazard157
 */
public interface IVedEnvironment
    extends ITsGuiContextable {

  /**
   * Returns the VISELs.
   *
   * @return {@link IStridablesList}&lt;{@link IVedVisel}&gt; - the ordered list of VISELs
   */
  IStridablesList<IVedVisel> visels();

  /**
   * Returns the actors.
   *
   * @return {@link IStridablesList}&lt;{@link IVedActor}&gt; - the ordered list of actors
   */
  IStridablesList<IVedActor> actors();

}
