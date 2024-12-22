package org.toxsoft.core.tsgui.bricks.cond.impl;

import static org.toxsoft.core.tsgui.bricks.cond.impl.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.cond.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IPanelSingleCondInfo} implementation.
 * <p>
 * Note: it is strongly recommended to set the topic manager with the method
 * {@link #setTopicManager(ITsConditionsTopicManager)} immediately after instance creation.
 *
 * @author hazard157
 */
public class PanelSingleCondInfo
    extends AbstractGenericEntityEditPanel<ITsSingleCondInfo>
    implements IPanelSingleCondInfo {

  /**
   * The reference in the context to initialize {@link #topicManager()}.
   */
  public static final ITsGuiContextRefDef<ITsConditionsTopicManager> REFDEF_TOPIC_MANAGER =
      new TsGuiContextRefDef<>( TS_FULL_ID + ".gui.PanelSingleCondInfo.RefTopicManager", //$NON-NLS-1$
          ITsConditionsTopicManager.class, IOptionSet.NULL );

  /**
   * Provides item text for type ID drop-down combo.
   *
   * @author hazard157
   */
  private class TypeIdVisualsProvider
      implements ITsVisualsProvider<String> {

    @Override
    public String getName( String aItem ) {
      if( aItem == null ) {
        return EMPTY_STRING;
      }
      ITsSingleCondType sct = topicManager.listSingleCondTypes().findByKey( aItem );
      if( sct == null ) {
        return String.format( FMT_SCPP_UNKNOWN_SCT_ID, aItem );
      }
      return StridUtils.printf( StridUtils.FORMAT_ID_NAME, sct );
    }

  }

  /**
   * Holds parameters {@link ITsSingleCondInfo#params()} to restore them when {@link #typeIdCombo} changes.
   */
  private final IOptionSetEdit condParams = new OptionSet();

  private ITsConditionsTopicManager topicManager; // never is null

  private ValedComboSelector<String> typeIdCombo = null;
  private IOptionSetPanel            paramsPanel = null;
  private boolean                    editable    = true;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - viewer flag, sets {@link #isViewer()} value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelSingleCondInfo( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
    editable = !aIsViewer;
    topicManager = REFDEF_TOPIC_MANAGER.getRef( aContext, ITsConditionsTopicManager.NONE );
    initTypeIdComboFromTopicManager();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void initTypeIdComboFromTopicManager() {
    if( typeIdCombo != null ) {
      typeIdCombo.setItems( topicManager.listSingleCondTypes().keys() );
    }
  }

  void whenValedTypeIdValueChanged() {
    updateParamsPanelAccordingTpTypeId();
    fireChangeEvent();
  }

  void whenParamsPanelChanged() {
    // defend parameters against typeIdCombo change
    if( !paramsPanel.canGetEntity().isError() ) {
      condParams.setAll( paramsPanel.getEntity() );
    }
    fireChangeEvent();
  }

  private void updateParamsPanelAccordingTpTypeId() {
    String typeId = typeIdCombo.getValue();
    ITsSingleCondType sct = null;
    if( typeId != null ) {
      sct = topicManager.listSingleCondTypes().findByKey( typeId );
    }
    IStridablesList<IDataDef> ddefs = sct != null ? sct.paramDefs() : IStridablesList.EMPTY;
    paramsPanel.setOptionDefs( ddefs );
    paramsPanel.setEntity( condParams );
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected ValidationResult doCanGetEntity() {
    // String name = nameText.getValue();
    // ValidationResult vr = IdPathStringValidator.IDPATH_EMPTY_VALIDATOR.validate( name );
    // if( vr.isError() ) {
    // return ValidationResult.error( MSG_ERR_SCPP_INV_NAME );
    // }
    String typeId = typeIdCombo.getValue();
    if( typeId == null ) {
      return ValidationResult.error( FMT_SCPP_UNKNOWN_SCT_ID, EMPTY_STRING );
    }
    if( topicManager == ITsConditionsTopicManager.NONE ) {
      return ValidationResult.error( FMT_NO_TOPIC_MANAGER, typeId );
    }
    ITsSingleCondType sct = topicManager.listSingleCondTypes().findByKey( typeId );
    if( sct == null ) {
      return ValidationResult.error( FMT_SCPP_UNKNOWN_SCT_ID, typeId );
    }
    ValidationResult vr = paramsPanel.canGetEntity();
    if( vr.isError() ) {
      return vr;
    }
    IOptionSet params = paramsPanel.getEntity();
    return ValidationResult.firstNonOk( vr, sct.validateParams( params ) );
  }

  @Override
  protected ITsSingleCondInfo doGetEntity() {
    String typeId = typeIdCombo.getValue();
    IOptionSet params = paramsPanel.getEntity();
    return new TsSingleCondInfo( typeId, params );
  }

  @Override
  protected void doProcessSetEntity() {
    if( specifiedEntity() == null ) {
      typeIdCombo.setValue( null );
      condParams.clear();
      paramsPanel.setOptionDefs( IStridablesList.EMPTY );
      return;
    }
    typeIdCombo.setValue( specifiedEntity().typeId() );
    condParams.setAll( specifiedEntity().params() );
    updateParamsPanelAccordingTpTypeId();
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new GridLayout( 2, false ) );
    Label l;
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
    l.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, false, 2, 1 ) );
    TsGuiContext ctx3 = new TsGuiContext( tsContext() );
    paramsPanel = new OptionSetPanel( ctx3, isViewer(), true );
    paramsPanel.createControl( board );
    paramsPanel.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, true, 2, 1 ) );
    // setup
    initTypeIdComboFromTopicManager();
    typeIdCombo.eventer().addListener( ( s, f ) -> whenValedTypeIdValueChanged() );
    paramsPanel.genericChangeEventer().addListener( s -> whenParamsPanelChanged() );
    typeIdCombo.setVisualsProvider( new TypeIdVisualsProvider() );
    return board;
  }

  // ------------------------------------------------------------------------------------
  // IPanelSingleCondInfo
  //

  @Override
  public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( !isViewer() && editable != aEditable ) {
      editable = aEditable;
      typeIdCombo.setEditable( editable );
      paramsPanel.setEditable( editable );
    }
  }

  @Override
  public ITsConditionsTopicManager topicManager() {
    return topicManager;
  }

  @Override
  public void setTopicManager( ITsConditionsTopicManager aTopicManager ) {
    TsNullArgumentRtException.checkNull( aTopicManager );
    topicManager = aTopicManager;
    initTypeIdComboFromTopicManager();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Invokes dialog with {@link IPanelSingleCondInfo} for {@link ITsSingleCondInfo} editing.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - the dialog window parameters
   * @param aInitVal {@link ITsSingleCondInfo} - initial value or <code>null</code>
   * @param aTopicManager {@link ITsConditionsTopicManager} - the conditions topic manager
   * @return {@link ITsSingleCondInfo} - edited value or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITsSingleCondInfo editDialog( ITsDialogInfo aDialogInfo, ITsSingleCondInfo aInitVal,
      ITsConditionsTopicManager aTopicManager ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aTopicManager );
    REFDEF_TOPIC_MANAGER.setRef( aDialogInfo.tsContext(), aTopicManager );
    IDialogPanelCreator<ITsSingleCondInfo, Object> creator = ( par, od ) //
    -> new TsDialogGenericEntityEditPanel<>( par, od, ( aContext, aViewer ) -> {
      PanelSingleCondInfo p = new PanelSingleCondInfo( aContext, aViewer );
      p.setTopicManager( aTopicManager );
      return p;
    } );
    TsDialog<ITsSingleCondInfo, Object> d = new TsDialog<>( aDialogInfo, aInitVal, null, creator );
    return d.execData();
  }

}
