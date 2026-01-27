package org.toxsoft.core.tsgui.graphics.image;

import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * An {@link IThumbSizeable} extension with size change notification.
 *
 * @author hazard157
 */
public interface IThumbSizeableEx
    extends IThumbSizeable {

  /**
   * Returns the thumb size change eventer.
   *
   * @return {@link IGenericChangeEventer} - the eventer
   */
  IGenericChangeEventer thumbSizeEventer();

}
