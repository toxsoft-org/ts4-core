package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedLibraryManager} implementation.
 *
 * @author hazard157
 */
class VedLibraryManager
    implements IVedLibraryManager {

  private final IStridablesListEdit<IVedLibrary> libs = new StridablesList<>();

  /**
   * Constriuctor.
   */
  public VedLibraryManager() {
    // nop
  }

  @Override
  public IStridablesList<IVedLibrary> listLibs() {
    return libs;
  }

  @Override
  public void registerLibrary( IVedLibrary aLibrary ) {
    TsNullArgumentRtException.checkNull( aLibrary );
    TsItemAlreadyExistsRtException.checkTrue( libs.hasKey( aLibrary.id() ) );
    for( IVedLibrary lib : libs ) {
      if( TsCollectionsUtils.intersects( lib.toolProviders().ids(), aLibrary.toolProviders().ids() ) ) {
        throw new TsItemAlreadyExistsRtException();
      }
    }
    libs.add( aLibrary );
  }

  @Override
  public IVedComponentProvider findProvider( String aLibraryId, String aComponentKindId ) {
    TsNullArgumentRtException.checkNulls( aLibraryId, aComponentKindId );
    IVedLibrary vl = libs.findByKey( aLibraryId );
    if( vl != null ) {
      return vl.componentProviders().findByKey( aComponentKindId );
    }
    return null;
  }

}
