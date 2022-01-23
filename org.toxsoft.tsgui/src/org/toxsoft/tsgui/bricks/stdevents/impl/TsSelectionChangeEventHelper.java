package org.toxsoft.tsgui.bricks.stdevents.impl;

import org.toxsoft.tsgui.bricks.stdevents.ITsSelectionChangeEventProducer;
import org.toxsoft.tsgui.bricks.stdevents.ITsSelectionChangeListener;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

// TODO TRANSLATE

/**
 * Вспомогтельный класс для облегчения реализации {@link ITsSelectionChangeEventProducer}.
 *
 * @author goga
 * @param <E> - конкретный тип элементов
 */
public class TsSelectionChangeEventHelper<E>
    implements ITsSelectionChangeEventProducer<E>, ITsSelectionChangeListener<E> {

  private final IListEdit<ITsSelectionChangeListener<E>> listeners = new ElemLinkedBundleList<>();
  private Object                                         source;

  /**
   * Создает помощник с привязкой к источнику сообщении.
   *
   * @param aSource Object - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsSelectionChangeEventHelper( Object aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Генерирует сообщение {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)}.
   *
   * @param aItem &lt;E&gt; - выбранный элемент, может быть <code>null</code>
   */
  public void fireTsSelectionEvent( E aItem ) {
    if( beforeSelectionEventFired( aItem ) ) {
      return;
    }
    if( !listeners.isEmpty() ) {
      IList<ITsSelectionChangeListener<E>> ll = new ElemArrayList<>( listeners );
      for( ITsSelectionChangeListener<E> l : ll ) {
        try {
          l.onTsSelectionChanged( source, aItem );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }
    afterSelectionEventFired( aItem );
  }

  /**
   * Определяет, что список зарегистрированных обработчиков пустой.
   *
   * @return boolean - признак отсутствия зарегистрированных обработчиков
   */
  public boolean isEmpty() {
    return listeners.isEmpty();
  }

  /**
   * Меняет ссылку на источник сообщения.
   *
   * @param aSource Object - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setSource( Object aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionChangeEventProducer
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<E> aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<E> aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionChangeListener
  //

  /**
   * {@inheritDoc}
   * <p>
   * Просто транслирует сообщение с заменой источника сообщения.
   */
  @Override
  public void onTsSelectionChanged( Object aSource, E aSelectedItem ) {
    fireTsSelectionEvent( aSelectedItem );
  }

  // ------------------------------------------------------------------------------------
  // Для переопределения наследниками
  //

  /**
   * Вызывается перед извещением слушателей.
   * <p>
   * Если метод вернет <code>true</code>, то слушатели не будут извещены.
   * <p>
   * В базовом классе просто возвращает <code>false</code>, при переопределении вызывать родительский метод не нужно.
   *
   * @param aItem &lt;E&gt; - выбранный элемент, может быть <code>null</code>
   * @return boolean - признак отказа от извещения слушателей
   */
  protected boolean beforeSelectionEventFired( E aItem ) {
    return false;
  }

  /**
   * Вызывается после извещения всех слушателей.
   * <p>
   * Метод <b>не</b> вызывается, если извещения были отменены методом {@link #beforeSelectionEventFired(Object)}.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   *
   * @param aItem &lt;E&gt; - выбранный элемент, может быть <code>null</code>
   */
  protected void afterSelectionEventFired( E aItem ) {
    // nop
  }

}
