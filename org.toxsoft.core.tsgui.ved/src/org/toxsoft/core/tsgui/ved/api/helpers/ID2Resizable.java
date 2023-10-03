package org.toxsoft.core.tsgui.ved.api.helpers;

/**
 * Интерфейс классов, которые могут изменять свои размеры ширину и/или высоту
 *
 * @author vs
 */
public interface ID2Resizable {

  /**
   * Возвращает ширину объекта.
   *
   * @return double - ширина объекта
   */
  double width();

  /**
   * Возвращает высоту объекта.
   *
   * @return double - высота объекта
   */
  double height();

  /**
   * Задает размеры объекта.
   *
   * @param aWidth double - ширина объекта
   * @param aHeight double - высота объекта
   */
  void setSize( double aWidth, double aHeight );
}
