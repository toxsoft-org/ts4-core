package org.toxsoft.core.tsgui.graphics.patterns;

import org.toxsoft.core.tsgui.bricks.ctx.*;

/**
 * Параметры "узора" для заливки областей при отрисовке.
 * <p>
 *
 * @author vs
 */
public interface ISwtPatternInfo {

  /**
   * Возвращает тип узора для заполнения фигуры.<br>
   *
   * @return ESwtPatternType - тип узора для заполнения
   */
  ESwtPatternType type();

  /**
   * Возвращает тип градиентной заливки.
   * <p>
   * Если тип заливки не является градиентом - возвращает {@link EGradientType#NONE}.
   *
   * @return EGradientType - тип градиентной заливки
   */
  EGradientType gradientType();

  /**
   * Создает соотвествующий узор для заполнения областей при рисовании.
   *
   * @param aInfo ISwtPatternInfo - параметры заливки
   * @param aContext ITsGuiContext - соотвествующий контекст
   * @return ISwtPattern - узор для заполнения областей при рисовании
   */
  ISwtPattern createSwtPattern( ISwtPatternInfo aInfo, ITsGuiContext aContext );

}
