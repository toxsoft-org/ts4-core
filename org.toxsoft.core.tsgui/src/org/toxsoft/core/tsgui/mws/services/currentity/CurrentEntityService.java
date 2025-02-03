package org.toxsoft.core.tsgui.mws.services.currentity;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link ICurrentEntityService} implementation.
 *
 * @author hazard157
 * @param <E> - type of "something"
 */
public class CurrentEntityService<E>
    implements ICurrentEntityService<E> {

  private final IListEdit<ICurrentEntityChangeListener<E>> listeners = new ElemLinkedBundleList<>();
  private final IEclipseContext                            appContext;
  private E                                                current   = null;

  /**
   * Constructor.
   */
  public CurrentEntityService() {
    appContext = null;
  }

  /**
   * Constructor.
   *
   * @param aAppContext {@link IEclipseContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CurrentEntityService( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void fireEntityChangedEvent() {
    if( !listeners.isEmpty() ) {
      IList<ICurrentEntityChangeListener<E>> ll = new ElemArrayList<>( listeners );
      for( ICurrentEntityChangeListener<E> l : ll ) {
        try {
          l.onCurrentEntityChanged( current );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }
  }

  void fireContentChangedEvent() {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ICurrentEntityChangeListener<E>> ll = new ElemArrayList<>( listeners );
    for( ICurrentEntityChangeListener<E> l : ll ) {
      l.onCurrentContentChanged( current );
    }
  }

  void fireAfterListenersEvent() {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ICurrentEntityChangeListener<E>> ll = new ElemArrayList<>( listeners );
    for( ICurrentEntityChangeListener<E> l : ll ) {
      l.afterListenersInformed();
    }
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the context, specified in constructor.
   *
   * @return {@link IEclipseContext} - the context or <code>null</code>
   */
  public IEclipseContext appContext() {
    return appContext;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may handle current entity change or even determine which value to set.
   * <p>
   * This method is called after current entity changed but before listeners are informed.
   * <p>
   * In base class simply returns <code>aNew</code>, there is no need to call superclass method when overriding.
   *
   * @param aOld &lt;E&gt; - old value of {@link #current()} may be <code>null</code>
   * @param aNew &lt;E&gt; - new value of {@link #current()} may be <code>null</code>
   * @return &lt;E&gt; - the value to be actually set as a new value
   */
  protected E beforeListenersInformed( E aOld, E aNew ) {
    return aNew;
  }

  /**
   * Subclass may perform additional actions after external listeners were called.
   * <p>
   * This method is called after current entity changed and after listeners were informed. Also all
   * {@link ICurrentEntityChangeListener#afterListenersInformed()} were called before invoking this method.
   * <p>
   * In base class does nothing, there is no need to call superclass method when overriding.
   */
  protected void afterListenersInformed() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ICurrentEpisodeService
  //

  @Override
  public E current() {
    return current;
  }

  @Override
  public void setCurrent( E aCurrent ) {
    if( aCurrent != current ) {
      E old = current;
      current = aCurrent; // set current to be available in next method
      current = beforeListenersInformed( old, current );
      fireEntityChangedEvent();
      fireAfterListenersEvent();
      afterListenersInformed();
    }
  }

  @Override
  public void informOnContentChange() {
    fireContentChangedEvent();
    fireAfterListenersEvent();
    afterListenersInformed();
  }

  @Override
  public void addCurrentEntityChangeListener( ICurrentEntityChangeListener<E> aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeCurrentEntityChangeListener( ICurrentEntityChangeListener<E> aListener ) {
    listeners.remove( aListener );
  }

}
