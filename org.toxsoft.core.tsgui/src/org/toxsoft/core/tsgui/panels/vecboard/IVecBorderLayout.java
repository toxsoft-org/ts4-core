package org.toxsoft.core.tsgui.panels.vecboard;

import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The layout places up to five controls at rectangle eges and in the center.
 * <p>
 * Layout may have up to five controls, one per constant of enum {@link EBorderLayoutPlacement}).
 *
 * @author hazard157
 */
public interface IVecBorderLayout
    extends IVecLayout<EBorderLayoutPlacement> {

  /**
   * Returns an item at the specified location if any.
   *
   * @param aPlacement {@link EBorderLayoutPlacement} - requested location
   * @return {@link ILazyControl} - an item or <code>null</code> if none
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ILazyControl<?> findItem( EBorderLayoutPlacement aPlacement );

}
