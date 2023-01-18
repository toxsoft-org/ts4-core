package org.toxsoft.core.tsgui.m5.std.models.misc;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.m5.std.models.misc.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * M5-model of the {@link ValidationResult}.
 * <p>
 * Model has no lifecuycle manager because {@link ValidationResult} is not uyser creatable/editable. Model assumes that
 * appropriate {@link IM5ItemsProvider} instance will be created before usage.
 *
 * @author hazard157
 */
public class ValidationResultM5Model
    extends M5Model<ValidationResult> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = M5_ID + ".ValidationResult"; //$NON-NLS-1$

  /**
   * ID of field {@link #TYPE_ICON}.
   */
  public static final String FID_VR_TYPE_ICON = "TypeIcon"; //$NON-NLS-1$

  /**
   * ID of field {@link #TYPE_NAME}.
   */
  public static final String FID_VR_TYPE_NAME = "TypeName"; //$NON-NLS-1$

  /**
   * ID of field {@link #MESSAGE}.
   */
  public static final String FID_VR_MESSAGE = "Message"; //$NON-NLS-1$

  /**
   * Attribute {@link ValidationResult#type()} as icon.
   */
  public final M5AttributeFieldDef<ValidationResult> TYPE_ICON = new M5AttributeFieldDef<>( FID_VR_TYPE_ICON, VALOBJ ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
      setNameAndDescription( STR_N_VR_TYPE, STR_D_VR_TYPE );
      setDefaultValue( avValobj( EValidationResultType.OK ) );
    }

    protected IAtomicValue doGetFieldValue( ValidationResult aEntity ) {
      return avValobj( aEntity.type() );
    }

    @Override
    protected String doGetFieldValueName( ValidationResult aEntity ) {
      return TsLibUtils.EMPTY_STRING;
    }

    protected Image doGetFieldValueIcon( ValidationResult aEntity, EIconSize aIconSize ) {
      String iconId = aEntity.type().iconId();
      return iconManager().loadStdIcon( iconId, aIconSize );
    }

  };

  /**
   * Attribute {@link ValidationResult#type()} as text.
   */
  public final M5AttributeFieldDef<ValidationResult> TYPE_NAME = new M5AttributeFieldDef<>( FID_VR_TYPE_NAME, VALOBJ ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
      setNameAndDescription( STR_N_VR_TYPE, STR_D_VR_TYPE );
      setDefaultValue( avValobj( EValidationResultType.OK ) );
    }

    protected IAtomicValue doGetFieldValue( ValidationResult aEntity ) {
      return avValobj( aEntity.type() );
    }

    @Override
    protected String doGetFieldValueName( ValidationResult aEntity ) {
      return aEntity.type().nmName();
    }

  };

  /**
   * Attribute{@link ValidationResult#message()}.
   */
  public final M5AttributeFieldDef<ValidationResult> MESSAGE = new M5AttributeFieldDef<>( FID_VR_MESSAGE, STRING ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
      setNameAndDescription( STR_N_VR_MESSAGE, STR_D_VR_MESSAGE );
    }

    @Override
    protected IAtomicValue doGetFieldValue( ValidationResult aEntity ) {
      return avStr( aEntity.message() );
    }

  };

  /**
   * Panel creator for this model.
   *
   * @author hazard157
   */
  static class PanelCreator
      extends M5DefaultPanelCreator<ValidationResult> {

    @Override
    protected IM5CollectionPanel<ValidationResult> doCreateCollViewerPanel( ITsGuiContext aContext,
        IM5ItemsProvider<ValidationResult> aItemsProvider ) {
      OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
      OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
      MultiPaneComponentModown<ValidationResult> mpc = new ValidationResultMpc( aContext, model(), aItemsProvider );
      return new M5CollectionPanelMpcModownWrapper<>( mpc, true );
    }

  }

  /**
   * Constructor.
   */
  public ValidationResultM5Model() {
    super( MODEL_ID, ValidationResult.class );
    setNameAndDescription( STR_N_M5M_VALIDATION_RESULT, STR_D_M5M_VALIDATION_RESULT );
    setPanelCreator( new M5DefaultPanelCreator<>() );
    addFieldDefs( TYPE_ICON, TYPE_NAME, MESSAGE );
  }

}
