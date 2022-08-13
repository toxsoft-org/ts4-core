package org.toxsoft.core.tsgui.graphics.patterns;

/**
 * @author vs
 */
public interface IGradientPattern {

  /**
   * Возвращает тип градиентной заливки.<br>
   *
   * @return EGradientType - тип градиентной заливки
   */
  EGradientType gradientType();

  // /**
  // * Возвращает "узор" для фона закрашиваемой фигуры или <b>null</b> если ширина или высота <= 0.<br>
  // *
  // * @param aGc GC - графический контекст
  // * @param aWidth int - ширина закрашиваемой области в пикселях
  // * @param aHeight int - высота закрашиваемой области в пикселях
  // * @return Pattern - "узор" для фона закрашиваемой фигуры
  // */
  // Pattern pattern( GC aGc, int aWidth, int aHeight );

}
