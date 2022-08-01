package org.toxsoft.core.tsgui.ved.impl;

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
public class VedVertexBaseToolMouseHandler
    extends VedAbstractToolMouseHandler
    implements IVedContextable {

  VedAbstractVertexBasedTool tool;

  /**
   * Конструктор.<br>
   *
   * @param aTool VedStdPointerTool - инструмент - указатель
   * @param aEnv IVedEnvironment - окружение редактора
   * @param aScreen IVedScreen - экран отображения
   */
  public VedVertexBaseToolMouseHandler( VedAbstractVertexBasedTool aTool, IVedEnvironment aEnv, IVedScreen aScreen ) {
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

  // ------------------------------------------------------------------------------------
  // API
  //

}
