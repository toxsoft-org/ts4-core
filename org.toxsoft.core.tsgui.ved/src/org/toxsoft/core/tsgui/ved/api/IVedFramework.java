package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
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
  IVedEntityProvidersRegistry getEntityProvidersRegistry( EVedEntityKind aKind );

  /**
   * Creates new envirement to veiw the specified document.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aDocumentData {@link IVedDocumentData} - the document data
   * @return {@link IVedEnvironment} - VED doument with environment
   */
  IVedEnvironment createEnvironment( ITsGuiContext aContext, IVedDocumentData aDocumentData );

  /**
   * Creates new envirement to edit a new, empty document.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IVedEnvironmentEdit} - editable VED document with environment
   */
  IVedEnvironmentEdit createEnvironmentEdit( ITsGuiContext aContext );

  // ------------------------------------------------------------------------------------
  // inline methods for convinience

  @SuppressWarnings( "javadoc" )
  default IVedEntityProvidersRegistry getComponentsRegistry() {
    return getEntityProvidersRegistry( EVedEntityKind.COMPONENT );
  }

  @SuppressWarnings( "javadoc" )
  default IVedEntityProvidersRegistry getActorsRegistry() {
    return getEntityProvidersRegistry( EVedEntityKind.ACTOR );
  }

  @SuppressWarnings( "javadoc" )
  default IVedEntityProvidersRegistry getTailorsRegistry() {
    return getEntityProvidersRegistry( EVedEntityKind.TAILOR );
  }

}
