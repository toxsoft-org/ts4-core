package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик "мыши".
 * <p>
 * В большинстве случаев необходимо обрабатывать все события "мыши", поэтому все java интерфейсы слушателей событий мыши
 * были сведены в один, для упрощения.<br>
 * Также помимо низкоуровневых событий типа MouseUp, MouseDown, MouseMove и т.п. есть необходимость обработки оработки
 * семантических событий таких как: MouseClicked, MouseIn и т.п.<br>
 * Также к семантическим событиям относятся события перетаскивания, которые представляют особый интерес для
 * "инструментов" редактора. Поэтому данный интерфейс расширяет {@link IDragEventProducer}.
 *
 * @author vs
 */
public interface IMouseHandler
    extends IDragEventProducer, IVedDisposable {

  /**
   * Нулевой (пустой) обработчик мыши, который ничего не делает
   */
  IMouseHandler NULL = new NullMouseHandler();

  /**
   * Левая кнопка мыши
   */
  int BTN_LEFT   = 1;
  /**
   * Средняя кнопка мыши
   */
  int BTN_MIDDLE = 2;
  /**
   * Правая кнопка мыши
   */
  int BTN_RIGHT  = 3;

  /**
   * Вызывается при двойном нажатии кнопи мыши. См {@link MouseListener#mouseDoubleClick(MouseEvent)}
   *
   * @param aEvent - информация о положениии курсора и состянии кнопок
   */
  default void onMouseDoubleClick( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается при нажатии кнопи мыши. См {@link MouseListener#mouseDown(MouseEvent)}
   *
   * @param aEvent - информация о положениии курсора и состянии кнопок
   */
  default void onMouseDown( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается при отпускании кнопи мыши. См {@link MouseListener#mouseUp(MouseEvent)}
   *
   * @param aEvent - информация о положениии курсора и состянии кнопок
   */
  default void onMouseUp( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается при перемещении курсора мыши. См {@link MouseMoveListener#mouseMove(MouseEvent)}
   *
   * @param aEvent - информация о положениии курсора и состянии кнопок
   */
  default void onMouseMove( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается при вращении колесика мыши. См {@link MouseWheelListener#mouseScrolled(MouseEvent)}
   *
   * @param aEvent MouseEvent - информация о положениии курсора и вращении колесика
   */
  default void onMouseScrolled( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается в момент появления курсора в области, за которую отвечает данный обработчик (Composite, Canvas ...). См
   * {@link MouseTrackListener#mouseEnter(MouseEvent)}
   */
  default void onMouseEnter() {
    // nop
  }

  /**
   * Вызывается в момент когда курсор покидает область, за которую отвечает данный обработчик (Composite, Canvas ...).
   * См {@link MouseTrackListener#mouseExit(MouseEvent)}
   */
  default void onMouseExit() {
    // nop
  }

  /**
   * Вызывается в момент когда курсор мыши останавливается и находится в покое определенное время. При этом неважно есть
   * ли под курсором какой-либо объект или нет. См {@link MouseTrackListener#mouseHover(MouseEvent)}
   *
   * @param aEvent MouseEvent - информация о положениии курсора
   */
  default void onMouseHover( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается в момент однократного "щелчка" мыши.<br>
   * Если в момент отпускания кнопки мыши зафиксирован "щелчок", то вместо {@linkplain #onMouseUp(MouseEvent)}
   * вызывается данный метод.
   *
   * @param aShape IScreenObject - объект на котором произошел щелчок или null если щелчок был на пустом месте
   * @param aEvent MouseEvent - информация о состоянии кнопок мыши, клавиатуры и т.д.
   */
  default void onClick( IScreenObject aShape, MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается в момент появления курсора в области объекта {@link IVedComponentView}
   *
   * @param aShape IVedComponentView - объект в области которого появился курсор
   */
  default void onObjectIn( IVedComponentView aShape ) {
    // nop
  }

  /**
   * Вызывается в момент когда курсор покидает область объекта {@link IVedComponentView}
   *
   * @param aShape IVedComponentView - объект в область которого покинул курсор
   */
  default void onObjectOut( IVedComponentView aShape ) {
    // nop
  }

  /**
   * Возвращает положение курсора мыши на экране.<br>
   *
   * @return IGeomPoint - точка положения курсора мыши на экране
   */
  ITsPoint cursorPosition();

  /**
   * Объект под курсором мыши.<br>
   * Перемещаясь по экрану, курсор время от времени находится над различными объектами. Вызывая данную функцию, можно
   * определить над каким объектом находится курсор в момент вызова. В случае если под курсором находятся несколько
   * объектов (накладывающиеся объекты), то возвращается первый в соотвествии с z-order.
   *
   * @return IVedComponentView - объект под курсором мыши или null если под курсором нет ни одного объекта
   */
  IVedComponentView hoveredObject();

  /**
   * Фигура содержащий указанную точку.<br>
   * В случае если несколько объектов (накладывающиеся объекты) содержат указанную точку, то возвращается первый в
   * соотвествии с z-order.
   *
   * @param aX int - x координата курсора в пикселях
   * @param aY int - y координата курсора в пикселях
   * @return IObject2d - объект содержащий указанную точку или null
   */
  IVedComponentView objectAt( int aX, int aY );

  /**
   * Список фигур содержащих указанную точку.<br>
   *
   * @param aX int - x координата курсора в пикселях
   * @param aY int - y координата курсора в пикселях
   * @return IStridablesList&lt;IVedComponentView> - список объектов содержащих указанную точку
   */
  IStridablesList<IVedComponentView> objectsAt( int aX, int aY );

}

class NullMouseHandler
    implements IMouseHandler {

  @Override
  public IVedComponentView hoveredObject() {
    return null;
  }

  @Override
  public ITsPoint cursorPosition() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IVedComponentView objectAt( int aX, int aY ) {
    return null;
  }

  @Override
  public IStridablesList<IVedComponentView> objectsAt( int aX, int aY ) {
    return IStridablesList.EMPTY;
  }

  @Override
  public boolean isDisposed() {
    return true;
  }

  @Override
  public void dispose() {
    // nop
  }

  @Override
  public void addDragListener( IDragListener aListener ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void removeDragListener( IDragListener aListener ) {
    throw new TsNullObjectErrorRtException();
  }

}
