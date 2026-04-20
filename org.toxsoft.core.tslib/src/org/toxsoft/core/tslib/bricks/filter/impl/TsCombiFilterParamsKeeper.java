package org.toxsoft.core.tslib.bricks.filter.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.math.logicop.*;

/**
 * Keeper of {@link ITsCombiFilterParams}.
 *
 * @author hazard157
 */
public class TsCombiFilterParamsKeeper
    extends AbstractEntityKeeper<ITsCombiFilterParams> {

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsCombiFilterParams"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ITsCombiFilterParams> KEEPER = new TsCombiFilterParamsKeeper();

  /**
   * Kept value of the constant {@link ITsCombiFilterParams#NONE}.
   */
  public static final IAtomicValue AV_NONE = AvUtils.avValobj( ITsCombiFilterParams.NONE, KEEPER, KEEPER_ID );

  /**
   * Kept value of the constant {@link ITsCombiFilterParams#ALL}.
   */
  public static final IAtomicValue AV_ALL = AvUtils.avValobj( ITsCombiFilterParams.ALL, KEEPER, KEEPER_ID );

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
