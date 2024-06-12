package org.toxsoft.core.tslib.math.cond.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCombiCondNode} implementation creates the immutable instances.
 * <p>
 * Static constructors used to create the instances.
 *
 * @author hazard157
 */
public abstract class TsCombiCondNode
    implements ITsCombiCondNode, Serializable {

  /**
   * Root node singleton for {@link ITsCombiCondInfo#ALWAYS}.
   */
  public static final ITsCombiCondNode ALWAYS = new CombiCondLeafNode( ITsSingleCondInfo.TYPE_ID_ALWAYS, false );

  /**
   * Root node singleton for {@link ITsCombiCondInfo#NEVER}.
   */
  public static final ITsCombiCondNode NEVER = new CombiCondLeafNode( ITsSingleCondInfo.TYPE_ID_NEVER, false );

  private static final long serialVersionUID = 157157L;

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ITsCombiCondNode> KEEPER =
      new AbstractEntityKeeper<>( ITsCombiCondNode.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ITsCombiCondNode aEntity ) {
          // isSingle
          aSw.writeBoolean( aEntity.isSingle() );
          aSw.writeSeparatorChar();
          // isInverted
          aSw.writeBoolean( aEntity.isInverted() );
          aSw.writeSeparatorChar();
          // single condition ID
          if( aEntity.isSingle() ) {
            aSw.writeAsIs( aEntity.singleCondId() );
            return;
          }
          // left ITsCombiCondNode
          KEEPER.write( aSw, aEntity.left() );
          aSw.writeSeparatorChar();
          // op
          ELogicalOp.KEEPER.write( aSw, aEntity.op() );
          aSw.writeSeparatorChar();
          // FORMATTING aSw.writeEol();
          // right ITsCombiCondNode
          KEEPER.write( aSw, aEntity.right() );
        }

        @Override
        protected ITsCombiCondNode doRead( IStrioReader aSr ) {
          // isSingle
          boolean isSingle = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          // isResultInverted
          boolean isResultInverted = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          if( isSingle ) {
            String combiCondId = aSr.readIdPath();
            return new CombiCondLeafNode( combiCondId, isResultInverted );
          }
          // left ITsCombiCondNode
          ITsCombiCondNode pfp1 = KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // op
          ELogicalOp op = ELogicalOp.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // right ITsCombiCondNode
          ITsCombiCondNode pfp2 = KEEPER.read( aSr );
          return new CombiCondGroupNode( pfp1, op, pfp2, isResultInverted );
        }

      };

  private final boolean isResultInverted;

  protected TsCombiCondNode( boolean aIsInverted ) {
    isResultInverted = aIsInverted;
  }

  // ------------------------------------------------------------------------------------
  // Static constructors
  //

  /**
   * Static constructor.
   *
   * @param aSingleCondId String - the single condition ID in a leaf node
   * @param aIsResultInverted boolean - the value of the flag {@link ITsCombiCondNode#isInverted()}
   * @return {@link ITsCombiCondNode} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public static ITsCombiCondNode createSingle( String aSingleCondId, boolean aIsResultInverted ) {
    return new CombiCondLeafNode( aSingleCondId, aIsResultInverted );
  }

  /**
   * Static non-inverted constructor.
   *
   * @param aSingleCondId String - the single condition ID in a leaf node
   * @return {@link ITsCombiCondNode} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public static ITsCombiCondNode createSingle( String aSingleCondId ) {
    return createSingle( aSingleCondId, false );
  }

  /**
   * Static constructor.
   *
   * @param aLeft {@link ITsCombiCondNode} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ITsCombiCondNode} - right condition parameters
   * @param aIsResultInverted boolean - the value of the flag {@link ITsCombiCondNode#isInverted()}
   * @return {@link ITsCombiCondNode} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITsCombiCondNode createCombi( ITsCombiCondNode aLeft, ELogicalOp aOp, ITsCombiCondNode aRight,
      boolean aIsResultInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    return new CombiCondGroupNode( aLeft, aOp, aRight, aIsResultInverted );
  }

  /**
   * Static non-inverted constructor.
   *
   * @param aLeft {@link ITsCombiCondNode} - left condition parameters
   * @param aOp {@link ELogicalOp} - logical operation between left and right conditions
   * @param aRight {@link ITsCombiCondNode} - right condition parameters
   * @return {@link ITsCombiCondNode} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITsCombiCondNode createCombi( ITsCombiCondNode aLeft, ELogicalOp aOp, ITsCombiCondNode aRight ) {
    return createCombi( aLeft, aOp, aRight, false );
  }

  // ------------------------------------------------------------------------------------
  // ITsCombiCondNode
  //

  @Override
  public boolean isInverted() {
    return isResultInverted;
  }

}
