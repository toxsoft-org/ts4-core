package org.toxsoft.core.tsgui.graphics.image;

/**
 * Mix-in interface of entities able to manage displayed thumbnail size.
 *
 * @author hazard157
 */
public interface IThumbSizeChangeCapable {

  /**
   * Returns the thumb size manager.
   *
   * @return {@link IThumbSizeableEx} - the thumb size manager
   */
  IThumbSizeableEx thumbSizeManager();

}
