package org.toxsoft.core.tsgui.ved.std.tool;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

public abstract class VedAbstractEditorTool
    extends Stridable
    implements IVedEditorTool {

  private final String iconId;

  private final ITsGuiContext tsContext;

  private final IStridablesListEdit<IVedComponentView> views = new StridablesList<>();

  VedScreen screen;

  /**
   * Конструктор для наследников.<br>
   *
   * @param aId String - идентификатор "инструмента"
   * @param aDescr String - описание "инструмента"
   * @param aName String - название "инструмента"
   * @param aIconId String - идентификатор значка
   * @param aContext ITsGuiContext - контекст компоненты
   */
  protected VedAbstractEditorTool( String aId, String aDescr, String aName, String aIconId, ITsGuiContext aContext ) {
    super( aId, aName, aDescr );
    iconId = aIconId;
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // {@link IVedEditorTool}
  //

  @Override
  public IOptionSet params() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String iconId() {
    return iconId;
  }

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  @Override
  public void setActivate( boolean aActive ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void activate( VedScreen aScreen ) {
    screen = aScreen;
    views.clear();
    for( IVedComponentView view : screen.listViews() ) {
      if( accept( view ) ) {
        views.add( view );
      }
    }
  }

  /**
   * Обработчик мыши, реализующий особенности обработки данного инструмента.
   * <p>
   *
   * @return IMouseHandler - обработчик мыши данного инструмента
   */
  public abstract IVedMouseHandler mouseHandler();

  /**
   * Возвращает список отображаемых элементов, с которыми работает данный инструмент.<br>
   *
   * @return IStridablesList&lt;IVedComponentView> - список "представлений", с которыми работает данный инструмент
   */
  public IStridablesList<IVedComponentView> listViews() {
    return views;
  }

  /**
   * Возвращает признак того, применим ли инструмент для данного элемента.
   *
   * @param aView IVedComponentView - "представление" компоненты
   * @return <b>true</b> - инструмент применим<br>
   *         <b>false</b> - интсрумент нельзя применять
   */
  public abstract boolean accept( IVedComponentView aView );
}
