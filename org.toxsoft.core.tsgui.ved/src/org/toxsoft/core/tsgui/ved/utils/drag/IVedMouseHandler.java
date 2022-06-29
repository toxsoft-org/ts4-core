package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.incub.geom.*;
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
public interface IVedMouseHandler
    extends IVedDragEventProducer, IVedDisposable {

  /**
   * Нулевой (пустой) обработчик мыши, который ничего не делает
   */
  IVedMouseHandler NULL = new NullMouseHandler();

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
   * @param aEvent MouseEvent - инфрмация о состоянии кнопок мыши, клавиатуры и т.д.
   */
  default void onClick( IScreenObject aShape, MouseEvent aEvent ) {
    // nop
  }

  /**
   * Вызывается в момент появления курсора в области объекта {@link IScreenObject}
   *
   * @param aShape IScreenObject - объект в области которого появился курсор
   */
  default void onObjectIn( IScreenObject aShape ) {
    // nop
  }

  /**
   * Вызывается в момент когда курсор покидает область объекта {@link IScreenObject}
   *
   * @param aShape IScreenObject - объект в область которого покинул курсор
   */
  default void onObjectOut( IScreenObject aShape ) {
    // nop
  }

  /**
   * Возвращает положение курсора мыши на экране.<br>
   *
   * @return ID2Point - точка положения курсора мыши на экране
   */
  ID2Point cursorPosition();

  /**
   * Объект под курсором мыши.<br>
   * Перемещаясь по экрану, курсор время от времени находится над различными объектами. Вызывая данную функцию, можно
   * определить над каким объектом находится курсор в момент вызова. В случае если под курсором находятся несколько
   * объектов (накладывающиеся объекты), то возвращается первый в соотвествии с z-order.
   *
   * @return IScreenObject - объект под курсором мыши или null если под курсором нет ни одного объекта
   */
  IScreenObject hoveredObject();

  /**
   * Фигура содержащий указанную точку.<br>
   * В случае если несколько объектов (накладывающиеся объекты) содержат указанную точку, то возвращается первый в
   * соотвествии с z-order.
   *
   * @param aX int - x координата курсора в пикселях
   * @param aY int - y координата курсора в пикселях
   * @return IScreenObject - объект содержащий указанную точку или null
   */
  IScreenObject objectAt( int aX, int aY );

  /**
   * Список фигур содержащих указанную точку.<br>
   *
   * @param aX int - x координата курсора в пикселях
   * @param aY int - y координата курсора в пикселях
   * @return IStridablesList&lt;IScreenObject> - список объектов содержащих указанную точку
   */
  IStridablesList<IScreenObject> objectsAt( int aX, int aY );

}

class NullMouseHandler
    implements IVedMouseHandler {

  @Override
  public IScreenObject hoveredObject() {
    return null;
  }

  @Override
  public ID2Point cursorPosition() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IScreenObject objectAt( int aX, int aY ) {
    return null;
  }

  @Override
  public IStridablesList<IScreenObject> objectsAt( int aX, int aY ) {
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
  public void addDragListener( IVedDragListener aListener ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void removeDragListener( IVedDragListener aListener ) {
    throw new TsNullObjectErrorRtException();
  }

}
