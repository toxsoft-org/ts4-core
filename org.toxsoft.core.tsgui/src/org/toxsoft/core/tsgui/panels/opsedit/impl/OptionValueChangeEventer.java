package org.toxsoft.core.tsgui.panels.opsedit.impl;

import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsEventer} implementation for {@link IOptionValueChangeListener}.
 *
 * @author hazard157
 */
public class OptionValueChangeEventer
    extends AbstractTsEventer<IOptionValueChangeListener> {

  private final IStringMapEdit<IAtomicValue> changedOpsMap = new StringMap<>();
  private final Object                       source;

  /**
   * Constructor.
   * <p>
   * The argument may be <code>null</code>.
   *
   * @param aSource Object - the source for event
   */
  public OptionValueChangeEventer( Object aSource ) {
    source = aSource;
  }

  @Override
  protected boolean doIsPendingEvents() {
    return !changedOpsMap.isEmpty();
  }

  @Override
  protected void doFirePendingEvents() {
    for( String opId : changedOpsMap.keys() ) {
      reallyFire( opId, changedOpsMap.getByKey( opId ) );
    }
  }

  @Override
  protected void doClearPendingEvents() {
    changedOpsMap.clear();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void reallyFire( String aOpId, IAtomicValue aNewValue ) {
    for( IOptionValueChangeListener l : listeners() ) {
      l.onOptionValueChange( source, aOpId, aNewValue );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Fires the event.
   *
   * @param aOpId String - the option ID
   * @param aNewValue {@link IAtomicValue} - the new value in editor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException argument is not an ID path
   */
  public void fireEvent( String aOpId, IAtomicValue aNewValue ) {
    StridUtils.checkValidIdPath( aOpId );
    TsNullArgumentRtException.checkNull( aNewValue );
    if( isFiringPaused() ) {
      // remove() and put() keeps oreder of options editing sequence
      changedOpsMap.removeByKey( aOpId );
      changedOpsMap.put( aOpId, aNewValue );
    }
    else {
      reallyFire( aOpId, aNewValue );
    }
  }

}
