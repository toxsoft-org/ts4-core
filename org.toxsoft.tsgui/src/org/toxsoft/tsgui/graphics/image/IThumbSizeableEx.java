package org.toxsoft.tsgui.graphics.image;

import org.toxsoft.tslib.bricks.events.change.IGenericChangeEventer;

/**
 * An {@link IThumbSizeable} extension with size change notificaion.
 *
 * @author hazard157
 */
public interface IThumbSizeableEx
    extends IThumbSizeable {

  /**
   * Returns the thumb size change event producer.
   *
   * @return {@link IGenericChangeEventer} - the event producer
   */
  IGenericChangeEventer thumbSizeEventer();

}
