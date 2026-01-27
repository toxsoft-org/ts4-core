package org.toxsoft.core.tsgui.graphics.image;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface for entities having concept of "variable size ofdisplayed thumbnails".
 * <p>
 * FIXME ThumbSizeableZoomDropDownMenuCreator
 *
 * @author hazard157
 */
public interface IThumbSizeable {

  /**
   * Returns current thumbnails size.
   *
   * @return {@link EThumbSize} - current thumbnails size
   */
  EThumbSize thumbSize();

  /**
   * Sets the current thumbnails size.
   *
   * @param aThumbSize {@link EThumbSize} - new size
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setThumbSize( EThumbSize aThumbSize );

  /**
   * Returns default size of the thumbnails.
   * <p>
   * Method assumes that each entity has concept of @default" size. This may be a initial size, or best size or
   * whatever. Anyway, reseting current thumbnails size to default will set this size.
   *
   * @return {@link EThumbSize} - default size of thumbnails
   */
  EThumbSize defaultThumbSize();

  /**
   * Sets the thumb size to the {@link #defaultThumbSize()}.
   */
  default void setDefaultThumbSize() {
    setThumbSize( defaultThumbSize() );
  }

}
