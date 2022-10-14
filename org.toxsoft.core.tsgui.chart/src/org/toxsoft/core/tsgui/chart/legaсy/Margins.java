package org.toxsoft.core.tsgui.chart.legaсy;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Поля - отступы по краям прямоугольной области.
 * <p>
 * Неизменяемый класс - все размеры задаются в конструкторе. Размер поля не может быть отрицательным.
 *
 * @author vs
 */
public class Margins {

  /**
   * Неизменяемые поля нулевого размера (0,0,0,0);
   */
  public static final Margins NONE = new Margins( 0, 0, 0, 0 );

  final int left;
  final int top;
  final int right;
  final int bottom;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aLeft int - размер в поля, идущего вдоль левой стороны прямоугольной области, в пикселях >= 0
   * @param aTop int - размер в поля, идущего вдоль верхней стороны прямоугольной области, в пикселях >= 0
   * @param aRight int - размер в поля, идущего вдоль правой стороны прямоугольной области, в пикселях >= 0
   * @param aBottom int - размер в поля, идущего вдоль нижней стороны прямоугольной области, в пикселях >= 0
   * @throws TsIllegalArgumentRtException - любой аргумент < 0
   */
  public Margins( int aLeft, int aTop, int aRight, int aBottom ) {
    TsIllegalArgumentRtException.checkTrue( aLeft < 0 );
    TsIllegalArgumentRtException.checkTrue( aTop < 0 );
    TsIllegalArgumentRtException.checkTrue( aRight < 0 );
    TsIllegalArgumentRtException.checkTrue( aBottom < 0 );
    left = aLeft;
    right = aRight;
    top = aTop;
    bottom = aBottom;
  }

  // --------------------------------------------------------------------------
  // публичный API
  //

  /**
   * Возвращает размер в поля, идущего вдоль левой стороны прямоугольной области, в пикселях.
   *
   * @return int - pазмер в поля, идущего вдоль левой стороны прямоугольной области, в пикселях
   */
  public int left() {
    return left;
  }

  /**
   * Возвращает размер в поля, идущего вдоль верхней стороны прямоугольной области, в пикселях.
   *
   * @return int - pазмер в поля, идущего вдоль верхней стороны прямоугольной области, в пикселях
   */
  public int top() {
    return top;
  }

  /**
   * Возвращает размер в поля, идущего вдоль правой стороны прямоугольной области, в пикселях.
   *
   * @return int - pазмер в поля, идущего вдоль правой стороны прямоугольной области, в пикселях
   */
  public int right() {
    return right;
  }

  /**
   * Возвращает размер в поля, идущего вдоль нижней стороны прямоугольной области, в пикселях.
   *
   * @return int - pазмер в поля, нижней вдоль верхней стороны прямоугольной области, в пикселях
   */
  public int bottom() {
    return bottom;
  }

}
