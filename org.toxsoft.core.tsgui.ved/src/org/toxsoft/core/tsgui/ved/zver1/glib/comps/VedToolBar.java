package org.toxsoft.core.tsgui.ved.zver1.glib.comps;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.zver1.utils.*;

/**
 * Панель быстрого доступа к функциональности редактора.
 * <p>
 * Явлется прямоугольной областью, в которой размещаются элементы управления, такие как кнопки, мкню, выпадающие списки
 * и т.д. В качестве примера стандартных элементов управления можно привести:<br>
 * <ul>
 * <li>Кнопки изенения масштаба отображения</li>
 * <li>Кнопки отмены и возврата операций</li>
 * </ul>
 *
 * @author vs
 */
public class VedToolBar
    extends TsPanel
    implements IVedContextable {

  /**
   * Панель быстрого доступа (расположена вверху панели)
   */
  private ITsToolbar toolbar;

  private EIconSize iconSize;

  public VedToolBar( Composite aParent, EIconSize aIconSize, ITsGuiContext aContext ) {
    super( aParent, aContext );
    iconSize = aIconSize;
    toolbar = createToolBar();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Добавляет слушателя панели быстрого доступа.<br>
   *
   * @param aListener ITsToolbarListener - слушатель панели быстрого доступа
   */
  public void addListener( ITsToolbarListener aListener ) {
    toolbar.addListener( aListener );
  }

  /**
   * Удаляет слушателя панели быстрого доступа.<br>
   *
   * @param aListener ITsToolbarListener - слушатель панели быстрого доступа
   */
  public void removeListener( ITsToolbarListener aListener ) {
    toolbar.removeListener( aListener );
  }

  /**
   * Находит "действие" с указанным идентификатором.<br>
   *
   * @param aActionId String - ИД "действия"
   * @return TsAction - "действие" с указанным идентификатором
   */
  public TsAction findAction( String aActionId ) {
    return toolbar.findAction( aActionId );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private ITsToolbar createToolBar() {

    BorderLayout bl = new BorderLayout();
    bl.setMargins( 2, 4, 0, 0 );
    this.setLayout( bl );

    ITsToolbar tb = new TsToolbar( tsContext() );

    tb = TsToolbar.create( this, tsContext(), EIconSize.IS_32X32, ACDEF_UNDO, ACDEF_REDO, ACDEF_ZOOM_IN, ACDEF_ZOOM_OUT,
        ACDEF_ZOOM_ORIGINAL_PUSHBUTTON );

    tb.setNameLabelText( "                " ); //$NON-NLS-1$

    tb.getAction( ACTID_UNDO ).setEnabled( false );
    tb.getAction( ACTID_REDO ).setEnabled( false );

    ((Composite)tb.getControl()).setLayoutData( BorderLayout.CENTER );

    return tb;
  }

}
