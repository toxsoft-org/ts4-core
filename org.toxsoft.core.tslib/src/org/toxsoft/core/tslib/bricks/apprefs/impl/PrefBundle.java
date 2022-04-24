package org.toxsoft.core.tslib.bricks.apprefs.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation of the {@link IPrefBundle}.
 *
 * @author hazard157
 */
class PrefBundle
    implements IPrefBundle, IStridableParameterized, IParameterizedEdit {

  private final ITsCollectionChangeListener paramsChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      storage.saveBundle( bundleId, prefsValues );
    }
  };

  final INotifierOptionSetEdit        prefsValues = new NotifierOptionSetEditWrapper( new OptionSet() );
  final IOptionSetEdit                params      = new OptionSet();
  final IStridablesListEdit<IDataDef> knownParams = new StridablesList<>();
  final AbstractAppPreferencesStorage storage;

  final String   bundleId;
  private String name        = EMPTY_STRING;
  private String description = EMPTY_STRING;

  PrefBundle( String aBundleId, IOptionSet aPrefs, IOptionSet aParams, AbstractAppPreferencesStorage aStorage ) {
    bundleId = aBundleId;
    storage = aStorage;
    params.setAll( aParams );
    prefsValues.addAll( aParams );
    prefsValues.addCollectionChangeListener( paramsChangeListener );
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
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  // TODO TRANSLATE

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
  public INotifierOptionSetEdit prefs() {
    return prefsValues;
  }

  @Override
  public IStridablesList<IDataDef> listKnownOptions() {
    return knownParams;
  }

  @Override
  public void defineOption( IDataDef aParamInfo ) {
    TsNullArgumentRtException.checkNull( aParamInfo );
    if( knownParams.hasKey( aParamInfo.id() ) ) {
      throw new TsItemAlreadyExistsRtException();
    }
    knownParams.add( aParamInfo );
  }

  @Override
  public void undefineOption( String aParamInfoId ) {
    knownParams.removeById( aParamInfoId );
  }

}
