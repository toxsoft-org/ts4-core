package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.editor.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.std.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility and helper methods.
 *
 * @author hazard157
 */
public class VedEditorUtils {

  /**
   * Dialog panel for VED item basic properties editing.
   *
   * @author hazard157
   */
  static class VedItemBasicPropertiesPanel
      extends AbstractTsDialogPanel<VedItemCfg, IVedScreen> {

    private final Text txtId;
    private final Text txtName;
    private final Text txtDescription;

    VedItemBasicPropertiesPanel( Composite aParent, TsDialog<VedItemCfg, IVedScreen> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new GridLayout( 2, false ) );
      CLabel label;
      // ID
      label = new CLabel( this, SWT.CENTER );
      label.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
      label.setText( STR_LABEL_VIBP_ID );
      label.setToolTipText( STR_LABEL_VIBP_ID_D );
      txtId = new Text( this, SWT.BORDER );
      txtId.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
      txtId.addModifyListener( notificationModifyListener );
      // nmName
      label = new CLabel( this, SWT.CENTER );
      label.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
      label.setText( STR_LABEL_VIBP_NAME );
      label.setToolTipText( STR_LABEL_VIBP_NAME );
      txtName = new Text( this, SWT.BORDER );
      txtName.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
      txtName.addModifyListener( notificationModifyListener );
      // description
      label = new CLabel( this, SWT.CENTER );
      label.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
      label.setText( STR_LABEL_VIBP_DESCRIPTION );
      label.setToolTipText( STR_LABEL_VIBP_DESCRIPTION_D );
      txtDescription = new Text( this, SWT.BORDER );
      txtDescription.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
      txtDescription.addModifyListener( notificationModifyListener );
    }

    // ------------------------------------------------------------------------------------
    // AbstractTsDialogPanel
    //

    @Override
    protected void doSetDataRecord( VedItemCfg aData ) {
      if( aData != null ) {
        txtId.setText( aData.id() );
        txtName.setText( aData.nmName() );
        txtDescription.setText( aData.description() );
      }
      else {
        txtId.setText( TsLibUtils.EMPTY_STRING );
        txtName.setText( TsLibUtils.EMPTY_STRING );
        txtDescription.setText( TsLibUtils.EMPTY_STRING );
      }
    }

    @Override
    protected ValidationResult doValidate() {
      String id = txtId.getText();
      // check ID is an ID path
      if( !StridUtils.isValidIdPath( id ) ) {
        return ValidationResult.error( STR_ERR_VIBP_ID_NOT_IDPATH );
      }
      // check ID is unique on the screen
      String oldId = dataRecordInput().id();
      EVedItemKind kind = dataRecordInput().kind();
      IVedItemsManager<?> itemManager;
      switch( kind ) {
        case ACTOR: {
          itemManager = environ().model().actors();
          break;
        }
        case VISEL: {
          itemManager = environ().model().visels();
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException( kind.id() );
      }
      if( itemManager.list().hasKey( id ) && !id.equals( oldId ) ) {
        return ValidationResult.error( FMT_ERR_VIBP_DUPLICATE_ID, kind.nmName(), id );
      }
      // warn of unassigned name
      String name = txtName.getText();
      ValidationResult vr = NameStringValidator.VALIDATOR.validate( name );
      if( !vr.isOk() ) {
        return vr;
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    protected VedItemCfg doGetDataRecord() {
      VedItemCfg cfg = new VedItemCfg( txtId.getText(), dataRecordInput() );
      cfg.propValues().setStr( PROP_NAME, txtName.getText() );
      cfg.propValues().setStr( PROP_DESCRIPTION, txtDescription.getText() );
      return cfg;
    }

  }

  /**
   * Invokes the dialog to edit VED item ID, nmName and description.
   *
   * @param aCfg {@link IVedItemCfg} - configuration to edit
   * @param aVedScreen {@link IVedScreen} - the item owner
   * @return {@link VedItemCfg} - the instance of the edited configuration or <code>null</code>
   */
  public static VedItemCfg editVedItemBasicProperties( IVedItemCfg aCfg, IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNulls( aCfg, aVedScreen );
    VedItemCfg cfg = new VedItemCfg( aCfg );
    IDialogPanelCreator<VedItemCfg, IVedScreen> creator = VedItemBasicPropertiesPanel::new;
    String caption = String.format( FMT_DLG_VED_ITEM_BASICS, aCfg.kind().id() );
    String title = String.format( FMT_DLG_VED_ITEM_BASICS_D, aCfg.kind().nmName() );
    TsDialogInfo tdi = new TsDialogInfo( aVedScreen.tsContext(), caption, title );
    TsDialog<VedItemCfg, IVedScreen> d = new TsDialog<>( tdi, cfg, aVedScreen, creator );
    return d.execData();
  }

  // /**
  // * Creates current configuration of the VED screen model.
  // *
  // * @param aVedScreen {@link IVedScreen} - the VED screen
  // * @return {@link VedScreenCfg} - created instance of the configuration
  // * @throws TsNullArgumentRtException any argument = <code>null</code>
  // */
  // public static VedScreenCfg getVedScreenConfig( IVedScreen aVedScreen ) {
  // TsNullArgumentRtException.checkNull( aVedScreen );
  // VedScreenCfg scrCfg = new VedScreenCfg();
  // IVedScreenModel sm = aVedScreen.model();
  // for( VedAbstractVisel item : sm.visels().list() ) {
  // VedItemCfg cfg = VedItemCfg.ofVisel( item.id(), item.factoryId(), item.params(), item.props() );
  // scrCfg.viselCfgs().add( cfg );
  // }
  // for( VedAbstractActor item : sm.actors().list() ) {
  // VedItemCfg cfg = VedItemCfg.ofActor( item.id(), item.factoryId(), item.params(), item.props() );
  // scrCfg.actorCfgs().add( cfg );
  // }
  // scrCfg.canvasCfg().copyFrom( aVedScreen.view().canvasConfig() );
  // return scrCfg;
  // }
  //
  // /**
  // * Sets configuration to the VED screen.
  // *
  // * @param aVedScreen {@link IVedScreen} - the VED screen
  // * @param aScreenCfg {@link IVedScreenCfg} - configuration to be applied to the screen
  // * @throws TsNullArgumentRtException any argument = <code>null</code>
  // */
  // public static void setVedScreenConfig( IVedScreen aVedScreen, IVedScreenCfg aScreenCfg ) {
  // TsNullArgumentRtException.checkNulls( aVedScreen, aScreenCfg );
  // IVedScreenModel sm = aVedScreen.model();
  // try {
  // sm.actors().eventer().pauseFiring();
  // sm.visels().eventer().pauseFiring();
  // sm.actors().clear();
  // sm.visels().clear();
  // for( IVedItemCfg cfg : aScreenCfg.viselCfgs() ) {
  // sm.visels().create( cfg );
  // }
  // for( IVedItemCfg cfg : aScreenCfg.actorCfgs() ) {
  // sm.actors().create( cfg );
  // }
  // aVedScreen.view().setCanvasConfig( aScreenCfg.canvasCfg() );
  // }
  // finally {
  // sm.visels().eventer().resumeFiring( true );
  // sm.actors().eventer().resumeFiring( true );
  // }
  // }

  /**
   * No subclasses.
   */
  private VedEditorUtils() {
    // nop
  }

}
