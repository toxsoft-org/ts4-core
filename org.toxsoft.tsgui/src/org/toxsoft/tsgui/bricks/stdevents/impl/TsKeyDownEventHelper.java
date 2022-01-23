package org.toxsoft.tsgui.bricks.stdevents.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.bricks.stdevents.ITsKeyDownEventProducer;
import org.toxsoft.tsgui.bricks.stdevents.ITsKeyEventListener;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Вспомогтельный класс для облегчения реализации {@link ITsKeyEventListener}.
 *
 * @author goga
 */
public class TsKeyDownEventHelper
    implements ITsKeyDownEventProducer, ITsKeyEventListener, Listener {

  private final IListEdit<ITsKeyEventListener> listeners = new ElemLinkedBundleList<>();

  private Object  source;
  private Control bindControl = null;

  /**
   * Создает помощник с привязкой к источнику сообщении.
   *
   * @param aSource Object - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsKeyDownEventHelper( Object aSource ) {
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
    bindControl.addListener( SWT.KeyDown, this );
    bindControl.addDisposeListener( e -> unbind() );
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
      bindControl.removeListener( SWT.KeyDown, this );
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
  public boolean fireTsKeyDownEvent( Event aEvent ) {
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
    fireTsKeyDownEvent( event );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsDoubleClickEventProducer
  //

  @Override
  public void addTsKeyDownListener( ITsKeyEventListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeTsKeyDownListener( ITsKeyEventListener aListener ) {
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
    return fireTsKeyDownEvent( aEvent );
  }

}
