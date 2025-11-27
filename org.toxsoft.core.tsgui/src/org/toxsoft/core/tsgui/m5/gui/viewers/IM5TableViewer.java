package org.toxsoft.core.tsgui.m5.gui.viewers;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5 table viewer.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5TableViewer<T>
    extends IM5CollectionViewer<T> {

  /**
   * Sets table rows painting helper (user drawn cells).
   * <p>
   * Warning: this method works only in RCP mode not in RAP.
   *
   * @param aHelper {@link ViewerPaintHelper} - painter or <code>null</code> for default drawing
   * @throws TsIllegalStateRtException lazy SWT control was not created yet
   */
  void setTablePaintHelper( ViewerPaintHelper<Table> aHelper );

}
