package org.toxsoft.tslib.bricks.apprefs.impl;

import static org.toxsoft.tslib.utils.TsLibUtils.*;

import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.av.opset.INotifierOptionSetEdit;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.impl.NotifierOptionSetEditWrapper;
import org.toxsoft.tslib.av.opset.impl.OptionSet;
import org.toxsoft.tslib.bricks.apprefs.IPrefBundle;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Implementation of the {@link IPrefBundle}.
 *
 * @author hazard157
 */
public class PrefBundle
    implements IPrefBundle {

  private final ITsCollectionChangeListener paramsChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      storage.saveBundle( bundleId, params );
    }
  };

  final INotifierOptionSetEdit        params      = new NotifierOptionSetEditWrapper( new OptionSet() );
  final IStridablesListEdit<IDataDef> knownParams = new StridablesList<>();
  final AbstractAppPreferencesStorage storage;

  final String   bundleId;
  private String name        = EMPTY_STRING;
  private String description = EMPTY_STRING;

  PrefBundle( String aBundleId, IOptionSet aParams, AbstractAppPreferencesStorage aStorage ) {
    bundleId = aBundleId;
    storage = aStorage;
    params.addAll( aParams );
    params.addCollectionChangeListener( paramsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Stridable
  //

  @Override
  public String id() {
    return bundleId;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает имя и описание.
   *
   * @param aName String - имя
   * @param aDescription String - описание
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  protected void setNameAndDescription( String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    name = aName;
    description = aDescription;
  }

  // ------------------------------------------------------------------------------------
  // IPrefBundle
  //

  @Override
  public INotifierOptionSetEdit params() {
    return params;
  }

  @Override
  public IStridablesList<IDataDef> knownParams() {
    return knownParams;
  }

  @Override
  public void defineParam( IDataDef aParamInfo ) {
    TsNullArgumentRtException.checkNull( aParamInfo );
    if( knownParams.hasKey( aParamInfo.id() ) ) {
      throw new TsItemAlreadyExistsRtException();
    }
    knownParams.add( aParamInfo );
  }

  @Override
  public void undefineParam( String aParamInfoId ) {
    knownParams.removeById( aParamInfoId );
  }

}
