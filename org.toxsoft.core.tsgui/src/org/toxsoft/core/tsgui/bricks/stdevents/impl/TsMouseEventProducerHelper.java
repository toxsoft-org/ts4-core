package org.toxsoft.core.tsgui.bricks.stdevents.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.rcp.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsMouseListener.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Вспомогтельный класс для облегчения реализации {@link ITsMouseEventProducer}.
 *
 * @author hazard157
 */
public class TsMouseEventProducerHelper
    implements ITsMouseEventProducer, MouseListener, ISingleSourcing_MouseWheelListener {

  final IListEdit<ITsMouseListener> listeners   = new ElemLinkedBundleList<>();
  private Object                    source;
  private Control                   bindControl = null;

  /**
   * Создает помощник с привязкой к источнику сообщении.
   *
   * @param aSource Object - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsMouseEventProducerHelper( Object aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    source = aSource;
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private ITsPoint pointFromEvent( MouseEvent aEvent ) {
    Display display = bindControl.getDisplay();
    Point p = display.map( bindControl, null, aEvent.x, aEvent.y );
    return new TsPoint( p.x, p.y );
  }

  private static EMouseButton buttonFromEvent( MouseEvent aEvent ) {
    switch( aEvent.button ) {
      case 1:
        return EMouseButton.LEFT;
      case 2:
        return EMouseButton.MIDDLE;
      case 3:
        return EMouseButton.RIGHT;
      default:
        return EMouseButton.OTHER;
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса MouseListener
  //

  @Override
  public void mouseDoubleClick( MouseEvent e ) {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsMouseListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener l = ll.get( i );
      try {
        l.onMouseDoubleClick( source, pointFromEvent( e ) );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  @Override
  public void mouseDown( MouseEvent e ) {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsMouseListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener l = ll.get( i );
      try {
        l.onMouseButtonDown( source, buttonFromEvent( e ), pointFromEvent( e ) );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  @Override
  public void mouseUp( MouseEvent e ) {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsMouseListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener l = ll.get( i );
      try {
        l.onMouseButtonUp( source, buttonFromEvent( e ), pointFromEvent( e ) );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ISingleSourcing_MouseWheelListener
  //

  @Override
  public void mouseScrolled( MouseEvent e ) {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsMouseListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener l = ll.get( i );
      try {
        l.onMouseWheelEvent( source, e.count );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsMouseEventProducer
  //

  @Override
  public void addTsMouseListener( ITsMouseListener aListener ) {
    listeners.add( aListener );
  }

  @Override
  public void removeTsMouseListener( ITsMouseListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Привязывает помощник к SWT-контроли, реально генерирующей SWT-события мыши.
   *
   * @param aBindControl {@link Control} - SWT-контроль, реально генерирующей SWT-события мыши.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException контроле уже уничтожен
   * @throws TsIllegalStateRtException повторный вызов метода без предварительного вызова {@link #unbind()}
   */
  public void bindToControl( Control aBindControl ) {
    TsNullArgumentRtException.checkNull( aBindControl );
    TsIllegalStateRtException.checkNoNull( bindControl );
    TsIllegalArgumentRtException.checkTrue( aBindControl.isDisposed() );
    bindControl = aBindControl;
    bindControl.addMouseListener( this );
    TsSinglesourcingUtils.Control_addMouseWheelListener( bindControl, this );
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
      bindControl.removeMouseListener( this );
      TsSinglesourcingUtils.Control_removeMouseWheelListener( bindControl, this );
      bindControl = null;
    }
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

}
