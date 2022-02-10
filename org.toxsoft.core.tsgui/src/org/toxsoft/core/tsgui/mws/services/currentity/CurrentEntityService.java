package org.toxsoft.core.tsgui.mws.services.currentity;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

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
   * Subclass may handle current entity change.
   * <p>
   * This method is called after current entity changed but before listeners are informed.
   *
   * @param aOld &lt;E&gt; - old value of {@link #current()} may be <code>null</code>
   * @param aNew &lt;E&gt; - new value of {@link #current()} may be <code>null</code>
   */
  protected void beforeListenersInformed( E aOld, E aNew ) {
    // nop
  }

  /**
   * Subclass may handle current entity change.
   * <p>
   * This method is called after current entity changed and after listeners were informed.
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
      current = aCurrent;
      beforeListenersInformed( old, current );
      fireEntityChangedEvent();
      afterListenersInformed();
    }
  }

  @Override
  public void informOnContentChange() {
    fireContentChangedEvent();
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
