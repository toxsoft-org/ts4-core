package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;

/**
 * {@link IVedDataModel} implementation.
 *
 * @author hazard157
 */
public class VedDataModel
    implements IVedDataModel {

  private final GenericChangeEventer genericEventer;

  private final INotifierOptionSetEdit canvasConfig = new NotifierOptionSetEditWrapper( new OptionSet() );

  private final INotifierStridablesListEdit<IVedComponent> compsList =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );

  /**
   * Constructor.
   */
  public VedDataModel() {
    genericEventer = new GenericChangeEventer( this );
    canvasConfig.addCollectionChangeListener( genericEventer );
    compsList.addCollectionChangeListener( genericEventer );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    genericEventer.pauseFiring();
    canvasConfig.pauseFiring();
    compsList.pauseFiring();
    try {
      canvasConfig.clear();
      compsList.clear();
    }
    finally {
      canvasConfig.resumeFiring( true );
      compsList.resumeFiring( true );
      genericEventer.resumeFiring( true );
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericEventer;
  }

  // ------------------------------------------------------------------------------------
  // IVedDataModel
  //

  @Override
  public INotifierOptionSetEdit canvasConfig() {
    return canvasConfig;
  }

  @Override
  public INotifierStridablesListEdit<IVedComponent> comps() {
    return compsList;
  }

}
