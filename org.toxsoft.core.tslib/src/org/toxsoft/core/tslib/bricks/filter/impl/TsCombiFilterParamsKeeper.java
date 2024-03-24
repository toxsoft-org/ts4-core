package org.toxsoft.core.tslib.bricks.filter.impl;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.math.logicop.*;

/**
 * Хранитель объектов типа {@link ITsCombiFilterParams}.
 *
 * @author hazard157
 */
public class TsCombiFilterParamsKeeper
    extends AbstractEntityKeeper<ITsCombiFilterParams> {

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ITsCombiFilterParams> KEEPER = new TsCombiFilterParamsKeeper();

  private TsCombiFilterParamsKeeper() {
    super( ITsCombiFilterParams.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, ITsCombiFilterParams aEntity ) {
    // isSingle
    aSw.writeBoolean( aEntity.isSingle() );
    aSw.writeSeparatorChar();
    // isResultInverted
    aSw.writeBoolean( aEntity.isInverted() );
    aSw.writeSeparatorChar();
    if( aEntity.isSingle() ) {
      TsSingleFilterParamsKeeper.KEEPER.write( aSw, aEntity.single() );
      return;
    }
    aSw.incNewLine();
    // left ITsCombiFilterParams
    KEEPER.write( aSw, aEntity.left() );
    aSw.writeSeparatorChar();
    aSw.writeEol();
    // op
    ELogicalOp.KEEPER.write( aSw, aEntity.op() );
    aSw.writeSeparatorChar();
    aSw.writeEol();
    // right ITsCombiFilterParams
    KEEPER.write( aSw, aEntity.right() );
    aSw.writeSeparatorChar();
    aSw.decNewLine();
  }

  @Override
  protected ITsCombiFilterParams doRead( IStrioReader aSr ) {
    // isSingle
    boolean isSingle = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    // isResultInverted
    boolean isResultInverted = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    if( isSingle ) {
      ITsSingleFilterParams sfp = TsSingleFilterParamsKeeper.KEEPER.read( aSr );
      return TsCombiFilterParams.createSingle( sfp, isResultInverted );
    }
    // left ITsCombiFilterParams
    ITsCombiFilterParams pfp1 = TsCombiFilterParamsKeeper.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    // op
    ELogicalOp op = ELogicalOp.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    // right ITsCombiFilterParams
    ITsCombiFilterParams pfp2 = TsCombiFilterParamsKeeper.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    return TsCombiFilterParams.createCombi( pfp1, op, pfp2, isResultInverted );
  }

}
