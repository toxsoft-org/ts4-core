package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMpcPaneBase;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMultiPaneModownComponent;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Базовый класс всех панелей компоненты {@link IMultiPaneModownComponent}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 * @param <C> - used SWT control type
 */
public abstract class AbstractPane<T, C extends Control>
    implements IMpcPaneBase<T> {

  private final BasicMultiPaneComponent<T> owner;

  private C control = null;

  /**
   * Конструктор для наследников.
   *
   * @param aOwner {@link BasicMultiPaneComponent} - компонента, создающая эту панель
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public AbstractPane( BasicMultiPaneComponent<T> aOwner ) {
    owner = TsNullArgumentRtException.checkNull( aOwner );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILazyControl
  //

  @Override
  final public C createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( control );
    control = doCreateControl( aParent );
    TsInternalErrorRtException.checkNull( control );
    return control;
  }

  @Override
  final public C getControl() {
    return control;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPaneBase
  //

  /**
   * Возвращает родительскую компоненту.
   *
   * @return {@link BasicMultiPaneComponent} - компонента, создающая эту панель
   */
  @Override
  public BasicMultiPaneComponent<T> owner() {
    return owner;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения
  //

  protected abstract C doCreateControl( Composite aParent );

}
