package org.toxsoft.core.tsgui.ved.extra.tools;

import static java.lang.Math.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Слушатель событий пользовательского ввода для инструментов, ориентированный на изменение свойств компонент с помощью
 * набора вершин.
 * <p>
 *
 * @author vs
 */
public class VedVertexBasedToolUIListener
    implements ITsUserInputListener {

  /**
   * Инкапсулированная и отделенная от обработчика мыши реакция на клик мыши.
   *
   * @author vs
   */
  public interface IMouseDownStrategy {

    /**
     * Стратегия, которая ничего не делает
     */
    InternalNullMouseDownStrategy NONE = new InternalNullMouseDownStrategy();

    /**
     * Выполняет сооствествующие действия при нажатии мыши.
     *
     * @param aObject IScreenObject - экранный объект, на котором произошло нажатие, м.б. <code>null</code>
     * @param aButton ETsMouseButton - кнопка мыши, которая была нажата
     * @param aStateMask int - SWT коды клавиш Ctrl, Shift, Alt
     */
    void onMouseDown( IScreenObject aObject, ETsMouseButton aButton, int aStateMask );

  }

  static class InternalNullMouseDownStrategy
      implements IMouseDownStrategy {

    @Override
    public void onMouseDown( IScreenObject aObject, ETsMouseButton aButton, int aStateMask ) {
      // nop
    }
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
  class DefaultMouseDownStrategy
      implements IMouseDownStrategy {

    @Override
    public void onMouseDown( IScreenObject aObject, ETsMouseButton aButton, int aStateMask ) {
      updateSelection( aObject, aButton, aStateMask );
    }
  }

  /**
   * Инкапсулированная и отделенная от обработчика мыши реакция на клик мыши.
   *
   * @author vs
   */
  public interface IClickStrategy {

    /**
     * Стратегия, которая ничего не делает
     */
    InternalNullClickStrategy NONE = new InternalNullClickStrategy();

    /**
     * Выполняет сооствествующие действия при click'e мыши.
     *
     * @param aObject IScreenObject - экранный объект, на котором произошел click, м.б. <code>null</code>
     * @param aButton ETsMouseButton - кнопка мыши, которой был произведен click
     * @param aStateMask int - SWT коды клавиш Ctrl, Shift, Alt
     */
    void onClick( IScreenObject aObject, ETsMouseButton aButton, int aStateMask );

  }

  static class InternalNullClickStrategy
      implements IClickStrategy {

    @Override
    public void onClick( IScreenObject aObject, ETsMouseButton aButton, int aStateMask ) {
      // nop
    }
  }

  /**
   * Инкапсулированная и отделенная от обработчика мыши реакция на перетаскивание курсора на пустом месте экрана.
   *
   * @author vs
   */
  public interface IFreeDragStrategy {

    /**
     * Стратегия, которая не реагирует на перетаскивание
     */
    IFreeDragStrategy NONE = new NoneFreeDragStrategy();

    /**
     * Вызывается в процессе перетаскивания.
     *
     * @param aDx double - смещение по X
     * @param aDy double - смещение по Y
     * @param aDragState ETsDragState - состояние процесса перетаскивание
     */
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
      tool.vedScreen().paintingManager().redraw();
      tool.vedScreen().paintingManager().update(); // немедленно перерисуем канву
    }
  }

  /**
   * Инкапсулированная и отделенная от обработчика мыши реакция на перетаскивание экранных объектов.
   *
   * @author vs
   */
  public interface IObjectsDragStrategy {

    IObjectsDragStrategy NONE = new NoneObjectsDragStrategy();

    void onDragComponentView( double aDx, double aDy, IScreenObject aScreenObj, ETsDragState aDragState );

    void onDragVertex( double aDx, double aDy, IVedVertex aVertex, ETsDragState aDragState );
  }

  static class NoneObjectsDragStrategy
      implements IObjectsDragStrategy {

    @Override
    public void onDragComponentView( double aDx, double aDy, IScreenObject aScreenObj, ETsDragState aDragState ) {
      // nop
    }

    @Override
    public void onDragVertex( double aDx, double aDy, IVedVertex aVertex, ETsDragState aDragState ) {
      // nop
    }
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
          tool.selectionDecorator().ignoredViewIds().add( view.id() );
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
        tool.selectionDecorator().ignoredViewIds().clear();
        if( !tool.selectedViews().isEmpty() ) {
          tool.vertexSet().init( tool.selectedViews() );
          tool.showVertexSet( tool.vertexSet() );
        }
      }
      tool.vedScreen().paintingManager().redraw();
      tool.vedScreen().paintingManager().update(); // немедленно перерисуем канву
    }

    @Override
    public void onDragVertex( double aDx, double aDy, IVedVertex aVertex, ETsDragState aDragState ) {
      if( aDragState == ETsDragState.START ) {
        tool.hideVertexSet( tool.vertexSet() );
        for( IVedComponentView view : tool.selectedViews() ) {
          tool.selectionDecorator().ignoredViewIds().add( view.id() );
        }
      }

      tool.onVertexDragged( aDx, aDy, aVertex, aDragState );

      if( aDragState == ETsDragState.FINISH || aDragState == ETsDragState.CANCEL ) {
        tool.selectionDecorator().ignoredViewIds().clear();
        if( !tool.selectedViews().isEmpty() ) {
          tool.vertexSet().init( tool.selectedViews() );
          tool.showVertexSet( tool.vertexSet() );
        }
      }
      tool.vedScreen().paintingManager().redraw();
      tool.vedScreen().paintingManager().update(); // немедленно перерисуем канву
    }

  }

  static class InternalDragInfo {

    private int dx = 0;
    private int dy = 0;

    int currX;
    int currY;

    private final int startX;
    private final int startY;

    private final IScreenObject screenObject;

    InternalDragInfo( int aX, int aY, IScreenObject aScreenObj ) {
      startX = aX;
      startY = aY;
      currX = startX;
      currY = startY;
      screenObject = aScreenObj;
    }

    void update( int aNewX, int aNewY ) {
      dx = aNewX - currX;
      dy = aNewY - currY;
      currX = aNewX;
      currY = aNewY;
    }

    int startX() {
      return startX;
    }

    int startY() {
      return startY;
    }

    int dx() {
      return dx;
    }

    int dy() {
      return dy;
    }

  }

  private InternalDragInfo dragInfo = null;

  private IMouseDownStrategy mouseDownStrategy = new DefaultMouseDownStrategy();

  private IClickStrategy clickStrategy = IClickStrategy.NONE;

  private IFreeDragStrategy freeDragStrategy = new DefaultFreeDragStrategy();

  IObjectsDragStrategy dragStrategy = new DefaultObjectsDragStrategy();

  private final VedAbstractVertexBasedTool tool;

  protected VedVertexBasedToolUIListener( VedAbstractVertexBasedTool aTool ) {
    tool = aTool;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( mouseDownStrategy != IMouseDownStrategy.NONE ) {
      IScreenObject scrObj = tool.objectAt( aCoors.x(), aCoors.y() );
      mouseDownStrategy.onMouseDown( scrObj, aButton, aState );
      return true;
    }
    return false;
  }

  @Override
  public boolean onMouseClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( clickStrategy != IClickStrategy.NONE ) {
      IScreenObject scrObj = tool.objectAt( aCoors.x(), aCoors.y() );
      clickStrategy.onClick( scrObj, aButton, aState );
      return true;
    }
    return false;
  }

  @Override
  public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
    if( freeDragStrategy != IFreeDragStrategy.NONE || dragStrategy != IObjectsDragStrategy.NONE ) {
      TsIllegalStateRtException.checkNoNull( dragInfo );
      int x = aDragInfo.startingPoint().x();
      int y = aDragInfo.startingPoint().y();
      IScreenObject scrObj = tool.objectAt( x, y );
      dragInfo = new InternalDragInfo( x, y, scrObj );
      aDragInfo.setCargo( dragInfo );
      return processDragEvent( aDragInfo, ETsDragState.START );
    }
    return false;
  }

  @Override
  public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    if( freeDragStrategy != IFreeDragStrategy.NONE || dragStrategy != IObjectsDragStrategy.NONE ) {
      TsIllegalStateRtException.checkNull( dragInfo );
      dragInfo.update( aCoors.x(), aCoors.y() );
      return processDragEvent( aDragInfo, ETsDragState.DRAGGING );
    }
    return false;
  }

  @Override
  public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    if( freeDragStrategy != IFreeDragStrategy.NONE || dragStrategy != IObjectsDragStrategy.NONE ) {
      TsIllegalStateRtException.checkNull( dragInfo );
      dragInfo.update( aCoors.x(), aCoors.y() );
      boolean result = processDragEvent( aDragInfo, ETsDragState.FINISH );
      dragInfo = null;
      return result;
    }
    return false;
  }

  @Override
  public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
    if( freeDragStrategy != IFreeDragStrategy.NONE || dragStrategy != IObjectsDragStrategy.NONE ) {
      TsIllegalStateRtException.checkNull( dragInfo );
      dragInfo.update( dragInfo.startX(), dragInfo.startY() );
      boolean result = processDragEvent( aDragInfo, ETsDragState.CANCEL );
      dragInfo = null;
      return result;
    }
    return false;
  }

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    tool.updateCursor( aCoors.x(), aCoors.y() );
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает алгоритм обработки клика мышки.
   *
   * @param aStrategy IClickStrategy - алгоритм обработки клика мышки м.б. <b>null<b>
   */
  public void setClickStrategy( IClickStrategy aStrategy ) {
    if( aStrategy == null ) {
      clickStrategy = IClickStrategy.NONE;
    }
    else {
      clickStrategy = aStrategy;
    }
  }

  /**
   * Задает алгоритм обработки переаскивания мышки на пустом поле.
   *
   * @param aStrategy IClickStrategy - алгоритм обработки переаскивания мышки на пустом поле м.б. <b>null<b>
   */
  public void setFreeDragStrategy( IFreeDragStrategy aStrategy ) {
    if( aStrategy == null ) {
      freeDragStrategy = IFreeDragStrategy.NONE;
    }
    else {
      freeDragStrategy = aStrategy;
    }
  }

  // ------------------------------------------------------------------------------------
  // to use
  //

  protected void moveComponents( double aDx, double aDy, IList<IVedComponentView> aViews ) {
    double alpha = tool.vedScreen().getConversion().rotation().radians();
    double dx = aDx * cos( -alpha ) - aDy * sin( -alpha );
    double dy = aDy * cos( -alpha ) + aDx * sin( -alpha );
    double zf = tool.vedScreen().getConversion().zoomFactor();

    for( IVedComponentView view : aViews ) {
      view.porter().shiftOn( dx / zf, dy / zf );
    }
  }

  // ------------------------------------------------------------------------------------
  // internal
  //

  private boolean processDragEvent( DragOperationInfo aDragInfo, ETsDragState aDragState ) {
    InternalDragInfo di = aDragInfo.cargo();

    IScreenObject scrObj = di.screenObject;
    if( scrObj == null && freeDragStrategy != IFreeDragStrategy.NONE ) {
      freeDragStrategy.onDrag( di.dx, di.dy, aDragState );
      return true;
    }
    if( scrObj != null && dragStrategy != IObjectsDragStrategy.NONE ) {
      if( scrObj.kind() == EScreenObjectKind.COMPONENT ) {
        dragStrategy.onDragComponentView( di.dx, di.dy, scrObj, aDragState );
        return true;
      }
      if( scrObj.kind() == EScreenObjectKind.VERTEX ) {
        dragStrategy.onDragVertex( di.dx, di.dy, scrObj.entity(), aDragState );
        return true;
      }
    }
    return false;
  }

  private void updateSelection( IScreenObject aObject, ETsMouseButton aButton, int aStateMask ) {
    if( aButton == ETsMouseButton.LEFT ) {
      if( (aStateMask & SWT.CTRL) == 0 ) { // Click без Ctrl
        if( aObject == null ) { // click на пустом месте
          tool.selectionManager.deselectAll();
        }
        else { // click на экранном объекте
          if( aObject.kind() == EScreenObjectKind.COMPONENT ) { // click на компоненте
            tool.selectionManager.deselectAll();
            IVedComponentView compView = aObject.entity();
            tool.selectionManager.setSelectedView( compView );
          }
        }
      }
      else { // Click с нажатой клавишей Ctrl
        if( aObject != null ) { // Click на пустом месте игнорируется
          if( aObject.kind() == EScreenObjectKind.COMPONENT ) { // если click на компоненте
            IVedComponentView compView = aObject.entity();
            tool.selectionManager.toggleSelection( compView );
          }
        }
      }
    }
  }

}
