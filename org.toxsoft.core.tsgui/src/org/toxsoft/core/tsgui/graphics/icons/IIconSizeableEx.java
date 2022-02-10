package org.toxsoft.core.tsgui.graphics.icons;

import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;

/**
 * Extends {@link IIconSizeable} with size change notification events.
 *
 * @author hazard157
 */
public interface IIconSizeableEx
    extends IIconSizeable {

  /**
   * Returns the icon size change event producer.
   * <p>
   * The icon size change events are produced only when icon size really changed. If {@link #setIconSize(EIconSize)}
   * does not chnages the {@link #iconSize()}, event is not produced.
   * <p>
   * The event may be also produced if icon size is changed by other means including user interaction on runtime.
   *
   * @return {@link IGenericChangeEventer} - the event producer
   */
  IGenericChangeEventer iconSizeChangeEventer();

}
