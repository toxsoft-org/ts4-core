package org.toxsoft.core.tslib.math.cond.impl;

import static org.toxsoft.core.tslib.math.cond.impl.ITsCombiConSharedResources.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.logican.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCombiCondInfoBuilder} implementation.
 *
 * @author hazard157
 */
class TsCombiCondInfoBuilder
    implements ITsCombiCondInfoBuilder {

  private final GenericChangeEventer genericChangeEventer;

  private final ITsConditionsTopicManager         topicManager;
  private final IStringMapEdit<ITsSingleCondInfo> singlesMap = new StridMap<>();

  private ITsCombiCondNode rootNode = null;

  /**
   * Constructor.
   *
   * @param aTopicManager {@link ITsConditionsTopicManager} - the single conditions
   */
  public TsCombiCondInfoBuilder( ITsConditionsTopicManager aTopicManager ) {
    TsNullArgumentRtException.checkNull( aTopicManager );
    genericChangeEventer = new GenericChangeEventer( this );
    topicManager = aTopicManager;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void collectMentionedSingleConditionUniqueIds( ITsCombiCondNode aNode, IStringListEdit aIdsList ) {
    if( aNode.isSingle() ) {
      String scId = aNode.singleCondId();
      if( !aIdsList.hasElem( scId ) ) {
        aIdsList.add( scId );
      }
    }
    else {
      collectMentionedSingleConditionUniqueIds( aNode.left(), aIdsList );
      collectMentionedSingleConditionUniqueIds( aNode.right(), aIdsList );
    }
  }

  private IStringList collectMentionedSingleConditionUniqueIds( ITsCombiCondNode aNode ) {
    IStringListEdit ll = new StringArrayList();
    collectMentionedSingleConditionUniqueIds( aNode, ll );
    return ll;
  }

  private ITsCombiCondNode makeNode( ILogFoNode aLfn ) {
    if( aLfn.isLeaf() ) {
      return TsCombiCondNode.createSingle( aLfn.keyword(), aLfn.isInverted() );
    }
    ITsCombiCondNode left = makeNode( aLfn.left() );
    ITsCombiCondNode right = makeNode( aLfn.right() );
    return TsCombiCondNode.createCombi( left, aLfn.op(), right, aLfn.isInverted() );
  }

  // ------------------------------------------------------------------------------------
  // ITsCombiCondInfoBuilder
  //

  @Override
  public ITsConditionsTopicManager topicManager() {
    return topicManager;
  }

  @Override
  public void setAsInfo( ITsCombiCondInfo aCombiInfo ) {
    TsNullArgumentRtException.checkNull( aCombiInfo );
    singlesMap.setAll( aCombiInfo.singleInfos() );
    setRootNode( aCombiInfo.rootNode() );
  }

  @Override
  public void setLogicalFormulaNode( ILogFoNode aLfNode ) {
    TsNullArgumentRtException.checkNull( aLfNode );
    setRootNode( aLfNode != ILogFoNode.NONE ? makeNode( aLfNode ) : null );
  }

  @Override
  public void setRootNode( ITsCombiCondNode aNode ) {
    if( rootNode != aNode ) {
      rootNode = aNode;
      genericChangeEventer.fireChangeEvent();
    }
  }

  @Override
  public void addNodeLeft( ITsCombiCondNode aLeft, ELogicalOp aOp, boolean aInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp );
    TsIllegalStateRtException.checkNull( rootNode );
    setRootNode( TsCombiCondNode.createCombi( aLeft, aOp, rootNode, aInverted ) );
  }

  @Override
  public void addNodeRight( ELogicalOp aOp, ITsCombiCondNode aRight, boolean aInverted ) {
    TsNullArgumentRtException.checkNulls( aOp, aRight );
    TsIllegalStateRtException.checkNull( rootNode );
    setRootNode( TsCombiCondNode.createCombi( rootNode, aOp, aRight, aInverted ) );
  }

  @Override
  public IStringMap<ITsSingleCondInfo> singlesMap() {
    return singlesMap;
  }

  @Override
  public void putSingle( String aSingleId, ITsSingleCondInfo aSingleInfo ) {
    StridUtils.checkValidIdPath( aSingleId );
    TsNullArgumentRtException.checkNull( aSingleInfo );
    singlesMap.put( aSingleId, aSingleInfo );
    genericChangeEventer.fireChangeEvent();
  }

  @Override
  public void removeSingle( String aSingleId ) {
    if( singlesMap.removeByKey( aSingleId ) != null ) {
      genericChangeEventer.fireChangeEvent();
    }
  }

  @Override
  public void reset() {
    if( !singlesMap.isEmpty() || rootNode != null ) {
      singlesMap.clear();
      rootNode = null;
      genericChangeEventer.fireChangeEvent();
    }
  }

  @Override
  public IStringList listAbsentSingleIds() {
    if( rootNode == null ) {
      return IStringList.EMPTY;
    }
    IStringList usedScIds = collectMentionedSingleConditionUniqueIds( rootNode );
    IStringListEdit ll = new StringArrayList();
    for( String scId : usedScIds ) {
      if( !singlesMap.hasKey( scId ) ) {
        ll.add( scId );
      }
    }
    return ll;
  }

  @Override
  public IStringList listUnusedSingleIds() {
    if( rootNode == null ) {
      return singlesMap.keys();
    }
    IStringList usedScIds = collectMentionedSingleConditionUniqueIds( rootNode );
    IStringListEdit ll = new StringArrayList();
    for( String scId : singlesMap.keys() ) {
      if( !usedScIds.hasElem( scId ) ) {
        ll.add( scId );
      }
    }
    return ll;
  }

  @Override
  public ValidationResult canBuild() {
    ValidationResult vr = ValidationResult.SUCCESS;
    // check something was specified
    if( rootNode == null ) {
      return ValidationResult.error( MSG_ERR_NO_CONDS_SPECIFIED_YET );
    }
    // check there is not absent single conditions
    int absentCount = listAbsentSingleIds().size();
    if( absentCount > 0 ) {
      return ValidationResult.error( FMT_ERR_UNSPECIFIED_CONDS_QTTY, Integer.valueOf( absentCount ) );
    }
    // check all mentioned single conditions can be created with topic manager
    for( String scId : collectMentionedSingleConditionUniqueIds( rootNode ) ) {
      ITsSingleCondInfo scInfo = singlesMap.findByKey( scId );
      if( scInfo != null ) {
        vr = ValidationResult.firstNonOk( vr, topicManager.checkSingleCondInfo( scInfo ) );
        if( vr.isError() ) {
          return vr;
        }
      }
    }
    // warn if unused single conditions exist
    int unusedCount = listUnusedSingleIds().size();
    if( unusedCount > 0 ) {
      vr = ValidationResult.warn( FMT_ERR_UNUSED_CONDS_QTTY, Integer.valueOf( absentCount ) );
    }
    return vr;
  }

  @Override
  public ITsCombiCondNode getRootNode() {
    return rootNode;
  }

  @Override
  public ITsCombiCondInfo build() {
    TsValidationFailedRtException.checkError( canBuild() );
    return new TsCombiCondInfo( rootNode, new StringMap<>( singlesMap() ) );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

}
