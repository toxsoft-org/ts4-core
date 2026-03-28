package org.toxsoft.core.tslib.bricks.coopcomp;

/**
 * Single usage {@link ITsCooperativeComponent} implementation - does <b>not</b> allows to start once stopped.
 *
 * @author hazard157
 */
public abstract non-sealed class AbstractTsCoopCompSingleUse
    extends AbstractTsCooperativeComponent {

  /**
   * Constructor.
   */
  public AbstractTsCoopCompSingleUse() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsCooperativeComponent
  //

  @Override
  final void internalSetToFinalState() {
    destroy();
  }

}
