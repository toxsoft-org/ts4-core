package org.toxsoft.core.tsgui.graphics.vpcalc2.impl;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.vpcalc2.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parameters determining how {@link IViewportCalculator} works.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public final class VpCalcCfg
    implements IVpCalcCfg {

  /**
   * Singleton of the settings: centered image (content).
   */
  public static final VpCalcCfg CENTERED_IMAGE = new VpCalcCfg( //
      ETsFulcrum.CENTER, //
      EVpFulcrumStartegy.INSIDE, //
      EVpBoundingStrategy.VIEWPORT, //
      new TsMargins( 0 ) //
  );

  private final ETsFulcrum          fulcrum;
  private final EVpFulcrumStartegy  fulcrumUsageStartegy;
  private final EVpBoundingStrategy boundingStrategy;
  private final ITsMargins          boundingMargins;

  /**
   * Constructor.
   *
   * @param aFulcrum {@link ETsFulcrum} - placement fulcrum
   * @param aFulcrumUsageStartegy {@link EVpFulcrumStartegy} - how to use fulcrum
   * @param aBoundingStrategy {@link EVpBoundingStrategy} - how to restrict content location
   * @param aBoundingMargins {@link ITsMargins} - gap values when restricting location
   * @param aUnlockFitMode boolean - <code>true</code> unlock fit mode when zoom or move is requested
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VpCalcCfg( ETsFulcrum aFulcrum, EVpFulcrumStartegy aFulcrumUsageStartegy,
      EVpBoundingStrategy aBoundingStrategy, ITsMargins aBoundingMargins ) {
    TsNullArgumentRtException.checkNulls( aBoundingMargins, aFulcrum, aFulcrumUsageStartegy, aBoundingStrategy );
    fulcrum = aFulcrum;
    fulcrumUsageStartegy = aFulcrumUsageStartegy;
    boundingStrategy = aBoundingStrategy;
    boundingMargins = new TsMargins( aBoundingMargins );
  }

  // ------------------------------------------------------------------------------------
  // IVpCalcCfg
  //

  @Override
  public ETsFulcrum fulcrum() {
    return fulcrum;
  }

  @Override
  public EVpFulcrumStartegy fulcrumStartegy() {
    return fulcrumUsageStartegy;
  }

  @Override
  public EVpBoundingStrategy boundsStrategy() {
    return boundingStrategy;
  }

  @Override
  public ITsMargins margins() {
    return boundingMargins;
  }

}
