package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedDecorator} base implementation.
 *
 * @author hazard157
 */
public abstract class VedAbstractDecorator
    extends VedAbstractSnippet
    implements IVedDecorator {

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractDecorator( IVedScreen aScreen ) {
    super( aScreen );
  }

  // ------------------------------------------------------------------------------------
  // ITsPaintable
  //

  @Override
  public abstract void paint( ITsGraphicsContext aPaintContext );

  // ------------------------------------------------------------------------------------
  // IPointsHost
  //

  @Override
  public abstract boolean isYours( double aX, double aY );

  @Override
  public abstract ID2Rectangle bounds();

  // ------------------------------------------------------------------------------------
  // IVedDecorator
  //

  @Override
  public String getViselIdOfDrawingTransform() {
    return TsLibUtils.EMPTY_STRING;
  }

}
