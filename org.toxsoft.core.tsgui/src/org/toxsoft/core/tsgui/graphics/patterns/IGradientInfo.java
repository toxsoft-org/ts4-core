package org.toxsoft.core.tsgui.graphics.patterns;

import org.toxsoft.core.tsgui.bricks.ctx.*;

/**
 * Параметры градиентной заливки областей при отрисовке.
 * <p>
 *
 * @author vs
 */
public interface IGradientInfo {

  // /**
  // * Возвращает тип узора для заполнения фигуры.<br>
  // *
  // * @return ETsFillKind - тип узора для заполнения
  // */
  // ETsFillKind type();

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
   * @param aInfo IGradientInfo - параметры заливки
   * @param aContext ITsGuiContext - соотвествующий контекст
   * @return ISwtPattern - узор для заполнения областей при рисовании
   */
  IGradient createGradient( IGradientInfo aInfo, ITsGuiContext aContext );

}
