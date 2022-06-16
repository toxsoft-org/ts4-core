package org.toxsoft.core.tsgui.panels.opsedit.impl;

import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsEventer} implementation for {@link IOpsetsKitPanel#eventer()}.
 *
 * @author hazard157
 */
class OpsetsKitChangeEventer
    extends AbstractTsEventer<IOpsetsKitChangeListener> {

  private final IOpsetsKitPanel source;

  /**
   * Last changed option values map "kit item ID" - "option ID" - "option value"
   */
  private final IStringMapEdit<IStringMapEdit<IAtomicValue>> changedOptionsMap = new StringMap<>();

  /**
   * The kititemID that was last selected while firing was paused.
   */
  private String lastSelectedKitId = null;

  /**
   * Flags that kit item was selected while firing was paused.
   */
  private boolean wasKitItemSelected = false;

  public OpsetsKitChangeEventer( IOpsetsKitPanel aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void reallyFireOptionValueChangeEvent( String aKitItemId, String aOptionId, IAtomicValue aNewValue ) {
    for( IOpsetsKitChangeListener l : listeners() ) {
      l.onOptionValueChanged( source, aKitItemId, aOptionId, aNewValue );
    }
  }

  private void reallyFireKitItemSelectionEvent( String aKitItemId ) {
    for( IOpsetsKitChangeListener l : listeners() ) {
      l.onKitItemsSelected( source, aKitItemId );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsEventer
  //

  @Override
  protected boolean doIsPendingEvents() {
    return !changedOptionsMap.isEmpty() || wasKitItemSelected;
  }

  @Override
  protected void doFirePendingEvents() {
    for( String kitId : changedOptionsMap.keys() ) {
      IStringMapEdit<IAtomicValue> opValMap = changedOptionsMap.getByKey( kitId );
      for( String optionId : opValMap.keys() ) {
        IAtomicValue av = opValMap.getByKey( optionId );
        reallyFireOptionValueChangeEvent( kitId, optionId, av );
      }
    }
    if( wasKitItemSelected ) {
      reallyFireKitItemSelectionEvent( lastSelectedKitId );
    }
  }

  @Override
  protected void doClearPendingEvents() {
    changedOptionsMap.clear();
    wasKitItemSelected = false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void fireOptionValueChangeEvent( String aKitItemId, String aOptionId, IAtomicValue aNewValue ) {
    TsNullArgumentRtException.checkNulls( aKitItemId, aOptionId, aNewValue );
    if( isFiringPaused() ) {
      IStringMapEdit<IAtomicValue> opValMap = changedOptionsMap.findByKey( aKitItemId );
      if( opValMap == null ) {
        opValMap = new StringMap<>();
        changedOptionsMap.put( aKitItemId, opValMap );
      }
      opValMap.put( aOptionId, aNewValue );
    }
    reallyFireOptionValueChangeEvent( aKitItemId, aOptionId, aNewValue );
  }

  public void fireKitItemSelectionEvent( String aKitItemID ) {
    if( isFiringPaused() ) {
      wasKitItemSelected = true;
      lastSelectedKitId = aKitItemID;
      return;
    }
    reallyFireKitItemSelectionEvent( aKitItemID );
  }

}
