package org.toxsoft.core.tslib.bricks.coopcomp;

/**
 * Multi-usage {@link ITsCooperativeComponent} implementation - allows to initialize and start after each stop.
 *
 * @author hazard157
 */
public abstract class AbstractTsCoopCompMultiUse
    extends AbstractTsCooperativeComponent {

  /**
   * Constructor.
   */
  public AbstractTsCoopCompMultiUse() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsCooperativeComponent
  //

  @Override
  final void internalSetToFinalState() {
    compState = ETsCoopCompState.INITIALIZED;
  }

}
