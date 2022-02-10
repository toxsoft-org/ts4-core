package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tsgui.panels.vecboard.EVecLayoutKind;
import org.toxsoft.core.tsgui.panels.vecboard.IVecLayout;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый класс всех раскладок, реализующих {@link IVecLayout}.
 *
 * @author goga
 * @param <D> - конкретный тип параметров
 */
abstract class AbstractVecLayout<D>
    implements IVecLayout<D> {

  protected static class Item<D> {

    private final ILazyControl<?> cb;
    private final D               layoutData;

    Item( ILazyControl<?> aControlBuilder, D aLayoutData ) {
      cb = aControlBuilder;
      layoutData = aLayoutData;
    }

    public ILazyControl<?> cb() {
      return cb;
    }

    public D layoutData() {
      return layoutData;
    }

  }

  private final IListEdit<Item<D>> items = new ElemLinkedBundleList<>();

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  final Composite createWidget( Composite aParent ) {
    Composite c = doCreateComposite( aParent );
    fillComposite( c );
    c.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent e ) {
        onDispose();
      }
    } );
    return c;
  }

  final IList<Item<D>> items() {
    return items;
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //

  protected void addItem( ILazyControl<?> aControlBuilder, D aLayoutData ) {
    items.add( new Item<>( aControlBuilder, aLayoutData ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILayout
  //

  @Override
  public abstract EVecLayoutKind layoutKind();

  @Override
  public final void addControl( ILazyControl<?> aControlBuilder, D aLayoutData ) {
    TsNullArgumentRtException.checkNulls( aControlBuilder, aLayoutData );
    TsIllegalArgumentRtException.checkTrue( aControlBuilder.getControl() != null );
    doCheckAddControl( aControlBuilder, aLayoutData );
    addItem( aControlBuilder, aLayoutData );
  }

  // ------------------------------------------------------------------------------------
  // Методы для реализации наследниками
  //

  /**
   * Наследник может создать спеуиальную компоненту вместо стандартной {@link Composite}.
   * <p>
   * Например, {@link EVecLayoutKind#SASH} создает {@link Sash}, а {@link EVecLayoutKind#TABS} компонент
   * {@link TabFolder}.
   *
   * @param aParent {@link Composite} - родительская панель
   * @return {@link Composite} - созданный композит
   */
  protected Composite doCreateComposite( Composite aParent ) {
    return new Composite( aParent, SWT.NONE );
  }

  /**
   * Вызывается, когда уничтожается виджет этой раскладки.
   * <p>
   * В базовом класссе ничего не делает, не нужно вызывать из наследника.
   */
  protected void onDispose() {
    // nop
  }

  /**
   * Наследник в этом методе должен создать дочерные контроли.
   *
   * @param aParent {@link Composite} - родительская панель
   */
  protected abstract void fillComposite( Composite aParent );

  /**
   * Наследник должен проверить возможность добавления контроля с указнными аргументами.
   * <p>
   * В базовом класссе ничего не делает, не нужно вызывать из наследника.
   *
   * @param aControlBuilder {@link ILazyControl} - создатель контроля, не бывает null
   * @param aLayoutData D - параметры раскладки, не бывает null
   * @throws TsRuntimeException при навозможности добавить контроль
   */
  protected void doCheckAddControl( ILazyControl<?> aControlBuilder, D aLayoutData ) {
    // nop
  }

}
