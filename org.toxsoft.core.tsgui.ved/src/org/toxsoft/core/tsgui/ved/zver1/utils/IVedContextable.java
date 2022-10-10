package org.toxsoft.core.tsgui.ved.zver1.utils;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.zver1.core.*;

/**
 * Extends {@link ITsGuiContextable} with VED specific references.
 *
 * @author hazard157
 */
public interface IVedContextable
    extends ITsGuiContextable {

  @SuppressWarnings( "javadoc" )
  default IVedEnvironment vedEnv() {
    return tsContext().get( IVedEnvironment.class );
  }

}
