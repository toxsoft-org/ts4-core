package org.toxsoft.core.tsgui.m5.gui.viewers;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.m5.*;

/**
 * Column of the viewer {@link IM5CollectionViewer}.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5Column<T>
    extends ITsViewerColumn {

  /**
   * Returns M5-model field ID shown in this column.
   *
   * @return String - {@link IM5Model} field ID
   */
  String fieldId();

  /**
   * Returns text of the entity to by displayed in the cell.
   *
   * @param aEntity &lt;T&gt; - model;ed entity (dsiplayed as the row in the viewer)
   * @return String - textual context of the cell, must not be <code>null</code>
   */
  String getCellText( T aEntity );

  /**
   * Returns text of the entity to by displayed as the tooltip to the cell.
   *
   * @param aEntity &lt;T&gt; - model;ed entity (dsiplayed as the row in the viewer)
   * @return String - textual context of the tooltip or <code>null</code> if no specific tootip for the cell is provided
   */
  String getCellTooltip( T aEntity );

  /**
   * Returns icon of the entity to by displayed in the cell.
   *
   * @param aEntity &lt;T&gt; - model;ed entity (dsiplayed as the row in the viewer)
   * @param aIconSize {@link EIconSize} - expected size of the icon
   * @return String - the icon or <code>null</code> if no icon is to be displayed
   */
  Image getCellIcon( T aEntity, EIconSize aIconSize );

  /**
   * Returns thumbnail image of the entity to by displayed in the cell.
   * <p>
   * Returned image {@link TsImage} may be an animated image.
   *
   * @param aEntity &lt;T&gt; - model;ed entity (dsiplayed as the row in the viewer)
   * @param aThubSize {@link EThumbSize} - expected size of the image
   * @return String - the thumbnail image or <code>null</code> if no icon is to be displayed
   */
  TsImage getCellThumb( T aEntity, EThumbSize aThubSize );

}
