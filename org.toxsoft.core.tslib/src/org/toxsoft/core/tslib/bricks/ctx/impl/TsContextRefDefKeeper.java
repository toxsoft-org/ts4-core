package org.toxsoft.core.tslib.bricks.ctx.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Keeper of {@link ITsContextRefDef}.
 * <p>
 * Note: reading from {@link IStrioReader} fails if reference class can not be loaded by {@link Class#forName(String)}.
 *
 * @author hazard157
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class TsContextRefDefKeeper
    extends AbstractEntityKeeper<ITsContextRefDef> {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ITsContextRefDef> KEEPER = new TsContextRefDefKeeper();

  private TsContextRefDefKeeper() {
    super( ITsContextRefDef.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, ITsContextRefDef aEntity ) {
    TsInternalErrorRtException.checkNull( aEntity.refClass().getCanonicalName() );
    aSw.writeQuotedString( aEntity.refKey() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.refClass().getName() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );

  }

  @Override
  protected ITsContextRefDef<?> doRead( IStrioReader aSr ) {
    String refKey = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String className = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
    try {
      Class refClass = Class.forName( className );
      return new TsContextRefDef<>( refKey, refClass, params );
    }
    catch( ClassNotFoundException ex ) {
      throw new TsIllegalStateRtException( ex );
    }
  }

}
