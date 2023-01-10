package org.toxsoft.core.tsgui.graphics.patterns;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый класс для создания "узоров" для заполнения областей при рисовании.
 *
 * @author vs
 */
public abstract class AbstractGradient
    implements IGradient, ITsGuiContextable {

  private final ITsGuiContext context;

  private boolean disposed = false;

  protected AbstractGradient( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    context = aContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return context;
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  /**
   * Возвращает признак того вызывался ли уже метод {@link #dispose()}
   *
   * @return <b>true</b> - метод уже был вызван<br>
   *         <b>false</b> - метод еще не вызывался
   */
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // to override
  //

  protected void doDispose() {
    // nop
  }

}
