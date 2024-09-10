package org.toxsoft.core.tsgui.graphics.vpcalc2.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.vpcalc2.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parameters determining how {@link IViewportCalculator} works.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class VpCalcCfg
    implements IVpCalcCfg {

  private final GenericChangeEventer eventer;

  private ETsFulcrum          fulcrum          = ETsFulcrum.CENTER;
  private EVpFulcrumStartegy  fulcrumStartegy  = EVpFulcrumStartegy.HINT;
  private EVpBoundingStrategy boundingStrategy = EVpBoundingStrategy.VIEWPORT;
  private ITsMargins          boundingMargins  = new TsMargins( 1 );
  private ERectFitMode        fitMode          = ERectFitMode.FIT_BEST;
  private boolean             expandToFit      = false;

  /**
   * Constructor.
   */
  public VpCalcCfg() {
    eventer = new GenericChangeEventer( this );
  }

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
    this();
    TsNullArgumentRtException.checkNulls( aBoundingMargins, aFulcrum, aFulcrumUsageStartegy, aBoundingStrategy );
    fulcrum = aFulcrum;
    fulcrumStartegy = aFulcrumUsageStartegy;
    boundingStrategy = aBoundingStrategy;
    boundingMargins = new TsMargins( aBoundingMargins );
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    aSw.writeChar( CHAR_SET_BEGIN );
    ETsFulcrum.KEEPER.write( aSw, fulcrum );
    aSw.writeSeparatorChar();
    EVpFulcrumStartegy.KEEPER.write( aSw, fulcrumStartegy );
    aSw.writeSeparatorChar();
    EVpBoundingStrategy.KEEPER.write( aSw, boundingStrategy );
    aSw.writeSeparatorChar();
    TsMargins.KEEPER.write( aSw, boundingMargins );
    aSw.writeSeparatorChar();
    ERectFitMode.KEEPER.write( aSw, fitMode );
    aSw.writeSeparatorChar();
    aSw.writeBoolean( expandToFit );
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    try {
      aSr.ensureChar( CHAR_SET_BEGIN );
      fulcrum = ETsFulcrum.KEEPER.read( aSr );
      aSr.ensureSeparatorChar();
      fulcrumStartegy = EVpFulcrumStartegy.KEEPER.read( aSr );
      aSr.ensureSeparatorChar();
      boundingStrategy = EVpBoundingStrategy.KEEPER.read( aSr );
      aSr.ensureSeparatorChar();
      boundingMargins = TsMargins.KEEPER.read( aSr );
      aSr.ensureSeparatorChar();
      fitMode = ERectFitMode.KEEPER.read( aSr );
      aSr.ensureSeparatorChar();
      expandToFit = aSr.readBoolean();
      aSr.ensureChar( CHAR_SET_END );
    }
    finally {
      eventer.fireChangeEvent();
    }
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
    return fulcrumStartegy;
  }

  @Override
  public EVpBoundingStrategy boundsStrategy() {
    return boundingStrategy;
  }

  @Override
  public ITsMargins margins() {
    return boundingMargins;
  }

  @Override
  public ERectFitMode fitMode() {
    return fitMode;
  }

  @Override
  public boolean isExpandToFit() {
    return expandToFit;
  }

  @Override
  public void setFulcrum( ETsFulcrum aFulcrum ) {
    TsNullArgumentRtException.checkNull( aFulcrum );
    if( fulcrum != aFulcrum ) {
      fulcrum = aFulcrum;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setFulcrumStartegy( EVpFulcrumStartegy aStrategy ) {
    TsNullArgumentRtException.checkNull( aStrategy );
    if( fulcrumStartegy != aStrategy ) {
      fulcrumStartegy = aStrategy;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setBoundingStrategy( EVpBoundingStrategy aStrategy ) {
    TsNullArgumentRtException.checkNull( aStrategy );
    if( boundingStrategy != aStrategy ) {
      boundingStrategy = aStrategy;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setMargins( ITsMargins aMargins ) {
    TsNullArgumentRtException.checkNull( aMargins );
    if( !boundingMargins.equals( aMargins ) ) {
      boundingMargins = new TsMargins( aMargins );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setFitMode( ERectFitMode aFitMode ) {
    TsNullArgumentRtException.checkNull( aFitMode );
    if( fitMode != aFitMode ) {
      fitMode = aFitMode;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setExpandToFit( boolean aExpandToFit ) {
    if( expandToFit != aExpandToFit ) {
      expandToFit = aExpandToFit;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void copyFrom( IVpCalcCfg aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    IVpCalcCfg s = aSource;
    if( fulcrum != s.fulcrum() || fulcrumStartegy != s.fulcrumStartegy() || boundingStrategy != s.boundsStrategy()
        || !boundingMargins.equals( s.margins() ) || fitMode != s.fitMode() || expandToFit != s.isExpandToFit() ) {
      fulcrum = s.fulcrum();
      fulcrumStartegy = s.fulcrumStartegy();
      boundingStrategy = s.boundsStrategy();
      boundingMargins = new TsMargins( s.margins() );
      fitMode = s.fitMode();
      expandToFit = s.isExpandToFit();
      eventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

}
