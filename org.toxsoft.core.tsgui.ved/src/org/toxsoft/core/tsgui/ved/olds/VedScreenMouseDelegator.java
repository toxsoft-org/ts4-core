package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.olds.*;
import org.toxsoft.core.tsgui.ved.olds.drag.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Класс делегирующий обработку событий мыши установленному обработчику.
 * <p>
 *
 * @author vs
 */
public class VedScreenMouseDelegator {

  MouseListener mouseListener = new MouseListener() {

    @Override
    public void mouseUp( MouseEvent aEvent ) {
      mouseHandler.mouseUp( aEvent );
    }

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      mouseHandler.mouseDown( aEvent );
    }

    @Override
    public void mouseDoubleClick( MouseEvent aEvent ) {
      mouseHandler.mouseDoubleClick( aEvent );
    }
  };

  MouseMoveListener mouseMoveListener = aEvent -> this.mouseHandler.mouseMove( aEvent );

  MouseTrackListener mouseTrackListener = new MouseTrackListener() {

    @Override
    public void mouseHover( MouseEvent aEvent ) {
      mouseHandler.mouseHover( aEvent );
    }

    @Override
    public void mouseExit( MouseEvent aEvent ) {
      mouseHandler.mouseExit( aEvent );
    }

    @Override
    public void mouseEnter( MouseEvent aEvent ) {
      mouseHandler.mouseEnter( aEvent );
    }
  };

  MouseWheelListener mouseWheelListener = aEvent -> this.mouseHandler.mouseScrolled( aEvent );

  /**
   * Холст рисования события мыши которого необходимо обрабатывать
   */
  private final Canvas canvas;

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
  public VedScreenMouseDelegator( Canvas aCanvas ) {
    canvas = aCanvas;
    canvas.addMouseListener( mouseListener );
    canvas.addMouseMoveListener( mouseMoveListener );
    canvas.addMouseTrackListener( mouseTrackListener );
    canvas.addMouseWheelListener( mouseWheelListener );
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
