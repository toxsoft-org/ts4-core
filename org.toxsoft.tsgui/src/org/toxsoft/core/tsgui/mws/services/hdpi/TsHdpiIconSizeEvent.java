package org.toxsoft.core.tsgui.mws.services.hdpi;

import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Icon size change event.
 *
 * @author hazard157
 * @param categoryId String - ID of the icons
 * @param newSize {@link EIconSize} - new size of the icon
 * @param oldSize {@link EIconSize} - old size of the icon
 */
public record TsHdpiIconSizeEvent ( String categoryId, EIconSize newSize, EIconSize oldSize ) {

  @SuppressWarnings( "javadoc" )
  public TsHdpiIconSizeEvent( String categoryId, EIconSize newSize, EIconSize oldSize ) {
    StridUtils.checkValidIdPath( categoryId );
    TsNullArgumentRtException.checkNulls( newSize, oldSize );
    this.categoryId = categoryId;
    this.newSize = newSize;
    this.oldSize = oldSize;
  }

}
