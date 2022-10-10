package org.toxsoft.core.tsgui.ved.zver1.core.impl;

import static org.toxsoft.core.tsgui.ved.zver1.core.impl.ITsResources.*;

import org.toxsoft.core.tsgui.ved.zver1.core.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedDataModel} implementation.
 *
 * @author hazard157
 */
class VedDataModel
    implements IVedDataModel {

  /**
   * Component change listener forces {@link #compsList} to fire {@link ECrudOp#EDIT} event.
   */
  private final IGenericChangeListener componentChangeListener = aSource -> {
    IVedComponent c = (IVedComponent)aSource;
    this.compsList.fireItemByKeyChangeEvent( c.id() );
  };

  private final INotifierStridablesListEdit<IVedComponent> compsList      =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );
  private final IListReorderer<IVedComponent>              compsReorderer =
      new ListReorderer<IVedComponent, IListEdit<IVedComponent>>( compsList );

  private final GenericChangeEventer   genericEventer;
  private final INotifierOptionSetEdit canvasConfig = new NotifierOptionSetEditWrapper( new OptionSet() );

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
  public INotifierStridablesList<IVedComponent> listComponents() {
    return compsList;
  }

  @Override
  public void addComponent( IVedComponent aComponent ) {
    TsNullArgumentRtException.checkNull( aComponent );
    if( compsList.hasKey( aComponent.id() ) ) {
      throw new TsItemAlreadyExistsRtException( FMT_ERR_DUP_COMP_ID, aComponent.id() );
    }
    aComponent.genericChangeEventer().addListener( componentChangeListener );
    compsList.add( aComponent );
  }

  @Override
  public void insertComponent( int aIndex, IVedComponent aComponent ) {
    TsNullArgumentRtException.checkNull( aComponent );
    if( compsList.hasKey( aComponent.id() ) ) {
      throw new TsItemAlreadyExistsRtException( FMT_ERR_DUP_COMP_ID, aComponent.id() );
    }
    aComponent.genericChangeEventer().addListener( componentChangeListener );
    compsList.insert( aIndex, aComponent );
  }

  @Override
  public void removeComponent( String aComponentId ) {
    TsNullArgumentRtException.checkNull( aComponentId );
    IVedComponent c = compsList.removeById( aComponentId );
    if( c != null ) {
      // FIXME ensure viewes are removed from screens and are disposed
      c.genericChangeEventer().removeListener( componentChangeListener );
    }
  }

  @Override
  public IListReorderer<IVedComponent> compsReorderer() {
    return compsReorderer;
  }

}
