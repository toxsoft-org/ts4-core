package org.toxsoft.tsgui.m5_3.gui.mpc;

import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.tsgui.bricks.stdevents.*;
import org.toxsoft.tsgui.m5_3.IM5Model;
import org.toxsoft.tsgui.m5_3.gui.viewers.IM5TreeViewer;
import org.toxsoft.tsgui.m5_3.model.IM5ItemsProvider;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tsgui.widgets.TsComposite;

/**
 * Multi-pane composite (MPC) - displays M5-modelled items with supplementary information and actions.
 * <p>
 * MPC consists of following panes:
 * <ul>
 * <li>tree viewer - main and mandatory panel displays the items as table or tree;</li>
 * <li>toolbar - (optional) contains actions as buttons to control display and MPC behaveuor;</li>
 * <li>filter pane - (optional) controls which items will be displayed in tree viewer;</li>
 * <li>detail pane - (optional) displays more datails of item selected in tree viewer;</li>
 * <li>summary pane - (options) displayes summaty information of all and/or filtered items.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMultiPaneComponent<T>
    extends //
    ITsSelectionProvider<T>, //
    ITsDoubleClickEventProducer<T>, //
    ITsKeyDownEventProducer, //
    ILazyControl<TsComposite>, //
    ITsGuiContextable {

  /**
   * Возвращает ссылку на дерево, показывающий элементы.
   *
   * @return {@link IM5TreeViewer} - дерево с элементами
   */
  IM5TreeViewer<T> tree();

  /**
   * Возвращает модель сущностей.
   *
   * @return {@link IM5Model} - модель сущностей
   */
  IM5Model<T> model();

  /**
   * Заполняет просмотрщик элементами из внешнего источника (фактически, обновляет список).
   *
   * @param aToSelect &lt;T&gt; - элемент, который будет выделен или <code>null</code>
   */
  void fillViewer( T aToSelect );

  /**
   * Обновляет содержимое компоненты.
   */
  void refresh();

  /**
   * Возвращает средство управления режимом дерева.
   *
   * @return {@link ITreeModeManager} - средство управления режимом дерева
   */
  ITreeModeManager<T> treeModeManager();

  /**
   * Определяет, разрешено ли редактирование в панели.
   *
   * @return boolean - признак разрешения редактирования
   */
  boolean isEditable();

  /**
   * Задает разрешение на редактирование.
   * <p>
   * Метод можно вызывать и на неинициализированной панели, поскольку метод {@link #createControl(Composite)} не меняет
   * режим редактирования.
   *
   * @param aEditable boolean - признак разрешения редактирования
   */
  void setEditable( boolean aEditable );

  /**
   * Возвращает поставщик элементов.
   *
   * @return {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   */
  IM5ItemsProvider<T> itemsProvider();

  /**
   * Задает поставщик элементов.
   * <p>
   * Смена постащика не приводит к обновлению содержимого или состояния, слудет явно вызвать метод {@link #refresh()}.
   *
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   */
  void setItemProvider( IM5ItemsProvider<T> aItemsProvider );

}
