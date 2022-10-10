package org.toxsoft.core.tsgui.ved.zver1.core.impl;

import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreenSelectionManager} implementation.
 *
 * @author hazard157
 */
class VedSelectedComponentManager
    implements IVedScreenSelectionManager {

  private final GenericChangeEventer   eventer;
  private final IGenericChangeListener listenerForScreen;

  // /**
  // * Contains list of selected components.
  // */
  // private IStridablesListEdit<IVedComponent> selComps = new StridablesList<>();

  /**
   * Contains list of selected component views.
   */
  private IStridablesListEdit<IVedComponentView> selViews = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aListenerForScreen {@link IGenericChangeListener} - listener for screen itself
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  VedSelectedComponentManager( IGenericChangeListener aListenerForScreen ) {
    TsNullArgumentRtException.checkNull( aListenerForScreen );
    eventer = new GenericChangeEventer( this );
    listenerForScreen = aListenerForScreen;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenSelectionManager
  //

  // @Override
  // public ESelectionKind selectionKind() {
  // switch( selComps.size() ) {
  // case 0: {
  // return ESelectionKind.NONE;
  // }
  // case 1: {
  // return ESelectionKind.ONE;
  // }
  // default: {
  // return ESelectionKind.MULTI;
  // }
  // }
  // }

  @Override
  public ESelectionKind selectionKind() {
    switch( selViews.size() ) {
      case 0: {
        return ESelectionKind.NONE;
      }
      case 1: {
        return ESelectionKind.ONE;
      }
      default: {
        return ESelectionKind.MULTI;
      }
    }
  }

  // @Override
  // public IVedComponent selectedComponent() {
  // return selComps.first();
  // }
  //
  // @Override
  // public IStridablesList<IVedComponent> selectedComponents() {
  // return selComps;
  // }

  @Override
  public void deselectAll() {
    if( !selViews.isEmpty() ) {
      selViews.clear();
      eventer.fireChangeEvent();
    }
  }

  // @Override
  // public void setSelectedComponent( IVedComponent aComp ) {
  // if( aComp != null ) {
  // if( selComps.size() != 1 || !selComps.first().equals( aComp ) ) {
  // selComps.clear();
  // selComps.add( aComp );
  // eventer.fireChangeEvent();
  // }
  // }
  // else {
  // deselectAll();
  // }
  // }
  //
  // @Override
  // public void setSelectedComponents( IStridablesList<IVedComponent> aComps ) {
  // TsNullArgumentRtException.checkNull( aComps );
  // if( !selComps.equals( aComps ) ) {
  // selComps.setAll( aComps );
  // eventer.fireChangeEvent();
  // }
  // }
  //
  // @Override
  // public void toggleSelection( IVedComponent aComp ) {
  // TsNullArgumentRtException.checkNull( aComp );
  // if( selComps.hasElem( aComp ) ) {
  // selComps.remove( aComp );
  // }
  // else {
  // selComps.add( aComp );
  // }
  // eventer.fireChangeEvent();
  // }

  // ------------------------------------------------------------------------------------
  // API fore screen - these methods does not call listener, specified in constructor
  //

  // void screenSetSelectedComponent( IVedComponent aComp ) {
  // eventer.muteListener( listenerForScreen );
  // try {
  // if( aComp == null ) {
  // setSelectedComponents( IStridablesList.EMPTY );
  // }
  // else {
  // setSelectedComponent( aComp );
  // }
  // }
  // finally {
  // eventer.unmuteListener( listenerForScreen );
  // }
  // }

  // void screenSetSelectedComponents( IStridablesList<IVedComponent> aComps ) {
  // eventer.muteListener( listenerForScreen );
  // try {
  // setSelectedComponents( aComps );
  // }
  // finally {
  // eventer.unmuteListener( listenerForScreen );
  // }
  // }

  void screenSetSelectedComponentView( IVedComponentView aView ) {
    eventer.muteListener( listenerForScreen );
    try {
      if( aView == null ) {
        setSelectedViews( IStridablesList.EMPTY );
      }
      else {
        setSelectedView( aView );
      }
    }
    finally {
      eventer.unmuteListener( listenerForScreen );
    }
  }

  void screenSetSelectedComponentViews( IStridablesList<IVedComponentView> aViews ) {
    eventer.muteListener( listenerForScreen );
    try {
      setSelectedViews( aViews );
    }
    finally {
      eventer.unmuteListener( listenerForScreen );
    }
  }

  // void screenToggleSelection( IVedComponent aComp ) {
  // eventer.muteListener( listenerForScreen );
  // try {
  // toggleSelection( aComp );
  // }
  // finally {
  // eventer.unmuteListener( listenerForScreen );
  // }
  // }
  //
  // @Override
  // public void setComponentSelection( IVedComponent aComp, boolean aSelection ) {
  // TsNullArgumentRtException.checkNull( aComp );
  // boolean selection = selComps.hasKey( aComp.id() );
  // if( !aSelection && !selection ) {
  // return;
  // }
  // if( aSelection && selection ) {
  // return;
  // }
  //
  // if( !aSelection ) {
  // selComps.removeById( aComp.id() );
  // }
  // else {
  // selComps.add( aComp );
  // }
  // eventer.fireChangeEvent();
  // }

  @Override
  public IStridablesList<IVedComponentView> selectedViews() {
    return selViews;
  }

  @Override
  public IVedComponentView selectedView() {
    return selViews.first();
  }

  @Override
  public void setSelectedView( IVedComponentView aView ) {
    if( aView != null ) {
      if( selViews.size() != 1 || !selViews.first().equals( aView ) ) {
        selViews.clear();
        selViews.add( aView );
        eventer.fireChangeEvent();
      }
    }
    else {
      deselectAll();
    }
  }

  @Override
  public void setViewSelection( IVedComponentView aView, boolean aSelection ) {
    TsNullArgumentRtException.checkNull( aView );
    boolean selection = selViews.hasKey( aView.id() );
    if( !aSelection && !selection ) {
      return;
    }
    if( aSelection && selection ) {
      return;
    }

    if( !aSelection ) {
      selViews.removeById( aView.id() );
    }
    else {
      selViews.add( aView );
    }
    eventer.fireChangeEvent();
  }

  @Override
  public void setSelectedViews( IStridablesList<IVedComponentView> aViews ) {
    TsNullArgumentRtException.checkNull( aViews );
    if( !selViews.equals( aViews ) ) {
      selViews.setAll( aViews );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void toggleSelection( IVedComponentView aView ) {
    TsNullArgumentRtException.checkNull( aView );
    if( selViews.hasElem( aView ) ) {
      selViews.remove( aView );
    }
    else {
      selViews.add( aView );
    }
    eventer.fireChangeEvent();
  }

}
