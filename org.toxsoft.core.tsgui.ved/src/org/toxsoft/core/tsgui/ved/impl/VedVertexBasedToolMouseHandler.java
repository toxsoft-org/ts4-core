package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Обработчик мыши для инструментов, которые изменяют свойства компонент с помощью набора вершин
 * {@link IVedVertexSetView}.
 * <p>
 * Основной задачей данного обработчика является скрыть всю работу связанную с определением перетаскиваемого элемента и
 * его перемещением сведя ее к простому уведомлению "инструмента", что конкретная вершина из набора должна быть
 * перемещена в новое положение. При этом новое положение определяется только позицией мыши, поэтому оно может быть
 * недопустимым. В этом случае "инструмент" должен сам скорректировать новое положение вершины.<br>
 *
 * @author vs
 */
public class VedVertexBasedToolMouseHandler
    extends VedAbstractToolMouseHandler
    implements IVedContextable {

  /**
   * Инкапсулированная и отделенная от обработчика мыши реакция на клик мыши.
   *
   * @author vs
   */
  interface IClickStrategy {

    void onClick( IScreenObject aObject, MouseEvent aEvent );

  }

  /**
   * Реализация IClickStrategy по-умолчанию.
   * <p>
   * Данная стратегия реализует следущее поведение без нажатой клавиши Ctrl:
   * <ul>
   * <li>Click на пустом месте экрана - снимается выделение со всех элементов</li>
   * <li>Click на компоненте - снимается выделение со всех элементов и выделяется только тот, на котором произошел
   * <li>Click на вершине набора - игнорируется</li>
   * </ul>
   * С нажатой клавишей Ctrl поведние следующее:
   * <ul>
   * <li>Click на пустом месте - игнорируется, чтобы при аддитивном выделении промах не приводил к потере ранее
   * выделенных элементов</li>
   * <li>Click на компоненте - переключает выделение компоненты, на которой произошел click</li>
   * <li>Click на вершине набора - игнорируется</li>
   * </ul>
   *
   * @author vs
   */
  class DefaultClickStrategy
      implements IClickStrategy {

    @Override
    public void onClick( IScreenObject aObject, MouseEvent aEvent ) {
      if( (aEvent.stateMask & SWT.CTRL) == 0 ) { // Click без Ctrl
        if( aObject == null ) { // click на пустом месте
          VedVertexBasedToolMouseHandler.this.tool.selectionManager.deselectAll();
        }
        else { // click на экранном объекте
          if( aObject.kind() == EScreenObjectKind.COMPONENT ) { // click на компоненте
            VedVertexBasedToolMouseHandler.this.tool.selectionManager.deselectAll();
            IVedComponentView compView = aObject.entity();
            VedVertexBasedToolMouseHandler.this.tool.selectionManager.setSelectedComponentView( compView );
          }
        }
      }
      else { // Click с нажатой клавишей Ctrl
        if( aObject != null ) { // Click на пустом месте игнорируется
          if( aObject.kind() == EScreenObjectKind.COMPONENT ) { // если click на компоненте
            IVedComponentView compView = aObject.entity();
            VedVertexBasedToolMouseHandler.this.tool.selectionManager.toggleSelection( compView );
          }
        }
      }
    }

  }

  VedAbstractVertexBasedTool tool;

  IClickStrategy clickStrategy = new DefaultClickStrategy();

  /**
   * Конструктор.<br>
   *
   * @param aTool VedStdPointerTool - инструмент - указатель
   * @param aEnv IVedEnvironment - окружение редактора
   * @param aScreen IVedScreen - экран отображения
   */
  public VedVertexBasedToolMouseHandler( VedAbstractVertexBasedTool aTool, IVedEnvironment aEnv, IVedScreen aScreen ) {
    super( aEnv, aScreen );
    tool = aTool;
    // cursorHand = cursorManager().getCursor( ECursorType.HAND );
    // stdDragListener = new StdDragVedCompViewsListener( screen() );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractToolMouseHandler
  //

  @Override
  protected IVedDragExecutor dragExecutor( IScreenObject aHoveredObject ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected IList<IScreenObject> objectsForDrag( IScreenObject aHoveredObject, MouseEvent aEvent ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doDispose() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onClick( IScreenObject aShape, MouseEvent aEvent ) {
    clickStrategy.onClick( aShape, aEvent );
  }

  // @Override
  // protected void doOnClick( IScreenObject aHoveredObject, MouseEvent aEvent ) {
  // // TODO Auto-generated method stub
  // super.doOnClick( aHoveredObject, aEvent );
  // }

  // ------------------------------------------------------------------------------------
  // API
  //

}
