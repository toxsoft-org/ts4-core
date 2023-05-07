package org.toxsoft.core.tslib.bricks.geometry;

import java.io.*;

import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Точка - реализация абстракции "точка на координатной плоскости".
 * <p>
 * Также может использоваться в качестве размера прямоугольника, и тогда {@link #x()} это ширина, а {@link #y()} -
 * высота прямоугольника.
 * <p>
 * Имеет две реализации - неизменяемую {@link TsPoint} и редактируемую {@link TsPointEdit}. Всегда надо стараться
 * использовать неизменяемый класс. Редактируемый класс бывает нужен при проведении расчетов.
 *
 * @author hazard157
 */
public interface ITsPoint {

  /**
   * Неизменяемая "никакая" точка, методы которого выбрасывают исключение {@link TsNullObjectErrorRtException}.
   */
  ITsPoint NONE = new InternalNoneTsPoint();

  /**
   * Неизменяемая точка с координатами (0,0);
   */
  ITsPoint ZERO = new TsPoint( 0, 0 );

  /**
   * Возвращает x координату.
   *
   * @return int - x координата
   */
  int x();

  /**
   * Возвращает y координату.
   *
   * @return int - y координата
   */
  int y();

}

class InternalNoneTsPoint
    implements ITsPoint, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Метод корректно восстанавливает сериализированный {@link ITsPoint#NONE}.
   *
   * @return Object объект {@link ITsPoint#NONE}
   * @throws ObjectStreamException это обявление, оно тут не выбрасывается
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsPoint.NONE;
  }

  @Override
  public int x() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int y() {
    throw new TsNullObjectErrorRtException();
  }

}
