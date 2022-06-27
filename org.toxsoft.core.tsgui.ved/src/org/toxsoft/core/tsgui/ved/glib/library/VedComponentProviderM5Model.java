package org.toxsoft.core.tsgui.ved.glib.library;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
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
   * {@link IVedComponentProvider#iconId()}
   */
  public static final IM5AttributeFieldDef<IVedComponentProvider> ICON = new M5StdFieldDefParamIconId<>();

  /**
   * {@link IVedComponentProvider#id()}
   */
  public static final IM5AttributeFieldDef<IVedComponentProvider> ID = new M5StdFieldDefId<>();

  /**
   * {@link IVedComponentProvider#nmName()}
   */
  public static final IM5AttributeFieldDef<IVedComponentProvider> NAME = new M5StdFieldDefName<>();

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
    addFieldDefs( ICON, ID, NAME, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<IVedComponentProvider> doCreateLifecycleManager( Object aMaster ) {
    return new LifecycleManager( this, IVedLibrary.class.cast( aMaster ) );
  }

}
