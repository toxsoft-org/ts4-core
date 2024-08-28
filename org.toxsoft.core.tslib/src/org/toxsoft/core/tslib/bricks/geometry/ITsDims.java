package org.toxsoft.core.tslib.bricks.geometry;

import java.io.*;

import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The dimensions of a something in the 2D integer coordinates space.
 * <p>
 * Dize of the rectangle with the same top-left and bottom-right points is (1,1), so {@link #width()} and
 * {@link #height()} are always >= 1.
 *
 * @author hazard157
 */
public interface ITsDims {

  /**
   * The singleton of the "none" point, all methods throwing an exception {@link TsNullObjectErrorRtException}.
   */
  ITsDims NONE = new InternalNoneTsDims();

  /**
   * Size with (1,1) dimensions.
   */
  ITsDims ONE = new TsDims( 1, 1 );

  /**
   * Returns the width.
   *
   * @return int - the width, always >= 1
   */
  int width();

  /**
   * Returns the height.
   *
   * @return int - the height, always >= 1
   */
  int height();

  @SuppressWarnings( "javadoc" )
  default int w() {
    return width();
  }

  @SuppressWarnings( "javadoc" )
  default int h() {
    return height();
  }

}

class InternalNoneTsDims
    implements ITsDims, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Method correctly deserializes {@link ITsDims#NONE} value.
   *
   * @return {@link ObjectStreamException} - {@link ITsDims#NONE}
   * @throws ObjectStreamException is declared but newer thrown by this method
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsDims.NONE;
  }

  @Override
  public int width() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int height() {
    throw new TsNullObjectErrorRtException();
  }

}
