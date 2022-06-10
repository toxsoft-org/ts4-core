package org.toxsoft.core.tsgui.m5;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.multilookup.*;
import org.toxsoft.core.tsgui.m5.valeds.multimodown.*;
import org.toxsoft.core.tsgui.m5.valeds.singlelookup.*;
import org.toxsoft.core.tsgui.m5.valeds.singlemodown.*;
import org.toxsoft.core.tsgui.valed.api.*;

/**
 * Library initialization quant.
 *
 * @author hazard157
 */
public class QuantM5
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantM5() {
    super( QuantM5.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    M5Utils.initAppContext( aAppContext );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    M5Utils.initWinContext( aWinContext );
    //
    IValedControlFactoriesRegistry vcr = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcr.registerFactory( ValedMultiModownEditor.FACTORY );
    vcr.registerFactory( ValedMultiLookupEditor.FACTORY );
    vcr.registerFactory( ValedSingleLookupEditor.FACTORY );
    vcr.registerFactory( ValedSingleModownEditor.FACTORY );
  }

}
