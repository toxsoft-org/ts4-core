package org.toxsoft.core.tsgui.ved.incub.tsg;

/**
 * Mixin interface of entities capable to be painted on TS graphics context.
 *
 * @author hazard157
 */
public interface ITsPaintable {

  /**
   * Paints the entity on the context.
   *
   * @param aPaintContext {@link ITsGraphicsContext} - the graphics context to paint on
   */
  void paint( ITsGraphicsContext aPaintContext );

}
