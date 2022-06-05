package org.toxsoft.core.tsgui.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link Canvas} extension with {@link ITsGuiContextable}.
 *
 * @author hazard157
 */
public abstract class TsAbstractCanvas
    extends Canvas
    implements ITsGuiContextable {

  private final PaintListener paintListener = new PaintListener() {

    @Override
    public void paintControl( PaintEvent aEvent ) {
      whenPaint( aEvent );
    }
  };

  private final MouseListener mouseListener = new MouseAdapter() {

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      doMouseDown( aEvent );
    }

    @Override
    public void mouseDoubleClick( MouseEvent aEvent ) {
      doMouseDoubleClick( aEvent );
    }

  };

  private final Listener keyListener = new Listener() {

    @Override
    public void handleEvent( Event aEvent ) {
      doKeyPressed( aEvent );
    }

  };

  private final ControlListener resizeListener = new ControlAdapter() {

    @Override
    public void controlResized( ControlEvent aEvent ) {
      doControlResized( aEvent );
    }
  };

  private final ITsGuiContext tsContext;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст с параметрами
   * @param aParent {@link Composite} - родительская панель
   */
  public TsAbstractCanvas( ITsGuiContext aContext, Composite aParent ) {
    super( aParent, SWT.NONE );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    addPaintListener( paintListener );
    addMouseListener( mouseListener );
    addControlListener( resizeListener );
    addListener( SWT.KeyDown, keyListener );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  final void whenPaint( PaintEvent aEvent ) {
    doPaint( aEvent.gc );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  /**
   * Возвращает контекст панели.
   *
   * @return {@link ITsGuiContext} - контекст панели
   */
  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // Дле реализации наследниками
  //

  protected abstract void doPaint( GC aGc );

  @SuppressWarnings( "unused" )
  protected void doMouseDown( MouseEvent aEvent ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doMouseDoubleClick( MouseEvent aEvent ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doKeyPressed( Event aEvent ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doControlResized( ControlEvent aEvent ) {
    // nop
  }

}
