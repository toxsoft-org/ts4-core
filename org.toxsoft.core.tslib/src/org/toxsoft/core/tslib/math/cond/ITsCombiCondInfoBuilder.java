package org.toxsoft.core.tslib.math.cond;

import static org.toxsoft.core.tslib.math.cond.impl.ITsCombiConSharedResources.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.logican.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The {@link ITsCombiCondInfo} builder.
 *
 * @author hazard157
 */
public interface ITsCombiCondInfoBuilder
    extends IGenericChangeEventCapable {

  /**
   * Singleton of the "null" builder that can not build anything and is not related to any topic manager.
   */
  ITsCombiCondInfoBuilder NONE = new InternalCombiCondNoneBuilder();

  /**
   * Returns the topic manager used for types information retrieval.
   * <p>
   * Topic manager is specified in the builder constructor and can not be changed.
   *
   * @return {@link ITsConditionsTopicManager} - the topic manager
   */
  ITsConditionsTopicManager topicManager();

  /**
   * Sets builder content according to the specified combined condition description.
   * <p>
   * Replaces whole content of the builder, including both combined condition tree {@link #getRootNode()} and single
   * conditions {@link #singlesMap()}.
   *
   * @param aCombiInfo {@link ITsCombiCondInfo} - source condition description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setAsInfo( ITsCombiCondInfo aCombiInfo );

  /**
   * Sets conditions tree as specified by logical formula parse result.
   * <p>
   * If argument is {@link ILogFoNode#NONE} then resets conditions tree {@link #getRootNode()} = <code>null</code>.
   * <p>
   * Replaces only combined condition tree {@link #getRootNode()}, single conditions {@link #singlesMap()} remain
   * unchanged.
   *
   * @param aLogicalFormulaNode {@link ILogFoNode} - logical formula parse result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setLogicalFormulaNode( ILogFoNode aLogicalFormulaNode );

  /**
   * Sets the conditions tree by specifying the root node.
   *
   * @param aNode {@link ITsCombiCondNode} - the root node or <code>null</code>
   */
  void setRootNode( ITsCombiCondNode aNode );

  /**
   * Creates new root node with specified node as left and old root as a right node.
   *
   * @param aLeft {@link ITsCombiCondNode} - the left of the new root node
   * @param aOp {@link ELogicalOp} - the operation
   * @param aInverted boolean - {@link ITsCombiCondNode#isInverted()} value of the new root
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addNodeLeft( ITsCombiCondNode aLeft, ELogicalOp aOp, boolean aInverted );

  /**
   * Creates new root node with specified node as right and old root as a left node.
   *
   * @param aOp {@link ELogicalOp} - the operation
   * @param aRight {@link ITsCombiCondNode} - the right of the new root node
   * @param aInverted boolean - {@link ITsCombiCondNode#isInverted()} value of the new root
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addNodeRight( ELogicalOp aOp, ITsCombiCondNode aRight, boolean aInverted );

  /**
   * Returns the user-specified single conditions.
   * <p>
   * The key in the map must match one of the {@link ITsCombiCondNode#singleCondId()} in the conditions tree,
   *
   * @return {@link IStringMap}&lt;{@link ITsSingleCondInfo}&gt; - map "ID in conditions tree" - "condition info"
   */
  IStringMap<ITsSingleCondInfo> singlesMap();

  /**
   * Add new or replaces existing entry in the map {@link #singlesMap()}.
   *
   * @param aSingleId String - the ID of the single condition
   * @param aSingleInfo {@link ITsSingleCondInfo} - the condition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  void putSingle( String aSingleId, ITsSingleCondInfo aSingleInfo );

  /**
   * Add new or replaces existing entries in the map {@link #singlesMap()}.
   *
   * @param aSingles {@link IStringMap}&lt;{@link ITsSingleCondInfo}&gt; - map "ID" - "single condition"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  void putSingles( IStringMap<ITsSingleCondInfo> aSingles );

  /**
   * Removes entry from the map {@link #singlesMap()}.
   * <p>
   * Does nothing if map does not contains such key.
   *
   * @param aSingleId String - ID of single condition to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeSingle( String aSingleId );

  /**
   * Resets builder as it was immediately after constructor.
   */
  void reset();

  /**
   * Returns IDs of the single conditions from the conditions tree does not having entry in {@link #singlesMap()}.
   *
   * @return {@link IStringList} - IDs of mentioned single conditions absent in {@link #singlesMap()}
   */
  IStringList listAbsentSingleIds();

  /**
   * Returns IDs from {@link #singlesMap()} does not mentioned in the conditions tree.
   *
   * @return {@link IStringList} - unused entries IDs from {@link #singlesMap()}
   */
  IStringList listUnusedSingleIds();

  /**
   * Checks if {@link ITsCombiCondInfo} can bue buile.
   * <p>
   * {@link #canBuild()} will create instance if and only if this method does not ertruns
   * {@link EValidationResultType#ERROR}.
   * <p>
   * Method checks that:
   * <ul>
   * <li>the condition was specified after {@link #reset()} or builder constructor;</li>
   * <li>there is no absent single conditions, that is {@link #listAbsentSingleIds()} is empty;</li>
   * <li>all mentioned single conditions pass
   * {@link ITsConditionsTopicManager#checkSingleCondInfo(ITsSingleCondInfo)};</li>
   * <li>warns if there is any unused ID in {@link #listUnusedSingleIds()}.</li>
   * </ul>
   *
   * @return {@link ValidationResult} - the check result
   */
  ValidationResult canBuild();

  /**
   * Returns the root of the conditions tree.
   *
   * @return {@link ITsCombiCondNode} - root of the conditions tree or <code>null</code>
   */
  ITsCombiCondNode getRootNode();

  /**
   * Creates and returns new instance of the {@link ITsCombiCondInfo}.
   *
   * @return {@link ITsCombiCondInfo} - created instance
   * @throws TsValidationFailedRtException failed {@link #canBuild()}
   */
  ITsCombiCondInfo build();

}

