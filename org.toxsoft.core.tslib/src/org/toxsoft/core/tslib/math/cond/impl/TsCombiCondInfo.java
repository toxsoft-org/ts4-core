package org.toxsoft.core.tslib.math.cond.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCombiCondInfo} immutable implementation.
 * <p>
 * Instances of this class are created by the builder {@link ITsCombiCondInfoBuilder}.
 *
 * @author hazard157
 */
public class TsCombiCondInfo
    implements ITsCombiCondInfo, Serializable {

  private static final long serialVersionUID = 4051727286625675305L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TsCombiCondInfo"; //$NON-NLS-1$

  private static final String KW_ROOT_NODE = "RootNode"; //$NON-NLS-1$
  private static final String KW_SINGLES   = "Singles";  //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ITsCombiCondInfo> KEEPER =
      new AbstractEntityKeeper<>( ITsCombiCondInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ITsCombiCondInfo aEntity ) {
          StrioUtils.writeKeywordHeader( aSw, KW_ROOT_NODE );
          TsCombiCondNode.KEEPER.write( aSw, aEntity.rootNode() );
          aSw.writeSeparatorChar();
          StrioUtils.writeStringMap( aSw, KW_SINGLES, aEntity.singleInfos(), TsSingleCondInfo.KEEPER, true );
        }

        @Override
        protected ITsCombiCondInfo doRead( IStrioReader aSr ) {
          StrioUtils.ensureKeywordHeader( aSr, KW_ROOT_NODE );
          ITsCombiCondNode rootNode = TsCombiCondNode.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IStringMap<ITsSingleCondInfo> singlesMap =
              StrioUtils.readStringMap( aSr, KW_SINGLES, TsSingleCondInfo.KEEPER );
          return new TsCombiCondInfo( rootNode, singlesMap );
        }

      };

  private final ITsCombiCondNode              rootNode;
  private final IStringMap<ITsSingleCondInfo> singleInfos;

  /**
   * Constructor.
   * <p>
   * The only place where this constructor is called is {@link TsCombiCondInfoBuilder#build()} so arguments are checked
   * to be valid and be a new instances, so this constructor simply remembers the references, does not creates defensive
   * copies of the arguments.
   *
   * @param aNode {@link ITsCombiCondNode} - the root node
   * @param aSinglesMap {@link IStringMap}&lt;{@link ITsSingleCondInfo}&gt; - map "single condition ID" - "the info"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsCombiCondInfo( ITsCombiCondNode aNode, IStringMap<ITsSingleCondInfo> aSinglesMap ) {
    TsNullArgumentRtException.checkNulls( aNode, aSinglesMap );
    rootNode = aNode;
    singleInfos = aSinglesMap;
  }

  // ------------------------------------------------------------------------------------
  // ITsCombiCondInfo
  //

  @Override
  public ITsCombiCondNode rootNode() {
    return rootNode;
  }

  @Override
  public IStringMap<ITsSingleCondInfo> singleInfos() {
    return singleInfos;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return rootNode.toString();
  }

}
