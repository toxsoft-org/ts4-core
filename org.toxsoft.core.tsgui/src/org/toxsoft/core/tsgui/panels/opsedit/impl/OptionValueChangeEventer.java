package org.toxsoft.core.tsgui.panels.opsedit.impl;

import org.toxsoft.core.tsgui.panels.opsedit.set.*;
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

  private final IStringListEdit changedOpIds = new StringLinkedBundleList();
  private final Object          source;

  /**
   * Constructor.
   * <p>
   * The argument may be <code>null</code>.
   *
   * @param aSource Object - the source for {@link IOptionValueChangeListener#onOptionValueChange(Object, String)}
   */
  public OptionValueChangeEventer( Object aSource ) {
    source = aSource;
  }

  @Override
  protected boolean doIsPendingEvents() {
    return !changedOpIds.isEmpty();
  }

  @Override
  protected void doFirePendingEvents() {
    for( String opId : changedOpIds ) {
      reallyFire( opId );
    }
  }

  @Override
  protected void doClearPendingEvents() {
    changedOpIds.clear();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void reallyFire( String aOpId ) {
    for( IOptionValueChangeListener l : listeners() ) {
      l.onOptionValueChange( source, aOpId );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Fires the event.
   *
   * @param aOpId String - the option ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException argument is not an ID path
   */
  public void fireEvent( String aOpId ) {
    StridUtils.checkValidIdPath( aOpId );
    if( isFiringPaused() ) {
      // remove() and add() keeps oreder of options editing sequence
      changedOpIds.remove( aOpId );
      changedOpIds.add( aOpId );
    }
    else {
      reallyFire( aOpId );
    }
  }

}