/**
 * {@link ITsCombiCondInfoBuilder#NONE} implementation.
 *
 * @author hazard157
 */
class InternalCombiCondNoneBuilder
    implements ITsCombiCondInfoBuilder {

  /**
   * Singleton instance.
   */
  static final ITsCombiCondInfoBuilder NONE = new InternalCombiCondNoneBuilder();

  InternalCombiCondNoneBuilder() {
    // nop
  }

  @Override
  public ITsConditionsTopicManager topicManager() {
    return ITsConditionsTopicManager.NONE;
  }

  @Override
  public void setAsInfo( ITsCombiCondInfo aCombiInfo ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setLogicalFormulaNode( ILogFoNode aLogicalFormulaNode ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setRootNode( ITsCombiCondNode aNode ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void addNodeLeft( ITsCombiCondNode aLeft, ELogicalOp aOp, boolean aInverted ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void addNodeRight( ELogicalOp aOp, ITsCombiCondNode aRight, boolean aInverted ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IStringMap<ITsSingleCondInfo> singlesMap() {
    return IStringMap.EMPTY;
  }

  @Override
  public void putSingle( String aSingleId, ITsSingleCondInfo aSingleInfo ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void putSingles( IStringMap<ITsSingleCondInfo> aSingles ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void removeSingle( String aSingleId ) {
    // nop
  }

  @Override
  public void reset() {
    // nop
  }

  @Override
  public IStringList listAbsentSingleIds() {
    return IStringList.EMPTY;
  }

  @Override
  public IStringList listUnusedSingleIds() {
    return IStringList.EMPTY;
  }

  @Override
  public ValidationResult canBuild() {
    return ValidationResult.error( FMT_ERR_NO_TPOIC_MANAGER );
  }

  @Override
  public ITsCombiCondNode getRootNode() {
    return null;
  }

  @Override
  public ITsCombiCondInfo build() {
    throw new TsValidationFailedRtException( canBuild() );
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return NoneGenericChangeEventer.INSTANCE;
  }

}
