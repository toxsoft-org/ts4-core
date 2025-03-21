package org.toxsoft.core.tsgui.m5.std.models.misc;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.m5.std.models.misc.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;

/**
 * M5-model of the {@link String} entities.
 *
 * @author hazard157
 */
public class StringM5Model
    extends M5Model<String> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = M5_ID + ".String"; //$NON-NLS-1$

  /**
   * ID of field {@link #STR}.
   */
  public static final String FID_STRING = "String"; //$NON-NLS-1$

  /**
   * Field {@link String#toString()}.
   */
  public final M5AttributeFieldDef<String> STR = new M5AttributeFieldDef<>( FID_STRING, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STRING_STR, STR_D_STRING_STR );
      setDefaultValue( AV_STR_EMPTY );
      setValedEditor( ValedAvStringText.FACTORY_NAME );
      ValedStringText.OPDEF_IS_MULTI_LINE.setValue( params(), AV_TRUE );
      setFlags( M5FF_COLUMN | M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( String aEntity ) {
      return avStr( aEntity );
    }
  };

  /**
   * LM for this model.
   *
   * @author hazard157
   */
  private static class DefaultLifecyleManager
      extends M5LifecycleManager<String, Object> {

    public DefaultLifecyleManager( IM5Model<String> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    @Override
    protected String doCreate( IM5Bunch<String> aValues ) {
      String str = aValues.getAsAv( FID_STRING ).asString();
      return str;
    }

    @Override
    protected String doEdit( IM5Bunch<String> aValues ) {
      return doCreate( aValues );
    }

    @Override
    protected void doRemove( String aEntity ) {
      // nop
    }

  }

  /**
   * Constructor for builtin model.
   */
  public StringM5Model() {
    this( MODEL_ID );
  }

  /**
   * Constructor for subclass.
   *
   * @param aModelId String - the model ID.
   */
  protected StringM5Model( String aModelId ) {
    super( aModelId, String.class );
    setNameAndDescription( STR_N_M5M_STRING, STR_D_M5M_STRING );
    addFieldDefs( STR );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      @Override
      protected IM5CollectionPanel<String> doCreateCollViewerPanel( ITsGuiContext aContext,
          IM5ItemsProvider<String> aItemsProvider ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_DETAILS_PANE.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_DETAILS_PANE_HIDDEN.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_TOOLBAR.setValue( aContext.params(), AV_FALSE );
        MultiPaneComponentModown<String> mpc = new MultiPaneComponentModown<>( aContext, model(), aItemsProvider );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, true );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<String> doCreateDefaultLifecycleManager() {
    return new DefaultLifecyleManager( this );
  }

  @Override
  protected IM5LifecycleManager<String> doCreateLifecycleManager( Object aMaster ) {
    /**
     * If not overridden, use default LM for String.
     */
    return new DefaultLifecyleManager( this );
  }

}
