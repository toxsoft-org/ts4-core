package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * "Создатель" элементов для контекстного (right-click) меню.
 *
 * @author vs
 */
public interface IVedContextMenuCreator {

  /**
   * Добавляет элементы в переданное меню.
   *
   * @param aMenu {@link Menu} - родительское меню
   * @param aClickedVisel {@link VedAbstractVisel} - VISEL на котором произошел щелчок мыши (м.б. null)
   * @param aSwtCoors {@link ITsPoint} - SWT координаты мыши
   * @return <b>true</b> - если был добавлен хотя бы один элемент<br>
   *         <b>false</b> - если ни один элемент не был добавлен
   */
  boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors );

}
