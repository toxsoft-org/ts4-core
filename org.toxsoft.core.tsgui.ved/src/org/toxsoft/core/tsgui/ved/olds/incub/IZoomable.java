package org.toxsoft.core.tsgui.ved.olds.incub;

/**
 * Поддержка масштабирования.
 * <p>
 * Частный случай масштабирования для плоскости, когда коэффициент масштабирования по оси X равен коеффициенту
 * масштабирования по оси Y.
 *
 * @author vs
 */
public interface IZoomable {

  /**
   * Возвращает коэффициент масштабирования.
   *
   * @return double - коэффициент масштабирования
   */
  double zoomFactor();

  /**
   * Устанавливает коэффициент масштабирования.
   *
   * @param aZoomFactor double - коэффициент масштабирования
   */
  void setZoomFactor( double aZoomFactor );

}
