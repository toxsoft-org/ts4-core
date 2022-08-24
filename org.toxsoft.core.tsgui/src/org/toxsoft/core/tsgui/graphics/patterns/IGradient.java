package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.eclipse.ui.services.*;

/**
 * "Узор" для заполнения фигуры при рисовании.
 *
 * @author vs
 */
public interface IGradient
    extends IDisposable {

  /**
   * Возвращает параметры "узора" для заполнения фигуры.<br>
   *
   * @return ISwtPatternInfo параметры "узора" для заполнения фигуры
   */
  IGradientInfo patternInfo();

  /**
   * Возвращает "узор" для фона закрашиваемой фигуры или <b>null</b> если ширина или высота <= 0.<br>
   *
   * @param aGc GC - графический контекст
   * @param aWidth int - ширина закрашиваемой области в пикселях
   * @param aHeight int - высота закрашиваемой области в пикселях
   * @return Pattern - "узор" для фона закрашиваемой фигуры
   */
  Pattern pattern( GC aGc, int aWidth, int aHeight );
}
