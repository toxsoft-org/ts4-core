package org.toxsoft.core.tsgui.bricks.ctx;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.mws.services.timers.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;

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

  default Display getDisplay() {
    return tsContext().get( Display.class );
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

  default ITsCursorManager cursorManager() {
    return tsContext().get( ITsCursorManager.class );
  }

  default IAppPreferences appPrefs() {
    return tsContext().get( IAppPreferences.class );
  }

  default IPrefBundle prefBundle( String aBundleId ) {
    return appPrefs().getBundle( aBundleId );
  }

  default IM5Domain m5() {
    return tsContext().get( IM5Domain.class );
  }

  default IAnimationSupport animSupport() {
    return tsContext().get( IAnimationSupport.class );
  }

  default ITsGuiTimersService guiTimersService() {
    return tsContext().get( ITsGuiTimersService.class );
  }

  default IAtomicValue getAppPrefsValue( String aPrefBundleId, IDataDef aPrmId ) {
    IPrefBundle pb = appPrefs().findBundle( aPrefBundleId );
    if( pb != null ) {
      return pb.prefs().getValue( aPrmId.id(), aPrmId.defaultValue() );
    }
    return IAtomicValue.NULL;
  }

  default void setAppPrefsValue( String aPrefBundleId, IStridable aPrmId, IAtomicValue aValue ) {
    IPrefBundle pb = appPrefs().findBundle( aPrefBundleId );
    if( pb != null ) {
      pb.prefs().setValue( aPrmId.id(), aValue );
    }
  }

}
