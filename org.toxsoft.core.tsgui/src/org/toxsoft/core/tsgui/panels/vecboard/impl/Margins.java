package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.toxsoft.core.tsgui.panels.vecboard.*;

/**
 * Неизменяемая реализация "полей" (отступов от края) для прямоугольной области в пикселях.
 * <p>
 *
 * @author vs
 */
public class Margins
    implements IMargins {

  private final int left;
  private final int top;
  private final int right;
  private final int bottom;

  /**
   * Конструктор со всеми инвариантами.<br>
   *
   * @param aLeft int - расстояние от левого края в пикселях
   * @param aTop int - расстояние от верхнего края в пикселях
   * @param aRight int - расстояние от правого края в пикселях
   * @param aBottom int - расстояние от нижнего края в пикселях
   */
  public Margins( int aLeft, int aTop, int aRight, int aBottom ) {
    left = aLeft;
    top = aTop;
    right = aRight;
    bottom = aBottom;
  }

  // ------------------------------------------------------------------------------------
  // IMargins
  //

  @Override
  public int left() {
    return left;
  }

  @Override
  public int top() {
    return top;
  }

  @Override
  public int right() {
    return right;
  }

  @Override
  public int bottom() {
    return bottom;
  }

}
