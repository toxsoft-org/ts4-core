package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров градиентной заливки.
 * <p>
 *
 * @author vs
 */
public class PanelGradientFillInfo
    extends TsPanel {

  TsPanel topPanel;
  TsPanel contentHolder;

  Combo gtypeCombo;

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - ссответствтвующий контекст
   */
  public PanelGradientFillInfo( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    init();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setFillInfo( TsGradientFillInfo aData ) {
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

  public TsGradientFillInfo fillInfo() {
    // if( stackLayout.topControl == linearGradientSelector ) {
    // return new TsGradientFillInfo( new TsGradientFillInfo( linearGradientSelector.patternInfo() ) );
    // }
    // if( stackLayout.topControl == radialGradientSelector ) {
    // return new TsGradientFillInfo( new TsGradientFillInfo( radialGradientSelector.patternInfo() ) );
    // }
    // if( stackLayout.topControl == cylinderGradientSelector ) {
    // return new TsGradientFillInfo( new TsGradientFillInfo( cylinderGradientSelector.patternInfo() ) );
    // }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  StackLayout stackLayout;

  PanelLinearGradientSelector   linearGradientSelector;
  PanelRadialGradientSelector   radialGradientSelector;
  PanelCylinderGradientSelector cylinderGradientSelector;

  ITsVisualsProvider<? extends IStridable> visualsProvider = IStridable::nmName;

  ValedEnumCombo<EGradientType> fillKindCombo;

  void init() {
    setLayout( new BorderLayout() );

    topPanel = new TsPanel( this, tsContext() );
    topPanel.setLayoutData( BorderLayout.NORTH );
    topPanel.setLayout( new RowLayout( SWT.HORIZONTAL ) );

    CLabel l = new CLabel( topPanel, SWT.NONE );
    l.setText( "Тип градиента: " );
    fillKindCombo = new ValedEnumCombo( tsContext(), EGradientType.class, visualsProvider );
    fillKindCombo.createControl( topPanel );
    fillKindCombo.setValue( EGradientType.LINEAR );
    fillKindCombo.eventer().addListener( ( aSource, aEditFinished ) -> {
      EGradientType type = fillKindCombo.getValue();
      switch( type ) {
        case NONE:
          break;
        case LINEAR:
          stackLayout.topControl = linearGradientSelector;
          break;
        case CYLINDER:
          stackLayout.topControl = cylinderGradientSelector;
          break;
        case RADIAL:
          stackLayout.topControl = radialGradientSelector;
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

    linearGradientSelector = new PanelLinearGradientSelector( contentHolder, tsContext() );
    radialGradientSelector = new PanelRadialGradientSelector( contentHolder, tsContext() );
    cylinderGradientSelector = new PanelCylinderGradientSelector( contentHolder, tsContext() );

    stackLayout.topControl = linearGradientSelector;

  }

}
