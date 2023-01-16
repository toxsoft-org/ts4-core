package org.toxsoft.core.tslib.utils.icons;

/**
 * Mix-in interface of entities that may be visualized with <code>ITsIconManager</code> provided icons.
 *
 * @author hazard157
 */
public interface IIconIdable {

  /**
   * Returns the icon identifier to be used with <code>ITsIconManager</code>.
   *
   * @return String - icon identifier or <code>null</code> if no icon specified
   */
  String iconId();

}
