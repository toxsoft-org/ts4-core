package org.toxsoft.core.tsgui.ved.api.helpers;

/**
 * "Перемещаемый" - интерфейс класса отображаемых объектов, которые могут быть перемещены (размещены) на плоскости.
 *
 * @author vs
 */
public interface ID2Portable {

  /**
   * Возвращает координату положения объекта по оси Х.
   *
   * @return double - координату положения объекта по оси Х
   */
  double originX();

  /**
   * Возвращает координату положения объекта по оси Y.
   *
   * @return double - координату положения объекта по оси Y
   */
  double originY();

  /**
   * Задает местоположение объекта на плоскости.
   *
   * @param aX double - x координата
   * @param aY double - y координата
   */
  void setLocation( double aX, double aY );
}
