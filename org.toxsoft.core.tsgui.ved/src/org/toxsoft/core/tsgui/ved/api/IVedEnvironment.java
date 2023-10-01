package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * The environment shared between all components of the mnemo editor.
 *
 * @author hazard157
 */
public interface IVedEnvironment
    extends ITsGuiContextable {

  /**
   * Returns all VISELs.
   *
   * @return {@link IStridablesList}&lt;{@link IVedVisel}&gt; - the ordered list of all VISELs
   */
  IStridablesList<IVedVisel> visels();

  IList<IVedDecorator> viselDecoratorsBefore( String aViselId );

  IList<IVedDecorator> viselDecoratorsAfter( String aViselId );

  /**
   * Returns all actors.
   *
   * @return {@link IStridablesList}&lt;{@link IVedActor}&gt; - the ordered list of all actors
   */
  IStridablesList<IVedActor> actors();

  /**
   * Returns active VISELs, the subset of {@link #visels()}.
   *
   * @return {@link IStridablesList}&lt;{@link IVedVisel}&gt; - the ordered list of active VISELs
   */
  IStridablesList<IVedVisel> activeVisels();

  /**
   * Returns all actors, the subset of {@link #activeActors()}.
   *
   * @return {@link IStridablesList}&lt;{@link IVedActor}&gt; - the ordered list of active actors
   */
  IStridablesList<IVedActor> activeActors();

  IList<IVedDecorator> screenDecoratorsBefore();

  IList<IVedDecorator> screenDecoratorsAfter();

}
