package org.toxsoft.core.tsgui.bricks.stdevents.impl;

import org.toxsoft.core.tsgui.bricks.stdevents.ITsDoubleClickEventProducer;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsDoubleClickListener;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

//TODO TRANSLATE

/**
 * Вспомогтельный класс для облегчения реализации {@link ITsDoubleClickEventProducer}.
 *
 * @author hazard157
 * @param <E> - конкретный тип элементов
 */
public class TsDoubleClickEventHelper<E>
    implements ITsDoubleClickEventProducer<E>, ITsDoubleClickListener<E> {

  private final IListEdit<ITsDoubleClickListener<E>> listeners = new ElemLinkedBundleList<>();
  private Object                                     source;

  /**
   * Создает помощник с привязкой к источнику сообщении.
   *
   * @param aSource Object - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsDoubleClickEventHelper( Object aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Генерирует сообщение {@link ITsDoubleClickListener#onTsDoubleClick(Object, Object)}.
   *
   * @param aSelected E - выбранный элемент, может быть null
   */
  public void fireTsDoublcClickEvent( E aSelected ) {
    if( beforeDoubleClickEventFired( aSelected ) ) {
      return;
    }
    if( !listeners.isEmpty() ) {
      IList<ITsDoubleClickListener<E>> ll = new ElemArrayList<>( listeners );
      for( ITsDoubleClickListener<E> l : ll ) {
        try {
          l.onTsDoubleClick( source, aSelected );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }
    afterDoubleClickEventFired( aSelected );
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
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<E> aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<E> aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickListener
  //

  /**
   * {@inheritDoc}
   * <p>
   * Просто транслирует сообщение с заменой источника сообщения.
   */
  @Override
  public void onTsDoubleClick( Object aSource, E aSelectedItem ) {
    fireTsDoublcClickEvent( aSelectedItem );
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
  protected boolean beforeDoubleClickEventFired( E aItem ) {
    return false;
  }

  /**
   * Вызывается после извещения всех слушателей.
   * <p>
   * Метод <b>не</b> вызывается, если извещения были отменены методом {@link #beforeDoubleClickEventFired(Object)}.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   *
   * @param aItem &lt;E&gt; - выбранный элемент, может быть <code>null</code>
   */
  protected void afterDoubleClickEventFired( E aItem ) {
    // nop
  }

}
