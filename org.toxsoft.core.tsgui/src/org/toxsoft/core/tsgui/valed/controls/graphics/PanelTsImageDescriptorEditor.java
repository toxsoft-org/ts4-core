package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров дескриптора изображения.
 * <p>
 *
 * @author vs
 * @author hazard157
 */
public class PanelTsImageDescriptorEditor
    extends AbstractTsDialogPanel<TsImageDescriptor, ITsGuiContext> {

  private ValedComboSelector<String> kindIdCombo;
  private IOptionSetPanel            panel;
  private IPdwWidget                 imageWidget;

  private TsImage           lastImage  = null;                   // last drawn image
  private TsImageDescriptor lastImDesc = TsImageDescriptor.NONE; // last drawn image's descriptor

  PanelTsImageDescriptorEditor( Composite aParent, TsDialog<TsImageDescriptor, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  /**
   * Constructor to be used as a generic panel.
   *
   * @param aParent {@link Composite} - the parent composite
   * @param aContext {@link ITsGuiContext} - the context
   * @param aData &lt;T&gt; - initial data record value, may be <code>null</code>
   * @param aFlags int - ORed dialog configuration flags <code>DF_XXX</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelTsImageDescriptorEditor( Composite aParent, ITsGuiContext aContext, TsImageDescriptor aData,
      int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
    init();
    doSetDataRecord( aData );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsImageDescriptor aData ) {
    genericChangeEventer().pauseFiring();
    try {
      if( aData != null ) {
        kindIdCombo.setValue( aData.kindId() );
        updateOptionsPanelOnKindIdChange();
        panel.setEntity( aData.params() );
      }
      else {
        kindIdCombo.setValue( TsImageSourceKindNone.KIND_ID );
        updateOptionsPanelOnKindIdChange();
      }
      fireContentChangeEvent();
    }
    finally {
      genericChangeEventer().resumeFiring( true );
    }
  }

  @Override
  protected ValidationResult doValidate() {
    String kindId = kindIdCombo.getValue();
    ValidationResult vr = panel.canGetEntity();
    if( !vr.isError() ) {
      IOptionSet ops = panel.getEntity();
      ITsImageSourceKind kind = TsImageDescriptor.getImageSourceKindsMap().getByKey( kindId );
      vr = ValidationResult.firstNonOk( vr, kind.validateParams( ops ) );
    }
    return vr;
  }

  @Override
  protected TsImageDescriptor doGetDataRecord() {
    String kindId = kindIdCombo.getValue();
    IOptionSet ops = panel.getEntity();
    return new TsImageDescriptor( kindId, ops );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void init() {
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    // LEFT
    Composite leftBoard = new Composite( sfMain, SWT.BORDER );
    leftBoard.setLayout( new BorderLayout() );
    // kind selection board
    Composite topBoard = new Composite( leftBoard, SWT.BORDER );
    topBoard.setLayoutData( BorderLayout.NORTH );
    topBoard.setLayout( new BorderLayout() );
    CLabel l = new CLabel( topBoard, SWT.CENTER );
    l.setLayoutData( BorderLayout.WEST );
    l.setText( STR_IMG_SOURCE_KIND );
    l.setToolTipText( STR_IMG_SOURCE_KIND_D );
    kindIdCombo = new ValedComboSelector<>( tsContext(), //
        TsImageDescriptor.getImageSourceKindsMap().keys(), //
        aItem -> {
          if( aItem != null ) {
            ITsImageSourceKind kind = TsImageDescriptor.getImageSourceKindsMap().findByKey( aItem );
            if( kind != null ) {
              return kind.nmName();
            }
          }
          return TsLibUtils.EMPTY_STRING;
        } //
    );
    kindIdCombo.createControl( topBoard );
    kindIdCombo.getControl().setLayoutData( BorderLayout.CENTER );
    kindIdCombo.eventer().addListener( ( aSource, aEditFinished ) -> updateOptionsPanelOnKindIdChange() );
    kindIdCombo.eventer().addListener( notificationValedControlChangeListener );
    // options panel
    panel = new OptionSetPanel( tsContext(), false, true );
    panel.createControl( leftBoard );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.genericChangeEventer().addListener( notificationGenericChangeListener );

    // RIGHT imageWidget
    imageWidget = new PdwWidgetSimple( new TsGuiContext( tsContext() ) );
    imageWidget.createControl( sfMain );
    imageWidget.getControl().setLayoutData( BorderLayout.EAST );
    imageWidget.setAreaPreferredSize( EThumbSize.SZ360.pointSize() );
    imageWidget.setFitInfo( RectFitInfo.BEST );
    imageWidget.setFulcrum( ETsFulcrum.CENTER );
    imageWidget.setPreferredSizeFixed( false );
    imageWidget.setTsImage( null );
    //
    updateOptionsPanelOnKindIdChange();
    genericChangeEventer().addListener( aSource -> refreshImage() );
    this.addDisposeListener( aE -> {
      if( lastImage != null ) {
        lastImage.dispose();
      }
    } );

    //
    sfMain.setWeights( 6000, 4000 );
  }

  /**
   * Sets the options definitions to {@link #panel} depending on image source kind ID selected in {@link #kindIdCombo}.
   */
  private void updateOptionsPanelOnKindIdChange() {
    String kindId = kindIdCombo.getValue();
    ITsImageSourceKind kind = TsImageDescriptor.getImageSourceKindsMap().getByKey( kindId );
    panel.setOptionDefs( kind.opDefs() );
  }

  /**
   * Refreshes displayed image, {@link #lastImage} and {@link #lastImDesc}.
   */
  private void refreshImage() {
    TsImageDescriptor newImDescr = TsImageDescriptor.NONE;
    if( !doValidate().isError() ) {
      newImDescr = doGetDataRecord();
      if( !newImDescr.equals( lastImDesc ) ) { // proceed only if image was actually changed
        if( lastImage != null ) {
          lastImage.dispose();
          lastImage = null;
        }
        ITsImageSourceKind kind = TsImageDescriptor.getImageSourceKindsMap().getByKey( newImDescr.kindId() );
        lastImage = kind.createImage( newImDescr, tsContext() );
        lastImDesc = newImDescr;
        imageWidget.setTsImage( lastImage );
        imageWidget.redraw();
      }
      return;
    }
    // reset image - image
    if( lastImage != null ) {
      lastImage.dispose();
      lastImage = null;
    }
    lastImDesc = TsImageDescriptor.NONE;
    imageWidget.setTsImage( null );
    imageWidget.redraw();
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit TsImageDescriptor
  //

  /**
   * Invokes {@link TsImageDescriptor} editing dialog.
   *
   * @param aImgDescr {@link TsImageDescriptor} - initial value or {@link TsImageDescriptor#NONE}
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsImageDescriptor} - edited value or <code>null</code>
   */
  public static final TsImageDescriptor editImageDescriptor( TsImageDescriptor aImgDescr, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aImgDescr, aContext );
    IDialogPanelCreator<TsImageDescriptor, ITsGuiContext> creator = PanelTsImageDescriptorEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_IMAGE_DESCRIPTOR, STR_MSG_IMAGE_DESCRIPTOR );
    TsDialog<TsImageDescriptor, ITsGuiContext> d = new TsDialog<>( dlgInfo, aImgDescr, aContext, creator );
    return d.execData();
  }

}
