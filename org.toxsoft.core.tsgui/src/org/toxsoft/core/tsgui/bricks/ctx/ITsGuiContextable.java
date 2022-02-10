package org.toxsoft.core.tsgui.bricks.ctx;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.graphics.colors.ITsColorManager;
import org.toxsoft.core.tsgui.graphics.fonts.ITsFontManager;
import org.toxsoft.core.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.core.tsgui.graphics.image.ITsImageManager;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.tsgui.mws.services.hdpi.ITsHdpiService;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextable;

/**
 * Mixin interface of entities with context {@link ITsGuiContext}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsGuiContextable
    extends ITsContextable {

  /**
   * Returns the context of the entity.
   *
   * @return {@link ITsContext} - the context of the entity
   */
  @Override
  ITsGuiContext tsContext();

  // ------------------------------------------------------------------------------------
  // API
  //

  default IEclipseContext eclipseContext() {
    return tsContext().eclipseContext();
  }

  default Shell getShell() {
    return tsContext().get( Shell.class );
  }

  default ITsE4Helper e4Helper() {
    return tsContext().get( ITsE4Helper.class );
  }

  default ITsHdpiService hdpiService() {
    return tsContext().get( ITsHdpiService.class );
  }

  default ITsColorManager colorManager() {
    return tsContext().get( ITsColorManager.class );
  }

  default ITsFontManager fontManager() {
    return tsContext().get( ITsFontManager.class );
  }

  default ITsIconManager iconManager() {
    return tsContext().get( ITsIconManager.class );
  }

  default ITsImageManager imageManager() {
    return tsContext().get( ITsImageManager.class );
  }

  default IM5Domain m5() {
    return tsContext().get( IM5Domain.class );
  }

}
