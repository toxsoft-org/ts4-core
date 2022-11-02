package org.toxsoft.core.tsgui.ved.zver0.tools_old.drag;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Интерфейс объекта, обрабатывающий события перетаскивания.
 * <p>
 *
 * @author vs
 */
public interface IVedDragExecutor {

  /**
   * Обработчик "перетаскивания", который ничего не делает
   */
  InternalNullDragExecutor NULL = new InternalNullDragExecutor();

  /**
   * Осуществляет подготовку к перетаскиванию и возвращает соответствующий курсор.
   *
   * @param aStartEvent MouseEvent - событие мыши на начало перетаскивания
   * @return Cursor - курсор, обозначающий готовность к перетаскиванию
   */
  Cursor onStartDrag( MouseEvent aStartEvent );

  /**
   * Выолняет перетаскивание и возвращает курсор, сигнализириующий о том, возможен drop или нет.
   *
   * @param aStartEvent MouseEvent - событие мыши на начало перетаскивания
   * @param aCurrEvent MouseEvent - текущее событие мыши
   * @return Cursor - курсор, сигнализириующий о том, возможен drop или нет
   */
  Cursor doDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent );

  /**
   * Завершает перетаскивание.<br>
   *
   * @param aStartEvent MouseEvent - параметры "мыши" на момент начала перетаскивания
   * @param aCurrEvent MouseEvent - параметры "мыши" на текущий момент
   * @return <b>true</b> - процедура перетаскивания прошла успешно<br>
   *         <b>false</b> - не удалось завершить перетаскивание
   */
  boolean doFinishDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent );

  /**
   * Выполняет отмену перетаскивания.<br>
   *
   * @param aStartEvent MouseEvent - параметры "мыши" на момент начала перетаскивания
   * @param aCurrEvent MouseEvent - параметры "мыши" на текущий момент м.б. null
   * @return <b>true</b> - процедура отмены прошла успешно, больше никаких действий не требуется<br>
   *         <b>false</b> - для завершения отмены требуются дополнительные действия от внешнего окружения
   */
  boolean doCancelDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent );

  /**
   * Выполняет отмену перетаскивания, инициированную извне (например ESC).<br>
   *
   * @param aStartEvent MouseEvent - параметры "мыши" на момент начала перетаскивания
   * @return <b>true</b> - процедура отмены прошла успешно, больше никаких действий не требуется<br>
   *         <b>false</b> - для завершения отмены требуются дополнительные действия от внешнего окружения
   */
  default boolean doForcedCancelDrag( MouseEvent aStartEvent ) {
    return doCancelDrag( aStartEvent, aStartEvent );
  }

  /**
   * Определеяет, возможно ли завершить процедуру перетаскивания для текушего положения "мыши".<br>
   *
   * @param aStartEvent MouseEvent - параметры "мыши" на момент начала перетаскивания
   * @param aCurrEvent MouseEvent - параметры "мыши" на текущий момент
   * @return <b>true</b> - перетаскивание может быть завершено<br>
   *         <b>false</b> - перетаскивание может быть только отменено или продолжено
   */
  default boolean canDrop( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    return true;
  }

}

class InternalNullDragExecutor
    implements IVedDragExecutor {

  @Override
  public Cursor onStartDrag( MouseEvent aStartEvent ) {
    return null;
  }

  @Override
  public Cursor doDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    return null;
  }

  @Override
  public boolean doFinishDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    return false;
  }

  @Override
  public boolean doCancelDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    return false;
  }

  @Override
  public boolean canDrop( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    return false;
  }

  @Override
  public boolean doForcedCancelDrag( MouseEvent aStartEvent ) {
    return false;
  }

}
