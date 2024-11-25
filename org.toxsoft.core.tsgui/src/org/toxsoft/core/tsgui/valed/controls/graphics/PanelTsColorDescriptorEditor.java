package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
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
public class PanelTsColorDescriptorEditor
    extends AbstractTsDialogPanel<TsColorDescriptor, ITsGuiContext> {

  private ValedComboSelector<String> kindIdCombo;
  private IOptionSetPanel            panel;
  private PanelColorPreview          previewPanel;

  private TsColorDescriptor lastColorDescr = TsColorDescriptor.NONE; // last drawn image's descriptor

  PanelTsColorDescriptorEditor( Composite aParent, TsDialog<TsColorDescriptor, ITsGuiContext> aOwnerDialog ) {
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
  public PanelTsColorDescriptorEditor( Composite aParent, ITsGuiContext aContext, TsColorDescriptor aData,
      int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
    init();
    doSetDataRecord( aData );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsColorDescriptor aData ) {
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
      ITsColorSourceKind kind = TsColorDescriptor.getColorSourceKindsMap().getByKey( kindId );
      vr = ValidationResult.firstNonOk( vr, kind.validateParams( ops ) );
    }
    return vr;
  }

  @Override
  protected TsColorDescriptor doGetDataRecord() {
    String kindId = kindIdCombo.getValue();
    IOptionSet ops = panel.getEntity();
    return new TsColorDescriptor( kindId, ops );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void init() {
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.VERTICAL );
    // TOP
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
        TsColorDescriptor.getColorSourceKindsMap().keys(), //
        aItem -> {
          if( aItem != null ) {
            ITsColorSourceKind kind = TsColorDescriptor.getColorSourceKindsMap().findByKey( aItem );
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
    // imageWidget = new PdwWidgetSimple( new TsGuiContext( tsContext() ) );
    // imageWidget.createControl( sfMain );
    // imageWidget.getControl().setLayoutData( BorderLayout.EAST );
    // imageWidget.setAreaPreferredSize( EThumbSize.SZ360.pointSize() );
    // imageWidget.setFitInfo( RectFitInfo.BEST );
    // imageWidget.setFulcrum( ETsFulcrum.CENTER );
    // imageWidget.setPreferredSizeFixed( false );
    // imageWidget.setTsImage( null );

    // BOTTOM colorPreview
    ITsColorManager cm = colorManager();
    previewPanel = new PanelColorPreview( sfMain, cm.getColor( ETsColor.GRAY ), cm.getColor( ETsColor.DARK_GRAY ), cm );
    previewPanel.setColor( currColor(), currColor().getAlpha() );

    //
    updateOptionsPanelOnKindIdChange();
    genericChangeEventer().addListener( aSource -> refreshImage() );

    sfMain.setWeights( 3, 7 );
  }

  /**
   * Sets the options definitions to {@link #panel} depending on image source kind ID selected in {@link #kindIdCombo}.
   */
  private void updateOptionsPanelOnKindIdChange() {
    String kindId = kindIdCombo.getValue();
    ITsColorSourceKind kind = TsColorDescriptor.getColorSourceKindsMap().getByKey( kindId );
    panel.setOptionDefs( kind.opDefs() );
  }

  /**
   * Refreshes displayed color.
   */
  private void refreshImage() {
    TsColorDescriptor newColorDescr = TsColorDescriptor.NONE;
    if( !doValidate().isError() ) {
      newColorDescr = doGetDataRecord();
      if( !newColorDescr.equals( lastColorDescr ) ) { // proceed only if image was actually changed
        lastColorDescr = newColorDescr;
        Color color = currColor();
        previewPanel.setColor( color, color.getAlpha() );
        previewPanel.redraw();
      }
      return;
    }
    lastColorDescr = TsColorDescriptor.NONE;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private Color currColor() {
    ITsColorSourceKind kind = TsColorDescriptor.getColorSourceKindsMap().getByKey( lastColorDescr.kindId() );
    Color color = kind.createColor( lastColorDescr );
    return color;
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit TsColorDescriptor
  //

  /**
   * Invokes {@link TsColorDescriptor} editing dialog.
   *
   * @param aColorDescr {@link TsColorDescriptor} - initial value or {@link TsColorDescriptor#NONE}
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsColorDescriptor} - edited value or <code>null</code>
   */
  public static final TsColorDescriptor editColorDescriptor( TsColorDescriptor aColorDescr, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aColorDescr, aContext );
    IDialogPanelCreator<TsColorDescriptor, ITsGuiContext> creator = PanelTsColorDescriptorEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "DLG_T_COLOR_DESCRIPTOR", "STR_MSG_COLOR_DESCRIPTOR" );
    TsDialog<TsColorDescriptor, ITsGuiContext> d = new TsDialog<>( dlgInfo, aColorDescr, aContext, creator );
    return d.execData();
  }

}
