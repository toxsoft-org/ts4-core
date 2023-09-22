package org.toxsoft.core.tsgui.ved.zver2.api.impl;

import org.toxsoft.core.tsgui.ved.zver2.api.comp.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreenSelectionManager} implementation.
 *
 * @author hazard157
 * @author vs
 */
class VedSelectedComponentManager
    implements IVedScreenSelectionManager {

  private final GenericChangeEventer   eventer;
  private final IGenericChangeListener listenerForScreen;

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

  @Override
  public void deselectAll() {
    if( !selViews.isEmpty() ) {
      selViews.clear();
      eventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // API fore screen - these methods does not call listener, specified in constructor
  //

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
