package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель для выбора цвет с прозрачностью.
 * <p>
 *
 * @author vs
 */
public class PanelRgbaSelector
    extends AbstractTsDialogPanel<RGBA, ITsGuiContext> {

  RgbaSelector rgbaSelector;

  protected PanelRgbaSelector( Composite aParent, TsDialog<RGBA, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    rgbaSelector = new RgbaSelector( this, SWT.NONE, eclipseContext() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( RGBA aData ) {
    rgbaSelector.setRgba( aData );
  }

  @Override
  protected RGBA doGetDataRecord() {
    return rgbaSelector.rgba();
  }

  // ------------------------------------------------------------------------------------
  //
  //

  /**
   * Рдактирует и возвращает значение цвета с прозрачностью.
   * <p>
   *
   * @param aRgba RGBA - компоненты цвета с прозрачностью
   * @param aContext - контекст
   * @return RGBA - компоненты цвета с прозрачностью или <b>null</b> в случает отказа от редактирования
   */
  public static final RGBA editRgba( RGBA aRgba, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<RGBA, ITsGuiContext> creator = PanelRgbaSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_COLOR_INFO, STR_MSG_COLOR_INFO );
    TsDialog<RGBA, ITsGuiContext> d = new TsDialog<>( dlgInfo, aRgba, aContext, creator );
    return d.execData();
  }

}
