package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VED framework entry point.
 * <p>
 * There is the single instance of this interface in the application context.
 *
 * @author hazard157
 */
public interface IVedFramework {

  /**
   * Returns the entity providers registry of the given kind.
   * <p>
   * The framework always contains a registry of each лштв.
   *
   * @param aKind {@link EVedEntityKind} - entity kind
   * @return {@link IVedEntityProvidersRegistry} - the registry
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IVedEntityProvidersRegistry getEntityRegistry( EVedEntityKind aKind );

  IVedEnvironment createEnvironment( ITsGuiContext aContext );

  IVedEnvironmentEdit createEnvironmentEdit( ITsGuiContext aContext );

  // ------------------------------------------------------------------------------------
  // inline methods for convinience

  @SuppressWarnings( "javadoc" )
  default IVedEntityProvidersRegistry getComponentsRegistry() {
    return getEntityRegistry( EVedEntityKind.COMPONENT );
  }

  @SuppressWarnings( "javadoc" )
  default IVedEntityProvidersRegistry getActorsRegistry() {
    return getEntityRegistry( EVedEntityKind.ACTOR );
  }

  @SuppressWarnings( "javadoc" )
  default IVedEntityProvidersRegistry getTailorsRegistry() {
    return getEntityRegistry( EVedEntityKind.TAILOR );
  }

}
