package org.toxsoft.core.tsgui.bricks.combicond.valed;

import static org.toxsoft.core.tsgui.bricks.combicond.valed.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALED to edit {@link ISingleCondParams}.
 * <p>
 * This {@link AbstractValedLabelAndButton} based implementation simple invokes
 * {@link DialogOptionsEdit#editOpset(ITsDialogInfo, IStridablesList, IOptionSet)} with
 * {@link ISingleCondType#paramDefs()} definitions.
 * <p>
 * Generally, when pressing EDIT button, first appears the dialog of the {@link ISingleCondType} selection, and after
 * type is selected corresponding {@link ISingleCondParams} editor dialog appears. Type selection dialog contains only
 * SCTs specified by {@link #REFDEF_ALLOWED_TYPE_IDS} reference.
 * <p>
 * However, in one case type selection dialog is omitted. If VALED contains non-<code>null</code>
 * {@link ISingleCondParams} of allowed type, and {@link #OPDEF_IS_TYPE_ID_SELECTION_ALLOWED} is set to
 * <code>false</code>, then type selection dialog has no sense and is not shown.
 * <p>
 * TODO in the base class the button to set {@link #scp} to <code>null</code> is required
 *
 * @author hazard157
 */
public class ValedSingleCondParams_LabelAndText
    extends AbstractValedLabelAndButton<ISingleCondParams> {

  /**
   * The factory class.
   *
   * @author hazard157
   */
  public static class Factory
      extends AbstractValedControlFactory {

    /**
     * Constructor.
     */
    public Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<ISingleCondParams> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSingleCondParams_LabelAndText( aContext );
    }

  }

  /**
   * The registered factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SingleCondParams"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<ISingleCondParams> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSingleCondParams_LabelAndText( aContext );
    }
  };

  /**
   * Reference to the {@link ISingleCondTypesRegistry} to be used for condition type retrieval.
   * <p>
   * May be set both before VALED creation and after VALED creation.
   * <p>
   * If not specified, the reference to {@link DefaultSingleCondTypesRegistry#INSTANCE} will be used.
   */
  @SuppressWarnings( "rawtypes" )
  public static final ITsContextRefDef<ISingleCondTypesRegistry> REFDEF_SCT_REGISTRY = new TsContextRefDef<>(
      TS_FULL_ID + ".tsgui.ValedSingleCondParams.SctRegistry", ISingleCondTypesRegistry.class, IOptionSet.NULL ); //$NON-NLS-1$

  /**
   * Determines which SCP type IDs allowed to be selected by the user when editing the value.
   * <p>
   * This reference specified the list of SCTs visible to the user when setting or changing SCP value.
   * <p>
   * May be set both before VALED creation and after VALED creation.
   * <p>
   * If not specified, all type IDs from the registry {@link #REFDEF_SCT_REGISTRY} are allowed.
   */
  public static final ITsContextRefDef<IStringList> REFDEF_ALLOWED_TYPE_IDS = new TsContextRefDef<>(
      TS_FULL_ID + ".tsgui.ValedSingleCondParams.AllowedTypeIds", IStringList.class, IOptionSet.NULL ); //$NON-NLS-1$

  /**
   * Determines if user is allowed to change existing {@link #scp} type.
   * <p>
   * Default value of this option is <code>true</code>.
   * <p>
   * May be set both before VALED creation and after VALED creation.
   */
  public static final IDataDef OPDEF_IS_TYPE_ID_SELECTION_ALLOWED =
      DataDef.create( TS_FULL_ID + ".tsgui.ValedSingleCondParams.IsTypeIdSelectionAllowed", BOOLEAN, //$NON-NLS-1$
          TSID_DEFAULT_VALUE, AV_TRUE //
      );

  private ISingleCondParams scp = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedSingleCondParams_LabelAndText( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISingleCondTypesRegistry<ISingleCondType> getRegistry() {
    return REFDEF_SCT_REGISTRY.getRef( tsContext(), DefaultSingleCondTypesRegistry.INSTANCE );
  }

  /**
   * Invokes {@link ISingleCondType} selection dialog.
   * <p>
   * If there is no known types in {@link #getRegistry()}, then invokes warning dialog and returns null.
   * <p>
   * Returns <code>null</code> if user cancels selection operation.
   *
   * @param aSelTypeId String - ID of preselected type or <code>null</code>
   * @return {@link ISingleCondType} - selected type or <code>null</code>
   */
  private ISingleCondType invokeTypeSelectionDialog( String aSelTypeId ) {
    ISingleCondTypesRegistry<ISingleCondType> registry = getRegistry();
    IStridablesList<ISingleCondType> typesList = registry.items();
    if( typesList.isEmpty() ) {
      TsDialogUtils.warn( getShell(), MSG_NO_KNOWN_SCTS );
      return null;
    }
    ISingleCondType sel = null;
    if( aSelTypeId != null ) {
      sel = typesList.findByKey( aSelTypeId );
    }
    ITsDialogInfo di = new TsDialogInfo( tsContext(), DLG_SELECT_SCT, DLG_SELECT_SCT_D );
    return DialogItemsList.select( di, typesList, sel );
  }

  /**
   * Allows user (if enabled) to select {@link ISingleCondType} or returns the only allowed type.
   * <p>
   * If value can not be edited {@link #scp} has unknown type and {@link #OPDEF_IS_TYPE_ID_SELECTION_ALLOWED} is
   * <code>false</code> displays the dialog and returns <code>null</code>.
   * <p>
   * Also returns <code>null</code> is user cancels the operation.
   *
   * @return {@link ISingleCondType} - type of edited SCP or <code>null</code>
   */
  private ISingleCondType selectType() {
    // editing of type for unspecified SCP is always allowed
    if( scp == null ) {
      return invokeTypeSelectionDialog( null );
    }
    boolean isTypeSelAllowed = OPDEF_IS_TYPE_ID_SELECTION_ALLOWED.getValue( tsContext().params() ).asBool();
    ISingleCondTypesRegistry<ISingleCondType> registry = getRegistry();
    IStridablesList<ISingleCondType> typesList = registry.items();
    ISingleCondType currSct = typesList.findByKey( scp.typeId() );
    // unknown SCP type, either select new or exit with warning
    if( currSct != null ) {
      if( !isTypeSelAllowed ) {
        TsDialogUtils.warn( getShell(), FMT_ERR_CANT_EDIT_UNKNOWN_SCP_TYPE, scp.typeId() );
        return null;
      }
      return invokeTypeSelectionDialog( null );
    }
    // type change is not allowed!
    if( !isTypeSelAllowed ) {
      return currSct;
    }
    // if there is only one known type, there is no sense to invoke selection dialog
    if( typesList.size() == 1 ) {
      return currSct;
    }
    // OK, finally we allow user to select SCP type
    return invokeTypeSelectionDialog( scp.typeId() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedLabelAndButton
  //

  @Override
  protected void doUpdateLabelControl() {
    String text = EMPTY_STRING;
    if( scp != null ) {
      ISingleCondType sct = getRegistry().find( scp.typeId() );
      if( sct != null ) {
        text = sct.humanReadableDescription( scp.params() );
      }
    }
    getLabelControl().setText( text );
  }

  @Override
  protected boolean doProcessButtonPress() {
    ISingleCondType sct = selectType();
    if( sct == null ) {
      return false;
    }
    // if no parameters to edit, fo not show empty dialog
    if( sct.paramDefs().isEmpty() ) {
      scp = new SingleCondParams( sct.id(), IOptionSet.NULL );
      return true;
    }
    // ask parameter values
    ITsDialogInfo di = new TsDialogInfo( tsContext(), sct.nmName(), DLG_SCP_PARAMS_EDITOR_D );
    IOptionSet p = DialogOptionsEdit.editOpset( di, sct.paramDefs(), scp.params() );
    if( p != null ) {
      scp = new SingleCondParams( sct.id(), p );
      return true;
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( ISingleCondParams aValue ) {
    scp = aValue;
  }

  @Override
  protected ISingleCondParams doGetUnvalidatedValue() {
    return scp;
  }

}
