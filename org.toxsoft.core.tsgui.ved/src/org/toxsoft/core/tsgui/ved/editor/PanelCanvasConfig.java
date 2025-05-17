package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.editor.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования свойств холста визуального редактора.
 * <p>
 *
 * @author vs
 */
public class PanelCanvasConfig
    extends AbstractTsDialogPanel<IVedCanvasCfg, ITsGuiContext> {

  private Spinner spinWidth;
  private Spinner spinHeight;

  private PanelTsFillInfoSelector fis;
  private PanelD2ConversionEditor convEditor;

  IGenericChangeListener changeListener;

  protected PanelCanvasConfig( Composite aParent, TsDialog<IVedCanvasCfg, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( IVedCanvasCfg aData ) {
    if( aData != null ) {
      spinWidth.setSelection( (int)aData.size().x() );
      spinHeight.setSelection( (int)aData.size().y() );
      fis.setFillInfo( aData.fillInfo() );
      convEditor.setDataRecord( aData.conversion() );
    }
  }

  @Override
  protected IVedCanvasCfg doGetDataRecord() {
    TsFillInfo fillInfo = fis.getFillInfo();
    int width = spinWidth.getSelection();
    int height = spinHeight.getSelection();
    return new VedCanvasCfg( fillInfo, new D2Point( width, height ), convEditor.getDataRecord() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void init() {
    setLayout( new GridLayout( 2, false ) );
    Group groupSize = new Group( this, SWT.NONE );
    groupSize.setText( STR_G_SIZES );
    groupSize.setLayout( new GridLayout( 2, false ) );

    CLabel l = new CLabel( groupSize, SWT.CENTER );
    l.setText( STR_L_WIDTH_PIX );

    spinWidth = new Spinner( groupSize, SWT.BORDER );
    spinWidth.setMinimum( 640 );
    spinWidth.setMaximum( 10000 );
    spinWidth.setSelection( 1920 );
    spinWidth.addSelectionListener( notificationSelectionListener );

    l = new CLabel( groupSize, SWT.CENTER );
    l.setText( STR_L_HEIGHT_PIX );

    spinHeight = new Spinner( groupSize, SWT.BORDER );
    spinHeight.setMinimum( 480 );
    spinHeight.setMaximum( 2160 );
    spinHeight.setSelection( 1080 );
    spinHeight.addSelectionListener( notificationSelectionListener );

    Group groupConversion = new Group( this, SWT.NONE );
    groupConversion.setText( STR_G_CONVERSIONS );
    groupConversion.setLayout( new GridLayout( 1, false ) );

    convEditor = new PanelD2ConversionEditor( groupConversion, tsContext(), null );

    fis = new PanelTsFillInfoSelector( this, environ(), null );
    fis.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
    fis.genericChangeEventer().addListener( notificationGenericChangeListener );

    if( tsContext().hasKey( IGenericChangeListener.class ) ) {
      changeListener = tsContext().get( IGenericChangeListener.class );
      genericChangeEventer().addListener( changeListener );
    }
  }

  // ------------------------------------------------------------------------------------
  // To use
  //

  /**
   * Статический метод вызова диалога редактирования свойств.
   *
   * @param aCanvasCfg {@link IVedCanvasCfg} - конфигурация холста отображения
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   * @return {@link IVedCanvasCfg} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final IVedCanvasCfg editCanvasConfig( IVedCanvasCfg aCanvasCfg, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<IVedCanvasCfg, ITsGuiContext> creator = PanelCanvasConfig::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_CANVAS_CFG, STR_MSG_CANVAS_CFG );
    TsDialog<IVedCanvasCfg, ITsGuiContext> d = new TsDialog<>( dlgInfo, aCanvasCfg, aContext, creator );
    return d.execData();
  }

}
