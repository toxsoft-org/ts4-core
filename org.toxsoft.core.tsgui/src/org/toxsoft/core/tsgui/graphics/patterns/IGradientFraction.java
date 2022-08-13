package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;

/**
 * Доля от разницы значений endValue и startValue для который строится градиент.
 * <p>
 *
 * @author vs
 */
public interface IGradientFraction {

  /**
   * Определяет принадлежит ли значение данной фракции.<br>
   *
   * @param aValue double - некое нормализованное значение параметра
   * @return <b>true</b> - значение принадлежит фракции<br>
   *         <b>false</b> - значение не принадлежит фракции
   */
  boolean isMine( double aValue );

  /**
   * Возвращает параметры цвета с прозрачностью для переданного значения.<br>
   *
   * @param aValue double - некое нормализованное значение параметра
   * @return RGBA - параметры цвета с прозрачностью
   */
  RGBA calcRgb( double aValue );
}
