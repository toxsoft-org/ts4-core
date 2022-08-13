package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class PanelSwtPatternSelector
    extends AbstractTsDialogPanel<ISwtPatternInfo, ITsGuiContext> {

  TsPanel topPanel;
  TsPanel contentHolder;

  Combo gtypeCombo;

  public PanelSwtPatternSelector( Composite aParent, TsDialog<ISwtPatternInfo, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ISwtPatternInfo aData ) {
    // nop
    if( aData != null ) {
      if( aData.type() == ESwtPatternType.GRADIENT ) {
        if( aData.gradientType() == EGradientType.LINEAR ) {
          stackLayout.topControl = linearGradientSelector;
          linearGradientSelector.setPatternInfo( aData );
        }
      }
    }

  }

  @Override
  protected ISwtPatternInfo doGetDataRecord() {
    if( stackLayout.topControl == linearGradientSelector ) {
      return linearGradientSelector.patternInfo();
    }
    if( stackLayout.topControl == radialGradientSelector ) {
      return radialGradientSelector.patternInfo();
    }
    if( stackLayout.topControl == cylinderGradientSelector ) {
      return cylinderGradientSelector.patternInfo();
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  StackLayout                   stackLayout;
  PanelLinearGradientSelector   linearGradientSelector;
  PanelRadialGradientSelector   radialGradientSelector;
  PanelCylinderGradientSelector cylinderGradientSelector;

  void init() {
    topPanel = new TsPanel( this, tsContext() );
    topPanel.setLayoutData( BorderLayout.NORTH );
    topPanel.setLayout( new RowLayout( SWT.HORIZONTAL ) );

    CLabel l = new CLabel( topPanel, SWT.NONE );
    l.setText( "Градиент: " );

    gtypeCombo = new Combo( topPanel, SWT.BORDER | SWT.DROP_DOWN );
    gtypeCombo.add( "Линейный" );
    gtypeCombo.add( "Радиальный" );
    gtypeCombo.add( "Цилиндрический" );
    gtypeCombo.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( gtypeCombo.getSelectionIndex() == 0 ) {
          stackLayout.topControl = linearGradientSelector;
        }
        if( gtypeCombo.getSelectionIndex() == 1 ) {
          stackLayout.topControl = radialGradientSelector;
        }
        if( gtypeCombo.getSelectionIndex() == 2 ) {
          stackLayout.topControl = cylinderGradientSelector;
        }
        pack();
      }
    } );

    stackLayout = new StackLayout();
    contentHolder = new TsPanel( this, tsContext() );
    contentHolder.setLayout( stackLayout );
    contentHolder.setLayoutData( BorderLayout.CENTER );

    linearGradientSelector = new PanelLinearGradientSelector( contentHolder, tsContext() );
    radialGradientSelector = new PanelRadialGradientSelector( contentHolder, tsContext() );
    cylinderGradientSelector = new PanelCylinderGradientSelector( contentHolder, tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает значение параметров заливки.
   * <p>
   *
   * @param aInfo ISwtPatternInfo - параметры заливки
   * @param aContext - контекст
   * @return ISwtPatternInfo - параметры заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final ISwtPatternInfo editPattern( ISwtPatternInfo aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<ISwtPatternInfo, ITsGuiContext> creator = PanelSwtPatternSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "Caption", "Title" );
    TsDialog<ISwtPatternInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
