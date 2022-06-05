package org.toxsoft.core.tsgui.utils.swt;

/**
 * Набор SWT констант, которые отсутвуют в RAP'е.
 * <p>
 * Значения этих констант совпадает со значениями из SWT для RCP. По мере появления аналогичных констант в SWT для RAP,
 * отсюда надо убирать эти константы.
 *
 * @author vs
 * @author hazard157
 */
public interface ISwt {

  /**
   * <code>SWT.TRANSPARENT</code>
   */
  int TRANSPARENT = 1 << 30;

  /**
   * <code>SWT.LINE_DOT</code>
   */
  int LINE_DOT = 3;

  /**
   * <code>SWT.LINE_SOLID</code>
   */
  int LINE_SOLID = 1;

  /**
   * <code>SWT.LINE_DASH</code>
   */
  int LINE_DASH = 2;

  /**
   * <code>SWT.EraseItem
   */
  int EraseItem = 40;

  /**
   * <code>SWT.MeasureItem
   */
  int MeasureItem = 41;

  /**
   * <code>SWT.PaintItem
   */
  int PaintItem = 42;

  /**
   * <code>SWT.SHADOW_ETCHED_OUT
   */
  int SHADOW_ETCHED_IN = 1 << 4;

  /**
   * <code>SWT.SHADOW_ETCHED_OUT
   */
  int SHADOW_ETCHED_OUT = 1 << 6;

}
