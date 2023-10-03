package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров линии.
 * <p>
 *
 * @author vs
 */
public class PanelGradientFillInfoEditor
    extends AbstractTsDialogPanel<TsGradientFillInfo, ITsGuiContext> {

  PanelGradientFillInfo gradientPanel;

  PanelGradientFillInfoEditor( Composite aParent, TsDialog<TsGradientFillInfo, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TsGradientFillInfo aData ) {
    if( aData != null ) {
      gradientPanel.setFillInfo( aData );
    }
  }

  @Override
  protected TsGradientFillInfo doGetDataRecord() {
    return gradientPanel.fillInfo();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void init() {
    setLayout( new BorderLayout() );
    gradientPanel = new PanelGradientFillInfo( this, environ() );
    gradientPanel.setLayoutData( BorderLayout.CENTER );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает значение параметров градиентной заливки.
   * <p>
   *
   * @param aInfo TsGradientFillInfo - параметры градиентной заливки
   * @param aContext - контекст
   * @return TsGradientFillInfo - параметры градиентной заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final TsGradientFillInfo editGradientInfo( TsGradientFillInfo aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<TsGradientFillInfo, ITsGuiContext> creator = PanelGradientFillInfoEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_LINE_INFO, STR_MSG_LINE_INFO );
    TsDialog<TsGradientFillInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
