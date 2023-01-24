package org.toxsoft.core.tsgui.panels.vecboard;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Параметры расположения элементов в раскладке {@link IVecLadderLayout}.
 *
 * @author hazard157
 */
public interface IVecLadderLayoutData {

  /**
   * Параметры, рекомендованные по умолчанию.
   */
  IVecLadderLayoutData DEFAULT =
      new VecLadderLayoutData( true, false, 1, TsLibUtils.EMPTY_STRING, EHorAlignment.LEFT, EVerAlignment.TOP );

  /**
   * Возвращает принак показа подписи к элементу.
   *
   * @return boolean - принак показа подписи к элементу
   */
  boolean isLabelShown();

  /**
   * Возвращает признак того, что контроль занимает всю ширину панели.
   * <p>
   * Контроль может занимать либо правую колонку, либо обе (всю ширину родительской панели). Когда отображение подписи
   * включенио, и задана полная ширина контроля, подпись "переезжает" над контролем.
   *
   * @return boolean - признак того, что контроль занимает всю ширину панели
   */
  boolean isFullWidthControl();

  /**
   * Возвращает количество строк (ступенек лесенки), занимаемый элементом раскладки.
   *
   * @return int - количество строк (ступенек лесенки), занимаемый элементом раскладки
   */
  int verticalSpan();

  /**
   * Возвращает текст подписи к SWT-контролю или панели.
   *
   * @return String - текст подписи к SWT-контролю или панели
   */
  String labelText();

  /**
   * Возвращаетс текст всплывающей подсказки к контроли.
   *
   * @return String - всплывающая подсказка
   */
  String tooltip();

  /**
   * Возвращает выравнивание контролей по горизонтали внутри ячейки.
   *
   * @return {@link EHorAlignment} - выравнивание контролей по горизонтали внутри ячейки
   */
  EHorAlignment horAlignment();

  /**
   * Возвращает выравнивание контролей по вертикали внутри ячейки.
   *
   * @return {@link EVerAlignment} - выравнивание контролей по вертикали внутри ячейки
   */
  EVerAlignment verAlignment();

}
