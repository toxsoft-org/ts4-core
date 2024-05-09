package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCheckerTopicManager} implementation.
 *
 * @author hazard157
 * @param <E> - the environment class
 */
public class TsCheckerTopicManager<E>
    extends TsConditionsTopicManager<ITsSingleCheckerType>
    implements ITsCheckerTopicManager<E> {

  private final Class<E> checkEnvironmentClass;

  /**
   * Constructor.
   *
   * @param aCheckEnvironmentClass {@link Class}&lt;E&gt; - checker environment class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsCheckerTopicManager( Class<E> aCheckEnvironmentClass ) {
    TsNullArgumentRtException.checkNull( aCheckEnvironmentClass );
    checkEnvironmentClass = aCheckEnvironmentClass;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ITsChecker internalCreateCombi( ITsCombiCondNode aNode, IStringMap<ITsChecker> aSfMap ) {
    if( aNode.isSingle() ) {
      return TsCombiChecker.createSingle( aSfMap.getByKey( aNode.singleCondId() ), aNode.isInverted() );
    }
    ITsChecker left = internalCreateCombi( aNode.left(), aSfMap );
    ITsChecker right = internalCreateCombi( aNode.right(), aSfMap );
    return TsCombiChecker.createCombi( left, aNode.op(), right, aNode.isInverted(), false );
  }

  // ------------------------------------------------------------------------------------
  // ITsCheckerTopicManager
  //

  @Override
  public Class<E> checkEnvironmentClass() {
    return checkEnvironmentClass;
  }

  @Override
  public ITsChecker createCombiChecker( ITsCombiCondInfo aCombiCondInfo, E aEnv ) {
    TsValidationFailedRtException.checkError( checkCombiCondInfo( aCombiCondInfo ) );
    // create single checkers
    IStringMapEdit<ITsChecker> singleCheckersMap = new StringMap<>();
    for( String sfId : aCombiCondInfo.singleInfos().keys() ) {
      ITsSingleCondInfo scInf = aCombiCondInfo.singleInfos().getByKey( sfId );
      ITsSingleCheckerType sfType = typesList().getByKey( scInf.typeId() );
      ITsChecker singleChecker = sfType.create( aEnv, scInf );
      singleCheckersMap.put( sfId, singleChecker );
    }
    return internalCreateCombi( aCombiCondInfo.rootNode(), singleCheckersMap );
  }

  @Override
  public void registerType( ITsSingleCheckerType aType ) {
    TsNullArgumentRtException.checkNull( aType );
    TsItemAlreadyExistsRtException.checkTrue( typesList().hasKey( aType.id() ) );
    typesList().add( aType );
  }

  @Override
  public IStridablesList<ITsSingleCheckerType> singleTypes() {
    return typesList();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Registers new or replaces existing registered type.
   *
   * @param aType {@link ITsSingleCheckerType} - the type to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void putType( ITsSingleCheckerType aType ) {
    TsNullArgumentRtException.checkNull( aType );
    typesList().put( aType );
  }

  /**
   * Removes type by ID if registered.
   *
   * @param aId String - the type ID
   * @return boolean - if type ID was removed, <code>false</code> - no such type was registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean unregister( String aId ) {
    return typesList().removeById( aId ) != null;
  }

}
