package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedSelectedComponentManager} implementation.
 *
 * @author hazard157
 */
class VedSelectedComponentManager
    implements IVedSelectedComponentManager {

  private final GenericChangeEventer   eventer;
  private final IGenericChangeListener listenerForScreen;

  /**
   * Contains list of selected components.
   */
  private IStridablesListEdit<IVedComponent> selComps = new StridablesList<>();

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
  // IVedSelectedComponentManager
  //

  @Override
  public ESelectionKind selectionKind() {
    switch( selComps.size() ) {
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
  public IVedComponent selectedComponent() {
    return selComps.first();
  }

  @Override
  public IStridablesList<IVedComponent> selectedComponents() {
    return selComps;
  }

  @Override
  public void deselectAll() {
    if( !selComps.isEmpty() ) {
      selComps.clear();
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setSelectedComponent( IVedComponent aComp ) {
    TsNullArgumentRtException.checkNull( aComp );
    if( selComps.size() != 1 || selComps.remove( aComp ) >= 0 ) {
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setSelectedComponents( IStridablesList<IVedComponent> aComps ) {
    TsNullArgumentRtException.checkNull( aComps );
    if( !selComps.equals( aComps ) ) {
      selComps.setAll( aComps );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void toggleSelection( IVedComponent aComp ) {
    TsNullArgumentRtException.checkNull( aComp );
    if( selComps.hasElem( aComp ) ) {
      selComps.remove( aComp );
    }
    else {
      selComps.add( aComp );
    }
    eventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // API fore screen - these methods does not call listener, specified in constructor
  //

  void screenSetSelectedComponent( IVedComponent aComp ) {
    eventer.muteListener( listenerForScreen );
    try {
      setSelectedComponent( aComp );
    }
    finally {
      eventer.unmuteListener( listenerForScreen );
    }
  }

  void screenSetSelectedComponents( IStridablesList<IVedComponent> aComps ) {
    eventer.muteListener( listenerForScreen );
    try {
      setSelectedComponents( aComps );
    }
    finally {
      eventer.unmuteListener( listenerForScreen );
    }
  }

  void screenToggleSelection( IVedComponent aComp ) {
    eventer.muteListener( listenerForScreen );
    try {
      toggleSelection( aComp );
    }
    finally {
      eventer.unmuteListener( listenerForScreen );
    }
  }

}
