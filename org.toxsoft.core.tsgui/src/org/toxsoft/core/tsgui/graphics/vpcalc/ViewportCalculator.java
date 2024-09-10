package org.toxsoft.core.tsgui.graphics.vpcalc;

/**
 * {@link IViewportCalculator} implementation.
 *
 * @author hazard157
 */
public class ViewportCalculator {

  public static IViewportCalculator create( CalculationStrategySettings aCss ) {
    return new ViewportCalculator_solpp( aCss );
  }

}
