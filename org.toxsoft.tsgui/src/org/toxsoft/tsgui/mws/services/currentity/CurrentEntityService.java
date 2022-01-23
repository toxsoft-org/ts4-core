package org.toxsoft.tsgui.mws.services.currentity;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link ICurrentEntityService}.
 *
 * @author goga
 * @param <E> - тип этого "текущего чего-то"
 */
public class CurrentEntityService<E>
    implements ICurrentEntityService<E> {

  private final IListEdit<ICurrentEntityChangeListener<E>> listeners = new ElemLinkedBundleList<>();
  private final IEclipseContext                            appContext;
  private E                                                current   = null;

  /**
   * Пустой конструктор.
   */
  public CurrentEntityService() {
    appContext = null;
  }

  /**
   * Конструктор.
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public CurrentEntityService( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void fireEntityChangedEvent() {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ICurrentEntityChangeListener<E>> ll = new ElemArrayList<>( listeners );
    for( ICurrentEntityChangeListener<E> l : ll ) {
      l.onCurrentEntityChanged( current );
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
  // Методы для наследников
  //

  /**
   * Возвращает контекст приложения.
   *
   * @return {@link IEclipseContext} - контекст приложения, может быть null
   */
  public IEclipseContext appContext() {
    return appContext;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник может отработать факт изменения ссылки на текущую сущность.
   * <p>
   * В отличие от {@link #afterListenersInformed()}, этот метод вызывается <b>до</b> того, как будут вызваны слушатели
   * изменения текущего значения.
   *
   * @param aOld &lt;E&gt; - старая ссылка, может быть <code>null</code>
   * @param aNew &lt;E&gt; - новая ссылка, может быть <code>null</code>
   */
  protected void onCurrentChanged( E aOld, E aNew ) {
    // nop
  }

  /**
   * Наследник может отработать факт изменения ссылки на текущую сущность.
   * <p>
   * В отличие от {@link #onCurrentChanged(Object, Object)}, этот метод вызывается <b>после</b> того, как будут вызваны
   * слушатели изменения текущего значения.
   */
  protected void afterListenersInformed() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICurrentEpisodeService
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
      onCurrentChanged( old, current );
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
