package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.editor.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.std.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

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
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static VedItemCfg editVedItemBasicProperties( IVedItemCfg aCfg, IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNulls( aCfg, aVedScreen );
    IDialogPanelCreator<VedItemCfg, IVedScreen> creator = VedItemBasicPropertiesPanel::new;
    String caption = String.format( FMT_DLG_VED_ITEM_BASICS, aCfg.kind().id() );
    String title = String.format( FMT_DLG_VED_ITEM_BASICS_D, aCfg.kind().nmName() );
    TsDialogInfo tdi = new TsDialogInfo( aVedScreen.tsContext(), caption, title );
    TsDialog<VedItemCfg, IVedScreen> d = new TsDialog<>( tdi, new VedItemCfg( aCfg ), aVedScreen, creator );
    return d.execData();
  }

  /**
   * Returns visel factory.
   *
   * @param aVisel IVedItem - visel
   * @param aTsContext - corresponding context
   * @return {@link IVedViselFactory} - visel factory
   * @throws TsIllegalArgumentRtException - if aItem is not {@link IVedVisel}
   * @throws TsItemNotFoundRtException - if there is no factory with requested ID.
   */
  public static IVedViselFactory viselFactory( IVedItem aVisel, ITsGuiContext aTsContext ) {
    TsIllegalArgumentRtException.checkFalse( aVisel.kind() == EVedItemKind.VISEL );
    IVedViselFactoriesRegistry vfReg = aTsContext.get( IVedViselFactoriesRegistry.class );
    return vfReg.get( aVisel.factoryId() );
  }

  /**
   * Returns result of checking if aClazz equals pointed property class.
   *
   * @param aClazz {@link Class} - class to be checked
   * @param aPropId String - property ID
   * @param aVisel {@link IVedItem} - visel
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   * @return <b>true</b> - aClazz equals the property Class<br>
   *         <b>false</b> - aClazz not equals the property Class
   */
  public static boolean isPropertyClass( Class<?> aClazz, String aPropId, IVedItem aVisel, ITsGuiContext aTsContext ) {
    IVedViselFactory vFact = viselFactory( aVisel, aTsContext );
    IDataDef dataDef = vFact.propDefs().getByKey( aPropId );
    return TsValobjUtils.getKeeperIdByClass( aClazz ).equals( dataDef.keeperId() );
  }

  /**
   * Returns result of checking has visel properties aPropId or not.
   *
   * @param aPropId String - property ID
   * @param aVisel {@link IVedItem} - visel
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   * @return <b>true</b> - visel properties has aPropId<br>
   *         <b>false</b> - there is no property with aPropId
   */
  public static boolean isViselProperyId( String aPropId, IVedItem aVisel, ITsGuiContext aTsContext ) {
    IVedViselFactory vFact = viselFactory( aVisel, aTsContext );
    return vFact.propDefs().findByKey( aPropId ) != null;
  }

  /**
   * No subclasses.
   */
  private VedEditorUtils() {
    // nop
  }

}
