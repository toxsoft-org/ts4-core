package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров заливки.
 * <p>
 *
 * @author vs
 */
public class PanelTsFillInfoSelector
    extends AbstractTsDialogPanel<TsFillInfo, ITsGuiContext> {

  TsPanel topPanel;
  TsPanel contentHolder;

  Combo gtypeCombo;

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
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
    if( aData != null ) {
      switch( aData.kind() ) {
        case NONE:
          break;
        case SOLID:
          fillKindCombo.setValue( ETsFillKind.SOLID );
          colorPanel.setRgba( aData.fillColor() );
          stackLayout.topControl = colorPanel;
          break;
        case GRADIENT:
          fillKindCombo.setValue( ETsFillKind.GRADIENT );
          gradientPanel.setFillInfo( aData.gradientFillInfo() );
          stackLayout.topControl = gradientPanel;
          break;
        case IMAGE:
          fillKindCombo.setValue( ETsFillKind.IMAGE );
          stackLayout.topControl = imagePanel;
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  @Override
  protected TsFillInfo doGetDataRecord() {
    return switch( fillKindCombo.getValue() ) {
      case NONE -> TsFillInfo.NONE;
      case GRADIENT -> new TsFillInfo( gradientPanel.fillInfo() );
      case IMAGE -> TsFillInfo.NONE;
      case SOLID -> new TsFillInfo( colorPanel.rgba() );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  StackLayout stackLayout;

  PanelColorFillInfo    colorPanel;
  PanelImageFillInfo    imagePanel;
  PanelGradientFillInfo gradientPanel;

  ValedEnumCombo<ETsFillKind> fillKindCombo;

  void init() {
    topPanel = new TsPanel( this, tsContext() );
    topPanel.setLayoutData( BorderLayout.NORTH );
    topPanel.setLayout( new RowLayout( SWT.HORIZONTAL ) );

    CLabel l = new CLabel( topPanel, SWT.NONE );
    l.setText( STR_L_FILL_TYPE );
    fillKindCombo = new ValedEnumCombo<>( tsContext(), ETsFillKind.class, IStridable::nmName );
    fillKindCombo.createControl( topPanel );
    fillKindCombo.setValue( ETsFillKind.SOLID );
    fillKindCombo.eventer().addListener( ( aSource, aEditFinished ) -> {
      stackLayout.topControl = switch( fillKindCombo.getValue() ) {
        case NONE -> null;
        case GRADIENT -> gradientPanel;
        case IMAGE -> imagePanel;
        case SOLID -> colorPanel;
        default -> throw new TsNotAllEnumsUsedRtException();
      };
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
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_FILL_INFO, STR_MSG_FILL_INFO );
    TsDialog<TsFillInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
