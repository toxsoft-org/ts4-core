package org.toxsoft.core.tsgui.ved.extra.tools.impl;

import static java.lang.Math.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

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
  public interface IClickStrategy {

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
            VedVertexBasedToolMouseHandler.this.tool.selectionManager.setSelectedView( compView );
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

  /**
   * Инкапсулированная и отделенная от обработчика мыши реакция на перетаскивание курсора на пустом месте экрана.
   *
   * @author vs
   */
  public interface IFreeDragStrategy {

    IFreeDragStrategy NONE = new NoneFreeDragStrategy();

    void onDrag( double aDx, double aDy, ETsDragState aDragState );
  }

  static class NoneFreeDragStrategy
      implements IFreeDragStrategy {

    @Override
    public void onDrag( double aDx, double aDy, ETsDragState aDragState ) {
      // nop
    }
  }

  /**
   * Реализация стратегии перетаскивания курсора на пустом месте экрана по умолчанию.
   * <p>
   * По умолчанию стратегия реализует следующее поведение: перетаскивается весь экран
   *
   * @author vs
   */
  class DefaultFreeDragStrategy
      implements IFreeDragStrategy {

    @Override
    public void onDrag( double aDx, double aDy, ETsDragState aDragState ) {
      D2ConversionEdit d2Conv = new D2ConversionEdit( tool.vedScreen().getConversion() );
      d2Conv.origin().setX( d2Conv.origin().x() + aDx );
      d2Conv.origin().setY( d2Conv.origin().y() + aDy );
      tool.vedScreen().setConversion( d2Conv );
      vedScreen().paintingManager().redraw();
      vedScreen().paintingManager().update(); // немедленно перерисуем канву
    }
  }

  /**
   * Инкапсулированная и отделенная от обработчика мыши реакция на перетаскивание экранных объектов.
   *
   * @author vs
   */
  public interface IObjectsDragStrategy {

    void onDragComponentView( double aDx, double aDy, IScreenObject aScreenObj, ETsDragState aDragState );

    void onDragVertex( double aDx, double aDy, IVedVertex aVertex, ETsDragState aDragState );
  }

  /**
   * Реализация стратегии перетаскивания экранных объектов по умолчанию.
   * <p>
   * По умолчанию стратегия реализует следующее поведение:
   * <ul>
   * <li>Перетаскивание не выделенного элемента - вне зависимости от выделения меняется положение только
   * перетаскиваемого элемента</li>
   * <li>Перетаскивание выделенного элемента - меняется положение всех выделенных элементов</li>
   * <li></li>
   * </ul>
   *
   * @author vs
   */
  class DefaultObjectsDragStrategy
      implements IObjectsDragStrategy {

    @Override
    public void onDragComponentView( double aDx, double aDy, IScreenObject aScreenObj, ETsDragState aDragState ) {
      if( aDragState == ETsDragState.START ) {
        tool.hideVertexSet( tool.vertexSet() );
        for( IVedComponentView view : tool.selectedViews() ) {
          tool.selectionDecorator().hideSelection( view.id() );
        }
      }
      IVedComponentView view = aScreenObj.entity();
      if( tool.selectedViews().hasKey( view.id() ) ) { // тянем один из выделенных элементов
        moveComponents( aDx, aDy, tool.selectedViews() );
      }
      else {
        moveComponents( aDx, aDy, new ElemArrayList<>( view ) );
      }
      if( aDragState == ETsDragState.FINISH || aDragState == ETsDragState.CANCEL ) {
        tool.selectionDecorator().showAll();
        if( !tool.selectedViews().isEmpty() ) {
          tool.vertexSet().init( tool.selectedViews() );
          tool.showVertexSet( tool.vertexSet() );
        }
      }
      vedScreen().paintingManager().redraw();
      vedScreen().paintingManager().update(); // немедленно перерисуем канву
    }

    @Override
    public void onDragVertex( double aDx, double aDy, IVedVertex aVertex, ETsDragState aDragState ) {
      if( aDragState == ETsDragState.START ) {
        tool.hideVertexSet( tool.vertexSet() );
        for( IVedComponentView view : tool.selectedViews() ) {
          tool.selectionDecorator().hideSelection( view.id() );
        }
      }

      tool.onVertexDragged( aDx, aDy, aVertex, aDragState );

      if( aDragState == ETsDragState.FINISH || aDragState == ETsDragState.CANCEL ) {
        tool.selectionDecorator().showAll();
        if( !tool.selectedViews().isEmpty() ) {
          tool.vertexSet().init( tool.selectedViews() );
          tool.showVertexSet( tool.vertexSet() );
        }
      }
      vedScreen().paintingManager().redraw();
      vedScreen().paintingManager().update(); // немедленно перерисуем канву
    }

  }

  IFreeDragStrategy freeDragStrategy = new DefaultFreeDragStrategy();

  IObjectsDragStrategy dragStrategy = new DefaultObjectsDragStrategy();

  VedAbstractVertexBasedTool tool;

  IVedDragObjectsListener moveListener = ( aDx, aDy, aShapes, aDragState ) -> {
    if( aShapes.isEmpty() ) {
      this.freeDragStrategy.onDrag( aDx, aDy, aDragState );
      return;
    }
    if( aShapes.first().kind() == EScreenObjectKind.COMPONENT ) {
      dragStrategy.onDragComponentView( aDx, aDy, aShapes.first(), aDragState );
      return;
    }
    if( aShapes.first().kind() == EScreenObjectKind.VERTEX ) {
      dragStrategy.onDragVertex( aDx, aDy, aShapes.first().entity(), aDragState );
    }

  };

  IClickStrategy clickStrategy = new DefaultClickStrategy();

  Cursor cursorHand;

  VedMoveObjectsDragExecutor moveExecutor;

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
    cursorHand = cursorManager().getCursor( ECursorType.HAND );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractToolMouseHandler
  //

  @Override
  protected IVedDragExecutor dragExecutor( IScreenObject aHoveredObject ) {
    if( aHoveredObject == null ) {
      moveExecutor = new VedMoveObjectsDragExecutor( IList.EMPTY, vedEnv() );
    }
    else {
      moveExecutor = new VedMoveObjectsDragExecutor( new ElemArrayList<>( aHoveredObject ), vedEnv() );
    }
    moveExecutor.addVedDragObjectsListener( moveListener );
    return moveExecutor;
  }

  @Override
  protected IList<IScreenObject> objectsForDrag( IScreenObject aHoveredObject, MouseEvent aEvent ) {
    return null;
  }

  @Override
  public void onClick( IScreenObject aShape, MouseEvent aEvent ) {
    clickStrategy.onClick( aShape, aEvent );
  }

  @Override
  public void onObjectIn( IScreenObject aObj ) {
    updateCursor( aObj );
  }

  @Override
  public void onObjectOut( IScreenObject aShape ) {
    screen().paintingManager().setCursor( null );
  }

  @Override
  protected void afterDragEnded() {
    updateCursor( hoveredObject );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает стратегию обработки {@link #onClick(IScreenObject, MouseEvent)}, null - для стратегии по умолчанию.
   *
   * @param aClickStrategy IClickStrategy - стратегия обработки {@link #onClick(IScreenObject, MouseEvent)}, м.б. null
   */
  public void setClickStrategy( IClickStrategy aClickStrategy ) {
    clickStrategy = aClickStrategy;
    if( clickStrategy == null ) {
      clickStrategy = new DefaultClickStrategy();
    }
  }

  /**
   * Задает стратегию обработки перетаскивания курсора на пустом месте, null - для стратегии по умолчанию.
   *
   * @param aDragStrategy IFreeDragStrategy - стратегия обработки перетаскивания, м.б. null
   */
  public void setFreeDragStrategy( IFreeDragStrategy aDragStrategy ) {
    freeDragStrategy = aDragStrategy;
    if( freeDragStrategy == null ) {
      freeDragStrategy = new DefaultFreeDragStrategy();
    }
  }

  /**
   * Задает стратегию обработки перетаскивания, null - для стратегии по умолчанию.
   *
   * @param aDragStrategy IObjectsDragStrategy - стратегия обработки перетаскивания, м.б. null
   */
  public void setDragStrategy( IObjectsDragStrategy aDragStrategy ) {
    dragStrategy = aDragStrategy;
    if( dragStrategy == null ) {
      dragStrategy = new DefaultObjectsDragStrategy();
    }
  }

  // ------------------------------------------------------------------------------------
  // to use
  //

  protected void moveComponents( double aDx, double aDy, IList<IVedComponentView> aViews ) {
    double alpha = vedScreen().getConversion().rotation().radians();
    double dx = aDx * cos( -alpha ) - aDy * sin( -alpha );
    double dy = aDy * cos( -alpha ) + aDx * sin( -alpha );
    double zf = vedScreen().getConversion().zoomFactor();

    for( IVedComponentView view : aViews ) {
      view.porter().shiftOn( dx / zf, dy / zf );
    }
  }

  // ------------------------------------------------------------------------------------
  // internal
  //

  private void updateCursor( IScreenObject aObj ) {
    Cursor cursor = null;
    if( aObj != null ) {
      cursor = cursorManager().findCursor( aObj.cursorType().id() );
      if( cursor == null ) {
        cursor = cursorHand;
      }
    }
    screen().paintingManager().setCursor( cursor );
  }

}
