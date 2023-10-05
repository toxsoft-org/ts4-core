package org.toxsoft.core.tsgui.ved.incub;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link ITsEventer} for CRUD operation events firing.
 * <p>
 * This is an eventer with simple logic: if two or more events of any kind occurred when firing was paused, then after
 * resuming it generates one event with the {@link ECrudOp#LIST} operation.
 *
 * @author hazard157
 * @param <L> - listener interface
 * @param <T> - items type
 * @param <S> - type of the events source object
 */
public abstract class AbstractSimpleCrudOpTsEventer<L, T, S>
    extends AbstractTsEventer<L> {

  private final S source;

  private ECrudOp op   = null;
  private T       item = null;

  /**
   * Constructor.
   *
   * @param aSource &lt;S&gt; - the source of the events, may be null
   */
  public AbstractSimpleCrudOpTsEventer( S aSource ) {
    source = aSource;
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsEventer
  //

  @Override
  protected boolean doIsPendingEvents() {
    return op != null;
  }

  @Override
  protected void doFirePendingEvents() {
    for( L l : listeners() ) {
      doReallyFireEvent( l, source, op, item );
    }
  }

  @Override
  protected void doClearPendingEvents() {
    op = null;
    item = null;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Fires an event either immediately or after firing is resumed.
   *
   * @param aOp {@link ECrudOp} - the operation kind
   * @param aItem &lt;T&gt; - the item affected by the operation, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException item is <code>null</code> while operation is not {@link ECrudOp#LIST}
   */
  public void fireEvent( ECrudOp aOp, T aItem ) {
    TsNullArgumentRtException.checkNull( aOp );
    TsIllegalArgumentRtException.checkTrue( aItem == null && aOp != ECrudOp.LIST );
    if( !isFiringPaused() ) {
      for( L l : listeners() ) {
        doReallyFireEvent( l, source, op, item );
      }
    }
    // save event to fire after resume
    if( op == null ) { // first event to remember?
      op = aOp;
      item = aItem;
    }
    else { // second and next events to remember
      op = ECrudOp.LIST;
      item = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must call the listener with the specified arguments.
   *
   * @param aListener &lt;L&gt; - the listener to call
   * @param aSource &lt;S&gt; - the event source object
   * @param aOp {@link ECrudOp} - the operation kind
   * @param aItem &lt;T&gt; - the item affected by the operation, or <code>null</code> for batch operations
   */
  protected abstract void doReallyFireEvent( L aListener, S aSource, ECrudOp aOp, T aItem );

}
