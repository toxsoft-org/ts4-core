package org.toxsoft.core.tsgui.bricks.combicond;

import static org.toxsoft.core.tsgui.bricks.combicond.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.bricks.validator.std.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to edit {@link SingleCondParamsPanel}.
 * <p>
 * Contains:
 * <ul>
 * <li>name {@link ISingleCondParams#varName()} editor line;</li>
 * <li>type ID {@link ISingleCondParams#typeId()} selector combo box;</li>
 * <li>parameters editor {@link IOptionSetPanel} corresponding to the selected type ID.</li>
 * </ul>
 *
 * @author hazard157
 */
public class SingleCondParamsPanel
    extends AbstractLazyPanel<Control>
    implements IGenericEntityEditPanel<ISingleCondParams> {

  private final GenericChangeEventer                      genericChangeEventer;
  private final ISingleCondTypesRegistry<ISingleCondType> sctTypesReg = new DefaultSingleCondTypesRegistry();

  private final ITsVisualsProvider<String> visualsProvider = aItem -> {
    if( aItem == null ) {
      return EMPTY_STRING;
    }
    ISingleCondType sct = sctTypesReg.find( aItem );
    if( sct == null ) {
      return String.format( FMT_SCPP_UNKNOWN_SCT_ID, aItem );
    }
    return StridUtils.printf( StridUtils.FORMAT_ID_NAME, sct );
  };

  private final boolean viewerMode;

  /**
   * Holds parameters {@link ISingleCondParams#params()} to restore them when {@link #typeIdCombo} changes.
   */
  private final IOptionSetEdit condParams = new OptionSet();

  // editable flag of this VALED used as editable flag of the whole panel
  private ValedStringText            nameText    = null;
  private ValedComboSelector<String> typeIdCombo = null;
  private IOptionSetPanel            paramsPanel = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aViewer boolean - <code>true</code> to create un-edtable panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SingleCondParamsPanel( ITsGuiContext aContext, boolean aViewer ) {
    super( new TsGuiContext( aContext ) );
    genericChangeEventer = new GenericChangeEventer( this );
    viewerMode = aViewer;
    IValedControlConstants.OPDEF_CREATE_UNEDITABLE.setValue( tsContext().params(), avBool( viewerMode ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new GridLayout( 2, false ) );
    // nameText
    Label l = new Label( board, SWT.LEFT );
    l.setText( STR_SCPP_NAME_LABEL );
    l.setToolTipText( STR_SCPP_NAME_LABEL_D );
    TsGuiContext ctx1 = new TsGuiContext( tsContext() );
    nameText = new ValedStringText( ctx1 );
    nameText.createControl( board );
    nameText.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    // typeIdCombo
    l = new Label( board, SWT.LEFT );
    l.setText( STR_SCPP_TYPEID_LABEL );
    l.setToolTipText( STR_SCPP_TYPEID_LABEL_D );
    TsGuiContext ctx2 = new TsGuiContext( tsContext() );
    typeIdCombo = new ValedComboSelector<>( ctx2 );
    typeIdCombo.createControl( board );
    typeIdCombo.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    // paramsPanel
    l = new Label( board, SWT.LEFT );
    l.setText( STR_SCPP_PARAMS_LABEL );
    l.setToolTipText( STR_SCPP_PARAMS_LABEL_D );
    TsGuiContext ctx3 = new TsGuiContext( tsContext() );
    paramsPanel = new OptionSetPanel( ctx3, false, true );
    paramsPanel.createControl( board );
    paramsPanel.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    // setup
    nameText.eventer().addListener( ( s, f ) -> whenAnyValedValueChanged() );
    typeIdCombo.eventer().addListener( ( s, f ) -> whenTypeIdChanged() );
    typeIdCombo.eventer().addListener( ( s, f ) -> whenAnyValedValueChanged() );
    paramsPanel.genericChangeEventer().addListener( s -> whenParamsValedValueChanged() );
    typeIdCombo.setVisualsProvider( visualsProvider );
    return board;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenAnyValedValueChanged() {
    genericChangeEventer.fireChangeEvent();
  }

  private void whenParamsValedValueChanged() {
    // defend parameters against typeIdCombo change
    if( !paramsPanel.canGetEntity().isError() ) {
      condParams.setAll( paramsPanel.getEntity() );
    }
    whenAnyValedValueChanged();
  }

  private void whenTypeIdChanged() {
    ISingleCondType sct = null;
    String typeId = typeIdCombo.getValue();
    if( typeId != null ) {
      sct = sctTypesReg.find( typeId );
    }
    IStridablesList<IDataDef> ddefs = sct != null ? sct.paramDefs() : IStridablesList.EMPTY;
    paramsPanel.setOptionDefs( ddefs );
    paramsPanel.setEntity( condParams );
  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityEditPanel
  //

  @Override
  public void setEntity( ISingleCondParams aEntity ) {
    if( aEntity == null ) {
      nameText.setValue( EMPTY_STRING );
      typeIdCombo.setValue( null );
      condParams.clear();
      paramsPanel.setOptionDefs( IStridablesList.EMPTY );
      return;
    }
    nameText.setValue( aEntity.varName() );
    typeIdCombo.setValue( aEntity.typeId() );
    condParams.setAll( aEntity.params() );
    whenTypeIdChanged();
  }

  @Override
  public ValidationResult canGetEntity() {
    String name = nameText.getValue();
    ValidationResult vr = IdPathStringValidator.IDPATH_EMPTY_VALIDATOR.validate( name );
    if( vr.isError() ) {
      return ValidationResult.error( MSG_ERR_SCPP_INV_NAME );
    }
    String typeId = typeIdCombo.getValue();
    if( typeId == null ) {
      return ValidationResult.error( FMT_SCPP_UNKNOWN_SCT_ID, EMPTY_STRING );
    }
    ISingleCondType sct = sctTypesReg.find( typeId );
    if( sct == null ) {
      return ValidationResult.error( FMT_SCPP_UNKNOWN_SCT_ID, typeId );
    }
    vr = ValidationResult.firstNonOk( vr, paramsPanel.canGetEntity() );
    IOptionSet params = paramsPanel.getEntity();
    return sct.validateParams( params );
  }

  @Override
  public ISingleCondParams getEntity() {
    TsValidationFailedRtException.checkError( canGetEntity() );
    String name = nameText.getValue();
    String typeId = typeIdCombo.getValue();
    IOptionSet params = paramsPanel.getEntity();
    return new SingleCondParams( name, typeId, params );
  }

  @Override
  public boolean isViewer() {
    return viewerMode;
  }

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if panel content editing is allowed right now.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> always returns <code>false</code>.
   *
   * @return boolean - edit mode flag
   */
  public boolean isEditable() {
    if( isViewer() ) {
      return false;
    }
    return nameText.isEditable();
  }

  /**
   * Toggles panel content edit mode.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> this method does nothing.
   *
   * @param aEditable boolean - edit mode flag
   */
  public void setEditable( boolean aEditable ) {
    if( !isViewer() ) {
      nameText.setEditable( aEditable );
      typeIdCombo.setEditable( aEditable );
      paramsPanel.setEditable( aEditable );
    }
  }

  /**
   * Adds condition types from the argument to the internal registry.
   *
   * @param aTypes {@link IStridablesList}&lt;{@link ISingleCondType}&gt; - types to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException any type ID is already registered
   */
  public void addRegistry( IStridablesList<? extends ISingleCondType> aTypes ) {
    TsNullArgumentRtException.checkNull( aTypes );
    TsItemAlreadyExistsRtException
        .checkTrue( TsCollectionsUtils.intersects( aTypes.ids(), sctTypesReg.items().ids() ) );
    for( ISingleCondType t : aTypes ) {
      sctTypesReg.register( t );
    }
    typeIdCombo.setItems( sctTypesReg.items().keys() );
  }

  /**
   * Adds condition types from the argument to the internal registry.
   *
   * @param aTypesRegistry {@link IStridablesRegisrty} - the registry of {@link ISingleCondType}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException any type ID is already registered
   */
  public void addRegistry( IStridablesRegisrty<? extends ISingleCondType> aTypesRegistry ) {
    TsNullArgumentRtException.checkNull( aTypesRegistry );
    addRegistry( aTypesRegistry.items() );
  }

}
