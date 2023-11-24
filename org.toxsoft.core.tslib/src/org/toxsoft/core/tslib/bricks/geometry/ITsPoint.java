package org.toxsoft.core.tslib.bricks.geometry;

import java.io.*;

import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A point is an implementation of the “point in a 2D coordinate space” abstraction.
 * <p>
 * May also be used as the size of a rectangle, in which case {@link #x()} is the width and {@link #y()} is the height
 * of the rectangle. *
 * <p>
 * It has two implementations - immutable {@link TsPoint} and editable {@link TsPointEdit}. You should always try to use
 * an immutable class. An editable class is sometimes needed when performing calculations. *
 *
 * @author hazard157
 */
public interface ITsPoint {

  /**
   * The singleton of the "none" point, all methods throwing an exception {@link TsNullObjectErrorRtException}.
   */
  ITsPoint NONE = new InternalNoneTsPoint();

  /**
   * The singleton of the point (0,0);
   */
  ITsPoint ZERO = new TsPoint( 0, 0 );

  /**
   * The singleton of the point (1,1);
   */
  ITsPoint ONE = new TsPoint( 1, 1 );

  /**
   * Returns the X coordinate.
   *
   * @return int - the X coordinate
   */
  int x();

  /**
   * Returns the Y coordinate.
   *
   * @return int - the Y coordinate
   */
  int y();

}

class InternalNoneTsPoint
    implements ITsPoint, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Method correctly deserializes {@link ITsPoint#NONE} value.
   *
   * @return {@link ObjectStreamException} - {@link ITsPoint#NONE}
   * @throws ObjectStreamException is declared but newer thrown by this method
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
