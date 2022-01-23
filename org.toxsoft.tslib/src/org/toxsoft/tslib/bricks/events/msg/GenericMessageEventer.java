package org.toxsoft.tslib.bricks.events.msg;

import org.toxsoft.tslib.bricks.events.AbstractTsEventer;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.derivative.IQueue;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * An {@link IGenericMessageEventer} impementation.
 * <p>
 * Also implements {@link IGenericMessageListener}, simply resending events.
 *
 * @author hazard157
 */
public class GenericMessageEventer
    extends AbstractTsEventer<IGenericMessageListener>
    implements IGenericMessageEventer, IGenericMessageListener {

  private IQueue<GenericMessage> queue;

  /**
   * Constructor.
   */
  public GenericMessageEventer() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void reallyFireEvent( GenericMessage aMessage ) {
    IList<IGenericMessageListener> ll = listeners();
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      IGenericMessageListener l = ll.get( i );
      try {
        l.onGenericMessage( aMessage );
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
    return !queue.isEmpty();
  }

  @Override
  protected void doFirePendingEvents() {
    while( !queue.isEmpty() ) {
      GenericMessage msg = queue.getHead();
      reallyFireEvent( msg );
    }
  }

  @Override
  protected void doClearPendingEvents() {
    queue.clear();
  }

  // ------------------------------------------------------------------------------------
  // IGenericMessageListener
  //

  @Override
  public void onGenericMessage( GenericMessage aMessage ) {
    fireMessageEvent( aMessage );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Fires or stores an event depending on {@link #isFiringPaused()} state.
   *
   * @param aMessage {@link GenericMessage} - the message
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void fireMessageEvent( GenericMessage aMessage ) {
    TsNullArgumentRtException.checkNull( aMessage );
    if( isFiringPaused() ) {
      queue.putTail( aMessage );
      return;
    }
    reallyFireEvent( aMessage );
  }

}
