package org.toxsoft.tsgui.panels;

import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.stdevents.*;
import org.toxsoft.tsgui.bricks.stdevents.impl.TsDoubleClickEventHelper;
import org.toxsoft.tsgui.bricks.stdevents.impl.TsSelectionChangeEventHelper;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Абстрактный класс панелей просмотра / редактирования набора объектов.
 * <p>
 * Облегчает реализацию панелью интерфейсов {@link ITsSelectionChangeEventProducer} и
 * {@link ITsDoubleClickEventProducer}.
 *
 * @author goga
 * @param <E> - тип объектов в наборе
 */
public abstract class TsStdEventsProducerPanel<E>
    extends TsPanel
    implements ITsSelectionProvider<E>, ITsDoubleClickEventProducer<E> {

  protected final TsSelectionChangeEventHelper<E> selectionChangeEventHelper;
  protected final TsDoubleClickEventHelper<E>     doubleClickEventHelper;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public TsStdEventsProducerPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //

  /**
   * Генерирует сообщение {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)}.
   *
   * @param aItem E - выбранный элемент, может быть null
   */
  public void fireTsSelectionEvent( E aItem ) {
    selectionChangeEventHelper.fireTsSelectionEvent( aItem );
  }

  /**
   * Генерирует сообщение {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)}.
   *
   * @param aItem E - выбранный элемент, может быть null
   */
  public void fireTsDoubleClickEvent( E aItem ) {
    doubleClickEventHelper.fireTsDoublcClickEvent( aItem );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionChangeEventProducer
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<E> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<E> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<E> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<E> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

}
