package org.toxsoft.core.tsgui.ved.zver1.glib.content;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.ved.zver1.core.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedComponent} M5-model.
 *
 * @author hazard157
 */
public class VedComponentM5Model
    extends M5Model<IVedComponent> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + ".gui.ved.Component"; //$NON-NLS-1$

  /**
   * {@link IVedComponent#id()}
   */
  public static final IM5AttributeFieldDef<IVedComponent> ID = new M5StdFieldDefId<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( 0 );
    }
  };

  /**
   * {@link IVedComponent#nmName()}
   */
  public static final IM5AttributeFieldDef<IVedComponent> NAME = new M5StdFieldDefName<>() {

    protected IAtomicValue doGetFieldValue( IVedComponent aEntity ) {
      String s = aEntity.nmName();
      if( s.isBlank() ) {
        s = aEntity.id() + " - " + aEntity.provider().nmName();
      }
      return avStr( s );
    }

    @Override
    protected Image doGetFieldValueIcon( IVedComponent aEntity, EIconSize aIconSize ) {
      IAtomicValue av = aEntity.provider().params().getValue( TSID_ICON_ID, defaultValue() );
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
   * {@link IVedComponent#description()}
   */
  public static final IM5AttributeFieldDef<IVedComponent> DESCRIPTION = new M5StdFieldDefDescription<>();

  static class LifecycleManager
      extends M5LifecycleManager<IVedComponent, IVedDataModel> {

    public LifecycleManager( IM5Model<IVedComponent> aModel, IVedDataModel aMaster ) {
      super( aModel, false, false, false, true, aMaster );
      TsNullArgumentRtException.checkNull( aMaster );
    }

    @Override
    protected IList<IVedComponent> doListEntities() {
      return master().listComponents();
    }

  }

  /**
   * Constructor.
   */
  public VedComponentM5Model() {
    super( MODEL_ID, IVedComponent.class );
    addFieldDefs( ID, NAME, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<IVedComponent> doCreateDefaultLifecycleManager() {
    IVedDataModel master = null;
    IVedEnvironment vedEnv = tsContext().find( IVedEnvironment.class );
    if( vedEnv != null ) {
      master = vedEnv.dataModel();
    }
    else {
      master = tsContext().find( IVedDataModel.class );
    }
    if( master != null ) {
      return new LifecycleManager( this, master );
    }
    return null;
  }

  @Override
  protected IM5LifecycleManager<IVedComponent> doCreateLifecycleManager( Object aMaster ) {
    return new LifecycleManager( this, IVedDataModel.class.cast( aMaster ) );
  }

}
