package org.toxsoft.tslib.bricks.events.change;

import org.toxsoft.tslib.bricks.events.AbstractTsEventer;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * An {@link IGenericChangeEventer} impementation.
 * <p>
 * Also implements {@link IGenericChangeListener}, simply resending events.
 *
 * @author hazard157
 */
public class GenericChangeEventer
    extends AbstractTsEventer<IGenericChangeListener>
    implements IGenericChangeEventer, IGenericChangeListener, ITsCollectionChangeListener {

  private Object source;

  private boolean wasEvent = false;

  /**
   * Constructor.
   *
   * @param aSource Object - the event source
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public GenericChangeEventer( Object aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    source = aSource;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void reallyFireEvent() {
    IList<IGenericChangeListener> ll = listeners();
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      IGenericChangeListener l = ll.get( i );
      try {
        l.onGenericChangeEvent( source );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsEventsFiringSupport
  //

  @Override
  protected boolean doIsPendingEvents() {
    return wasEvent;
  }

  @Override
  protected void doFirePendingEvents() {
    reallyFireEvent();
  }

  @Override
  protected void doClearPendingEvents() {
    wasEvent = false;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeListener
  //

  @Override
  public void onGenericChangeEvent( Object aSource ) {
    fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollectionChangeListener
  //

  @Override
  public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
    fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Fires or stores an event depending on {@link #isFiringPaused()} state.
   */
  public void fireChangeEvent() {
    if( isFiringPaused() ) {
      wasEvent = true;
      return;
    }
    reallyFireEvent();
  }

}
