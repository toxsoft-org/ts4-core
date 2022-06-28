package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Класс делегирующий обработку событий мыши установленному обработчику.
 * <p>
 *
 * @author vs
 */
public class VedScreenMouseDelegator
    implements IVedDisposable {

  MouseListener mouseListener = new MouseListener() {

    @Override
    public void mouseUp( MouseEvent aEvent ) {
      mouseHandler.onMouseUp( aEvent );
    }

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      mouseHandler.onMouseDown( aEvent );
    }

    @Override
    public void mouseDoubleClick( MouseEvent aEvent ) {
      mouseHandler.onMouseDoubleClick( aEvent );
    }
  };

  MouseMoveListener mouseMoveListener = aEvent -> this.mouseHandler.onMouseMove( aEvent );

  MouseTrackListener mouseTrackListener = new MouseTrackListener() {

    @Override
    public void mouseHover( MouseEvent aEvent ) {
      mouseHandler.onMouseHover( aEvent );
    }

    @Override
    public void mouseExit( MouseEvent aEvent ) {
      mouseHandler.onMouseExit();
    }

    @Override
    public void mouseEnter( MouseEvent aEvent ) {
      mouseHandler.onMouseEnter();
    }
  };

  MouseWheelListener mouseWheelListener = aEvent -> this.mouseHandler.onMouseScrolled( aEvent );

  /**
   * Холст рисования события мыши которого необходимо обрабатывать
   */
  private final VedScreen canvas;

  /**
   * Признак того, что все ресурсы были освобождены
   */
  boolean disposed = false;

  /**
   * Обработчик мыши.
   */
  private IVedMouseHandler mouseHandler = IVedMouseHandler.NULL;

  /**
   * Конструктор.<br>
   *
   * @param aCanvas Canvas - холст рисования
   */
  public VedScreenMouseDelegator( VedScreen aCanvas ) {
    canvas = aCanvas;
    canvas.addMouseListener( mouseListener );
    canvas.addMouseMoveListener( mouseMoveListener );
    canvas.addMouseTrackListener( mouseTrackListener );
    canvas.addMouseWheelListener( mouseWheelListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedDisposabe}
  //

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    if( !disposed ) {
      disposed = true;
      if( !canvas.isDisposed() ) {
        canvas.removeMouseListener( mouseListener );
        canvas.removeMouseMoveListener( mouseMoveListener );
        canvas.removeMouseTrackListener( mouseTrackListener );
        canvas.removeMouseWheelListener( mouseWheelListener );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает обработчик мыши.<br>
   *
   * @return IMouseHandler - обработчик мыши
   */
  IVedMouseHandler mouseHandler() {
    return mouseHandler;
  }

  /**
   * Устанавливает обработчик мыши.<br>
   *
   * @param aMouseHandler IMouseHandler - обработчик мыши м.б. {@link IMouseHandler#NULL}
   * @throws TsNullArgumentRtException - если аргумент null
   */
  public void setMouseHandler( IVedMouseHandler aMouseHandler ) {
    mouseHandler = TsNullArgumentRtException.checkNull( aMouseHandler );
  }

}
