package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;

/**
 * Extends {@link ITsGuiContextable} with VED specific references.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IVedContextable
    extends ITsGuiContextable {

  default IVedEnvironment vedEnv() {
    return tsContext().get( IVedEnvironment.class );
  }

  default IVedEnvironmentEdit vedEnvEdit() {
    return tsContext().get( IVedEnvironmentEdit.class );
  }

  default IVedDocument vedDoc() {
    return tsContext().get( IVedDocument.class );
  }

  default IVedDocumentEdit vedDocEdit() {
    return tsContext().get( IVedDocumentEdit.class );
  }

  default IVedFramework vedFramework() {
    return tsContext().get( IVedFramework.class );
  }

}
