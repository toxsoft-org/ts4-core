package org.toxsoft.tsgui.m5_2.gui.panels;

import org.toxsoft.tsgui.m5_2.*;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.tslib.utils.errors.TsUnderDevelopmentRtException;

public class CollEditController<T> {

  // ------------------------------------------------------------------------------------
  // API for subclass
  //

  protected IM5CollPanelBase<T> owner() {
    // TODO реализовать CollEditController.owner()
    throw new TsUnderDevelopmentRtException( "CollEditController.owner()" );
  }

  protected IM5Bunch<T> getNewItemDefaultValues( T aSelItem ) {
    // TODO реализовать CollEditController.getNewItemDefaultValues()
    throw new TsUnderDevelopmentRtException( "CollEditController.getNewItemDefaultValues()" );
  }

  protected IM5Model<T> model() {
    // TODO реализовать CollEditController.model()
    throw new TsUnderDevelopmentRtException( "CollEditController.model()" );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  T papiMakeNewItemToAdd( T aSelItem ) {
    IM5Bunch<T> initVals = getNewItemDefaultValues( aSelItem );

    return null;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  public T makeNewItemToAdd( T aSelItem ) {
    return null;
  }

  public T makeItemCopyToAdd( T aSelItem ) {
    return null;
  }

  public T editSelItem( T aSelItem ) {
    return null;
  }

  public void removeItem( T aSelItem ) {
    // nop
  }

  public boolean isCrudOpAllowed( ECrudOp aOp ) {
    return false;
  }

  public boolean isCrudOpEnabled( ECrudOp aOp, T aSelItem ) {
    return false;
  }

  public IM5ItemsProvider<T> itemsProvider() {
    return null;
  }

}
