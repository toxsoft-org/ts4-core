package org.toxsoft.core.tslib.bricks.geometry;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Geometry utility methods for 2D geometry with <code>int</code> coordinates.
 *
 * @author hazard157
 */
public class TsGeometryUtils {

  /**
   * Creates {@link TsRectangleEdit} from {@link ID2Rectangle}.
   *
   * @param aSource {@link ID2Rectangle} - the source
   * @return {@link TsRectangleEdit} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsRectangleEdit create( ID2Rectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    int x = (int)aSource.x1();
    int y = (int)aSource.y1();
    int width = (int)aSource.width();
    int height = (int)aSource.height();
    return new TsRectangleEdit( x, y, width, height );
  }

  // TODO other methods like union(), contains(), intersects(), etc.

  /**
   * No subclasses. Constructor.
   */
  public TsGeometryUtils() {
    // nop
  }
}
