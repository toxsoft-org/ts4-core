package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Базовый класс для создания инструментов редактора.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractVedTool
    extends StridableParameterized
    implements IVedEditorTool {

  protected IStridablesList<IDataDef> COMMON_DDEFS = new StridablesList<>( //
      DDEF_NAME, //
      DDEF_DESCRIPTION, //
      DDEF_ICON_ID //
  );

  private final ITsGuiContext tsContext;

  protected AbstractVedTool( String aId, String aName, String aDescription, String aIconId, ITsGuiContext aContext ) {
    super( aId );
    setName( aName );
    setDescription( aDescription );
    params().setStr( DDEF_ICON_ID, aIconId );
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContext
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IIconIdable}
  //

  @Override
  public String iconId() {
    return DDEF_ICON_ID.getValue( params() ).asString();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedEditorTool}
  //

  @Override
  public void setActivate( boolean aActive ) {
    // TODO Auto-generated method stub
  }

  // ------------------------------------------------------------------------------------
  // Методы для обязательного переопределения в наследниках
  //

  /**
   * Обработчик мыши, реализующий особенности обработки данного инструмента.
   * <p>
   *
   * @return IMouseHandler - обработчик мыши данного инструмента
   */
  public abstract IMouseHandler mouseHandler();

}
