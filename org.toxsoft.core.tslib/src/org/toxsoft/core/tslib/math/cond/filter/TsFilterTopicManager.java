package org.toxsoft.core.tslib.math.cond.filter;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsFilterTopicManager} implementation.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public class TsFilterTopicManager<T>
    extends TsConditionsTopicManager<ITsSingleFilterType>
    implements ITsFilterTopicManager<T> {

  private final Class<T> filterObjectClass;

  /**
   * Constructor.
   *
   * @param aFilterObjectClass {@link Class}&lt;T&gt; - filter input objects class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsFilterTopicManager( Class<T> aFilterObjectClass ) {
    TsNullArgumentRtException.checkNull( aFilterObjectClass );
    filterObjectClass = aFilterObjectClass;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ITsFilter<T> internalCreateCombi( ITsCombiCondNode aNode, IStringMap<ITsFilter<T>> aSfMap ) {
    if( aNode.isSingle() ) {
      return TsCombiFilter.createSingle( aSfMap.getByKey( aNode.singleCondId() ), aNode.isInverted() );
    }
    ITsFilter<T> left = internalCreateCombi( aNode.left(), aSfMap );
    ITsFilter<T> right = internalCreateCombi( aNode.right(), aSfMap );
    return TsCombiFilter.createCombi( left, aNode.op(), right, aNode.isInverted(), false );
  }

  // ------------------------------------------------------------------------------------
  // ITsFilterTopicManager
  //

  @Override
  public Class<T> filterObjectClass() {
    return filterObjectClass;
  }

  @Override
  public ITsFilter<T> createCombiFilter( ITsCombiCondInfo aCombiCondInfo ) {
    TsValidationFailedRtException.checkError( checkCombiCondInfo( aCombiCondInfo ) );
    // create single filters
    IStringMapEdit<ITsFilter<T>> singleFiltersMap = new StringMap<>();
    for( String sfId : aCombiCondInfo.singleInfos().keys() ) {
      ITsSingleCondInfo scInf = aCombiCondInfo.singleInfos().getByKey( sfId );
      ITsSingleFilterType sfType = typesList().getByKey( scInf.typeId() );
      ITsFilter<T> singleFilter = sfType.create( scInf );
      singleFiltersMap.put( sfId, singleFilter );
    }
    return internalCreateCombi( aCombiCondInfo.rootNode(), singleFiltersMap );
  }

  @Override
  public void registerType( ITsSingleFilterType aType ) {
    TsNullArgumentRtException.checkNull( aType );
    TsItemAlreadyExistsRtException.checkTrue( typesList().hasKey( aType.id() ) );
    typesList().add( aType );
  }

  @Override
  public IStridablesList<ITsSingleFilterType> singleTypes() {
    return typesList();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Registers new or replaces existing registered type.
   *
   * @param aType {@link ITsSingleFilterType} - the type to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void putType( ITsSingleFilterType aType ) {
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
