package org.toxsoft.core.tsgui.graphics.icons;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Mixin interace for mostly for visual elements with changeable icon size.
 *
 * @author hazard157
 */
public interface IIconSizeable {

  /**
   * Return the size of the icons in visual element.
   *
   * @return {@link EIconSize} - the icons size
   */
  EIconSize iconSize();

  /**
   * Sets the size of the icons in visual element.
   *
   * @param aIconSize {@link EIconSize} - the icons size
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void setIconSize( EIconSize aIconSize );

  /**
   * Returns the default (initial) size of the icons.
   * <p>
   * By convention the default size can not change during program execution while it may be changed between program
   * executions.
   *
   * @return {@link EIconSize} - the default icons size
   */
  EIconSize defaultIconSize();

}
