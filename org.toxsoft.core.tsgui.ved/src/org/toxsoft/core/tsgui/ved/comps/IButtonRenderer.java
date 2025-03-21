package org.toxsoft.core.tsgui.ved.comps;

import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Интерфейс отрисовщика кнопки.
 * <p>
 *
 * @author vs
 */
public interface IButtonRenderer {

  /**
   * Рисует кнопку с учетом её текущего состояния.<br>
   *
   * @param aPaintConext {@link ITsGraphicsContext} - контекст рисования
   */
  void drawButton( ITsGraphicsContext aPaintConext );

  /**
   * Осуществляет обновление внутренних структур данных.
   */
  void update();

  /**
   * Возвращает упакованный размер визуального элемента если один из aWidth или aHeight < 0.0, то соответствующее
   * измерение вычисляется (запрос типа "Дай минимальную высоту при заданной ширине"). А если оба меньше нуля, то
   * вычисляется минимальный обрамляющие прямоугольник. При этом, реализация метода по умолчанию возвращает размеры
   * прямоугольника метода {@link IVedVisel#bounds()}.
   *
   * @param aWidth double - желаемая ширина или -1
   * @param aHeight double - желаемая высота или -1
   * @return {@link ID2Point} - упакованный размер визуального элемента
   */
  ID2Point getPackedSize( double aWidth, double aHeight );

}
