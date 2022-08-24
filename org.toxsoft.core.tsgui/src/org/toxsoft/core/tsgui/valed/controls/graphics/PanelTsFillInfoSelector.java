package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class PanelTsFillInfoSelector
    extends AbstractTsDialogPanel<TsFillInfo, ITsGuiContext> {

  TsPanel topPanel;
  TsPanel contentHolder;

  Combo gtypeCombo;

  public PanelTsFillInfoSelector( Composite aParent, TsDialog<TsFillInfo, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsFillInfo aData ) {
    // nop
    if( aData != null ) {
      // if( aData.kind() == ETsFillKind.GRADIENT ) {
      // TsGradientFillInfo gfi = aData.gradientFillInfo();
      // if( gfi.gradientType() == EGradientType.LINEAR ) {
      // stackLayout.topControl = linearGradientSelector;
      // linearGradientSelector.setPatternInfo( gfi.linearGradientInfo() );
      // }
      // }
    }

  }

  @Override
  protected TsFillInfo doGetDataRecord() {
    switch( fillKindCombo.getValue() ) {
      case NONE:
        return TsFillInfo.NONE;
      case GRADIENT:
        return TsFillInfo.NONE;
      case IMAGE:
        return TsFillInfo.NONE;
      case SOLID:
        return new TsFillInfo( colorPanel.rgba() );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  StackLayout stackLayout;

  PanelColorFillInfo    colorPanel;
  PanelImageFillInfo    imagePanel;
  PanelGradientFillInfo gradientPanel;

  ITsVisualsProvider<? extends IStridable> visualsProvider = IStridable::nmName;

  ValedEnumCombo<ETsFillKind> fillKindCombo;

  void init() {
    topPanel = new TsPanel( this, tsContext() );
    topPanel.setLayoutData( BorderLayout.NORTH );
    topPanel.setLayout( new RowLayout( SWT.HORIZONTAL ) );

    CLabel l = new CLabel( topPanel, SWT.NONE );
    l.setText( "Тип заливки: " );
    fillKindCombo = new ValedEnumCombo( tsContext(), ETsFillKind.class, visualsProvider );
    fillKindCombo.createControl( topPanel );
    fillKindCombo.setValue( ETsFillKind.SOLID );
    fillKindCombo.eventer().addListener( ( aSource, aEditFinished ) -> {
      ETsFillKind kind = fillKindCombo.getValue();
      switch( kind ) {
        case NONE:
          break;
        case GRADIENT:
          stackLayout.topControl = gradientPanel;
          break;
        case IMAGE:
          stackLayout.topControl = imagePanel;
          break;
        case SOLID:
          stackLayout.topControl = colorPanel;
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
      contentHolder.layout( true );
    } );

    stackLayout = new StackLayout();
    contentHolder = new TsPanel( this, tsContext() );
    contentHolder.setLayout( stackLayout );
    contentHolder.setLayoutData( BorderLayout.CENTER );

    colorPanel = new PanelColorFillInfo( contentHolder, tsContext() );
    imagePanel = new PanelImageFillInfo( contentHolder, tsContext() );
    gradientPanel = new PanelGradientFillInfo( contentHolder, tsContext() );

    stackLayout.topControl = colorPanel;
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает значение параметров заливки.
   * <p>
   *
   * @param aInfo TsFillInfo - параметры заливки
   * @param aContext - контекст
   * @return TsFillInfo - параметры заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final TsFillInfo editPattern( TsFillInfo aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsFillInfo, ITsGuiContext> creator = PanelTsFillInfoSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "Caption", "Title" );
    TsDialog<TsFillInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
