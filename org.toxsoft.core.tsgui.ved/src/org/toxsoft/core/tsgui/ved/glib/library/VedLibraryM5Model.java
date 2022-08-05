package org.toxsoft.core.tsgui.ved.glib.library;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedLibrary} M5-model.
 *
 * @author hazard157
 */
public class VedLibraryM5Model
    extends M5Model<IVedLibrary> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + ".gui.ved.Library"; //$NON-NLS-1$

  /**
   * {@link IVedLibrary#id()}
   */
  public static final IM5AttributeFieldDef<IVedLibrary> ID = new M5StdFieldDefId<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( 0 );
    }
  };

  /**
   * {@link IVedLibrary#nmName()}
   */
  public static final IM5AttributeFieldDef<IVedLibrary> NAME = new M5StdFieldDefName<>() {

    @Override
    protected Image doGetFieldValueIcon( IVedLibrary aEntity, EIconSize aIconSize ) {
      IAtomicValue av = aEntity.params().getValue( TSID_ICON_ID, defaultValue() );
      if( av.isAssigned() && av.atomicType() == EAtomicType.STRING ) {
        String iconId = av.asString();
        if( !iconId.isBlank() ) {
          return iconManager().loadStdIcon( iconId, aIconSize );
        }
      }
      return null;
    }

  };

  /**
   * {@link IVedLibrary#description()}
   */
  public static final IM5AttributeFieldDef<IVedLibrary> DESCRIPTION = new M5StdFieldDefDescription<>();

  static class LifecycleManager
      extends M5LifecycleManager<IVedLibrary, IVedLibraryManager> {

    public LifecycleManager( IM5Model<IVedLibrary> aModel, IVedLibraryManager aMaster ) {
      super( aModel, false, false, false, true, aMaster );
      TsNullArgumentRtException.checkNull( aMaster );
    }

    @Override
    protected IList<IVedLibrary> doListEntities() {
      return master().listLibs();
    }

  }

  /**
   * Constructor.
   */
  public VedLibraryM5Model() {
    super( MODEL_ID, IVedLibrary.class );
    addFieldDefs( ID, NAME, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<IVedLibrary> doCreateDefaultLifecycleManager() {
    IVedLibraryManager master = null;
    IVedEnvironment vedEnv = tsContext().find( IVedEnvironment.class );
    if( vedEnv != null ) {
      master = vedEnv.libraryManager();
    }
    else {
      master = tsContext().find( IVedLibraryManager.class );
    }
    if( master != null ) {
      return new LifecycleManager( this, master );
    }
    return null;
  }

  @Override
  protected IM5LifecycleManager<IVedLibrary> doCreateLifecycleManager( Object aMaster ) {
    return new LifecycleManager( this, IVedLibraryManager.class.cast( aMaster ) );
  }

}
