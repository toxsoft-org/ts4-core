package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый класс для реализации всех "представлений" компонент.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractComponentView
    implements IVedComponentView, IVedContextable {

  boolean disposed = false;

  private final VedAbstractComponent owner;

  /**
   * Конструктор.<br>
   *
   * @param aOwner VedAbstractComponent - родительская компонента (модель) не <b>null</b>
   */
  public VedAbstractComponentView( VedAbstractComponent aOwner ) {
    owner = TsNullArgumentRtException.checkNull( aOwner );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IStridable}
  //

  @Override
  public final String id() {
    return owner.id();
  }

  @Override
  public String nmName() {
    return owner.nmName();
  }

  @Override
  public String description() {
    return owner.description();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedDisposable}
  //

  @Override
  public final boolean isDisposed() {
    return disposed;
  }

  @Override
  public final void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedComponentView}
  //

  @Override
  public abstract IVedPainter painter();

  @Override
  public abstract IVedPorter porter();

  @Override
  public abstract IVedOutline outline();

  @Override
  public final IVedComponent owner() {
    return owner;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedContextable}
  //

  @Override
  public ITsGuiContext tsContext() {
    return owner.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //
  protected abstract void doDispose();

}
