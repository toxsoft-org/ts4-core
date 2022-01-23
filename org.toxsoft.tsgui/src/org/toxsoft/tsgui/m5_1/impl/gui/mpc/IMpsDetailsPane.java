package org.toxsoft.tsgui.m5_1.impl.gui.mpc;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.tstree.ITsNode;
import org.toxsoft.tsgui.m5_1.api.IM5Bunch;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;

/**
 * Интерфейс, которую должна реализовывать панель детальной информации.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMpsDetailsPane<T>
    extends ILazyControl<Control> {

  // TODO сделать редактирование на месте

  /**
   * Устанавливает значения в виджетах панели из значений свойств объекта.
   * <p>
   * Когда в дереве выбран узел, содержащий элемент, то оба аргумента не-null и указывают на выбранный узел и его
   * содержимое (сущность, представленную значениями aValues). Когда выбран группирующий узел, он передается в аргументе
   * aSelectedNode, а значения полей сущности aValues = <code>null</code>. Когда ни один узел не выбран, оба аргумента
   * равны <code>null</code>.
   *
   * @param aSelectedNode {@link ITsNode} - выбранный узел дерева, может быть <code>null</code>
   * @param aValues {@link IM5Bunch} - значения свойств объекта, может быть <code>null</code>
   */
  void setValues( ITsNode aSelectedNode, IM5Bunch<T> aValues );

}
