package org.toxsoft.tsgui.bricks.stdevents.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.singlesrc.rcp.ISingleSourcing_MouseWheelListener;
import org.toxsoft.singlesrc.rcp.TsSinglesourcingUtils;
import org.toxsoft.tsgui.bricks.stdevents.ITsMouseEventProducer;
import org.toxsoft.tsgui.bricks.stdevents.ITsMouseListener;
import org.toxsoft.tsgui.bricks.stdevents.ITsMouseListener.EMouseButton;
import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * Вспомогтельный класс для облегчения реализации {@link ITsMouseEventProducer}.
 *
 * @author goga
 * @param <S> - тип источника сообщения
 */
public class TsMouseEventProducerHelper<S>
    implements ITsMouseEventProducer<S>, MouseListener, ISingleSourcing_MouseWheelListener {

  final IListEdit<ITsMouseListener<S>> listeners   = new ElemLinkedBundleList<>();
  private S                            source;
  private Control                      bindControl = null;

  /**
   * Создает помощник с привязкой к источнику сообщении.
   *
   * @param aSource &lt;S&gt; - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsMouseEventProducerHelper( S aSource ) {
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
    IList<ITsMouseListener<S>> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener<S> l = ll.get( i );
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
    IList<ITsMouseListener<S>> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener<S> l = ll.get( i );
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
    IList<ITsMouseListener<S>> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener<S> l = ll.get( i );
      try {
        l.onMouseButtonUp( source, buttonFromEvent( e ), pointFromEvent( e ) );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISingleSourcing_MouseWheelListener
  //

  @Override
  public void mouseScrolled( MouseEvent e ) {
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsMouseListener<S>> ll = new ElemArrayList<>( listeners );
    for( int i = 0, n = ll.size(); i < n; i++ ) {
      ITsMouseListener<S> l = ll.get( i );
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
  public void addTsMouseListener( ITsMouseListener<S> aListener ) {
    listeners.add( aListener );
  }

  @Override
  public void removeTsMouseListener( ITsMouseListener<S> aListener ) {
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
      bindControl.removeMouseListener( this );
      TsSinglesourcingUtils.Control_removeMouseWheelListener( bindControl, this );
      bindControl = null;
    }
  }

  /**
   * Меняет ссылку на источник сообщения.
   *
   * @param aSource &lt;S&gt; - истчоник сообщении
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setSource( S aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

}
