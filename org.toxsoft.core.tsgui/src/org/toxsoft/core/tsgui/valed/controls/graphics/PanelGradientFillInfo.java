package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

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

  /**
   * Задает параметры заливки.
   *
   * @param aData TsGradientFillInfo - параметры заливки
   */
  public void setFillInfo( TsGradientFillInfo aData ) {
    if( aData != null ) {
      switch( aData.gradientType() ) {
        case NONE:
          break;
        case LINEAR:
          fillKindCombo.setValue( EGradientType.LINEAR );
          linearGradientSelector.setGradientInfo( aData.linearGradientInfo() );
          stackLayout.topControl = linearGradientSelector;
          break;
        case RADIAL:
          fillKindCombo.setValue( EGradientType.RADIAL );
          radialGradientSelector.setGradientInfo( aData.radialGradientInfo() );
          stackLayout.topControl = radialGradientSelector;
          break;
        case CYLINDER:
          fillKindCombo.setValue( EGradientType.CYLINDER );
          cylinderGradientSelector.setGradientInfo( aData.cylinderGradientInfo() );
          stackLayout.topControl = cylinderGradientSelector;
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }

  }

  /**
   * Возвращает параметры градиентной заливки.
   *
   * @return TsGradientFillInfo - параметры градиентной заливки
   */
  public TsGradientFillInfo fillInfo() {
    switch( fillKindCombo.getValue() ) {
      case NONE:
        break;
      case CYLINDER:
        return new TsGradientFillInfo( cylinderGradientSelector.gradientInfo() );
      case LINEAR:
        return new TsGradientFillInfo( linearGradientSelector.gradientInfo() );
      case RADIAL:
        return new TsGradientFillInfo( radialGradientSelector.gradientInfo() );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  StackLayout stackLayout;

  PanelLinearGradientSelectorEx linearGradientSelector;
  PanelRadialGradientSelector   radialGradientSelector;
  PanelCylinderGradientSelector cylinderGradientSelector;

  ITsVisualsProvider<EGradientType> visualsProvider = IStridable::nmName;

  ValedEnumCombo<EGradientType> fillKindCombo;

  void init() {
    setLayout( new BorderLayout() );

    topPanel = new TsPanel( this, tsContext() );
    topPanel.setLayoutData( BorderLayout.NORTH );
    topPanel.setLayout( new RowLayout( SWT.HORIZONTAL ) );

    CLabel l = new CLabel( topPanel, SWT.NONE );
    l.setText( STR_L_GRADIENT_TYPE );
    fillKindCombo = new ValedEnumCombo<>( tsContext(), EGradientType.class, visualsProvider );
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

    linearGradientSelector = new PanelLinearGradientSelectorEx( contentHolder, tsContext() );
    radialGradientSelector = new PanelRadialGradientSelector( contentHolder, tsContext() );
    cylinderGradientSelector = new PanelCylinderGradientSelector( contentHolder, tsContext() );

    stackLayout.topControl = linearGradientSelector;

  }

}
