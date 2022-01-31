package org.toxsoft.core.tsgui.panels;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Базовый класс панели в приложениях с контекстом {@link ITsGuiContext}.
 *
 * @author goga
 */
public class TsPanel
    extends TsComposite
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public TsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent aE ) {
        doDispose();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContext
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Вызывается при уничтожении компоненты, наследники могут освободить занятые ресурсы.
   * <p>
   * В базовом классе {@link TsPanel} ничего не делает.
   */
  protected void doDispose() {
    // nop
  }

}
