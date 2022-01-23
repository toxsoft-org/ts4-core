package org.toxsoft.tsgui.m5_1.impl.gui;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.m5_1.gui.IM5PanelBase;
import org.toxsoft.tslib.bricks.events.change.*;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Базовая реализация {@link IM5PanelBase}.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public abstract class M5PanelBase<T>
    implements IM5PanelBase<T> {

  private final GenericChangeEventer eventer;

  private final boolean isViewer;
  private boolean       editable;
  private Control       control = null;

  private final ITsGuiContext context;
  private final IM5Model<T>   model;

  /**
   * Конструктор для наследников.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aModel {@link IM5Model} - модель
   * @param aViewer boolean - признак просмотрщика (панели только для просмотра)
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  protected M5PanelBase( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    TsNullArgumentRtException.checkNulls( aContext, aModel );
    context = aContext;
    model = aModel;
    eventer = new GenericChangeEventer( this );
    isViewer = aViewer;
    editable = !isViewer;
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  final public Control createControl( Composite aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    TsIllegalStateRtException.checkNoNull( control );
    Control c = doCreateControl( aParent );
    TsInternalErrorRtException.checkNull( c );
    if( control != null ) {
      TsInternalErrorRtException.checkTrue( c != control );
    }
    control = c;
    // общая инициализация
    control.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent e ) {
        doDispose();
      }
    } );
    return control;
  }

  @Override
  final public Control getControl() {
    return control;
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return context;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5PanelBase
  //

  @Override
  public boolean isViewer() {
    return isViewer;
  }

  @Override
  final public IM5Model<T> model() {
    return model;
  }

  @Override
  final public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( isViewer ) {
      return;
    }
    if( editable != aEditable ) {
      editable = aEditable;
      doEditableStateChanged();
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  /**
   * Задает значение, возвращаемое методом {@link #getControl()}.
   * <p>
   * Позволяет наследнику в {@link #doCreateControl(Composite)} задатьвозвращаемое методом {@link #getControl()}
   * значение. Это может понадобиться, чтобы из {@link #doCreateControl(Composite)} вызывать методы, рассчитывающие на
   * получение ссылки {@link #getControl()}.
   * <p>
   * Метод можно вызвать один раз, и задать только не-null значение. Если {@link #doCreateControl(Composite)} врнет
   * другую ссылку (а не ту, которая была тут задана), то будет выброшено исключние.
   *
   * @param aControl {@link Composite} - контроль
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException контроль {@link #getControl()} уже был задан
   */
  protected void setControl( Composite aControl ) {
    TsNullArgumentRtException.checkNull( aControl );
    TsIllegalStateRtException.checkNoNull( control );
    control = aControl;
  }

  /**
   * Генерирует сообщения интерфейса {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   */
  public void fireChangeEvent() {
    eventer.fireChangeEvent();
  }

  /**
   * Возвращает ссылку на главное окно.
   *
   * @return {@link Shell} - главное окно
   */
  public Shell getShell() {
    if( control != null ) {
      return control.getShell();
    }
    return tsContext().get( Shell.class );
  }

  // ------------------------------------------------------------------------------------
  // Методы для реализации наследниками
  //

  /**
   * Наследник должен создать SWT-виджет, реализующий панель.
   * <p>
   * Гарантируется, что этот метод вызывается ровно один раз.
   *
   * @param aParent {@link Composite} - родительская панель, не бывает null
   * @return {@link Control} - созданный виджет, не должен быть null
   */
  protected abstract Control doCreateControl( Composite aParent );

  /**
   * Наследник может освободить ресурсы, захваченные панелью.
   * <p>
   * Вызывается перед уничтожением SWT-виджета просмотрщика (если {@link #createControl(Composite)} был вызван).
   * Обратите внимание, что этот метод не будет вызван, если не был создан SWT-виджет методом
   * {@link #doCreateControl(Composite)}.
   * <p>
   * Здесь ничего не делает, в наследнике вызвать родительский метод не нужно, однако, при дальнейшем наследовании,
   * обязательно нужно вызывать вложенные методы.
   */
  protected void doDispose() {
    // nop
  }

  /**
   * Наследник может изменить внешний вид и поведение панели при разрешении/запрете редактирования.
   * <p>
   * Вызывается при изменении состояния {@link #isEditable()}.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   */
  protected void doEditableStateChanged() {
    // nop
  }

}
