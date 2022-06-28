package org.toxsoft.core.tsgui.ved.glib.library;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedComponentProvider} M5-model.
 *
 * @author hazard157
 */
public class VedComponentProviderM5Model
    extends M5Model<IVedComponentProvider> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + ".gui.ved.ComponentProvider"; //$NON-NLS-1$

  /**
   * {@link IVedComponentProvider#id()}
   */
  public static final IM5AttributeFieldDef<IVedComponentProvider> ID = new M5StdFieldDefId<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( 0 );
    }
  };

  /**
   * {@link IVedComponentProvider#nmName()}
   */
  public static final IM5AttributeFieldDef<IVedComponentProvider> NAME = new M5StdFieldDefName<>() {

    @Override
    protected Image doGetFieldValueIcon( IVedComponentProvider aEntity, EIconSize aIconSize ) {
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
   * {@link IVedComponentProvider#description()}
   */
  public static final IM5AttributeFieldDef<IVedComponentProvider> DESCRIPTION = new M5StdFieldDefDescription<>();

  static class LifecycleManager
      extends M5LifecycleManager<IVedComponentProvider, IVedLibrary> {

    public LifecycleManager( IM5Model<IVedComponentProvider> aModel, IVedLibrary aMaster ) {
      super( aModel, false, false, false, true, aMaster );
      TsNullArgumentRtException.checkNull( aMaster );
    }

    @Override
    protected IList<IVedComponentProvider> doListEntities() {
      return master().componentProviders();
    }

  }

  /**
   * Constructor.
   */
  public VedComponentProviderM5Model() {
    super( MODEL_ID, IVedComponentProvider.class );
    addFieldDefs( ID, NAME, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<IVedComponentProvider> doCreateLifecycleManager( Object aMaster ) {
    return new LifecycleManager( this, IVedLibrary.class.cast( aMaster ) );
  }

}
