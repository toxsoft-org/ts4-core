package org.toxsoft.tsgui.bricks.stdevents.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.bricks.stdevents.ITsKeyEventListener;
import org.toxsoft.tsgui.bricks.stdevents.ITsKeyUpEventProducer;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Вспомогтельный класс для облегчения реализации {@link ITsKeyEventListener}.
 *
 * @author goga
 */
public class TsKeyUpEventHelper
    implements ITsKeyUpEventProducer, ITsKeyEventListener, Listener {

  private final IListEdit<ITsKeyEventListener> listeners = new ElemLinkedBundleList<>();

  private Object  source;
  private Control bindControl = null;

  /**
   * Создает помощник с привязкой к источнику сообщении.
   *
   * @param aSource Object - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsKeyUpEventHelper( Object aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Привязывает помощник к SWT-контроли, реально генерирующей SWT-события нажатия клавиш.
   *
   * @param aBindControl {@link Control} - SWT-контроль, реально генерирующей SWT-события нажатия клавиш.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException контроле уже уничтожен
   * @throws TsIllegalStateRtException повторный вызов метода без предварительного вызова {@link #unbind()}
   */
  public void bindToControl( Control aBindControl ) {
    TsNullArgumentRtException.checkNull( aBindControl );
    TsIllegalStateRtException.checkNoNull( bindControl );
    TsIllegalArgumentRtException.checkTrue( aBindControl.isDisposed() );
    bindControl = aBindControl;
    bindControl.addListener( SWT.KeyUp, this );
    bindControl.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent e ) {
        unbind();
      }
    } );
  }

  /**
   * Отменяет привязку, сделанную методом {@link #bindToControl(Control)}.
   * <p>
   * Этот метод вызывается автоматически перед уничтожением контроли.
   * <p>
   * Повторный вызов метода безопасен.
   */
  public void unbind() {
    if( bindControl != null ) {
      bindControl.removeListener( SWT.KeyUp, this );
      bindControl = null;
    }
  }

  /**
   * Извешает всех слушателей нажатия клавиши.
   * <p>
   * Извещение прекращается, как только один из слушателей вренет <code>true</code>.
   *
   * @param aEvent {@link Event} - событие клавиатуры
   * @return boolean - <code>true</code> тогда и тольо тогда, когда один обработчик вернул <code>true</code>
   */
  public boolean fireTsKeyUpEvent( Event aEvent ) {
    if( !listeners.isEmpty() ) {
      IList<ITsKeyEventListener> ll = new ElemArrayList<>( listeners );
      for( ITsKeyEventListener l : ll ) {
        if( l.onTsKeyEvent( source, aEvent ) ) {
          return true;
        }
      }
    }
    return false;
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
  // Реализация интерфейса Listener
  //

  @Override
  public void handleEvent( Event event ) {
    fireTsKeyUpEvent( event );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsDoubleClickEventProducer
  //

  @Override
  public void addTsKeyUpListener( ITsKeyEventListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeTsKeyUpListener( ITsKeyEventListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsKeyEventListener
  //

  /**
   * {@inheritDoc}
   * <p>
   * Просто транслирует сообщение с заменой источника сообщения.
   */
  @Override
  public boolean onTsKeyEvent( Object aSource, Event aEvent ) {
    return fireTsKeyUpEvent( aEvent );
  }

}
