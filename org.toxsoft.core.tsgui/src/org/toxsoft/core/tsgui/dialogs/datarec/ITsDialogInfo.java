package org.toxsoft.core.tsgui.dialogs.datarec;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

// TODO TRANSLATE

/**
 * Набор параметров для создания диалогового окна, в который встраивается {@link AbstractTsDialogPanel}.
 * <p>
 * Эти параметры являются собранным в один класс аргументами, используемые при создании экземпляров класса
 * {@link TitleAreaDialog}, в частности, диалога {@link TsDialog}.
 *
 * @author hazard157
 */
public interface ITsDialogInfo {

  /**
   * Возвращает заголовок далогового окна.
   *
   * @return String - заголовок далогового окна
   */
  String caption();

  /**
   * Возвращает текст сообщения далогового окна.
   *
   * @return String - текст сообщения далогового окна
   */
  String title();

  /**
   * Возвращает флаги диалогового окна.
   * <p>
   * Флагами диалогового окна являются собранные по ИЛИ биты {@link ITsDialogConstants}<b>.DF_XXX</b>.
   *
   * @return int - флаги диалогового окна собранные по ИЛИ
   */
  int flags();

  /**
   * Возвращает родительское окно.
   *
   * @return {@link Shell} - родительское окно
   */
  Shell shell();

  /**
   * Возвращает минимальный размер панели содержимого диалога.
   * <p>
   * Положительные значения интерпретируются как размеры в пикселях для {@link TsComposite#setMinimumWidth(int)} и
   * {@link TsComposite#setMinimumHeight(int)}. Отрицательные - как проценты (с измененным знаком) от соответствующего
   * размера дисплея для {@link TsComposite#setMinWidthDisplayRelative(int)} и
   * {@link TsComposite#setMinHeightDisplayRelative(int)}.
   * <p>
   * Значения 0 и {@link SWT#DEFAULT} указвывают, что размер не задан.
   *
   * @return {@link ITsPoint} - минимальный размер панели содержимого диалога
   */
  ITsPoint minSize();

  /**
   * Возвращает максимальный размер панели содержимого диалога.
   * <p>
   * Положительные значения интерпретируются как размеры в пикселях для {@link TsComposite#setMaximumWidth(int)} и
   * {@link TsComposite#setMaximumHeight(int)}. Отрицательные - как проценты (с измененным знаком) от соответствующего
   * размера дисплея для {@link TsComposite#setMaxWidthDisplayRelative(int)} и
   * {@link TsComposite#setMaxHeightDisplayRelative(int)}.
   * <p>
   * Значения 0 и {@link SWT#DEFAULT} указвывают, что размер не задан.
   *
   * @return {@link ITsPoint} - максимальный размер панели содержимого диалога
   */
  ITsPoint maxSize();

  /**
   * Returns the GUI context.
   *
   * @return {@link ITsContext} - the context
   */
  ITsGuiContext tsContext();

}
