package org.toxsoft.core.tsgui.graphics;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Miscallenous graphics utility methods.
 *
 * @author hazard157
 */
public class TsGraphicsUtils {

  /**
   * Converts {@link ID2Conversion} to SWT {@link Transform}.
   *
   * @param aD2Conv {@link ID2Conversion} - conversion
   * @param aGc {@link GC} - graphics context
   * @return {@link Transform} - transformation matrix for graphics context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static Transform conv2transform( ID2Conversion aD2Conv, GC aGc ) {
    TsNullArgumentRtException.checkNulls( aD2Conv, aGc );
    Transform t = new Transform( aGc.getDevice() );
    float zf = (float)aD2Conv.zoomFactor();
    t.translate( (float)aD2Conv.origin().x(), (float)aD2Conv.origin().y() );
    t.scale( zf, zf );
    if( aD2Conv.rotation() != ID2Rotation.NONE ) {
      t.rotate( (float)aD2Conv.rotation().degrees() );
    }
    return t;
  }

  /**
   * Prohibition of descendants creation.
   */
  private TsGraphicsUtils() {
    // nop
  }

}
